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
import java.io.File
import java.nio.file.Files
import java.nio.file.Files.createTempFile
import java.security.KeyStore
import java.security.KeyStore.PasswordProtection
import java.util.*
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext

private fun copyToTempFile(resource: Resource, prefix: String): File {
    val tempFolder = Files.createTempDirectory("")
    val tempFile = createTempFile(tempFolder, prefix, ".jks")
    tempFolder.toFile().deleteOnExit()

    return tempFile.toFile().apply {
        deleteOnExit()
        resource.inputStream.use { i -> outputStream().use { o -> i.copyTo(o) } }
    }
}

@Configuration
class CryptoConfiguration(
    @Value("\${no.nav.modig.security.appkey}") val keyAndCertAlias: String,
    @Value("\${no.nav.modig.security.appcert.keystore}") val keystoreResource: Resource,
    @Value("\${no.nav.modig.security.appcert.password}") val keystorePassword: String
) {
    val keystorePath: String by lazy { copyToTempFile(keystoreResource, "keystore").absolutePath }

    @Throws(WSSecurityException::class)
    @Bean
    fun crypto(): Crypto {
        val properties = Properties()
        properties["org.apache.wss4j.crypto.provider"] = "org.apache.wss4j.common.crypto.Merlin"
        properties["org.apache.wss4j.crypto.merlin.keystore.password"] = keystorePassword
        properties["org.apache.wss4j.crypto.merlin.keystore.file"] = keystorePath

        return CryptoFactory.getInstance(properties)
    }

    @Bean
    fun cryptoConfigurationParameters() = CryptoConfigurationParameters(
        keyAndCertAlias = keyAndCertAlias,

        keystorePath = keystorePath,
        keystorePassword = keystorePassword,
    )

    @Bean
    fun keyStore(): KeyStore {
        keystoreResource.inputStream.use { inputStream ->
            // TODO: Maybe use PKCS12? Java 9 and onwards uses that, Java 8 or below uses JKS.
            val keystore = KeyStore.getInstance("JKS")
            keystore.load(inputStream, keystorePassword.toCharArray())
            return keystore
        }
    }

    @Bean
    fun sslContext(keyStore: KeyStore): SSLContext {
        val kmf = KeyManagerFactory.getInstance("SunX509")
        kmf.init(keyStore, keystorePassword.toCharArray())

        val context = SSLContext.getInstance("TLS")
        context.init(kmf.keyManagers, null, null)
        return context
    }

    @Bean
    fun x509Credential(keyStore: KeyStore): X509Credential {
        return KeyStoreX509CredentialAdapter(keyStore, keyAndCertAlias, keystorePassword.toCharArray())
    }

    @Bean
    fun rsaJsonWebKey(keyStore: KeyStore): RsaJsonWebKey {
        val jwk = newPublicJwk(keyStore.getCertificate(this.keyAndCertAlias).publicKey) as RsaJsonWebKey
        jwk.privateKey = (
            keyStore.getEntry(
                this.keyAndCertAlias,
                PasswordProtection(keystorePassword.toCharArray())
            ) as KeyStore.PrivateKeyEntry
            ).privateKey
        jwk.keyId = "1"
        return jwk
    }
}
