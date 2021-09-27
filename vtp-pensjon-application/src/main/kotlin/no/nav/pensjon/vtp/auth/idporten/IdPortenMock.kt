package no.nav.pensjon.vtp.auth.idporten

import io.swagger.annotations.ApiOperation
import no.nav.pensjon.vtp.auth.JsonWebKeySupport
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository
import no.nav.pensjon.vtp.util.asResponseEntity
import no.nav.pensjon.vtp.util.withoutQueryParameters
import org.jose4j.jwt.JwtClaims
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.HtmlUtils
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/rest/idporten")
class IdPortenMock(
    private val jsonWebKeySupport: JsonWebKeySupport,
    private val personModellRepository: PersonModellRepository,
) {
    // dummy method to get URI
    @GetMapping
    fun dummy() = "pong".asResponseEntity()

    @GetMapping("/userinfo")
    fun userinfo() = "userinfo".asResponseEntity()

    @GetMapping(value = ["/.well-known/openid-configuration"])
    @ApiOperation(value = "Idporten well-known URL", notes = "Mock impl av ID-porten well-known URL. ")
    fun wellKnown(req: HttpServletRequest): WellKnownResponse {
        return WellKnownResponse(
            issuer = issuer(),
            authorization_endpoint = linkTo<IdPortenMock> { loginUI("foo", "bar") }.toUri().withoutQueryParameters(),
            jwks_uri = linkTo<IdPortenMock> { jwks() }.toUri(),
            userinfo_endpoint = linkTo<IdPortenMock> { userinfo() }.toUri(),
            token_endpoint = linkTo<IdPortenMock> {
                token(
                    grantType = "",
                    client_assertion_type = "",
                    client_assertion = "",
                    client_id = "",
                    code = "",
                )
            }.toUri().withoutQueryParameters(),
        )
    }

    @GetMapping("/jwks")
    @ApiOperation(value = "Idporten public key set")
    fun jwks() = Keys(jsonWebKeySupport.jwks())

    @GetMapping("/privateKey")
    @ApiOperation(value = "Idporten public key set")
    fun privateKey() = jsonWebKeySupport.privateKey()

    @PostMapping("/token")
    @ApiOperation(value = "Exchange token via tokendings")
    fun token(
        @RequestParam("grant_type") grantType: String,
        @RequestParam("code") code: String,
        @RequestParam("client_id") client_id: String,
        @RequestParam("client_assertion") client_assertion: String,
        @RequestParam("client_assertion_type") client_assertion_type: String,
    ) = TokenResponse(
        access_token = accessToken(
            jsonWebKeySupport = jsonWebKeySupport,
            issuer = issuer(),
            audience = client_id,
            pid = code,
        ),
        expires_in = 3600,
        id_token = accessToken(
            jsonWebKeySupport = jsonWebKeySupport,
            issuer = issuer(),
            audience = client_id,
            pid = code,
        ),
        refresh_token = "refresh:$code",
        scope = client_id,
        ).asResponseEntity()


    // Deprecated: Use the /#/loginservice/login endpoint instead. Leave this legacy endpoint just for a little while, while consumers update.
    @GetMapping(value = ["/login-ui"])
    fun loginUI(@RequestParam("redirect_uri") redirect: String, @RequestParam("nonce") nonce: String): ResponseEntity<String> {
        val persons = personModellRepository.findAll()


        val usersText = if (persons.isNotEmpty()) {
            persons
                .joinToString("\n") { (fnr, _, fornavn, etternavn) ->
                    val redirectUriString = URLEncoder.encode(UriComponentsBuilder.fromUri(URI(redirect)).queryParam("code", fnr).queryParam("nonce", nonce).build().toUri().toString(), StandardCharsets.UTF_8)

                    val navn = "$fornavn $etternavn"
                    "<a href=\"login?fnr=$fnr&redirect_uri=$redirectUriString\">$navn</a> ($fnr)<br>"
                }
        } else {
            "Det finnes ingen personer i VTP akkurat nå. Prøv å <a href=\"/#/\">laste inn et scenario</a>!"
        }

        return """
<!DOCTYPE html>
<html>
    <head>
        <title>Velg bruker</title>
    </head>

    <body>
        <div style="text-align:center;width:100%;">
            <h1>ID-porten Mock</h1>
            <h3>Velg bruker:</h3>
            $usersText
            <form method="get" action="login-redirect-with-cookie">
                <input type="hidden" name="redirect" value="${HtmlUtils.htmlEscape(redirect)}">
                <input name="fnr" placeholder="Fyll inn et annet fødselsnummer" style="width: 200px">
                <input type="submit">
            </form>
        </div>
    </body>
</html>
""".asResponseEntity()
    }

    @GetMapping(value = ["/login"])
    fun doLogin(@RequestParam("redirect_uri") redirect: String, @RequestParam("fnr") fnr: String): ResponseEntity<*> {
        return ResponseEntity
            .status(HttpStatus.TEMPORARY_REDIRECT)
            .location(
                URI(redirect)
            )
            .build<Any>()
    }

    @GetMapping(value = ["/users"])
    fun getUsers(@RequestParam("redirect_uri") redirect: String): List<UserSummary> =
        personModellRepository.findAll().map {
            UserSummary(
                ident = it.ident,
                firstName = it.fornavn,
                lastName = it.etternavn,
                redirect = linkTo<IdPortenMock> { doLogin(redirect, it.ident) }.toUri()
            )
        }

    data class TokenResponse(
        val access_token: String,
        val expires_in: Int,
        val id_token: String,
        val refresh_token: String,
        val scope: String,
    )

    data class WellKnownResponse(
        val issuer: URI,
        val authorization_endpoint: URI,
        val token_endpoint: URI,
        val jwks_uri: URI,
        val userinfo_endpoint: URI,
        // end_session_endpoint
        // revocation_endpoint
        // introspection_endpoint

        val response_types_supported: List<String> = listOf("code", "id_token", "id_token token", "token"),
        val response_modes_supported: List<String> = listOf("query", "form_post", "fragment"),
        val subject_types_supported: List<String> = listOf("pairwise"),
        val id_token_signing_alg_values_supported: List<String> = listOf("RS256"),
        val code_challenge_methods_supported: List<String> = listOf("S256"),
        val scopes_supported: List<String> = listOf("openid", "profile"),
        val ui_locales_supported: List<String> = listOf("nb", "nn", "en", "se"),
        val acr_values_supported: List<String> = listOf("Level3", "Level4"),
        val frontchannel_logout_supported: Boolean = true,
        val token_endpoint_auth_methods_supported: List<String> = listOf("client_secret_post", "client_secret_basic", "private_key_jwt", "none"),
        val request_parameter_supported: Boolean = true,
        val request_uri_parameter_supported: Boolean = false,
        val request_object_signing_alg_values_supported: List<String> = listOf("RS256", "RS384", "RS512"),
    )

    data class UserSummary(
        val ident: String,
        val firstName: String,
        val lastName: String,
        val redirect: URI
    )

    open class Keys(val keys: List<JsonWebKeySupport.Jwks>)

    companion object {
        private fun issuer() = linkTo<IdPortenMock> { dummy() }.toUri()

        private fun accessToken(
            jsonWebKeySupport: JsonWebKeySupport,
            issuer: URI,
            audience: String,
            pid: String,
        ) = jsonWebKeySupport.createRS256Token(
            JwtClaims().apply {
                setIssuer(issuer.toString())
                setAudience(audience)
                setExpirationTimeMinutesInTheFuture(60F)
                setIssuedAtToNow()
                setNotBeforeMinutesInThePast(0F)
                subject = pid
            }.toJson()
        ).compactSerialization
    }
}
