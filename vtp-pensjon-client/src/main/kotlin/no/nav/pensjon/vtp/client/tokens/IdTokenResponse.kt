package no.nav.pensjon.vtp.client.tokens

import com.fasterxml.jackson.annotation.JsonProperty

data class IdTokenResponse(
    @JsonProperty("id_token") val id_token: String,
    @JsonProperty("refresh_token") val refresh_token: String,
    @JsonProperty("access_token") val access_token: String,
    @JsonProperty("expires_in") val expires_in: Int,
    @JsonProperty("token_type") val token_type: String
)
