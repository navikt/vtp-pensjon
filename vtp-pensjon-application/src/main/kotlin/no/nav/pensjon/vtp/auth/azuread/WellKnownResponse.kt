package no.nav.pensjon.vtp.auth.azuread

import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URI

@Suppress("unused")
data class WellKnownResponse(
    val issuer: String,
    @JsonProperty("authorization_endpoint")
    val authorizationEndpoint: URI,
    val baseUrl: String,
    val graphUrl: String,
    val tenant: String,
    val profile: String?
) {
    @JsonProperty("claims_supported")
    val claimsSupported: MutableList<String> = mutableListOf(
        "sub",
        "iss",
        "cloud_instance_name",
        "cloud_instance_host_name",
        "cloud_graph_host_name",
        "msgraph_host",
        "aud",
        "exp",
        "iat",
        "auth_time",
        "acr",
        "nonce",
        "preferred_username",
        "name",
        "tid",
        "ver",
        "at_hash",
        "c_hash",
        "email"
    )

    @JsonProperty("cloud_graph_host_name")
    val cloudGraphHostName = "graph.windows.net"

    @JsonProperty("cloud_instance_name")
    val cloudInstanceName = "microsoftonline.com"

    @get:JsonProperty("end_session_endpoint")
    val endSessionEndpoint: String
        get() = "$baseUrl/$tenant/v2.0/logout"

    @JsonProperty("frontchannel_logout_supported")
    val frontchannelLogoutSupported = true

    @JsonProperty("http_logout_supported")
    val httpLogoutSupported = true

    @JsonProperty("id_token_signing_alg_values_supported")
    val idTokenSigningAlgValuesSupported: MutableList<String> = mutableListOf("RS256")

    @get:JsonProperty("jwks_uri")
    val jwksUri: String
        get() = "$baseUrl/$tenant/discovery/v2.0/keys"

    @JsonProperty("msgraph_host")
    val msgraphHost = "graph.microsoft.com"

    @JsonProperty("rbac_url")
    val rbacUrl = "https://pas.windows.net"

    @JsonProperty("request_uri_parameter_supported")
    val requestUriParameterSupported = false

    @JsonProperty("response_modes_supported")
    val responseModesSupported: MutableList<String> = mutableListOf("query", "fragment", "form_post")

    @JsonProperty("response_types_supported")
    val responseTypesSupported: MutableList<String> = mutableListOf("code", "id_token", "code id_token", "id_token token")

    @JsonProperty("scopes_supported")
    val scopesSupported: MutableList<String> = mutableListOf("openid", "profile", "email", "offline_access")

    @JsonProperty("subject_types_supported")
    val subjectTypesSupported: MutableList<String> = mutableListOf("pairwise")

    @JsonProperty("tenant_region_scope")
    val tenantRegionScope = "EU"

    @get:JsonProperty("token_endpoint")
    val tokenEndpoint: String
        get() = "$baseUrl/$tenant/oauth2/v2.0/token"

    @JsonProperty("token_endpoint_auth_methods_supported")
    val tokenEndpointAuthMethodsSupported: MutableList<String> = mutableListOf("client_secret_post", "private_key_jwt", "client_secret_basic")

    @get:JsonProperty("userinfo_endpoint")
    val userinfoEndpoint: String
        get() = "$graphUrl/oidc/userinfo"
}
