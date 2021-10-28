package no.nav.pensjon.vtp.auth.azuread

import io.swagger.annotations.ApiOperation
import no.nav.pensjon.vtp.auth.*
import no.nav.pensjon.vtp.auth.JsonWebKeySupport.*
import no.nav.pensjon.vtp.testmodell.ansatt.AnsattService
import no.nav.pensjon.vtp.testmodell.ansatt.NAVAnsatt
import no.nav.pensjon.vtp.util.asResponseEntity
import org.apache.http.client.utils.URIBuilder
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.HttpStatus.TEMPORARY_REDIRECT
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.MediaType.TEXT_HTML_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.badRequest
import org.springframework.http.ResponseEntity.ok
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder.fromUriString
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/rest/AzureAd")
class AzureAdMock(
    private val ansattService: AnsattService,
    private val jsonWebKeySupport: JsonWebKeySupport,
) {
    @GetMapping(value = ["/isAlive"], produces = [TEXT_HTML_VALUE])
    fun isAliveMock() = ok("Azure AD is OK")

    @GetMapping("/{tenant}/v2.0/authorize")
    fun authorize(req: HttpServletRequest, @PathVariable("tenant") tenant: String, @RequestParam requestParams: MultiValueMap<String, String>): ResponseEntity<Any> {
        val uri = if (requestParams.isEmpty()) {
            fromUriString(getFrontendUrl(req))
                .fragment("/azuread/$tenant/v2.0/authorize")
                .build()
                .toUri()
        } else {
            val query = fromUriString(getFrontendUrl(req) + "/azuread/$tenant/v2.0/authorize")
                .queryParams(requestParams).build().getQuery()

            fromUriString(getFrontendUrl(req))
                .fragment("/azuread/$tenant/v2.0/authorize?" + query)
                .build()
                .toUri()
        }

        return ResponseEntity
            .status(TEMPORARY_REDIRECT)
            .location(uri)
            .build<Any>()
    }

    @GetMapping(value = ["/{tenant}/v2.0/.well-known/openid-configuration"], produces = [APPLICATION_JSON_VALUE])
    @ApiOperation(value = "Azure AD Discovery url", notes = "Mock impl av Azure AD discovery urlen. ")
    fun wellKnown(req: HttpServletRequest, @PathVariable("tenant") tenant: String, @RequestParam("p") profile: String?) =
        WellKnownResponse(
            authorizationEndpoint = linkTo<AzureAdMock> { authorize(req, tenant, LinkedMultiValueMap()) }.toUri(),
            baseUrl = getBaseUrl(req),
            graphUrl = getGraphUrl(req),
            tenant = tenant,
            profile = profile
        )

    @GetMapping(value = ["/{tenant}/discovery/v2.0/keys"])
    fun keys(@PathVariable tenant: String) = Keys(jsonWebKeySupport.jwks()).asResponseEntity()

    @PostMapping(value = ["/{tenant}/oauth2/v2.0/token"])
    fun accessToken(
        req: HttpServletRequest,
        @PathVariable("tenant") tenant: String,
        @RequestParam("grant_type") grantType: String,
        @RequestParam("client_id") clientId: String,
        @RequestParam("realm") realm: String?,
        @RequestParam("code") code: String,
        @RequestParam("refresh_token", required = false) refreshToken: String?,
        @RequestParam("redirect_uri") redirectUri: String,
        @RequestParam("scope", required = false) scope: String?,
    ): ResponseEntity<*> {
        return when (grantType) {
            "authorization_code" -> {
                ok(
                    Oauth2AccessTokenResponse(
                        idToken = createIdToken(code = code, tenant = tenant, clientId = clientId, scope = scope, sid = code),
                        refreshToken = createIdToken(code = code, tenant = tenant, clientId = clientId, scope = scope, sid = code),
                        accessToken = createIdToken(code = code, tenant = tenant, clientId = clientId, scope = scope, sid = code),
                    )
                )
            }
            "refresh_token" -> {
                if (refreshToken == null) {
                    badRequest().body("Missing required parameter 'request_token'")
                } else {
                    val usernameWithNonce = refreshToken.substring(8)
                    ok(
                        Oauth2AccessTokenResponse(
                            idToken = createIdToken(
                                code = usernameWithNonce,
                                tenant = tenant,
                                clientId = clientId,
                                scope = scope,
                            ),
                            refreshToken = "refresh:$usernameWithNonce",
                            accessToken = "access:$usernameWithNonce"
                        )
                    )
                }
            }
            else -> {
                badRequest().body("Unknown grant_type $grantType")
            }
        }
    }

    private fun createIdToken(code: String, tenant: String, clientId: String, scope: String? = null, sid: String? = null): String {
        val codeData = code.split(";".toRegex()).toTypedArray()
        val ansattId = codeData[0]
        val user = ansattService.findByCn(codeData[0]) ?: throw RuntimeException("Fant ikke NAV-ansatt med brukernavn $ansattId")

        return azureOidcToken(
            sub = "$clientId:$ansattId",
            jsonWebKeySupport = jsonWebKeySupport,
            email = user.email,
            nonce = if (codeData.size > 1) codeData[1] else null,
            issuer = getIssuer(tenant),
            groups = user.groups.map { ldapGroupName: String -> toAzureGroupId(ldapGroupName) },
            aud = listOf(clientId),
            additionalClaims = mapOf(
                "tid" to tenant,
                "oid" to UUID.nameUUIDFromBytes(user.cn.toByteArray()).toString(), // user id - which is normally a UUID in Azure AD
                "name" to user.displayName,
                "preferred_username" to user.email,
            ),
            scope = scope,
            sid = sid,
        )
    }

    @GetMapping(value = ["/{tenant}/v2.0/users"], produces = [APPLICATION_JSON_VALUE])
    @ApiOperation(value = "/v2.0/users", notes = "Hent brukere/saksbehandlere man kan logge inn som")
    fun users(
        req: HttpServletRequest?,
        resp: HttpServletResponse?,
        @PathVariable("tenant") tenant: String,
        @RequestParam(value = "response_type", defaultValue = "code") responseType: String,
        @RequestParam(value = "scope", defaultValue = "openid") scope: String,
        @RequestParam("client_id") clientId: String,
        @RequestParam("state") state: String,
        @RequestParam("nonce", defaultValue = "") nonce: String,
        @RequestParam("redirect_uri") redirectUri: String
    ): ResponseEntity<List<UserEntry>> {
        require(responseType == "code") { "Unsupported responseType [$responseType], should be 'code'" }

        Objects.requireNonNull(clientId, "Missing the ?client_id=xxx query parameter")
        Objects.requireNonNull(state, "Missing the ?state=xxx query parameter")
        Objects.requireNonNull(redirectUri, "Missing the ?redirect_uri=xxx query parameter")

        return ok(
            ansattService.findAll(includeGenerated = false)
                .sortedBy { it.displayName }
                .map { user: NAVAnsatt ->
                    val redirect = URIBuilder(redirectUri).apply {
                        addParameter("scope", scope)
                        addParameter("state", state)
                        addParameter("client_id", clientId)
                        addParameter("iss", getIssuer(tenant))
                        addParameter("redirect_uri", redirectUri)
                        addParameter("code", "${user.cn};$nonce")
                    }.toString()
                    UserEntry(username = user.cn, displayName = user.displayName, redirect = redirect, details = user)
                }
        )
    }

    @PostMapping(value = ["/{tenant}/v2.0/users"])
    @ApiOperation(
        value = "/v2.0/users",
        notes = "Creates a ansatt with the given attributes and issues a token for the ansatt"
    )
    fun newUser(
        @PathVariable("tenant") tenant: String,
        @RequestParam("client_id") clientId: String,
        @RequestBody ansattRequest: AnsattRequest
    ) = createUser(ansattRequest.groups).let { (cn) ->
        Oauth2AccessTokenResponse(
            idToken = createIdToken(
                code = cn,
                tenant = tenant,
                clientId = clientId
            ),
            refreshToken = "refresh:$cn",
            accessToken = "access:$cn"
        )
    }

    fun createUser(groups: List<String>) = ansattService.addAnnsatt(groups = groups, enheter = emptyList(), generated = true)

    private fun getFrontendUrl(req: HttpServletRequest): String {
        return req.scheme + "://" + req.serverName + ":" + req.serverPort + "/#"
    }

    private fun getBaseUrl(req: HttpServletRequest): String {
        return req.scheme + "://" + req.serverName + ":" + req.serverPort + "/rest/AzureAd"
    }

    private fun getGraphUrl(req: HttpServletRequest): String {
        return req.scheme + "://" + req.serverName + ":" + req.serverPort + "/rest/MicrosoftGraphApi"
    }

    private fun getIssuer(tenant: String): String {
        return "https://login.microsoftonline.com/$tenant/v2.0"
    }
}
