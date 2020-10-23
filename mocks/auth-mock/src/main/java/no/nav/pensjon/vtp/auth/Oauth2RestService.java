package no.nav.pensjon.vtp.auth;

import java.net.URISyntaxException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.SearchResult;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import no.nav.pensjon.vtp.core.annotations.JaxrsResource;
import no.nav.pensjon.vtp.felles.KeyStoreTool;
import no.nav.pensjon.vtp.felles.OidcTokenGenerator;

@JaxrsResource
@Api(tags = {"Openam"})
@Path("/isso")
public class Oauth2RestService {

    private static final Logger LOG = LoggerFactory.getLogger(Oauth2RestService.class);

    private static final Map<String, String> nonceCache = new HashMap<>();

    private static final Map<String, String> clientIdCache = new HashMap<>();
    private static final String DEFAULT_ISSUER = "http://localhost:8060/rest/isso/oauth2";

    private UserRepository userRepository;

    @Context
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GET
    @Path("/oauth2/authorize")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
    @ApiOperation(value = "oauth2/authorize", notes = ("Mock impl av Oauth2 authorize"))
    @SuppressWarnings("unused")
    public Response authorize(
            @Context HttpServletRequest req,
            @Context HttpServletResponse resp,
            @QueryParam("session") @DefaultValue("winssochain") String session,
            @QueryParam("authIndexType") @DefaultValue("service") String authIndexType,
            @QueryParam("authIndexValue") @DefaultValue("winssochain") String authIndexValue,
            @QueryParam("response_type") @DefaultValue("code") String responseType,
            @QueryParam("scope") @DefaultValue("openid") String scope,
            @QueryParam("client_id") String clientId,
            @QueryParam("state") String state,
            @QueryParam("redirect_uri") String redirectUri) throws URISyntaxException {
        LOG.info("kall mot oauth2/authorize med redirecturi " + redirectUri);
        Objects.requireNonNull(scope, "scope");
        if (!Objects.equals(scope, "openid")) {
            throw new IllegalArgumentException("Unsupported scope [" + scope + "], should be 'openid'");
        }
        Objects.requireNonNull(responseType, "responseType");
        if (!Objects.equals(responseType, "code")) {
            throw new IllegalArgumentException("Unsupported responseType [" + responseType + "], should be 'code'");
        }

        Objects.requireNonNull(clientId, "client_id");
        Objects.requireNonNull(state, "state");
        Objects.requireNonNull(redirectUri, "redirectUri");

        URIBuilder uriBuilder = new URIBuilder(redirectUri);
        uriBuilder.addParameter("scope", scope);
        uriBuilder.addParameter("state", state);
        uriBuilder.addParameter("client_id", clientId);
        uriBuilder.addParameter("iss", getIssuer());
        uriBuilder.addParameter("redirect_uri", redirectUri);
        clientIdCache.put(state, clientId);
        if (!"".equals(req.getParameter("nonce"))) {
            nonceCache.put(state, req.getParameter("nonce"));
        }

        String acceptHeader = req.getHeader("Accept-Header");
        if ((null == req.getContentType() || req.getContentType().equals("text/html")) && (acceptHeader == null || !acceptHeader.contains("json"))) {
            return authorizeHtmlPage(uriBuilder);
        } else {
            return authorizeRedirect(uriBuilder);
        }
    }

    private Response authorizeRedirect(URIBuilder location) throws URISyntaxException {
        // SEND JSON RESPONSE TIL OPENAM HELPER
        location.addParameter("code", "im-just-a-fake-code");
        return Response.status(HttpServletResponse.SC_FOUND).location(location.build()).build();
    }

