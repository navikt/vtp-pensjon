package no.nav.pensjon.vtp.felles

import org.springframework.core.io.Resource

data class CryptoConfigurationParameters(
        val keyAndCertAlias: String,

        val keystorePath: String,
        val keystorePassword: String,

        val truststorePath: String,
        val truststorePassword: String
)