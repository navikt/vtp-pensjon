package no.nav.pensjon.vtp.client.tokens

data class AccessTokenResponse(
    val access_token: String,
    val token_type: String,
    val expires_in: Int?,
    val refresh_token: String?,
    val scope: String?
)