    private Response authorizeHtmlPage(URIBuilder location) {
        // LAG HTML SIDE
        List<Entry<String, String>> usernames = getUsernames();

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
                        username -> "<tr><a href=\"" + location.toString() + "&code=" + username.getKey() + "\"><h1>" + username.getValue() + "</h1></a></tr>\n")
                        .collect(Collectors.joining("\n"))
                +
                "            </tbody>\n" +
                "        </table>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        return Response.ok(html, MediaType.TEXT_HTML).build();
    }

    private List<Map.Entry<String, String>> getUsernames() {
        List<SearchResult> allUsers = userRepository.getAllUsers();

        // Long story, økonomi forventer (per 2018-10-30) at alle interne brukere har max 8 bokstaver i bruker identen sin :-(
        // pass derfor på at CN er definert med maks 8 bokstaver.

        return allUsers.stream()
                .map(u -> {
                    String cn = getAttribute(u, "cn");
                    String displayName = getAttribute(u, "displayName");
                    return new SimpleEntry<>(cn, displayName);
                }).collect(Collectors.toList());
    }

    private String getAttribute(SearchResult u, String attribName) {
        Attribute attribute = u.getAttributes().get(attribName);
        try {
            return (String) attribute.get();
        } catch (NamingException e) {
            throw new IllegalStateException(e);
        }
    }

    // TODO (FC): Trengs denne fortsatt?
    @POST
    @Path("/oauth2/access_token")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "oauth2/access_token", notes = ("Mock impl av Oauth2 access_token"))
    public Response accessToken(
            @Context HttpServletRequest req,
            @FormParam("grant_type") String grantType,
            @FormParam("realm") String realm,
            @FormParam("code") String code,
            @FormParam("refresh_token") String refreshToken,
            @FormParam("redirect_uri") String redirectUri) {
        if ("authorization_code".equals(grantType)) {
            String token = createIdToken(req, code);
            LOG.info("Fikk parametere:" + req.getParameterMap().toString());
            LOG.info("kall på /oauth2/access_token, opprettet token: " + token + " med redirect-url: " + redirectUri);
            String generatedRefreshToken = "refresh:" + code;
            String generatedAccessToken = "access:" + code;
            Oauth2AccessTokenResponse oauthResponse = new Oauth2AccessTokenResponse(token, generatedRefreshToken, generatedAccessToken);
            return Response.ok(oauthResponse).build();
        } else if ("refresh_token".equals(grantType)) {
            if (!refreshToken.startsWith("refresh:")) {
                String message = "Invalid refresh token " + code;
                LOG.error(message);
                return Response.status(Response.Status.FORBIDDEN).entity(message).build();
            } else {
                String username = refreshToken.substring(8);
                String token = createIdToken(req, username);
                LOG.info("Fikk parametere:" + req.getParameterMap().toString());
                LOG.info("refresh-token-kall på /oauth2/access_token, opprettet nytt token: " + token);
                String generatedRefreshToken = "refresh:" + username;
                String generatedAccessToken = "access:" + username;
                Oauth2AccessTokenResponse oauthResponse = new Oauth2AccessTokenResponse(token, generatedRefreshToken, generatedAccessToken);
                return Response.ok(oauthResponse).build();
            }
        } else {
            LOG.error("Unknown grant_type " + grantType);
            return Response.serverError().entity("Unknown grant_type " + grantType).build();
        }

    }

    public static String getIssuer() {
        if (null != System.getenv("ISSO_OAUTH2_ISSUER")) {
            return System.getenv("ISSO_OAUTH2_ISSUER");
        } else {
            return DEFAULT_ISSUER;
        }
    }

    private String createIdToken(HttpServletRequest req, String username) {
        String issuer = getIssuer();
        String state = req.getParameter("state");
        String nonce = nonceCache.get(state);
        OidcTokenGenerator tokenGenerator = new OidcTokenGenerator(username, nonce).withIssuer(issuer);
        if (clientIdCache.containsKey(state)) {
            String clientId = clientIdCache.get(state);
            tokenGenerator.addAud(clientId);
        }
        return tokenGenerator.create();
    }

    @GET
    @Path("/isAlive.jsp")
    @Produces(MediaType.TEXT_HTML)
    public Response isAliveMock() {
        String isAlive = "Server is ALIVE";
        return Response.ok(isAlive).build();
    }

    @GET
    @Path("/oauth2/../isAlive.jsp")
    @Produces(MediaType.TEXT_HTML)
    public Response isAliveMockRassUrl() {
        String isAlive = "Server is ALIVE";
        return Response.ok(isAlive).build();
    }

    @GET
    @Path("/oauth2/connect/jwk_uri")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "oauth2/connect/jwk_uri", notes = ("Mock impl av Oauth2 jwk_uri"))
    public Response authorize(@Context HttpServletRequest req) {
        LOG.info("kall på /oauth2/connect/jwk_uri");
        String jwks = KeyStoreTool.getJwks();
        LOG.info("JWKS: " + jwks);
        return Response.ok(jwks).build();
    }

    /**
     * brukes til autentisere bruker slik at en slipper å autentisere senere. OpenAM mikk-makk .
     */
    @POST
    @Path("/json/authenticate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "json/authenticate", notes = ("Mock impl av OpenAM autenticate for service bruker innlogging"))
    public Response serviceBrukerAuthenticate(@Context HttpServletRequest req,
                                              @ApiParam("Liste over aksjonspunkt som skal bekreftes, inklusiv data som trengs for å løse de.") EndUserAuthenticateTemplate enduserTemplate) {
        LOG.info("kall på /json/authenticate");
        if (enduserTemplate == null) {
            EndUserAuthenticateTemplate template = new EndUserAuthenticateTemplate();
            template.setAuthId(UUID.randomUUID().toString());
            template.setHeader("Sign in to VTP");
            template.setStage("DataStore1");
            template.setTemplate("");

            EndUserAuthenticateTemplate.Name namePrompt = new EndUserAuthenticateTemplate.Name("prompt", "User Name:");
            EndUserAuthenticateTemplate.Name usernameInput = new EndUserAuthenticateTemplate.Name("IDToken1", "");
            EndUserAuthenticateTemplate.Callback nameCallback = new EndUserAuthenticateTemplate.Callback("NameCallback", namePrompt, usernameInput);

            EndUserAuthenticateTemplate.Name passwordPrompt = new EndUserAuthenticateTemplate.Name("prompt", "Password:");
            EndUserAuthenticateTemplate.Name passwordInput = new EndUserAuthenticateTemplate.Name("IDToken2", "");
            EndUserAuthenticateTemplate.Callback passwordCallback = new EndUserAuthenticateTemplate.Callback("PasswordCallback", passwordPrompt, passwordInput);

            template.setCallbacks(Arrays.asList(nameCallback, passwordCallback));

            return Response.ok(template).build();
        } else {
            // generer token som brukes til å bekrefte innlogging ovenfor openam

            // TODO ingen validering av authId?
            // TODO generer unik session token?

            EndUserAuthenticateSuccess success = new EndUserAuthenticateSuccess("i-am-just-a-dummy-session-token-workaround", "/isso/console");
            return Response.ok(success).build();
        }

    }

    @GET
    @Path("/oauth2/.well-known/openid-configuration")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Discovery url", notes = ("Mock impl av discovery urlen. "))
    public Response wellKnown(@Context HttpServletRequest req) {
        LOG.info("kall på /oauth2/.well-known/openid-configuration");
        String baseUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort();
        WellKnownResponse wellKnownResponse = new WellKnownResponse(baseUrl, getIssuer());
        return Response.ok(wellKnownResponse).build();
    }

}
