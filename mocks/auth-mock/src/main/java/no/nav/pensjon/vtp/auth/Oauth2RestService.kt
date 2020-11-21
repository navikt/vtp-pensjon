package no.nav.pensjon.vtp.auth

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import no.nav.pensjon.vtp.felles.JsonWebKeySupport
import no.nav.pensjon.vtp.felles.OidcTokenGenerator
import no.nav.pensjon.vtp.testmodell.ansatt.AnsatteIndeks
import no.nav.pensjon.vtp.testmodell.ansatt.NAVAnsatt
import org.apache.http.client.utils.URIBuilder
import org.slf4j.LoggerFactory.getLogger
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.badRequest
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*
import java.net.URISyntaxException
import java.util.*
import java.util.UUID.randomUUID
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest

@RestController
@Api(tags = ["Openam"])
@RequestMapping("/rest/isso")
class Oauth2RestService(private val ansatteIndeks: AnsatteIndeks, private val jsonWebKeySupport: JsonWebKeySupport, @Value("\${ISSO_OAUTH2_ISSUER}") val issuer: String) {
    private val logger = getLogger(Oauth2RestService::class.java)
    private val nonceCache: MutableMap<String, String> = HashMap()
    private val clientIdCache: MutableMap<String, String> = HashMap()

    @GetMapping(value = ["/oauth2/authorize"], produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_HTML_VALUE])
    @ApiOperation(value = "oauth2/authorize", notes = "Mock impl av Oauth2 authorize")
    @Throws(URISyntaxException::class)
    fun authorize(
            req: HttpServletRequest,
            @RequestParam(defaultValue = "winssochain") session: String?,
            @RequestParam(defaultValue = "service") authIndexType: String?,
            @RequestParam(defaultValue = "winssochain") authIndexValue: String?,
            @RequestParam(defaultValue = "code") responseType: String,
            @RequestParam(defaultValue = "openid") scope: String,
            @RequestParam client_id: String,
            @RequestParam state: String,
            @RequestParam redirect_uri: String,
            @RequestParam(required = false) nonce: String?
    ): ResponseEntity<*> {
        logger.info("kall mot oauth2/authorize med redirecturi $redirect_uri")

        require(scope == "openid") { "Unsupported scope [$scope], should be 'openid'" }
        require(responseType == "code") { "Unsupported responseType [$responseType], should be 'code'" }

        val uriBuilder = URIBuilder(redirect_uri)
        uriBuilder.addParameter("scope", scope)
        uriBuilder.addParameter("state", state)
        uriBuilder.addParameter("client_id", client_id)
        uriBuilder.addParameter("iss", issuer)
        uriBuilder.addParameter("redirect_uri", redirect_uri)
        clientIdCache[state] = client_id
        if (nonce != null && nonce != "") {
            nonceCache[state] = nonce
        }
        val acceptHeader: String? = req.getHeader("Accept-Header")
        return if ((null == req.contentType || req.contentType == "text/html") && (acceptHeader == null || !acceptHeader.contains("json"))) {
            authorizeHtmlPage(uriBuilder)
        } else {
            authorizeRedirect(uriBuilder)
        }
    }

    @Throws(URISyntaxException::class)
    private fun authorizeRedirect(location: URIBuilder): ResponseEntity<*> {
        // SEND JSON RESPONSE TIL OPENAM HELPER
        location.addParameter("code", "im-just-a-fake-code")
        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT).location(location.build()).build<Any>()
    }

    private fun authorizeHtmlPage(location: URIBuilder): ResponseEntity<*> {
        val html = ("""<!DOCTYPE html>
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
""" +
                ansatteIndeks.hentAlleAnsatte()
                        .sorted(Comparator.comparing { obj: NAVAnsatt -> obj.getDisplayName() })
                        .map { username: NAVAnsatt ->
                            """
     <tr><a href="$location&code=${username.cn}"><h1>${username.displayName}</h1></a></tr>
     
     """.trimIndent()
                        }
                        .collect(Collectors.joining("\n"))
                +
                "            </tbody>\n" +
                "        </table>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>")
        return ok(html)
    }

    // TODO (FC): Trengs denne fortsatt?
    @PostMapping(value = ["/oauth2/access_token"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiOperation(value = "oauth2/access_token", notes = "Mock impl av Oauth2 access_token")
    fun accessToken(
            req: HttpServletRequest,
            @RequestParam grant_type: String,
            @RequestParam(required = false) realm: String?,
            @RequestParam code: String,
            @RequestParam(required = false) refresh_token: String?,
            @RequestParam redirect_uri: String
    ): ResponseEntity<*> {
        return if ("authorization_code" == grant_type) {
            val token = createIdToken(req, code)
            logger.info("Fikk parametere:" + req.parameterMap.toString())
            logger.info("kall på /oauth2/access_token, opprettet token: $token med redirect-url: $redirect_uri")
            val generatedRefreshToken = "refresh:$code"
            val generatedAccessToken = "access:$code"
            val oauthResponse = Oauth2AccessTokenResponse(token, generatedRefreshToken, generatedAccessToken)
            ok(oauthResponse)
        } else if ("refresh_token" == grant_type) {
            if (refresh_token == null) {
                badRequest().body("Missing required parameter 'request_token'")
            } else {
                if (!refresh_token.startsWith("refresh:")) {
                    val message = "Invalid refresh token $refresh_token"
                    logger.error(message)
                    ResponseEntity.status(HttpStatus.FORBIDDEN).body(message)
                } else {
                    val username = refresh_token.substring(8)
                    val token = createIdToken(req, username)
                    logger.info("Fikk parametere:" + req.parameterMap.toString())
                    logger.info("refresh-token-kall på /oauth2/access_token, opprettet nytt token: $token")
                    val generatedRefreshToken = "refresh:$username"
                    val generatedAccessToken = "access:$username"
                    val oauthResponse = Oauth2AccessTokenResponse(token, generatedRefreshToken, generatedAccessToken)
                    ok(oauthResponse)
                }
            }
        } else {
            logger.error("Unknown grant_type $grant_type")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unknown grant_type $grant_type")
        }
    }

    private fun createIdToken(req: HttpServletRequest, username: String): String {
        val state = req.getParameter("state")

        val tokenGenerator = OidcTokenGenerator(
                jsonWebKeySupport = jsonWebKeySupport,
                subject = username,
                nonce = nonceCache[state],
                issuer = issuer
        )
        clientIdCache[state]?.let { tokenGenerator.addAud(it) }

        return tokenGenerator.create()
    }

    @GetMapping(value = ["/isAlive.jsp"], produces = [MediaType.TEXT_HTML_VALUE])
    fun isAliveMock() = ok("Server is ALIVE")

    @GetMapping(value = ["/oauth2/../isAlive.jsp"], produces = [MediaType.TEXT_HTML_VALUE])
    fun isAliveMockRassUrl() = ok("Server is ALIVE")

    @GetMapping(value = ["/oauth2/connect/jwk_uri"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiOperation(value = "oauth2/connect/jwk_uri", notes = "Mock impl av Oauth2 jwk_uri")
    fun authorize(req: HttpServletRequest?): ResponseEntity<String> {
        return ok(jsonWebKeySupport.jwks())
    }

    /**
     * brukes til autentisere bruker slik at en slipper å autentisere senere. OpenAM mikk-makk .
     */
    @PostMapping(value = ["/json/authenticate"], produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ApiOperation(value = "json/authenticate", notes = "Mock impl av OpenAM autenticate for service bruker innlogging")
    fun serviceBrukerAuthenticate(
            @ApiParam("Liste over aksjonspunkt som skal bekreftes, inklusiv data som trengs for å løse de.") enduserTemplate: EndUserAuthenticateTemplate?
    ): ResponseEntity<*> =
            if (enduserTemplate == null) {
                ok(EndUserAuthenticateTemplate(
                        authId = randomUUID().toString(),
                        header = "Sign in to VTP",
                        stage = "DataStore1",
                        template = "",
                        callbacks = listOf(
                                Callback(
                                        type = "NameCallback",
                                        input = Name("IDToken1", ""),
                                        output = Name("prompt", "User Name:")
                                ),
                                Callback(
                                        type = "PasswordCallback",
                                        input = Name("IDToken2", ""),
                                        output = Name("prompt", "Password:")
                                )
                        )
                ))
            } else {
                // generer token som brukes til å bekrefte innlogging ovenfor openam

                // TODO ingen validering av authId?
                // TODO generer unik session token?
                ok(EndUserAuthenticateSuccess("i-am-just-a-dummy-session-token-workaround", "/isso/console"))
            }

    @GetMapping(value = ["/oauth2/.well-known/openid-configuration"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiOperation(value = "Discovery url", notes = "Mock impl av discovery urlen. ")
    fun wellKnown(req: HttpServletRequest) =
            ok(WellKnownResponse(
                    baseUrl = req.scheme + "://" + req.serverName + ":" + req.serverPort,
                    issuer = issuer)
            )
}
