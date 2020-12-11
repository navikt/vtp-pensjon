package no.nav.pensjon.vtp.auth

data class EndUserAuthenticateSuccess(
    val tokenId: String,
    val successUrl: String
)
