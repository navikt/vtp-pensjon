package no.nav.pensjon.vtp.auth.sts

import java.net.URI

data class WellKnown(
    val issuer: String,
    val token_endpoint: URI,
    val exchange_token_endpoint: URI,
    val jwks_uri: URI,
    val subject_types_supported: List<String> = listOf("public"),
    val grant_types_supported: List<String> = listOf(
        "urn:ietf:params:oauth:grant-type:token-exchange",
        "client_credentials"
    ),
    val scopes_supported: List<String> = listOf("openid"),
    val token_endpoint_auth_methods_supported: List<String> = listOf("client_secret_basic"),
    val response_types_supported: List<String> = listOf("id_token token"),
    val response_modes_supported: List<String> = listOf("form_post"),
    val id_token_signing_alg_values_supported: List<String> = listOf("RS256"),
)
