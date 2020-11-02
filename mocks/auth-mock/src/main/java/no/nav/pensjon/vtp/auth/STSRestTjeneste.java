package no.nav.pensjon.vtp.auth;

import io.swagger.annotations.Api;
import no.nav.pensjon.vtp.felles.OidcTokenGenerator;
import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenResponseType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.HttpHeaders;
import javax.xml.bind.JAXB;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;

import static org.apache.commons.codec.binary.Base64.encodeBase64String;

@RestController
@Api(tags = {"Security Token Service"})
@RequestMapping("/v1/sts")
public class STSRestTjeneste {
    private final STSIssueResponseGenerator generator = new STSIssueResponseGenerator();
    private final SamlTokenGenerator samlTokenGenerator = new SamlTokenGenerator();

    @PostMapping(value = "/token/exchange", produces = MediaType.APPLICATION_JSON_VALUE)
    public SAMLResponse dummySaml(@RequestParam String grant_type,
                                       @RequestParam String subject_token_type,
                                       @RequestParam String subject_token) {
        RequestSecurityTokenResponseType token = generator.buildRequestSecurityTokenResponseType("urn:oasis:names:tc:SAML:2.0:assertion");
        StringWriter sw = new StringWriter();
        JAXB.marshal(token, sw);
        String xmlString = sw.toString();

        SAMLResponse response = new SAMLResponse();
        response.setAccess_token(Base64.getUrlEncoder().withoutPadding().encodeToString(xmlString.getBytes()));
        response.setDecodedToken(xmlString);
        response.setToken_type("Bearer");
        response.setIssued_token_type(subject_token_type);
        response.setExpires_in(ZonedDateTime.of(LocalDateTime.MAX, ZoneId.systemDefault()));

        return response;
    }

    @GetMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserTokenResponse dummyToken(@RequestParam String grant_type,
                                        @RequestParam String scope) {
        String token = new OidcTokenGenerator("dummyBruker", "").withIssuer(Oauth2RestService.getIssuer()).create();
        return new UserTokenResponse(token, 600000 * 1000L, "Bearer");
    }

    @GetMapping(value = "/samltoken", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity dummyToken(HttpHeaders headers) throws Exception {
        String username = "CN=InternBruker,OU=AccountGroups,OU=Groups,OU=NAV,OU=BusinessUnits,DC=test,DC=local";

        String samlToken = samlTokenGenerator.issueToken(username);
        SAMLResponse samlResponse = new SAMLResponse();
        samlResponse.setAccess_token(encodeBase64String(samlToken.getBytes()));
        samlResponse.setDecodedToken(samlToken);
        samlResponse.setToken_type("Bearer");
        samlResponse.setIssued_token_type("urn:ietf:params:oauth:token-type:saml2");
        samlResponse.setExpires_in(ZonedDateTime.of(LocalDateTime.MAX, ZoneId.systemDefault()));

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Cache-Control", "no-store")
                .header("Pragma", "no-cache")
                .body(samlResponse);
    }

    public static class SAMLResponse {

        private String access_token;
        private String issued_token_type;
        private String token_type;
        private String decodedToken;
        private ZonedDateTime expires_in;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getIssued_token_type() {
            return issued_token_type;
        }

        public void setIssued_token_type(String issued_token_type) {
            this.issued_token_type = issued_token_type;
        }

        public String getToken_type() {
            return token_type;
        }

        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }

        public String getDecodedToken() {
            return decodedToken;
        }

        public void setDecodedToken(String decodedToken) {
            this.decodedToken = decodedToken;
        }

        public ZonedDateTime getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(ZonedDateTime expires_in) {
            this.expires_in = expires_in;
        }
    }

    public static class UserTokenResponse {
        private String access_token;
        private Long expires_in;
        private String token_type;
        private final LocalDateTime issuedTime = LocalDateTime.now();

        @SuppressWarnings("unused")
        public UserTokenResponse() {
            //Required by Jackson when mapping json object
        }

        public UserTokenResponse(String access_token, Long expires_in, String token_type) {
            this.access_token = access_token;
            this.expires_in = expires_in;
            this.token_type = token_type;
        }

        /**
         *
         * @param expirationLeeway the amount of seconds to be subtracted from the expirationTime to avoid returning false positives
         * @return <code>true</code> if "now" is after the expirationtime(minus leeway), else returns <code>false</code>
         */
        public boolean isExpired(long expirationLeeway) {
            return LocalDateTime.now().isAfter(issuedTime.plusSeconds(expires_in).minusSeconds(expirationLeeway));
        }

        public String getAccess_token() {
            return access_token;
        }

        public Long getExpires_in() {
            return expires_in;
        }

        public String getToken_type() {
            return token_type;
        }

        @Override
        public String toString() {
            return "UserTokenImpl{" +
                    "access_token='" + access_token + '\'' +
                    ", expires_in=" + expires_in +
                    ", token_type='" + token_type + '\'' +
                    '}';
        }
    }
}
