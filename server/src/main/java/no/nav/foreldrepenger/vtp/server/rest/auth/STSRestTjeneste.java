package no.nav.foreldrepenger.vtp.server.rest.auth;

import io.swagger.annotations.Api;
import no.nav.foreldrepenger.vtp.felles.OidcTokenGenerator;
import no.nav.foreldrepenger.vtp.server.ws.STSIssueResponseGenerator;
import no.nav.foreldrepenger.vtp.server.ws.SamlTokenGenerator;
import org.apache.cxf.ws.security.sts.provider.model.RequestSecurityTokenResponseType;
import org.jose4j.lang.JoseException;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.xml.bind.JAXB;
import java.io.StringWriter;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

import static org.apache.commons.codec.binary.Base64.encodeBase64String;

@Api(tags = {"Security Token Service"})
@Path("/v1/sts")
public class STSRestTjeneste {
    private final STSIssueResponseGenerator generator = new STSIssueResponseGenerator();
    private final SamlTokenGenerator samlTokenGenerator = new SamlTokenGenerator();

    @POST
    @Path("/token/exchange")
    @Produces({MediaType.APPLICATION_JSON})
    public SAMLResponse dummySaml(@QueryParam("grant_type") String grant_type,
                                       @QueryParam("subject_token_type") String issuedTokenType,
                                       @QueryParam("subject_token") String subject_token) {
        RequestSecurityTokenResponseType token = generator.buildRequestSecurityTokenResponseType("urn:oasis:names:tc:SAML:2.0:assertion");
        StringWriter sw = new StringWriter();
        JAXB.marshal(token, sw);
        String xmlString = sw.toString();

        SAMLResponse response = new SAMLResponse();
        response.setAccess_token(Base64.getUrlEncoder().withoutPadding().encodeToString(xmlString.getBytes()));
        response.setDecodedToken(xmlString);
        response.setToken_type("Bearer");
        response.setIssued_token_type(issuedTokenType);
        response.setExpires_in(LocalDateTime.MAX);

        return response;
    }

    @GET
    @Path("/token")
    @Produces({MediaType.APPLICATION_JSON})
    public UserTokenResponse dummyToken(@QueryParam("grant_type") String grant_type,
                                        @QueryParam("scope") String scope) throws JoseException {
        String token = new OidcTokenGenerator("dummyBruker", "").withIssuer(Oauth2RestService.getIssuer()).create();
        return new UserTokenResponse(token, 600000L, "Bearer");
    }

    @GET
    @Path("/samltoken")
    @Produces({MediaType.APPLICATION_JSON})
    public Response dummyToken(@Context SecurityContext securityContext, @Context HttpHeaders headers) throws Exception {
        String username = Optional.ofNullable(securityContext)
                .map(SecurityContext::getUserPrincipal)
                .map(Principal::getName)
                .orElse("CN=InternBruker,OU=AccountGroups,OU=Groups,OU=NAV,OU=BusinessUnits,DC=test,DC=local");

        String samlToken = samlTokenGenerator.issueToken(username);
        SAMLResponse samlResponse = new SAMLResponse();
        samlResponse.setAccess_token(encodeBase64String(samlToken.getBytes()));
        samlResponse.setDecodedToken(samlToken);
        samlResponse.setToken_type("Bearer");
        samlResponse.setIssued_token_type("urn:ietf:params:oauth:token-type:saml2");
        samlResponse.setExpires_in(LocalDateTime.MAX);

        return Response.status(Response.Status.OK).entity(samlResponse).header("Cache-Control", "no-store").header("Pragma", "no-cache").build();
    }

    public static class SAMLResponse {

        private String access_token;
        private String issued_token_type;
        private String token_type;
        private String decodedToken;
        private LocalDateTime expires_in;

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

        public LocalDateTime getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(LocalDateTime expires_in) {
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
