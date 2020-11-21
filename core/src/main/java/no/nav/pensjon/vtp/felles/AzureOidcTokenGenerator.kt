package no.nav.pensjon.vtp.felles

import org.jose4j.jwt.JwtClaims
import org.jose4j.jwt.NumericDate
import org.jose4j.lang.JoseException
import org.springframework.util.ObjectUtils
import java.util.*
import kotlin.collections.ArrayList

fun azureOidcToken(
        jsonWebKeySupport: JsonWebKeySupport,
        subject: String,
        nonce: String?,
        aud: List<String> = ArrayList(),
        groups: MutableList<String> = ArrayList(),
        expiration: NumericDate = NumericDate.fromSeconds(NumericDate.now().value + 3600 * 6),
        issuer: String? = null,
        issuedAt: NumericDate = NumericDate.now(),
        additionalClaims: Map<String, String> = HashMap()

): String {
    val claims = JwtClaims()
    claims.issuer = issuer
    claims.expirationTime = expiration
    claims.setGeneratedJwtId()
    claims.issuedAt = issuedAt
    claims.notBefore = issuedAt
    claims.subject = subject
    claims.setClaim("ver", "2.0")
    if (!ObjectUtils.isEmpty(nonce)) {
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
    claims.setClaim("groups", groups)
    return jsonWebKeySupport.createRS256Token(claims.toJson()).compactSerialization
}
