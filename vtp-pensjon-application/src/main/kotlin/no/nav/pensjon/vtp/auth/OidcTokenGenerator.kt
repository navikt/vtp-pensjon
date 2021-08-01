package no.nav.pensjon.vtp.auth

import com.google.common.base.Strings
import org.jose4j.jwt.JwtClaims
import org.jose4j.jwt.NumericDate
import org.jose4j.jwt.NumericDate.now
import org.springframework.stereotype.Component

@Component
class OidcTokenGenerator(
    private val jsonWebKeySupport: JsonWebKeySupport
) {
    fun oidcToken(
        subject: String,
        nonce: String? = null,
        issuer: String,
        aud: List<String>,
        expiration: NumericDate,
        issuedAt: NumericDate = now(),
        additionalClaims: Map<String, String> = mapOf(
            "azp" to "OIDC",
            "acr" to "Level4"
        )
    ): String {
        return jsonWebKeySupport.createRS256Token(
            JwtClaims().apply {
                this.audience = aud
                this.issuer = issuer
                this.expirationTime = expiration
                this.issuedAt = issuedAt
                this.subject = subject

                setGeneratedJwtId()

                if (!Strings.isNullOrEmpty(nonce)) {
                    setClaim("nonce", nonce)
                }

                for ((key, value) in additionalClaims) {
                    setStringClaim(key, value)
                }
            }.toJson()
        ).compactSerialization
    }

    fun jwks() = jsonWebKeySupport.jwks()
}
