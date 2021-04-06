package no.nav.pensjon.vtp.auth

data class CryptoConfigurationParameters(
    val keyAndCertAlias: String,

    val keystorePath: String,
    val keystorePassword: String,
)
