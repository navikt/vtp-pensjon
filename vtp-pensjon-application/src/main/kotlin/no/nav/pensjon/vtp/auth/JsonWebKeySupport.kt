package no.nav.pensjon.vtp.auth

import com.nimbusds.jose.jwk.KeyUse
import com.nimbusds.jose.jwk.RSAKey
import org.jose4j.base64url.Base64Url.encode
import org.jose4j.jwk.RsaJsonWebKey
import org.jose4j.jws.AlgorithmIdentifiers.RSA_USING_SHA256
import org.jose4j.jws.JsonWebSignature
import org.springframework.stereotype.Component
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.UUID

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

    fun privateKey(): String = RSAKey.Builder(jwk.publicKey as RSAPublicKey)
        .privateKey(jwk.privateKey as RSAPrivateKey)
        .keyUse(KeyUse.SIGNATURE)
        .keyID(UUID.randomUUID().toString())
        .build().toJSONString()

    data class Jwks(
        val kty: String,
        val alg: String,
        val use: String,
        val kid: String,
        val n: String,
        val e: String
    )

    data class Keys(val keys: List<Jwks>)

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
