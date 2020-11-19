package no.nav.pensjon.vtp.application.configuration

import no.nav.pensjon.vtp.felles.CryptoConfigurationParameters
import org.apache.wss4j.common.crypto.Crypto
import org.apache.wss4j.common.crypto.CryptoFactory
import org.apache.wss4j.common.ext.WSSecurityException
import org.jose4j.jwk.PublicJsonWebKey.Factory.newPublicJwk
import org.jose4j.jwk.RsaJsonWebKey
import org.opensaml.security.x509.X509Credential
import org.opensaml.security.x509.impl.KeyStoreX509CredentialAdapter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import java.io.IOException
import java.security.KeyStore
import java.security.KeyStore.PasswordProtection
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.util.*
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext

@Configuration
class CryptoConfiguration(
        @Value("\${no.nav.modig.security.appkey}") val keyAndCertAlias: String,
        @Value("\${no.nav.modig.security.appcert.keystore}") val keystorePath: Resource,
        @Value("\${no.nav.modig.security.appcert.password}") val keystorePassword: String,
        @Value("\${no.nav.modig.security.truststore.path}") val truststorePath: Resource,
        @Value("\${no.nav.modig.security.truststore.password}") val truststorePassword: String
) {

    @Throws(WSSecurityException::class)
    @Bean
    fun crypto(cryptoConfigurationParameters: CryptoConfigurationParameters): Crypto {
        val properties = Properties()
        properties["org.apache.wss4j.crypto.provider"] = "org.apache.wss4j.common.crypto.Merlin"
        properties["org.apache.wss4j.crypto.merlin.keystore.password"] = cryptoConfigurationParameters.keyStorePassword
        properties["org.apache.wss4j.crypto.merlin.keystore.file"] = cryptoConfigurationParameters.keystoreResource

        return CryptoFactory.getInstance(properties)
    }

    @Bean
    fun cryptoConfigurationParameters(
    ): CryptoConfigurationParameters {
        return CryptoConfigurationParameters(
                keyAndCertAlias = keyAndCertAlias,

                keystoreResource = keystorePath,
                keyStorePassword = keystorePassword,

                truststoreResource = truststorePath,
                truststorePassword = truststorePassword
        )
    }

    @Bean
    fun keyStore(cryptoConfigurationParameters: CryptoConfigurationParameters): KeyStore {
        return loadKeyStore(cryptoConfigurationParameters.keystoreResource, cryptoConfigurationParameters.keyStorePassword.toCharArray())
    }

    @Throws(IOException::class, KeyStoreException::class, CertificateException::class, NoSuchAlgorithmException::class)
    fun loadKeyStore(keyStoreResource: Resource, keystorePassword: CharArray): KeyStore {
        keyStoreResource.inputStream.use { inputStream ->
            // TODO: Maybe use PKCS12? Java 9 and onwards uses that, Java 8 or below uses JKS.
            val keystore = KeyStore.getInstance("JKS")
            keystore.load(inputStream, keystorePassword)
            return keystore
        }
    }

    @Bean
    fun sslContext(keyStore: KeyStore, cryptoConfigurationParameters: CryptoConfigurationParameters): SSLContext {
        val kmf = KeyManagerFactory.getInstance("SunX509")
        kmf.init(keyStore, cryptoConfigurationParameters.keyStorePassword.toCharArray())

        val context = SSLContext.getInstance("TLS")
        context.init(kmf.keyManagers, null, null)
        return context
    }

    @Bean
    fun x509Credential(keyStore: KeyStore, cryptoConfigurationParameters: CryptoConfigurationParameters): X509Credential {
        return KeyStoreX509CredentialAdapter(keyStore, cryptoConfigurationParameters.keyAndCertAlias, cryptoConfigurationParameters.keyStorePassword.toCharArray())
    }

    @Bean
    fun rsaJsonWebKey(keyStore: KeyStore, cryptoConfigurationParameters: CryptoConfigurationParameters) : RsaJsonWebKey {
        val keyAndCertAlias = cryptoConfigurationParameters.keyAndCertAlias

        val jwk = newPublicJwk(keyStore.getCertificate(keyAndCertAlias).publicKey) as RsaJsonWebKey
        jwk.privateKey = (keyStore.getEntry(keyAndCertAlias, PasswordProtection(cryptoConfigurationParameters.keyStorePassword.toCharArray())) as KeyStore.PrivateKeyEntry).privateKey
        jwk.keyId = "1"
        return jwk
    }
}
