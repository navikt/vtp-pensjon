package no.nav.pensjon.vtp.auth.azuread

import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URI
import java.util.*

/*
authorization_endpoint: "https://login.microsoftonline.com/966ac572-f5b7-4bbe-aa88-c76419c0f851/oauth2/v2.0/authorize"
claims_supported: ["sub", "iss", "cloud_instance_name", "cloud_instance_host_name", "cloud_graph_host_name",…]
cloud_graph_host_name: "graph.windows.net"
cloud_instance_name: "microsoftonline.com"
end_session_endpoint: "https://login.microsoftonline.com/966ac572-f5b7-4bbe-aa88-c76419c0f851/oauth2/v2.0/logout"
frontchannel_logout_supported: true
http_logout_supported: true
id_token_signing_alg_values_supported: ["RS256"]
issuer: "https://login.microsoftonline.com/966ac572-f5b7-4bbe-aa88-c76419c0f851/v2.0"
jwks_uri: "https://login.microsoftonline.com/966ac572-f5b7-4bbe-aa88-c76419c0f851/discovery/v2.0/keys"
msgraph_host: "graph.microsoft.com"
rbac_url: "https://pas.windows.net"
request_uri_parameter_supported: false
response_modes_supported: ["query", "fragment", "form_post"]
response_types_supported: ["code", "id_token", "code id_token", "id_token token"]
scopes_supported: ["openid", "profile", "email", "offline_access"]
subject_types_supported: ["pairwise"]
tenant_region_scope: "EU"
token_endpoint: "https://login.microsoftonline.com/966ac572-f5b7-4bbe-aa88-c76419c0f851/oauth2/v2.0/token"
token_endpoint_auth_methods_supported: ["client_secret_post", "private_key_jwt", "client_secret_basic"]
userinfo_endpoint: "https://graph.microsoft.com/oidc/userinfo"
* */
data class WellKnownResponse(
    @JsonProperty("authorization_endpoint")
    val authorizationEndpoint: URI,
    val baseUrl: String,
    val graphUrl: String,
    val tenant: String,
    val profile: String?
) {
    @JsonProperty("claims_supported")
    val claimsSupported = Arrays.asList(
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
    val idTokenSigningAlgValuesSupported = Arrays.asList("RS256")

    // Spesialhåndtering for Azure AD B2C.
    // Veldig rart at Azure AD gjør det sånn, men vi får mocke det realistisk, det sparer oss
    // for en del problemer andre steder (f.eks. LoginService sin issuer)
    @get:JsonProperty("issuer")
    val issuer: String
        get() = // Spesialhåndtering for Azure AD B2C.
            // Veldig rart at Azure AD gjør det sånn, men vi får mocke det realistisk, det sparer oss
            // for en del problemer andre steder (f.eks. LoginService sin issuer)
            if (tenant == "NAVtestB2C.onmicrosoft.com") {
                "https://login.microsoftonline.com/d38f25aa-eab8-4c50-9f28-ebf92c1256f2/v2.0" + if ("B2C_1A_idporten_ver1" == profile) "/" else ""
            } else "https://login.microsoftonline.com/$tenant/v2.0"

    @get:JsonProperty("jwks_uri")
    val jwksUri: String
        get() = "$baseUrl/$tenant/discovery/v2.0/keys"

    @JsonProperty("msgraph_host")
    val msgraphHost = "graph.microsoft.com"

    @JsonProperty("rbac_url")
    val rbacUrl = "https://pas.windows.net"

    @JsonProperty("request_uri_parameter_supported")
    val request_uri_parameter_supported = false

    @JsonProperty("response_modes_supported")
    val responseModesSupported = Arrays.asList("query", "fragment", "form_post")

    @JsonProperty("response_types_supported")
    val responseTypesSupported = Arrays.asList("code", "id_token", "code id_token", "id_token token")

    @JsonProperty("scopes_supported")
    val scopesSupported = Arrays.asList("openid", "profile", "email", "offline_access")

    @JsonProperty("subject_types_supported")
    val subjectTypesSupported = Arrays.asList("pairwise")

    @JsonProperty("tenant_region_scope")
    val tenantRegionScope = "EU"

    @get:JsonProperty("token_endpoint")
    val tokenEndpoint: String
        get() = "$baseUrl/$tenant/oauth2/v2.0/token"

    @JsonProperty("token_endpoint_auth_methods_supported")
    val tokenEndpointAuthMethodsSupported = Arrays.asList("client_secret_post", "private_key_jwt", "client_secret_basic")

    @get:JsonProperty("userinfo_endpoint")
    val userinfoEndpoint: String
        get() = "$graphUrl/oidc/userinfo"
}
