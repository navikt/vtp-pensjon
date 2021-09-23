package no.nav.pensjon.vtp.auth.tokenx

import io.swagger.annotations.ApiOperation
import no.nav.pensjon.vtp.auth.*
import org.jose4j.jwt.JwtClaims
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/rest/tokenx")
class TokenxMock(
    private val jsonWebKeySupport: JsonWebKeySupport
) {
    @GetMapping(value = ["/.well-known/oauth-authorization-server"], produces = [APPLICATION_JSON_VALUE])
    @ApiOperation(value = "TokenX well-known URL", notes = "Mock impl av TokenX well-known URL. ")
    fun wellKnown(req: HttpServletRequest): WellKnownResponse {
        val baseUrl = baseUrl(req.serverName, req.serverPort)
        return WellKnownResponse(
            issuer = baseUrl,
            jwks_uri = "$baseUrl/jwks",
            token_endpoint = "$baseUrl/token",
        )
    }

    @GetMapping("/jwks")
    @ApiOperation(value = "TokenX public key set")
    fun jwks() = jsonWebKeySupport.jwks()

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
            issuer = baseUrl(req.serverName, req.serverPort),
            audience = audience
        )
    )
}

private fun accessToken(
    jsonWebKeySupport: JsonWebKeySupport,
    issuer: String,
    audience: String
) = jsonWebKeySupport.createRS256Token(
    JwtClaims().apply {
        setIssuer(issuer)
        setAudience(audience)
        setExpirationTimeMinutesInTheFuture(60F)
        setIssuedAtToNow()
        setNotBeforeMinutesInThePast(0F)
    }.toJson()
).compactSerialization

private fun baseUrl(serverAddress: String, serverPort: Int) = "http://$serverAddress:$serverPort/rest/tokenx"

data class TokenResponse(
    val access_token: String,
    val issued_token_type: String = "urn:ietf:params:oauth:token-type:access_token",
    val token_type: String = "Bearer",
    val expiresIn: Int = 3600
)

data class WellKnownResponse(
    val issuer: String = "temp",
    val token_endpoint: String,
    val jwks_uri: String,
    val grant_types_supported: List<String> = listOf("urn:ietf:params:oauth:grant-type:token-exchange"),
    val token_endpoint_auth_methods_supported: List<String> = listOf("private_key_jwt"),
    val token_endpoint_auth_signing_alg_values_supported: List<String> = listOf("RS256"),
    val subject_types_supported: List<String> = listOf("public"),
)
