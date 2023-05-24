package no.nav.pensjon.vtp.auth.azuread

import com.nimbusds.jose.JOSEObject
import com.nimbusds.jwt.JWTClaimsSet
import io.swagger.v3.oas.annotations.Operation
import no.nav.pensjon.vtp.auth.*
import no.nav.pensjon.vtp.auth.JsonWebKeySupport.*
import no.nav.pensjon.vtp.testmodell.ansatt.AnsattService
import no.nav.pensjon.vtp.testmodell.ansatt.NAVAnsatt
import no.nav.pensjon.vtp.util.asResponseEntity
import org.apache.http.client.utils.URIBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus
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
    @Value("\${AZUREAD_OAUTH2_ISSUER:#{null}}") private val issuer: String?,
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
                .queryParams(requestParams).build().query

            fromUriString(getFrontendUrl(req))
                .fragment("/azuread/$tenant/v2.0/authorize?$query")
                .build()
                .toUri()
        }

        return ResponseEntity
            .status(TEMPORARY_REDIRECT)
            .location(uri)
            .build()
    }

    @GetMapping(value = ["/{tenant}/v2.0/.well-known/openid-configuration"], produces = [APPLICATION_JSON_VALUE])
    @Operation(summary = "Azure AD Discovery url", description = "Mock impl av Azure AD discovery urlen. ")
    fun wellKnown(req: HttpServletRequest, @PathVariable("tenant") tenant: String, @RequestParam("p") profile: String?) =
        WellKnownResponse(
            issuer = getIssuer(tenant, profile),
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
        @RequestHeader(AUTHORIZATION) authorization: String?,
        @PathVariable("tenant") tenant: String,
        @RequestParam("grant_type") grantType: String?,
        @RequestParam("client_id") inputClientId: String?,
        @RequestParam("realm") realm: String?,
        @RequestParam("code") code: String?,
        @RequestParam("refresh_token", required = false) refreshToken: String?,
        @RequestParam("scope", required = false) scope: String?,
        @RequestParam("requested_token_use", required = false) requestedTokenUse: String?,
        @RequestParam("assertion", required = false) assertion: String?,
        @RequestParam("client_assertion", required = false) clientAssertion: String?,
        @RequestParam("client_assertion_type", required = false) clientAssertionType: String?
    ): ResponseEntity<*> {
        val clientId = getUser(authorization)
            ?: getClientId(clientAssertion, clientAssertionType)
            ?: inputClientId
            ?: return badRequest().body("Must supply either a AUTHORIZATION header or client_id request param")

        return when (grantType) {
            "authorization_code" -> {
                if (code == null) {
                    badRequest().body("Missing required parameter 'code'")
                } else {
                    val codeSplit = code.split(";")
                    val ansattId = code.ansattIdFromCode()
                    val nonce = if (codeSplit.size > 1) codeSplit[1] else null

                    ok(
                        Oauth2AccessTokenResponse(
                            idToken = createToken(
                                audience = listOf(clientId),
                                ansattId = ansattId,
                                tenant = tenant,
                                clientId = clientId,
                                nonce = nonce,
                                scope = scope,
                                sid = code
                            ),
                            refreshToken = createToken(
                                audience = listOf(clientId),
                                ansattId = ansattId,
                                tenant = tenant,
                                clientId = clientId,
                                nonce = nonce,
                                scope = scope,
                                sid = code
                            ),
                            accessToken = createToken(
                                audience = listOf(clientId),
                                ansattId = ansattId,
                                tenant = tenant,
                                clientId = clientId,
                                nonce = nonce,
                                scope = scope,
                                sid = code
                            ),
                        )
                    )
                }
            }
            "refresh_token" -> {
                if (refreshToken == null) {
                    badRequest().body("Missing required parameter 'request_token'")
                } else {
                    val claims = JWTClaimsSet.parse(JOSEObject.parse(refreshToken).payload.toJSONObject())
                    ok(
                        Oauth2AccessTokenResponse(
                            idToken = createToken(
                                audience = listOf(clientId),
                                ansattId = claims.subject.ansattIdFromSubject(),
                                tenant = tenant,
                                clientId = clientId,
                                scope = scope,
                                nonce = claims.getStringClaimOrNull("nonce")
                            ),
                            refreshToken = createToken(
                                audience = listOf(clientId),
                                ansattId = claims.subject.ansattIdFromSubject(),
                                tenant = tenant,
                                clientId = clientId,
                                scope = scope,
                                nonce = claims.getStringClaimOrNull("nonce")
                            ),
                            accessToken = createToken(
                                audience = listOf(clientId),
                                ansattId = claims.subject.ansattIdFromSubject(),
                                tenant = tenant,
                                clientId = clientId,
                                scope = scope,
                                nonce = claims.getStringClaimOrNull("nonce")
                            )
                        )
                    )
                }
            }
            // https://docs.microsoft.com/en-us/azure/active-directory/develop/v2-oauth2-on-behalf-of-flow
            "urn:ietf:params:oauth:grant-type:jwt-bearer" -> {
                if (requestedTokenUse != "on_behalf_of") {
                    badRequest().body("Unknown requested_token_use $requestedTokenUse")
                } else if (scope.isNullOrBlank()) {
                    badRequest().body("Missing scope for on_behalf_of flow")
                } else if (assertion.isNullOrBlank()) {
                    badRequest().body("Missing assertion for on_behalf_of flow")
                } else {
                    val claims = JWTClaimsSet.parse(JOSEObject.parse(assertion).payload.toJSONObject())

                    if (!claims.audience.contains(clientId)) {
                        badRequest().body("Audience must contain clientId. Audience=${claims.audience.joinToString(",","[","]")} clientId=$clientId")
                    }
                    ok(
                        Oauth2AccessTokenResponse(
                            idToken = createToken(
                                audience = listOf(clientId),
                                ansattId = claims.subject.ansattIdFromSubject(),
                                tenant = tenant,
                                clientId = clientId,
                                scope = scope,
                                nonce = claims.getStringClaimOrNull("nonce")
                            ),
                            refreshToken = createToken(
                                audience = listOf(clientId),
                                ansattId = claims.subject.ansattIdFromSubject(),
                                tenant = tenant,
                                clientId = clientId,
                                scope = scope,
                                nonce = claims.getStringClaimOrNull("nonce")
                            ),
                            accessToken = createToken(
                                audience = scope.split(" ").filter { it.startsWith("api://") }.map { it.substring("api://".length).replaceAfter("/", "").replace("/", "") },
                                ansattId = claims.subject.ansattIdFromSubject(),
                                tenant = tenant,
                                clientId = clientId,
                                scope = scope,
                                nonce = claims.getStringClaimOrNull("nonce")
                            )
                        )
                    )
                }
            }
            "client_credentials" -> {
                if (scope.isNullOrBlank()) {
                    badRequest().body("Missing scope for client_credentials flow")
                } else {
                    ok(
                        Oauth2AccessTokenResponse(
                            accessToken = azureSystemToken(
                                aud = listOf(scope),
                                sub = UUID.randomUUID().toString().substring(0,19),
                                clientId = clientId,
                                issuer = getIssuer(tenant),
                                tenantId = tenant,
                                jsonWebKeySupport = jsonWebKeySupport
                            ),
                            idToken = "",
                            refreshToken = ""
                        )
                    )
                }
            }
            null -> {
                badRequest().body("Missing required parameter grant_type")
            }
            else -> {
                badRequest().body("Unknown grant_type $grantType")
            }
        }
    }

    private fun createToken(audience: List<String>, ansattId: String, tenant: String, clientId: String, nonce: String? = null, scope: String? = null, sid: String? = null, requestedIssuer: String? = null): String {
        val user = ansattService.findByCn(ansattId)
            ?: throw RuntimeException("Fant ikke NAV-ansatt med brukernavn $ansattId")

        return azureOidcToken(
            sub = "$clientId:$ansattId",
            jsonWebKeySupport = jsonWebKeySupport,
            email = user.email,
            nonce = nonce,
            issuer = requestedIssuer ?: getIssuer(tenant),
            groups = user.groups.map { toAzureGroupId(it) },
            aud = audience,
            additionalClaims = mapOf(
                "tid" to tenant,
                "oid" to UUID.nameUUIDFromBytes(user.cn.toByteArray())
                    .toString(), // user id - which is normally a UUID in Azure AD
                "name" to user.displayName,
                "preferred_username" to user.email,
                "NAVident" to ansattId
            ),
            scope = scope,
            sid = sid,
        )
    }

    @GetMapping(value = ["/{tenant}/v2.0/users"], produces = [APPLICATION_JSON_VALUE])
    @Operation(summary = "/v2.0/users", description = "Hent brukere/saksbehandlere man kan logge inn som")
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
    @Operation(
        summary = "/v2.0/users",
        description = "Creates a ansatt with the given attributes and issues a token for the ansatt"
    )
    fun newUser(
        @PathVariable("tenant") tenant: String,
        @RequestParam("client_id") clientId: String,
        @RequestBody ansattRequest: AnsattRequest
    ) = createUser(ansattRequest.groups).let { (cn) ->
        Oauth2AccessTokenResponse(
            idToken = createToken(
                audience = listOf(clientId),
                ansattId = cn,
                tenant = tenant,
                clientId = clientId,
            ),
            refreshToken = createToken(
                audience = listOf(clientId),
                ansattId = cn,
                tenant = tenant,
                clientId = clientId,
            ),
            accessToken = createToken(
                audience = listOf(clientId),
                ansattId = cn,
                tenant = tenant,
                clientId = clientId,
            )
        )
    }

    @PostMapping("/{tenant}/v2.0/ansatt")
    fun newAnsatt(
        @PathVariable("tenant") tenant: String,
        @RequestHeader(AUTHORIZATION) authorization: String?,
        @RequestBody ansattRequest: AnsattRequest,
        @RequestParam("issuer") requestedIssuer: String?,
    ): ResponseEntity<*> = getUser(authorization)
        ?.let { clientId ->
            createUser(ansattRequest.groups).let { (cn) ->
                val sid = UUID.randomUUID().toString()
                ok(
                    Oauth2AccessTokenResponse(
                        idToken = createToken(
                            audience = listOf(clientId),
                            ansattId = cn,
                            tenant = tenant,
                            clientId = clientId,
                            scope = "foo",
                            sid = sid,
                            requestedIssuer = requestedIssuer,
                        ),
                        refreshToken = createToken(
                            audience = listOf(clientId),
                            ansattId = cn,
                            tenant = tenant,
                            clientId = clientId,
                            scope = "foo",
                            sid = sid,
                            requestedIssuer = requestedIssuer,
                        ),
                        accessToken = createToken(
                            audience = listOf(clientId),
                            ansattId = cn,
                            tenant = tenant,
                            clientId = clientId,
                            scope = "foo",
                            sid = sid,
                            requestedIssuer = requestedIssuer,
                        ),
                    )
                )
            }
        }
        ?: ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing basic authorization header")

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

    private fun getIssuer(tenant: String, profile: String? = null) =
        if (tenant == "NAVtestB2C.onmicrosoft.com") {
            // Spesialhåndtering for Azure AD B2C.
            // Veldig rart at Azure AD gjør det sånn, men vi får mocke det realistisk, det sparer oss
            // for en del problemer andre steder (f.eks. LoginService sin issuer)
            "https://login.microsoftonline.com/d38f25aa-eab8-4c50-9f28-ebf92c1256f2/v2.0" + if ("B2C_1A_idporten_ver1" == profile) "/" else ""
        } else {
            issuer
                ?: "https://login.microsoftonline.com/$tenant/v2.0"
        }

    companion object {
        private fun String.ansattIdFromCode(): String = split(";")[0]
        private fun String.ansattIdFromSubject(): String = split(":")[1]

        private fun JWTClaimsSet.getStringClaimOrNull(name: String): String? =
            getClaim(name) as? String
    }
}
