package no.nav.pensjon.vtp.felles

data class CryptoConfigurationParameters(
        val keyAndCertAlias: String,

        val keystoreFilePath: String,
        val keyStorePassword: String,

        val truststoreFilePath: String,
        val truststorePassword: String
)