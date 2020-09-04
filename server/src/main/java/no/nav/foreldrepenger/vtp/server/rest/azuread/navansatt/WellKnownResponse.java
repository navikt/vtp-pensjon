package no.nav.foreldrepenger.vtp.server.rest.azuread.navansatt;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;

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
class WellKnownResponse {
    private final String baseUrl;
    private final String tenant;

    @JsonProperty("authorization_endpoint")
    public String getAuthorizationEndpoint() {
        return baseUrl + "/" + tenant + "/v2.0/authorize";
    }

    @JsonProperty("claims_supported")
    public final List<String> claimsSupported = Arrays.asList(
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
    );

    @JsonProperty("cloud_graph_host_name")
    public final String cloudGraphHostName = "graph.windows.net";

    @JsonProperty("cloud_instance_name")
    public final String cloudInstanceName = "microsoftonline.com";

    @JsonProperty("end_session_endpoint")
    public final String getEndSessionEndpoint() {
        return baseUrl + "/" + tenant + "/v2.0/logout";
    }

    @JsonProperty("frontchannel_logout_supported")
    public final boolean frontchannelLogoutSupported = true;

    @JsonProperty("http_logout_supported")
    public final boolean httpLogoutSupported = true;


    @JsonProperty("id_token_signing_alg_values_supported")
    public final List<String> idTokenSigningAlgValuesSupported = Arrays.asList("RS256");

    @JsonProperty("issuer")
    public final String getIssuer() {
        return "https://login.microsoftonline.com/" + tenant + "/v2.0";
    }

    @JsonProperty("jwks_uri")
    public String getJwksUri() {
        return baseUrl + "/" + tenant + "/discovery/v2.0/keys";
    }

    @JsonProperty("msgraph_host")
    public final String msgraphHost = "graph.microsoft.com";

    @JsonProperty("rbac_url")
    public final String rbacUrl = "https://pas.windows.net";

    @JsonProperty("request_uri_parameter_supported")
    public final boolean request_uri_parameter_supported = false;

    @JsonProperty("response_modes_supported")
    public final List<String> responseModesSupported = Arrays.asList("query", "fragment", "form_post");

    @JsonProperty("response_types_supported")
    public final List<String> responseTypesSupported = Arrays.asList("code", "id_token", "code id_token", "id_token token");

    @JsonProperty("scopes_supported")
    public final List<String> scopesSupported = Arrays.asList("openid", "profile", "email", "offline_access");

    @JsonProperty("subject_types_supported")
    public final List<String> subjectTypesSupported = Arrays.asList("pairwise");

    @JsonProperty("tenant_region_scope")
    public final String tenantRegionScope = "EU";

    @JsonProperty("token_endpoint")
    public String getTokenEndpoint() {
        return baseUrl + "/" + tenant + "/oauth2/v2.0/token";
    }

    @JsonProperty("token_endpoint_auth_methods_supported")
    public final List<String> tokenEndpointAuthMethodsSupported = Arrays.asList("client_secret_post", "private_key_jwt", "client_secret_basic");

    @JsonProperty("userinfo_endpoint")
    public String getUserinfoEndpoint() {
        return baseUrl + "/rest/AzureGraphAPI/oidc/userinfo";
    }

    WellKnownResponse(String url, String tenant) {
        this.baseUrl = url;
        this.tenant = tenant;
    }
}