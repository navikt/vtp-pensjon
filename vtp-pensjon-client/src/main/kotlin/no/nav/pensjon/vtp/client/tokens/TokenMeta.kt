package no.nav.pensjon.vtp.client.tokens

data class TokenMeta(
    val tokenResponse: AccessTokenResponse,
    val username: String,
)
