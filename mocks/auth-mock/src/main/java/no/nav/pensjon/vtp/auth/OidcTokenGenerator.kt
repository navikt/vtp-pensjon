package no.nav.pensjon.vtp.auth

import com.google.common.base.Strings
import org.jose4j.jwt.JwtClaims
import org.jose4j.jwt.NumericDate
import org.jose4j.jwt.NumericDate.fromSeconds
import org.jose4j.jwt.NumericDate.now
import org.jose4j.lang.JoseException
import java.util.*

class OidcTokenGenerator(
        private val jsonWebKeySupport: JsonWebKeySupport,
        private val subject: String,
        private val nonce: String?,
        private val issuer: String,
        private val aud: MutableList<String> = ArrayList(listOf("OIDC")),
        private var expiration: NumericDate = fromSeconds(now().value + 3600 * 6),
        private val issuedAt: NumericDate = now(),
        private val additionalClaims: Map<String, String> = mapOf(
                "azp" to "OIDC",
                "acr" to "Level4"
        )
) {
    fun addAud(e: String) {
        aud.add(e)
    }

    fun create(): String {
        val claims = JwtClaims()
        claims.issuer = issuer
        claims.expirationTime = expiration
        claims.setGeneratedJwtId()
        claims.issuedAt = issuedAt
        claims.subject = subject
        if (!Strings.isNullOrEmpty(nonce)) {
            claims.setClaim("nonce", nonce)
        }
        if (aud.size == 1) {
            claims.setAudience(aud[0])
        } else {
            claims.audience = aud
        }
        for ((key, value) in additionalClaims) {
            claims.setStringClaim(key, value)
        }
        return try {
            jsonWebKeySupport.createRS256Token(claims.toJson()).compactSerialization
        } catch (e: JoseException) {
            throw RuntimeException(e)
        }
    }
}
