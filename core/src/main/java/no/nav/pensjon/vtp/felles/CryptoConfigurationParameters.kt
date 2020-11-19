package no.nav.pensjon.vtp.felles

import org.springframework.core.io.Resource

data class CryptoConfigurationParameters(
        val keyAndCertAlias: String,

        val keystoreResource: Resource,
        val keyStorePassword: String,

        val truststoreResource: Resource,
        val truststorePassword: String
)