package no.nav.pensjon.vtp.auth.azuread

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import no.nav.pensjon.vtp.auth.Oauth2AccessTokenResponse
import no.nav.pensjon.vtp.auth.JsonWebKeySupport
import no.nav.pensjon.vtp.testmodell.ansatt.AnsatteIndeks
import org.apache.http.client.utils.URIBuilder
import org.slf4j.LoggerFactory
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

    // Authorize URL:
    // https://login.microsoftonline.com/966ac572-f5b7-4bbe-aa88-c76419c0f851/oauth2/v2.0/authorize
    // ?response_type=code
    // &client_id=15f01fee-bd6d-4427-bb70-fc9e75caa13a
    // &redirect_uri=http%3A%2F%2Flocalhost%3A9080%2Fpsak%2Foidc%2Fcallback
    // &scope=openid+profile+user.read
    // &state=PpZ9uI3drAWm7vfLM1rvb-ev4fvzc9RmHqQxW725wWE
    // &nonce=xJDu2DjmaiFY8ivIakELFLVZDY6OivuE1GAUJ0fIEf0
    // &prompt=select_account
    @GetMapping(value = ["/{tenant}/v2.0/authorize"], produces = [APPLICATION_JSON_VALUE, TEXT_HTML_VALUE])
    @ApiOperation(value = "AzureAD/v2.0/authorize", notes = "Mock impl av Azure AD authorize")
    @Throws(Exception::class)
    fun authorize(
            req: HttpServletRequest?,
            resp: HttpServletResponse?,
            @PathVariable("tenant") tenant: String,
            @RequestParam(value = "response_type", defaultValue = "code") responseType: String,
            @RequestParam(value = "scope", defaultValue = "openid") scope: String,
            @RequestParam("client_id") clientId: String,
            @RequestParam("state") state: String,
            @RequestParam("nonce") nonce: String,
            @RequestParam("redirect_uri") redirectUri: String
    ): String {
        logger.info("kall mot AzureAD authorize med redirecturi $redirectUri")

        val validScopes = Arrays.asList("openid", "profile", "offline_access")
        val scopes = scope.split("\\s+".toRegex()).toTypedArray()

        scopes.forEach {
            require(validScopes.contains(it)) { "Unsupported scope [$it], supported scopes are: ${validScopes.joinToString(separator = ", ")}" }
        }

        require(responseType == "code") { "Unsupported responseType [$responseType], should be 'code'" }

        val uriBuilder = URIBuilder(redirectUri).apply {
            addParameter("scope", scope)
            addParameter("state", state)
            addParameter("client_id", clientId)
            addParameter("iss", getIssuer(tenant))
            addParameter("redirect_uri", redirectUri)
        }
        return authorizeHtmlPage(uriBuilder, nonce)
    }

    private fun authorizeHtmlPage(location: URIBuilder, nonce: String): String {
        val userRows = ansatteIndeks.findAll()
                .sortedBy { it.displayName }
                .joinToString(separator = "\n                ") {
                    """<tr><a href="$location&code=${it.cn};$nonce"><h1>${it.displayName}</h1></a></tr>""".trimIndent()
                }

        return """<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Velg bruker</title>
</head>
    <body>
    <div style="text-align:center;width:100%;">
       <caption><h3>Velg bruker:</h3></caption>
        <table>
            <tbody>
                $userRows
            </tbody>
        </table>
    </div>
    </body>
</html>"""
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

    companion object {
        private val logger = LoggerFactory.getLogger(AzureAdNAVAnsattService::class.java)
    }
}
