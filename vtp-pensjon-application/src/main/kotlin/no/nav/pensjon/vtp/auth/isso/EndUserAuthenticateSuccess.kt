package no.nav.pensjon.vtp.auth.isso

data class EndUserAuthenticateSuccess(
    val tokenId: String,
    val successUrl: String
)
