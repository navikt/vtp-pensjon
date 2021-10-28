package no.nav.pensjon.vtp.auth.azuread

import no.nav.pensjon.vtp.auth.JsonWebKeySupport
import org.jose4j.jwt.JwtClaims
import org.jose4j.jwt.NumericDate
import org.springframework.util.ObjectUtils

fun azureOidcToken(
    sub: String,
    jsonWebKeySupport: JsonWebKeySupport,
    email: String,
    nonce: String?,
    aud: List<String> = ArrayList(),
    groups: List<String> = ArrayList(),
    expiration: NumericDate = NumericDate.fromSeconds(NumericDate.now().value + 3600 * 6),
    issuer: String? = null,
    issuedAt: NumericDate = NumericDate.now(),
    additionalClaims: Map<String, String> = HashMap(),
    scope: String?,
    sid: String?,
): String {
    val claims = JwtClaims()
    claims.issuer = issuer
    claims.expirationTime = expiration
    claims.setGeneratedJwtId()
    claims.issuedAt = issuedAt
    claims.notBefore = issuedAt

    claims.subject = sub
    claims.setClaim("preferred_username", email)

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
    scope?.let { claims.setClaim("scp", scope) }
    sid?.let { claims.setClaim("sid", sid) }
    return jsonWebKeySupport.createRS256Token(claims.toJson()).compactSerialization
}
