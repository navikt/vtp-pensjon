package no.nav.pensjon.vtp.auth.azuread

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import no.nav.pensjon.vtp.auth.JsonWebKeySupport
import no.nav.pensjon.vtp.auth.Oauth2AccessTokenResponse
import no.nav.pensjon.vtp.testmodell.ansatt.AnsatteIndeks
import no.nav.pensjon.vtp.testmodell.ansatt.NAVAnsatt
import org.apache.http.client.utils.URIBuilder
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.MediaType.TEXT_HTML_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.badRequest
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@Api(tags = ["AzureAd"])
@RequestMapping("/rest/AzureAd")
class AzureAdNAVAnsattService(private val ansatteIndeks: AnsatteIndeks, private val jsonWebKeySupport: JsonWebKeySupport) {
    @GetMapping(value = ["/isAlive"], produces = [TEXT_HTML_VALUE])
    fun isAliveMock() = ok("Azure AD is OK")

    @GetMapping(value = ["/{tenant}/v2.0/.well-known/openid-configuration"], produces = [APPLICATION_JSON_VALUE])
    @ApiOperation(value = "Azure AD Discovery url", notes = "Mock impl av Azure AD discovery urlen. ")
    fun wellKnown(req: HttpServletRequest, @PathVariable("tenant") tenant: String, @RequestParam("p") profile: String?) =
        WellKnownResponse(
            frontendUrl = getFrontendUrl(req),
            baseUrl = getBaseUrl(req),
            graphUrl = getGraphUrl(req),
            tenant = tenant,
            profile = profile
        )

    @GetMapping(value = ["/{tenant}/discovery/v2.0/keys"], produces = [APPLICATION_JSON_VALUE])
    @ApiOperation(value = "azureAd/discovery/keys", notes = "Mock impl av Azure AD jwk_uri")
    fun authorize() = jsonWebKeySupport.jwks()

    @PostMapping(value = ["/{tenant}/oauth2/v2.0/token"], produces = [APPLICATION_JSON_VALUE])
    @ApiOperation(value = "azureAd/access_token", notes = "Mock impl av Azure AD access_token")
    fun accessToken(
        req: HttpServletRequest,
        @PathVariable("tenant") tenant: String,
        @RequestParam("grant_type") grantType: String,
        @RequestParam("client_id") clientId: String,
        @RequestParam("realm") realm: String?,
        @RequestParam("code") code: String,
        @RequestParam("refresh_token", required = false) refreshToken: String?,
        @RequestParam("redirect_uri") redirectUri: String
    ): ResponseEntity<*> {
        return when (grantType) {
            "authorization_code" -> {
                val token = createIdToken(code, tenant, clientId)
                logger.info("kall på /oauth2/access_token, opprettet token: $token med redirect-url: $redirectUri")
                ok(Oauth2AccessTokenResponse(idToken = token, refreshToken = "refresh:$code", accessToken = "access:$code"))
            }
            "refresh_token" -> {
                if (refreshToken == null) {
                    badRequest().body("Missing required parameter 'request_token'")
                } else {
                    val usernameWithNonce = refreshToken.substring(8)
                    val token = createIdToken(usernameWithNonce /*+ ";"*/, tenant, clientId)
                    logger.info("Fikk parametere:" + req.parameterMap.toString())
                    logger.info("refresh-token-kall på /oauth2/access_token, opprettet nytt token: $token")
                    ok(Oauth2AccessTokenResponse(idToken = token, refreshToken = "refresh:$usernameWithNonce", accessToken = "access:$usernameWithNonce"))
                }
            }
            else -> {
                logger.error("Unknown grant_type $grantType")
                badRequest().body("Unknown grant_type $grantType")
            }
        }
    }

    private fun createIdToken(code: String, tenant: String, clientId: String): String {
        val codeData = code.split(";".toRegex()).toTypedArray()
        val user = ansatteIndeks.findByCn(codeData[0]) ?: throw RuntimeException("Fant ikke NAV-ansatt med brukernavn ${codeData[0]}")

        return azureOidcToken(
            jsonWebKeySupport = jsonWebKeySupport,
            subject = codeData[0],
            nonce = if (codeData.size > 1) codeData[1] else null,
            issuer = getIssuer(tenant),
            groups = user.groups.stream().map { ldapGroupName: String? -> AzureADGroupMapping.toAzureGroupId(ldapGroupName) }.collect(Collectors.toList()),
            aud = listOf(clientId),
            additionalClaims = mapOf(
                "tid" to tenant,
                "oid" to UUID.nameUUIDFromBytes(user.cn.toByteArray()).toString(), // user id - which is normally a UUID in Azure AD
                "name" to user.displayName,
                "preferred_username" to user.email
            )
        )
    }

    data class UserEntry(val username: String, val displayName: String, val redirect: String)
    @GetMapping(value = ["/{tenant}/v2.0/users"], produces = [APPLICATION_JSON_VALUE])
    @ApiOperation(value = "/v2.0/users", notes = "Hent brukere/saksbehandlere man kan logge inn som")
    @Throws(Exception::class)
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
        logger.info("kall mot AzureAD authorize med redirecturi $redirectUri")

        val validScopes = Arrays.asList("openid", "profile", "offline_access")
        val scopes = scope.split("\\s+".toRegex()).toTypedArray()

        scopes.forEach {
            require(validScopes.contains(it)) { "Unsupported scope [$it], supported scopes are: ${validScopes.joinToString(separator = ", ")}" }
        }

        require(responseType == "code") { "Unsupported responseType [$responseType], should be 'code'" }

        Objects.requireNonNull(clientId, "Missing the ?client_id=xxx query parameter")
        Objects.requireNonNull(state, "Missing the ?state=xxx query parameter")
        Objects.requireNonNull(redirectUri, "Missing the ?redirect_uri=xxx query parameter")

        return ok(
            ansatteIndeks.findAll()
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
                    UserEntry(username = user.cn, displayName = user.displayName, redirect = redirect)
                }
        )
    }

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

    data class JsonResponse(val message: String)
    @ExceptionHandler(value = [Exception::class])
    fun handleException(e: Exception): ResponseEntity<JsonResponse> {
        return ResponseEntity(JsonResponse(e.message ?: ""), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AzureAdNAVAnsattService::class.java)
    }
}
