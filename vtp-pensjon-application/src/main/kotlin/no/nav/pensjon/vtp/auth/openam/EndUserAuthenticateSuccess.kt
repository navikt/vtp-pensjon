package no.nav.pensjon.vtp.auth.openam

data class EndUserAuthenticateSuccess(
    val tokenId: String,
    val successUrl: String
)
