package no.nav.pensjon.vtp.auth;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import no.nav.pensjon.vtp.felles.KeyStoreTool;
import no.nav.pensjon.vtp.felles.OidcTokenGenerator;
import no.nav.pensjon.vtp.testmodell.ansatt.AnsatteIndeks;
import no.nav.pensjon.vtp.testmodell.ansatt.NAVAnsatt;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@RestController
@Api(tags = {"Openam"})
@RequestMapping("/rest/isso")
public class Oauth2RestService {

    private static final Logger LOG = LoggerFactory.getLogger(Oauth2RestService.class);

    private static final Map<String, String> nonceCache = new HashMap<>();

    private static final Map<String, String> clientIdCache = new HashMap<>();
    private static final String DEFAULT_ISSUER = "http://localhost:8060/rest/isso/oauth2";

    private final AnsatteIndeks ansatteIndeks;
    private final KeyStoreTool keyStoreTool;

    public Oauth2RestService(AnsatteIndeks ansatteIndeks, KeyStoreTool keyStoreTool) {
        this.ansatteIndeks = ansatteIndeks;
        this.keyStoreTool = keyStoreTool;
    }

    @GetMapping(value = "/oauth2/authorize", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_HTML_VALUE})
    @ApiOperation(value = "oauth2/authorize", notes = ("Mock impl av Oauth2 authorize"))
    public ResponseEntity authorize(
            HttpServletRequest req,
            HttpServletResponse resp,
            @RequestParam(defaultValue = "winssochain") String session,
            @RequestParam(defaultValue ="service") String authIndexType,
            @RequestParam(defaultValue ="winssochain") String authIndexValue,
            @RequestParam(defaultValue ="code") String responseType,
            @RequestParam(defaultValue ="openid") String scope,
            @RequestParam String client_id,
            @RequestParam String state,
            @RequestParam String redirect_uri) throws URISyntaxException {
        LOG.info("kall mot oauth2/authorize med redirecturi " + redirect_uri);
        Objects.requireNonNull(scope, "scope");
        if (!Objects.equals(scope, "openid")) {
            throw new IllegalArgumentException("Unsupported scope [" + scope + "], should be 'openid'");
        }
        Objects.requireNonNull(responseType, "responseType");
        if (!Objects.equals(responseType, "code")) {
            throw new IllegalArgumentException("Unsupported responseType [" + responseType + "], should be 'code'");
        }

        Objects.requireNonNull(client_id, "client_id");
        Objects.requireNonNull(state, "state");
        Objects.requireNonNull(redirect_uri, "redirect_uri");

        URIBuilder uriBuilder = new URIBuilder(redirect_uri);
        uriBuilder.addParameter("scope", scope);
        uriBuilder.addParameter("state", state);
        uriBuilder.addParameter("client_id", client_id);
        uriBuilder.addParameter("iss", getIssuer());
        uriBuilder.addParameter("redirect_uri", redirect_uri);
        clientIdCache.put(state, client_id);
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

    private ResponseEntity authorizeRedirect(URIBuilder location) throws URISyntaxException {
        // SEND JSON RESPONSE TIL OPENAM HELPER
        location.addParameter("code", "im-just-a-fake-code");
        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT).location(location.build()).build();
    }

    private ResponseEntity authorizeHtmlPage(URIBuilder location) {
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
                ansatteIndeks.hentAlleAnsatte()
                        .sorted(comparing(NAVAnsatt::getDisplayName))
                        .map(username -> "<tr><a href=\"" + location.toString() + "&code=" + username.cn + "\"><h1>" + username.displayName + "</h1></a></tr>\n")
                        .collect(Collectors.joining("\n"))
                +
                "            </tbody>\n" +
                "        </table>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        return ResponseEntity.ok(html);
    }

    // TODO (FC): Trengs denne fortsatt?
    @PostMapping(value = "/oauth2/access_token", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "oauth2/access_token", notes = ("Mock impl av Oauth2 access_token"))
    public ResponseEntity accessToken(
            HttpServletRequest req,
            @RequestParam String grant_type,
            @RequestParam(required = false) String realm,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String refresh_token,
            @RequestParam String redirect_uri) {
        if ("authorization_code".equals(grant_type)) {
            String token = createIdToken(req, code);
            LOG.info("Fikk parametere:" + req.getParameterMap().toString());
            LOG.info("kall på /oauth2/access_token, opprettet token: " + token + " med redirect-url: " + redirect_uri);
            String generatedRefreshToken = "refresh:" + code;
            String generatedAccessToken = "access:" + code;
            Oauth2AccessTokenResponse oauthResponse = new Oauth2AccessTokenResponse(token, generatedRefreshToken, generatedAccessToken);
            return ResponseEntity.ok(oauthResponse);
        } else if ("refresh_token".equals(grant_type)) {
            if (!refresh_token.startsWith("refresh:")) {
                String message = "Invalid refresh token " + refresh_token;
                LOG.error(message);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
            } else {
                String username = refresh_token.substring(8);
                String token = createIdToken(req, username);
                LOG.info("Fikk parametere:" + req.getParameterMap().toString());
                LOG.info("refresh-token-kall på /oauth2/access_token, opprettet nytt token: " + token);
                String generatedRefreshToken = "refresh:" + username;
                String generatedAccessToken = "access:" + username;
                Oauth2AccessTokenResponse oauthResponse = new Oauth2AccessTokenResponse(token, generatedRefreshToken, generatedAccessToken);
                return ResponseEntity.ok(oauthResponse);
            }
        } else {
            LOG.error("Unknown grant_type " + grant_type);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unknown grant_type " + grant_type);
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
        OidcTokenGenerator tokenGenerator = new OidcTokenGenerator(keyStoreTool, username, nonce).withIssuer(issuer);
        if (clientIdCache.containsKey(state)) {
            String clientId = clientIdCache.get(state);
            tokenGenerator.addAud(clientId);
        }
        return tokenGenerator.create();
    }

    @GetMapping(value = "/isAlive.jsp" ,produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity isAliveMock() {
        String isAlive = "Server is ALIVE";
        return ResponseEntity.ok(isAlive);
    }

    @GetMapping(value = "/oauth2/../isAlive.jsp" ,produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity isAliveMockRassUrl() {
        String isAlive = "Server is ALIVE";
        return ResponseEntity.ok(isAlive);
    }

    @GetMapping(value = "/oauth2/connect/jwk_uri", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "oauth2/connect/jwk_uri", notes = ("Mock impl av Oauth2 jwk_uri"))
    public ResponseEntity authorize(HttpServletRequest req) {
        LOG.info("kall på /oauth2/connect/jwk_uri");
        String jwks = keyStoreTool.getJwks();
        LOG.info("JWKS: " + jwks);
        return ResponseEntity.ok(jwks);
    }

    /**
     * brukes til autentisere bruker slik at en slipper å autentisere senere. OpenAM mikk-makk .
     */
    @PostMapping(value = "/json/authenticate", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "json/authenticate", notes = ("Mock impl av OpenAM autenticate for service bruker innlogging"))
    public ResponseEntity serviceBrukerAuthenticate(HttpServletRequest req,
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

            return ResponseEntity.ok(template);
        } else {
            // generer token som brukes til å bekrefte innlogging ovenfor openam

            // TODO ingen validering av authId?
            // TODO generer unik session token?

            EndUserAuthenticateSuccess success = new EndUserAuthenticateSuccess("i-am-just-a-dummy-session-token-workaround", "/isso/console");
            return ResponseEntity.ok(success);
        }

    }

    @GetMapping(value = "/oauth2/.well-known/openid-configuration", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Discovery url", notes = ("Mock impl av discovery urlen. "))
    public ResponseEntity wellKnown(HttpServletRequest req) {
        LOG.info("kall på /oauth2/.well-known/openid-configuration");
        String baseUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort();
        WellKnownResponse wellKnownResponse = new WellKnownResponse(baseUrl, getIssuer());
        return ResponseEntity.ok(wellKnownResponse);
    }

}
