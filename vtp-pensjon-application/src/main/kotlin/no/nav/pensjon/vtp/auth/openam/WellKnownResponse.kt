package no.nav.pensjon.vtp.auth.openam

import com.fasterxml.jackson.annotation.JsonProperty

data class WellKnownResponse(
    @JsonProperty("issuer")
    val issuer: String,

    @JsonProperty("response_types_supported")
    val responseTypesSupported: List<String> = listOf(
        "code token id_token",
        "code",
        "code id_token",
        "id_token",
        "code token",
        "token",
        "token id_token"
    ),

    @JsonProperty("claims_parameter_supported")
    val claimsParameterSupported: Boolean = false,

    @JsonProperty("end_session_endpoint")
    val endSessionEndpoint: String,

    @JsonProperty("version")
    val version: String = "3.0",

    @JsonProperty("check_session_iframe")
    val checkSessionIframe: String,

    @JsonProperty("scopes_supported")
    val scopesSupported: List<String> = listOf("openid"),

    @JsonProperty("id_token_encryption_enc_values_supported")
    val idTokenEncryptionEncValuesSupported: List<String> = listOf(
        "A128CBC-HS256",
        "A256CBC-HS512"
    ),

    @JsonProperty("acr_values_supported")
    val acrValuesSupported: List<String> = listOf(),

    @JsonProperty("authorization_endpoint")
    var authorizationEndpoint: String? = null,

    @JsonProperty("claims_supported")
    val claimsSupported: List<String> = listOf(),

    @JsonProperty("id_token_encryption_alg_values_supported")
    val idTokenEncryptionAlgValuesSupported: List<String> = listOf(
        "RSA1_5"
    ),

    @JsonProperty("jwks_uri")
    val jwksUri: String,

    @JsonProperty("subject_types_supported")
    val subjectTypesSupported: List<String> = listOf(
        "public"
    ),

    @JsonProperty("id_token_signing_alg_values_supported")
    val idTokenSigningAlgValuesSupported: List<String> = listOf(
        "ES384",
        "HS256",
        "HS512",
        "ES256",
        "RS256",
        "HS384",
        "ES512"
    ),

    @JsonProperty("registration_endpoint")
    val registrationEndpoint: String,

    @JsonProperty("token_endpoint_auth_methods_supported")
    val tokenEndpointAuthMethodsSupported: List<String> = listOf(
        "private_key_jwt",
        "client_secret_basic"
    ),

    @JsonProperty("token_endpoint")
    val tokenEndpoint: String
) {
    constructor(frontendUrl: String, baseUrl: String, issuer: String) : this(
        issuer = issuer,
        endSessionEndpoint = "$baseUrl/rest/isso/oauth2/connect/endSession",
        checkSessionIframe = "$baseUrl/rest/isso/oauth2/connect/checkSession",
        jwksUri = "$baseUrl/rest/isso/oauth2/connect/jwk_uri",
        registrationEndpoint = "$baseUrl/rest/isso/oauth2/connect/register",
        tokenEndpoint = "$baseUrl/rest/isso/oauth2/access_token",
        authorizationEndpoint = "$frontendUrl/openam/authorize"
    )
}
