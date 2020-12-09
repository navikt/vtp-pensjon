package no.nav.pensjon.vtp.auth

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import no.nav.pensjon.vtp.testmodell.ansatt.AnsatteIndeks
import no.nav.pensjon.vtp.testmodell.ansatt.NAVAnsatt
import org.apache.http.client.utils.URIBuilder
import org.slf4j.LoggerFactory.getLogger
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus.*
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.MediaType.TEXT_HTML_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*
import java.net.URISyntaxException
import java.util.*
import java.util.UUID.randomUUID
import javax.servlet.http.HttpServletRequest

@RestController
@Api(tags = ["Openam"])
@RequestMapping("/rest/isso")
class Oauth2RestService(private val ansatteIndeks: AnsatteIndeks, private val jsonWebKeySupport: JsonWebKeySupport, @Value("\${ISSO_OAUTH2_ISSUER}") val issuer: String) {
    private val logger = getLogger(Oauth2RestService::class.java)
    private val clientIdCache: MutableMap<String, String> = HashMap()

    data class UserEntry(val username: String, val displayName: String, val redirect: String)

    @GetMapping(value = ["/oauth2/users"], produces = [APPLICATION_JSON_VALUE])
    @ApiOperation(value = "oauth2/users", notes = "Liste over brukere til OpenAM-innlogging")
    @Throws(URISyntaxException::class)
    fun users(
            req: HttpServletRequest,
            @RequestParam(defaultValue = "winssochain") session: String?,
            @RequestParam(defaultValue = "service") authIndexType: String?,
            @RequestParam(defaultValue = "winssochain") authIndexValue: String?,
            @RequestParam(defaultValue = "code") responseType: String,
            @RequestParam(defaultValue = "openid") scope: String,
            @RequestParam("client_id") clientId: String,
            @RequestParam state: String,
            @RequestParam("redirect_uri") redirectUri: String,
            @RequestParam(required = false) nonce: String?
    ): List<UserEntry> {
        logger.info("kall mot oauth2/users med redirecturi $redirectUri")

        require(scope == "openid") { "Unsupported scope [$scope], should be 'openid'" }
        require(responseType == "code") { "Unsupported responseType [$responseType], should be 'code'" }

        clientIdCache[state] = clientId

        return ansatteIndeks.findAll()
                .sortedBy { it.displayName }
                .map { user: NAVAnsatt ->
                    val uriBuilder = URIBuilder(redirectUri)
                    uriBuilder.addParameter("scope", scope)
                    uriBuilder.addParameter("state", state)
                    uriBuilder.addParameter("client_id", clientId)
                    uriBuilder.addParameter("iss", issuer)
                    uriBuilder.addParameter("redirect_uri", redirectUri)
                    uriBuilder.addParameter("code", "${user.cn};${nonce ?: ""}")
                    val redirect = uriBuilder.toString()
                    UserEntry(username = user.cn, displayName = user.displayName, redirect = redirect)
                }
    }

    // TODO (FC): Trengs denne fortsatt?
    @PostMapping(value = ["/oauth2/access_token"], produces = [APPLICATION_JSON_VALUE])
    @ApiOperation(value = "oauth2/access_token", notes = "Mock impl av Oauth2 access_token")
    fun accessToken(
            req: HttpServletRequest,
            @RequestParam grant_type: String,
            @RequestParam(required = false) realm: String?,
            @RequestParam code: String,
            @RequestParam(required = false) refresh_token: String?,
            @RequestParam redirect_uri: String
    ): ResponseEntity<*> {
        return when (grant_type) {
            "authorization_code" -> {
                ok(Oauth2AccessTokenResponse(
                        idToken = createIdToken(req, code),
                        refreshToken = "refresh:$code",
                        accessToken = "access:$code"))
            }
            "refresh_token" -> {
                return refresh_token
                        ?.let {
                            if (!refresh_token.startsWith("refresh:")) {
                                status(FORBIDDEN).body("Invalid refresh token $refresh_token")
                            } else {
                                ok(Oauth2AccessTokenResponse(
                                        idToken = createIdToken(req, refresh_token.substring(8)),
                                        refreshToken = "refresh:${refresh_token.substring(8)}",
                                        accessToken = "access:${refresh_token.substring(8)}"
                                ))
                            }

                        }
                        ?: badRequest().body("Missing required parameter 'request_token'")
            }
            else -> {
                logger.error("Unknown grant_type $grant_type")
                status(BAD_REQUEST).body("Unknown grant_type $grant_type")
            }
        }
    }

    private fun createIdToken(req: HttpServletRequest, code: String): String {
        val codeData = code.split(";".toRegex()).toTypedArray()
        val state = req.getParameter("state")

        val tokenGenerator = OidcTokenGenerator(
                jsonWebKeySupport = jsonWebKeySupport,
                subject = codeData[0],
                nonce = if (codeData.size > 1) codeData[1] else null,
                issuer = issuer
        )
        clientIdCache[state]?.let { tokenGenerator.addAud(it) }

        return tokenGenerator.create()
    }

    @GetMapping(value = ["/isAlive.jsp"], produces = [TEXT_HTML_VALUE])
    fun isAliveMock() = "Server is ALIVE"

    @GetMapping(value = ["/oauth2/../isAlive.jsp"], produces = [TEXT_HTML_VALUE])
    fun isAliveMockRassUrl() = "Server is ALIVE"

    @GetMapping(value = ["/oauth2/connect/jwk_uri"], produces = [APPLICATION_JSON_VALUE])
    @ApiOperation(value = "oauth2/connect/jwk_uri", notes = "Mock impl av Oauth2 jwk_uri")
    fun authorize(req: HttpServletRequest?) = jsonWebKeySupport.jwks()

    /**
     * brukes til autentisere bruker slik at en slipper å autentisere senere. OpenAM mikk-makk .
     */
    @PostMapping(value = ["/json/authenticate"], produces = [APPLICATION_JSON_VALUE], consumes = [APPLICATION_JSON_VALUE])
    @ApiOperation(value = "json/authenticate", notes = "Mock impl av OpenAM autenticate for service bruker innlogging")
    fun serviceBrukerAuthenticate(
            @ApiParam("Liste over aksjonspunkt som skal bekreftes, inklusiv data som trengs for å løse de.") enduserTemplate: EndUserAuthenticateTemplate?
    ) = enduserTemplate
            ?.let {
                EndUserAuthenticateTemplate(
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
                )
            }
    // TODO ingen validering av authId?
    // TODO generer unik session token?
    // generer token som brukes til å bekrefte innlogging ovenfor openam
            ?: EndUserAuthenticateSuccess("i-am-just-a-dummy-session-token-workaround", "/isso/console")

    private fun getFrontendUrl(req: HttpServletRequest): String {
        return req.scheme + "://" + req.serverName + ":" + req.serverPort + "/#"
    }

    @GetMapping(value = ["/oauth2/.well-known/openid-configuration"], produces = [APPLICATION_JSON_VALUE])
    @ApiOperation(value = "Discovery url", notes = "Mock impl av discovery urlen. ")
    fun wellKnown(req: HttpServletRequest) =
            WellKnownResponse(
                    frontendUrl = getFrontendUrl(req),
                    baseUrl = req.scheme + "://" + req.serverName + ":" + req.serverPort,
                    issuer = issuer
            )
}
