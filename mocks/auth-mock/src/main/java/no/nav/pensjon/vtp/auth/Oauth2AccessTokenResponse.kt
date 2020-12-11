package no.nav.pensjon.vtp.auth

import com.fasterxml.jackson.annotation.JsonProperty

data class Oauth2AccessTokenResponse(
    @JsonProperty("id_token")
    val idToken: String,
    @JsonProperty("refresh_token")
    val refreshToken: String,
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("expires_in")
    val expiresIn: Int = 3600,
    @JsonProperty("token_type")
    val tokenType: String = "Bearer"
)
