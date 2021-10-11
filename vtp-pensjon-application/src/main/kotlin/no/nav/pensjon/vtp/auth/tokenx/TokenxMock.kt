package no.nav.pensjon.vtp.auth.tokenx

import io.swagger.annotations.ApiOperation
import no.nav.pensjon.vtp.auth.JsonWebKeySupport
import no.nav.pensjon.vtp.util.asResponseEntity
import no.nav.pensjon.vtp.util.withoutQueryParameters
import org.jose4j.jwt.JwtClaims
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/rest/tokenx")
class TokenxMock(
    private val jsonWebKeySupport: JsonWebKeySupport
) {
    // dummy method to get URI
    @GetMapping
    fun dummy() = "pong".asResponseEntity()

    @GetMapping(value = ["/.well-known/oauth-authorization-server"], produces = [APPLICATION_JSON_VALUE])
    @ApiOperation(value = "TokenX well-known URL", notes = "Mock impl av TokenX well-known URL. ")
    fun wellKnown(req: HttpServletRequest): WellKnownResponse {
        return WellKnownResponse(
            issuer = issuer(),
            jwks_uri = linkTo<TokenxMock> { jwks() }.toUri(),
            token_endpoint = linkTo<TokenxMock> {
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
    @ApiOperation(value = "TokenX public key set")
    fun jwks() = JsonWebKeySupport.Keys(jsonWebKeySupport.jwks()).asResponseEntity()

    @GetMapping("/privateKey")
    @ApiOperation(value = "TokenX public key set")
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

    data class TokenResponse(
        val access_token: String,
        val issued_token_type: String = "urn:ietf:params:oauth:token-type:access_token",
        val token_type: String = "Bearer",
        val expiresIn: Int = 3600
    )

    data class WellKnownResponse(
        val issuer: URI,
        val token_endpoint: URI,
        val jwks_uri: URI,
        val grant_types_supported: List<String> = listOf("urn:ietf:params:oauth:grant-type:token-exchange"),
        val token_endpoint_auth_methods_supported: List<String> = listOf("private_key_jwt"),
        val token_endpoint_auth_signing_alg_values_supported: List<String> = listOf("RS256"),
        val subject_types_supported: List<String> = listOf("public"),
    )

    companion object {
        private fun issuer() = linkTo<TokenxMock> { dummy() }.toUri()

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
