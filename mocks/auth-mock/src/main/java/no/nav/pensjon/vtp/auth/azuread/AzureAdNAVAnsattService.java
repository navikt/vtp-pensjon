package no.nav.pensjon.vtp.auth.azuread;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.pensjon.vtp.auth.Oauth2AccessTokenResponse;
import no.nav.pensjon.vtp.felles.AzureOidcTokenGenerator;
import no.nav.pensjon.vtp.felles.KeyStoreTool;
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
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@RestController
@Api(tags = {"AzureAd"})
@RequestMapping("/AzureAd")
public class AzureAdNAVAnsattService {
    private static final Logger LOG = LoggerFactory.getLogger(AzureAdNAVAnsattService.class);

    private final AnsatteIndeks ansatteIndeks;

    public AzureAdNAVAnsattService(AnsatteIndeks ansatteIndeks) {
        this.ansatteIndeks = ansatteIndeks;
    }

    @GetMapping(value = "/isAlive" ,produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity isAliveMock() {
        String isAlive = "Azure AD is OK";
        return ResponseEntity.ok(isAlive);
    }

    @GetMapping(value = "/{tenant}/v2.0/.well-known/openid-configuration", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Azure AD Discovery url", notes = ("Mock impl av Azure AD discovery urlen. "))
    public ResponseEntity wellKnown(@Context HttpServletRequest req, @PathVariable("tenant") String tenant, @RequestParam("p") String profile) {
        String baseUrl = getBaseUrl(req);
        String graphUrl = getGraphUrl(req);
        WellKnownResponse wellKnownResponse = new WellKnownResponse(baseUrl, graphUrl, tenant, profile);
        return ResponseEntity.ok(wellKnownResponse);
    }

    @GetMapping(value = "/{tenant}/discovery/v2.0/keys", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "azureAd/discovery/keys", notes = ("Mock impl av Azure AD jwk_uri"))
    public ResponseEntity authorize(@Context HttpServletRequest req) {
        LOG.info("kall på /oauth2/connect/jwk_uri");
        String jwks = KeyStoreTool.getJwks();
        LOG.info("JWKS: " + jwks);
        return ResponseEntity.ok(jwks);
    }

    @PostMapping(value = "/{tenant}/oauth2/v2.0/token", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "azureAd/access_token", notes = ("Mock impl av Azure AD access_token"))
    public ResponseEntity accessToken(
            @Context HttpServletRequest req,
            @PathVariable("tenant") String tenant,
            @FormParam("grant_type") String grantType,
            @FormParam("client_id") String clientId,
            @FormParam("realm") String realm,
            @FormParam("code") String code,
            @FormParam("refresh_token") String refreshToken,
            @FormParam("redirect_uri") String redirectUri) {
        if ("authorization_code".equals(grantType)) {
            String token = createIdToken(code, tenant, clientId);
            String generatedRefreshToken = "refresh:" + code;
            String generatedAccessToken = "access:" + code;
            LOG.info("Fikk parametere:" + req.getParameterMap().toString());
            LOG.info("kall på /oauth2/access_token, opprettet token: " + token + " med redirect-url: " + redirectUri);
            Oauth2AccessTokenResponse oauthResponse = new Oauth2AccessTokenResponse(token, generatedRefreshToken, generatedAccessToken);
            return ResponseEntity.ok(oauthResponse);
        } else if ("refresh_token".equals(grantType)) {
            String usernameWithNonce = refreshToken.substring(8);
            String token = createIdToken(usernameWithNonce /*+ ";"*/, tenant, clientId);
            LOG.info("Fikk parametere:" + req.getParameterMap().toString());
            LOG.info("refresh-token-kall på /oauth2/access_token, opprettet nytt token: " + token);
            String generatedRefreshToken = "refresh:" + usernameWithNonce;
            String generatedAccessToken = "access:" + usernameWithNonce;
            Oauth2AccessTokenResponse oauthResponse = new Oauth2AccessTokenResponse(token, generatedRefreshToken, generatedAccessToken);
            return ResponseEntity.ok(oauthResponse);
        } else {
            LOG.error("Unknown grant_type " + grantType);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unknown grant_type " + grantType);
        }
    }

    private String createIdToken(String code, String tenant, String clientId) {
        try {
            String[] codeData = code.split(";");
            String username = codeData[0];
            String nonce = codeData.length > 1 ? codeData[1] : null;
            NAVAnsatt user = ansatteIndeks.hentNAVAnsatt(username).orElseThrow(() -> new RuntimeException("Fant ikke NAV-ansatt med brukernavn " + username));

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

    @GetMapping(value = "/{tenant}/v2.0/authorize", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_HTML_VALUE})
    @ApiOperation(value = "AzureAD/v2.0/authorize", notes = ("Mock impl av Azure AD authorize"))
    public ResponseEntity authorize(
            @Context HttpServletRequest req,
            @Context HttpServletResponse resp,
            @PathVariable("tenant") String tenant,
            @RequestParam("response_type") @DefaultValue("code") String responseType,
            @RequestParam("scope") @DefaultValue("openid") String scope,
            @RequestParam("client_id") String clientId,
            @RequestParam("state") String state,
            @RequestParam("nonce") String nonce,
            @RequestParam("redirect_uri") String redirectUri
    )
            throws Exception {
        LOG.info("kall mot AzureAD authorize med redirecturi " + redirectUri);
        Objects.requireNonNull(scope, "Missing the ?scope=xxx query parameter");
        List<String> validScopes = Arrays.asList("openid", "profile", "offline_access");
        String[] scopes = scope.split("\\s+");
        for (String s : scopes) {
            if (!validScopes.contains(s)) {
                throw new IllegalArgumentException("Unsupported scope [" + s + "], supported scopes are: " + String.join(", ", validScopes));
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

    private ResponseEntity authorizeHtmlPage(URIBuilder location, String nonce) {
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
                        .map(username -> "<tr><a href=\"" + location.toString() + "&code=" + username.cn + ";" + nonce + "\"><h1>" + username.displayName + "</h1></a></tr>\n")
                        .collect(Collectors.joining("\n"))
                +
                "            </tbody>\n" +
                "        </table>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        return ResponseEntity.ok(html);
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
