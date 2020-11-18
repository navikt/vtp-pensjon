package no.nav.pensjon.vtp.application.configuration

import no.nav.pensjon.vtp.felles.CryptoConfigurationParameters
import org.apache.wss4j.common.crypto.Crypto
import org.apache.wss4j.common.crypto.CryptoFactory
import org.apache.wss4j.common.ext.WSSecurityException
import org.apache.wss4j.dom.engine.WSSConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class CryptoConfiguration {
    @Throws(WSSecurityException::class)
    @Bean
    fun crypto(cryptoConfigurationParameters: CryptoConfigurationParameters): Crypto {
        WSSConfig.init()

        val properties = Properties()
        properties["org.apache.wss4j.crypto.provider"] = "org.apache.wss4j.common.crypto.Merlin"
        properties["org.apache.wss4j.crypto.merlin.keystore.password"] = cryptoConfigurationParameters.keyStorePassword
        properties["org.apache.wss4j.crypto.merlin.keystore.file"] = cryptoConfigurationParameters.keystoreFilePath

        return CryptoFactory.getInstance(properties)
    }

    @Bean
    fun cryptoConfigurationParameters(
            @Value("\${no.nav.modig.security.appkey}") keyAndCertAlias: String,
            @Value("\${no.nav.modig.security.appcert.keystore}") keystorePath: String,
            @Value("\${no.nav.modig.security.appcert.password}") keystorePassword: String,
            @Value("\${no.nav.modig.security.truststore.path}") truststorePath: String,
            @Value("\${no.nav.modig.security.truststore.password}") truststorePassword: String
    ): CryptoConfigurationParameters {
        return CryptoConfigurationParameters(
                keyAndCertAlias = keyAndCertAlias,

                keystoreFilePath = keystorePath,
                keyStorePassword = keystorePassword,

                truststoreFilePath = truststorePath,
                truststorePassword = truststorePassword
        )
    }
}
