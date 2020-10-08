package no.nav.foreldrepenger.vtp.server.rest.azuread.navansatt;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.foreldrepenger.vtp.felles.AzureOidcTokenGenerator;
import no.nav.foreldrepenger.vtp.felles.KeyStoreTool;
import no.nav.foreldrepenger.vtp.server.rest.auth.Oauth2AccessTokenResponse;
import no.nav.foreldrepenger.vtp.server.rest.auth.UserRepository;
import no.nav.foreldrepenger.vtp.testmodell.ansatt.NAVAnsatt;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.SearchResult;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@Api(tags = {"AzureAd"})
@Path("/AzureAd")
public class AzureAdNAVAnsattService {
    private static final Logger LOG = LoggerFactory.getLogger(AzureAdNAVAnsattService.class);

    @GET
    @Path("/isAlive")
    @Produces(MediaType.TEXT_HTML)
    public Response isAliveMock() {
        String isAlive = "Azure AD is OK";
        return Response.ok(isAlive).build();
    }

    @GET
    @Path("/{tenant}/v2.0/.well-known/openid-configuration")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Azure AD Discovery url", notes = ("Mock impl av Azure AD discovery urlen. "))
    public Response wellKnown(@SuppressWarnings("unused") @Context HttpServletRequest req, @PathParam("tenant") String tenant, @QueryParam("p") String profile) {
        String baseUrl = getBaseUrl(req);
        String graphUrl = getGraphUrl(req);
        WellKnownResponse wellKnownResponse = new WellKnownResponse(baseUrl, graphUrl, tenant, profile);
        return Response.ok(wellKnownResponse).build();
    }

