package no.nav.pensjon.vtp.auth

import org.jose4j.base64url.Base64Url.encode
import org.jose4j.jwk.RsaJsonWebKey
import org.jose4j.jws.AlgorithmIdentifiers.RSA_USING_SHA256
import org.jose4j.jws.JsonWebSignature
import org.springframework.stereotype.Component
import java.security.interfaces.RSAPublicKey

@Component
class JsonWebKeySupport(private val jwk: RsaJsonWebKey) {
    fun jwks() = listOf(
        Jwks(
            kty = "RSA",
            alg = "RS256",
            use = "sig",
            kid = "1",
            n = encode((jwk.publicKey as RSAPublicKey).modulus.toByteArray()),
            e = encode(jwk.getRsaPublicKey().publicExponent.toByteArray())
        )
    )

    data class Jwks(
        val kty: String,
        val alg: String,
        val use: String,
        val kid: String,
        val n: String,
        val e: String
    )

    fun createRS256Token(string: String?): JsonWebSignature {
        with(JsonWebSignature()) {
            payload = string
            keyIdHeaderValue = jwk.keyId
            algorithmHeaderValue = "RS256"
            key = jwk.privateKey
            algorithmHeaderValue = RSA_USING_SHA256
            return this
        }
    }
}
