package no.nav.pensjon.vtp.felles

data class CryptoConfigurationParameters(
    val keyAndCertAlias: String,

    val keystorePath: String,
    val keystorePassword: String,
)