    @GET
    @Path("/{tenant}/discovery/v2.0/keys")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "azureAd/discovery/keys", notes = ("Mock impl av Azure AD jwk_uri"))
    public Response authorize(@SuppressWarnings("unused") @Context HttpServletRequest req) {
        LOG.info("kall på /oauth2/connect/jwk_uri");
        String jwks = KeyStoreTool.getJwks();
        LOG.info("JWKS: " + jwks);
        return Response.ok(jwks).build();
    }

    @POST
    @Path("/{tenant}/oauth2/v2.0/token")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "azureAd/access_token", notes = ("Mock impl av Azure AD access_token"))
    @SuppressWarnings("unused")
    public Response accessToken(
            @Context HttpServletRequest req,
            @PathParam("tenant") String tenant,
            @FormParam("grant_type") String grantType,
            @FormParam("client_id") String clientId,
            @FormParam("realm") String realm,
            @FormParam("code") String code,
            @FormParam("refresh_token") String refreshToken,
            @FormParam("redirect_uri") String redirectUri) {
        if ("authorization_code".equals(grantType)) {
            String token = createIdToken(req, code, tenant, clientId);
            String generatedRefreshToken = "refresh:" + code;
            String generatedAccessToken = "access:" + code;
            LOG.info("Fikk parametere:" + req.getParameterMap().toString());
            LOG.info("kall på /oauth2/access_token, opprettet token: " + token + " med redirect-url: " + redirectUri);
            Oauth2AccessTokenResponse oauthResponse = new Oauth2AccessTokenResponse(token, generatedRefreshToken, generatedAccessToken);
            return Response.ok(oauthResponse).build();
        } else if ("refresh_token".equals(grantType)) {
            String usernameWithNonce = refreshToken.substring(8);
            String token = createIdToken(req, usernameWithNonce /*+ ";"*/, tenant, clientId);
            LOG.info("Fikk parametere:" + req.getParameterMap().toString());
            LOG.info("refresh-token-kall på /oauth2/access_token, opprettet nytt token: " + token);
            String generatedRefreshToken = "refresh:" + usernameWithNonce;
            String generatedAccessToken = "access:" + usernameWithNonce;
            Oauth2AccessTokenResponse oauthResponse = new Oauth2AccessTokenResponse(token, generatedRefreshToken, generatedAccessToken);
            return Response.ok(oauthResponse).build();
        } else {
            LOG.error("Unknown grant_type " + grantType);
            return Response.serverError().entity("Unknown grant_type " + grantType).build();
        }
    }

    private String createIdToken(HttpServletRequest req, String code, String tenant, String clientId) {
        try {
            String[] codeData = code.split(";");
            String username = codeData[0];
            String nonce = codeData.length > 1 ? codeData[1] : null;
            NAVAnsatt user = BasisdataProviderFileImpl.getInstance().getAnsatteIndeks().hentNAVAnsatt(username).orElseThrow(() -> new RuntimeException("Fant ikke NAV-ansatt med brukernavn " + username));

            String issuer = getIssuer(tenant);
            AzureOidcTokenGenerator tokenGenerator = new AzureOidcTokenGenerator(username, nonce).withIssuer(issuer);
            tokenGenerator.setAud(clientId);

            tokenGenerator.withClaim("tid", tenant);
            tokenGenerator.withClaim("oid", UUID.nameUUIDFromBytes(user.cn.getBytes()).toString()); // user id - which is normally a UUID in Azure AD
            tokenGenerator.withClaim("name", user.displayName);
            tokenGenerator.withClaim("preferred_username", user.email);
            tokenGenerator.withGroups(user.groups.stream().map(AzureADGroupMapping::toAzureGroupId).collect(Collectors.toList()));
            return tokenGenerator.create();
        } catch (Exception ex) {
            throw new RuntimeException("Kunne ikke generere Azure AD-token", ex);
        }
    }

    // Authorize URL:
    // https://login.microsoftonline.com/966ac572-f5b7-4bbe-aa88-c76419c0f851/oauth2/v2.0/authorize
    // ?response_type=code
    // &client_id=15f01fee-bd6d-4427-bb70-fc9e75caa13a
    // &redirect_uri=http%3A%2F%2Flocalhost%3A9080%2Fpsak%2Foidc%2Fcallback
    // &scope=openid+profile+user.read
    // &state=PpZ9uI3drAWm7vfLM1rvb-ev4fvzc9RmHqQxW725wWE
    // &nonce=xJDu2DjmaiFY8ivIakELFLVZDY6OivuE1GAUJ0fIEf0
    // &prompt=select_account

    @GET
    @Path("/{tenant}/v2.0/authorize")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
    @ApiOperation(value = "AzureAD/v2.0/authorize", notes = ("Mock impl av Azure AD authorize"))
    @SuppressWarnings("unused")
    public Response authorize(
            @Context HttpServletRequest req,
            @Context HttpServletResponse resp,
            @PathParam("tenant") String tenant,
            @QueryParam("response_type") @DefaultValue("code") String responseType,
            @QueryParam("scope") @DefaultValue("openid") String scope,
            @QueryParam("client_id") String clientId,
            @QueryParam("state") String state,
            @QueryParam("nonce") String nonce,
            @QueryParam("redirect_uri") String redirectUri
    )
            throws Exception {
        LOG.info("kall mot AzureAD authorize med redirecturi " + redirectUri);
        Objects.requireNonNull(scope, "Missing the ?scope=xxx query parameter");
        List<String> validScopes = Arrays.asList("openid", "profile");
        String[] scopes = scope.split("\\s+");
        for (String s : scopes) {
            if (!validScopes.contains(s)) {
                throw new IllegalArgumentException("Unsupported scope [" + s + "], supported scopes are: " + StringUtils.joinWith(", ", validScopes));
            }
        }

        Objects.requireNonNull(responseType, "Missing the ?responseType=xxx query parameter");
        if (!Objects.equals(responseType, "code")) {
            throw new IllegalArgumentException("Unsupported responseType [" + responseType + "], should be 'code'");
        }

        Objects.requireNonNull(clientId, "Missing the ?client_id=xxx query parameter");
        Objects.requireNonNull(state, "Missing the ?state=xxx query parameter");
        Objects.requireNonNull(redirectUri, "Missing the ?redirect_uri=xxx query parameter");

        URIBuilder uriBuilder = new URIBuilder(redirectUri);
        uriBuilder.addParameter("scope", scope);
        uriBuilder.addParameter("state", state);
        uriBuilder.addParameter("client_id", clientId);
        final String issuer = getIssuer(tenant);
        uriBuilder.addParameter("iss", issuer);
        uriBuilder.addParameter("redirect_uri", redirectUri);

        return authorizeHtmlPage(uriBuilder, nonce);
    }

    private Response authorizeHtmlPage(URIBuilder location, String nonce) throws URISyntaxException, NamingException {
        // LAG HTML SIDE
        List<Map.Entry<String, String>> usernames = getUsernames();

        String html = "<!DOCTYPE html>\n"
                + "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "<title>Velg bruker</title>\n" +
                "</head>\n" +
                "    <body>\n" +
                "    <div style=\"text-align:center;width:100%;\">\n" +
                "       <caption><h3>Velg bruker:</h3></caption>\n" +
                "        <table>\r\n" +
                "            <tbody>\r\n" +
                usernames.stream().map(
                        username -> "<tr><a href=\"" + location.toString() + "&code=" + username.getKey() + ";" + nonce + "\"><h1>" + username.getValue() + "</h1></a></tr>\n")
                        .collect(Collectors.joining("\n"))
                +
                "            </tbody>\n" +
                "        </table>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        return Response.ok(html, MediaType.TEXT_HTML).build();
    }

    private List<Map.Entry<String, String>> getUsernames() throws NamingException {
        List<SearchResult> allUsers = UserRepository.getAllUsers();
        List<Map.Entry<String, String>> usernames = allUsers.stream()
                .map(u -> {
                    String cn = getAttribute(u, "cn");
                    String displayName = getAttribute(u, "displayName");
                    return new AbstractMap.SimpleEntry<String, String>(cn, displayName);
                }).collect(Collectors.toList());
        return usernames;
    }

    private String getAttribute(SearchResult u, String attribName) {
        Attribute attribute = u.getAttributes().get(attribName);
        try {
            return (String) attribute.get();
        } catch (NamingException e) {
            throw new IllegalStateException(e);
        }
    }

    private String getBaseUrl(@Context HttpServletRequest req) {
        return req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/rest/AzureAd";
    }

    private String getGraphUrl(@Context HttpServletRequest req) {
        return req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/rest/MicrosoftGraphApi";
    }

    private String getIssuer(String tenant) {
        return "https://login.microsoftonline.com/" + tenant + "/v2.0";
    }
}
