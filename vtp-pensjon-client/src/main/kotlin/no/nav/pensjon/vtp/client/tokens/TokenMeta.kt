package no.nav.pensjon.vtp.client.tokens

data class TokenMeta<T>(
    val token: T,
    val username: String,
)
