package no.nav.pensjon.vtp.auth.idporten

import io.swagger.annotations.ApiOperation
import no.nav.pensjon.vtp.auth.JsonWebKeySupport
import no.nav.pensjon.vtp.auth.UserSummary
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository
import no.nav.pensjon.vtp.util.asResponseEntity
import no.nav.pensjon.vtp.util.withoutQueryParameters
import org.jose4j.jwt.JwtClaims
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
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
            authorization_endpoint = linkTo<IdPortenMock> { getUsers("foo") }.toUri().withoutQueryParameters(),
            jwks_uri = linkTo<IdPortenMock> { jwks() }.toUri(),
            userinfo_endpoint = linkTo<IdPortenMock> { userinfo() }.toUri(),
            token_endpoint = linkTo<IdPortenMock> {
                token(
                    req,
                    grantType = "",
                    client_assertion_type = "",
                    client_assertion = "",
                    subject_token_type = "",
                    subject_token = "",
                    audience = ""
                )
            }.toUri().withoutQueryParameters(),
        )
    }

    @GetMapping("/jwks")
    @ApiOperation(value = "Idporten public key set")
    fun jwks() = jsonWebKeySupport.jwks()

    @GetMapping("/privateKey")
    @ApiOperation(value = "Idporten public key set")
    fun privateKey() = jsonWebKeySupport.privateKey()

    @PostMapping("/token")
    @ApiOperation(value = "Exchange token via tokendings")
    fun token(
        req: HttpServletRequest,
        @RequestParam("grant_type", defaultValue = "urn:ietf:params:oauth:grant-type:token-exchange") grantType: String,
        @RequestParam("client_assertion_type", defaultValue = "urn:ietf:params:oauth:grant-type:token-exchange") client_assertion_type: String,
        @RequestParam("client_assertion") client_assertion: String,
        @RequestParam("subject_token_type", defaultValue = "urn:ietf:params:oauth:token-type:jwt") subject_token_type: String,
        @RequestParam("subject_token") subject_token: String,
        @RequestParam("audience") audience: String,
    ) = TokenResponse(
        access_token = accessToken(
            jsonWebKeySupport = jsonWebKeySupport,
            issuer = issuer(),
            audience = audience
        )
    ).asResponseEntity()


    @GetMapping(value = ["/login"])
    fun doLogin(@RequestParam("redirect_uri") redirect: String, @RequestParam("fnr") fnr: String): ResponseEntity<*> {
        // TODO: legg på kode
        return ResponseEntity
            .status(HttpStatus.TEMPORARY_REDIRECT)
            .location(URI(redirect))
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
        val issued_token_type: String = "urn:ietf:params:oauth:token-type:access_token",
        val token_type: String = "Bearer",
        val expiresIn: Int = 3600
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

    companion object {
        private fun issuer() = linkTo<IdPortenMock> { dummy() }.toUri()

        private fun accessToken(
            jsonWebKeySupport: JsonWebKeySupport,
            issuer: URI,
            audience: String
        ) = jsonWebKeySupport.createRS256Token(
            JwtClaims().apply {
                setIssuer(issuer.toString())
                setAudience(audience)
                setExpirationTimeMinutesInTheFuture(60F)
                setIssuedAtToNow()
                setNotBeforeMinutesInThePast(0F)
            }.toJson()
        ).compactSerialization
    }
}
