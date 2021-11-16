package no.nav.pensjon.vtp.auth.idporten

import io.swagger.annotations.ApiOperation
import no.nav.pensjon.vtp.auth.JsonWebKeySupport
import no.nav.pensjon.vtp.auth.JsonWebKeySupport.*
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository
import no.nav.pensjon.vtp.util.asResponseEntity
import no.nav.pensjon.vtp.util.withoutQueryParameters
import org.jose4j.jwt.JwtClaims
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.HttpStatus.TEMPORARY_REDIRECT
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder.fromUriString
import java.net.URI
import java.util.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/rest/idporten")
class IdPortenMock(
    private val jsonWebKeySupport: JsonWebKeySupport,
    private val personModellRepository: PersonModellRepository,
    private val idPortenSessionRepository: IdPortenSessionRepository,
) {
    // dummy method to get issuer URI
    @GetMapping
    fun dummy() = "pong".asResponseEntity()

    @GetMapping("/userinfo")
    fun userinfo() = "userinfo".asResponseEntity()

    @GetMapping(value = ["/.well-known/openid-configuration"])
    @ApiOperation(value = "Idporten well-known URL", notes = "Mock impl av ID-porten well-known URL. ")
    fun wellKnown(req: HttpServletRequest): WellKnownResponse {
        return WellKnownResponse(
            issuer = issuer(),
            authorization_endpoint = URI("http://localhost:8060/rest/idporten/login-ui"), // linkTo<IdPortenMock> { loginUI("foo", "bar", "foobar") }.toUri().withoutQueryParameters(),
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
    fun jwks() = Keys(jsonWebKeySupport.jwks()).asResponseEntity()

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
    ) = idPortenSessionRepository.findByCode(code)
        ?.let {
            TokenResponse(
                access_token = accessToken(
                    jsonWebKeySupport = jsonWebKeySupport,
                    issuer = issuer(),
                    audience = client_id,
                    pid = code,
                    sid = UUID.randomUUID().toString(),
                ),
                expires_in = 3600,
                id_token = accessToken(
                    jsonWebKeySupport = jsonWebKeySupport,
                    issuer = issuer(),
                    audience = client_id,
                    pid = code,
                    sid = UUID.randomUUID().toString(),
                    nonce = it.nonce,
                ),
                refresh_token = "refresh:$code",
                scope = client_id,
            ).asResponseEntity()
        }
        ?: ResponseEntity.status(UNAUTHORIZED).body("Unknown code $code")

    @GetMapping(value = ["/login-ui"])
    fun loginUI(
        @RequestParam("redirect_uri") redirect: String,
        @RequestParam("nonce") nonce: String,
        @RequestParam("state") state: String,
    ) = """
<!DOCTYPE html>
<html>
<head>
    <title>ID-porten Mock</title>
</head>

<body>
    <div style="text-align:center;width:100%;">
        <h1>ID-porten Mock</h1>
        ${users(redirect, nonce, state)}
    </div>
</body>
</html>
""".asResponseEntity()

    private fun users(redirect: String, nonce: String, state: String) = personModellRepository.findAll().let { persons ->
        if (persons.isNotEmpty()) {
            persons.joinToString("\n") {
                "<a href=\"login?pid=${it.ident}&nonce=$nonce&state=$state&redirect=${redirect}\">${it.fornavn} ${it.etternavn}</a> (${it.ident})<br>"
            }
        } else {
            "Det finnes ingen personer i VTP akkurat nå. Prøv å <a href=\"/#/\">laste inn et scenario</a>!"
        }
    }

    @GetMapping(value = ["/login"])
    fun doLogin(@RequestParam("redirect") redirect: String, @RequestParam("pid") pid: String, @RequestParam("nonce", required = false) nonce: String, @RequestParam("state") state: String): ResponseEntity<Any> {
        val idPortenSession = idPortenSessionRepository.save(IdPortenSession(pid = pid, nonce = nonce))

        val redirectUriString = fromUriString(redirect)
            .queryParam("code", idPortenSession.code)
            .queryParam("nonce", nonce)
            .queryParam("state", state)
            .build()
            .toUriString()

        return ResponseEntity
            .status(TEMPORARY_REDIRECT)
            .location(URI(redirectUriString))
            .build<Any>()
    }
/*
    @GetMapping(value = ["/users"])
    fun getUsers(@RequestParam("redirect_uri") redirect: String) = personModellRepository.findAll().map {
        UserSummary(
            ident = it.ident,
            firstName = it.fornavn,
            lastName = it.etternavn,
            redirect = linkTo<IdPortenMock> { doLogin(redirect, it.ident) }.toUri()
        )
    }
*/
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
        val token_endpoint_auth_methods_supported: List<String> = listOf(
            "client_secret_post",
            "client_secret_basic",
            "private_key_jwt",
            "none"
        ),
        val request_parameter_supported: Boolean = true,
        val request_uri_parameter_supported: Boolean = false,
        val request_object_signing_alg_values_supported: List<String> = listOf("RS256", "RS384", "RS512"),
    )
/*
    data class UserSummary(
        val ident: String,
        val firstName: String,
        val lastName: String,
        val redirect: URI
    )
*/
    companion object {
        private fun issuer() = linkTo<IdPortenMock> { dummy() }.toUri()

        private fun accessToken(
            jsonWebKeySupport: JsonWebKeySupport,
            issuer: URI,
            audience: String,
            pid: String,
            sid: String,
            nonce: String? = null,
        ) = jsonWebKeySupport.createRS256Token(
            JwtClaims().apply {
                setIssuer(issuer.toString())
                setAudience(audience)
                setExpirationTimeMinutesInTheFuture(60F)
                setIssuedAtToNow()
                setNotBeforeMinutesInThePast(0F)
                subject = pid
                setClaim("sid", sid)
                setClaim("acr", "Level4")
                if (nonce != null) {
                    setClaim("nonce", nonce)
                }
            }.toJson()
        ).compactSerialization
    }
}
