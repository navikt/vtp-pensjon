package no.nav.pensjon.vtp.auth.maskinporten

import com.nimbusds.jose.JOSEObject
import com.nimbusds.jwt.JWTClaimsSet
import no.nav.pensjon.vtp.auth.JsonWebKeySupport.*
import no.nav.pensjon.vtp.auth.OidcTokenGenerator
import no.nav.pensjon.vtp.util.asResponseEntity
import org.jose4j.jwt.NumericDate
import org.jose4j.jwt.NumericDate.now
import org.springframework.beans.factory.annotation.Value
import org.springframework.hateoas.server.LinkBuilder
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.badRequest
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.text.ParseException
import java.util.*

@RestController
@RequestMapping("/rest/maskinporten")
class MaskinportenMock(
    @Value("\${maskinportenIssuer}") val issuer: String,
    private val oidcTokenGenerator: OidcTokenGenerator
) {
    @GetMapping("/.well-known/oauth-authorization-server")
    fun wellKnown() = WellKnownResponse(
        issuer = issuer,
        token_endpoint = linkTo<MaskinportenMock> { token("", "") }.toPathUri(),
        jwks_uri = linkTo<MaskinportenMock> { jwks() }.toPathUri(),
        token_endpoint_auth_methods_supported = listOf("private_key_jwt"),
        grant_types_supported = listOf("urn:ietf:params:oauth:grant-type:jwt-bearer")
    )

    private fun LinkBuilder.toPathUri() = toUri().run { URI(scheme, null, host, port, path, null, null) }

    @GetMapping("/jwk")
    fun jwks() = Keys(oidcTokenGenerator.jwks()).asResponseEntity()

    @PostMapping("/token", consumes = [APPLICATION_FORM_URLENCODED_VALUE])
    fun token(
        @RequestParam("grant_type") grantType: String,
        @RequestParam("assertion") assertion: String
    ) = when (grantType) {
        "urn:ietf:params:oauth:grant-type:jwt-bearer" -> {
            val claims = JWTClaimsSet.parse(JOSEObject.parse(assertion).payload.toJSONObject())
            val now = now()

            ok(
                AccessTokenResponse(
                    access_token = generateAccessToken(
                        resource = claims.getStringClaim("resource"),
                        consumer = claims.issuer,
                        scope = claims.getStringClaim("scope"),
                        issuedAt = now,
                        expiration = now.apply { addSeconds(3600L * 6L) }
                    ),
                    expires_in = 3600L * 6L,
                    scope = claims.getStringClaim("scope"),
                    token_type = "Bearer"
                )
            )
        }
        else -> badRequest().body("Unsupported grant_type '$grantType'")
    }

    @PostMapping("/mock_access_token")
    fun generateAccessToken(
        @RequestParam(required = false)
        resource: String?,
        @RequestParam
        consumer: String,
        @RequestParam
        scope: String,
        @RequestParam(required = false)
        issuedAt: NumericDate?,
        @RequestParam(required = false)
        expiration: NumericDate?
    ) = oidcTokenGenerator.oidcToken(
        subject = "subject",
        issuer = issuer,
        aud = listOfNotNull(resource),
        additionalClaims = mapOf(
            "client_id" to consumer,
            "client_amr" to "virksomhetssertifikat",
            "consumer" to mapOf(
                "authority" to "iso6523-actorid-upis",
                "ID" to "0192:$consumer"
            ),
            "scope" to scope,
            "token_type" to "Bearer",
            "jti" to UUID.randomUUID().toString()
            // TODO: supplier delegation_source
        ),
        issuedAt = issuedAt ?: now(),
        expiration = expiration ?: now().apply { addSeconds(3600L * 6L) }
    )

    @ExceptionHandler
    fun onParseException(exception: ParseException) = ResponseEntity(exception.message, BAD_REQUEST)

    data class AccessTokenResponse(
        val access_token: String,
        val expires_in: Long,
        val scope: String,
        val token_type: String
    )

    data class WellKnownResponse(
        val issuer: String,
        val token_endpoint: URI,
        val jwks_uri: URI,
        val token_endpoint_auth_methods_supported: List<String>,
        val grant_types_supported: List<String>
    )
}
