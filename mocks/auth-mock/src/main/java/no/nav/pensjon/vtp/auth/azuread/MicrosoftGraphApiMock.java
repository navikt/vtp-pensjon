package no.nav.pensjon.vtp.auth.azuread;

import static java.util.stream.Collectors.toList;

import io.swagger.annotations.Api;

import no.nav.pensjon.vtp.core.annotations.JaxrsResource;
import no.nav.pensjon.vtp.testmodell.ansatt.AnsatteIndeks;
import no.nav.pensjon.vtp.testmodell.ansatt.NAVAnsatt;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

@JaxrsResource
@Api(tags = {"AzureAd"})
@Path("/MicrosoftGraphApi")
public class MicrosoftGraphApiMock {
    private final AnsatteIndeks ansatteIndeks;

    public MicrosoftGraphApiMock(AnsatteIndeks ansatteIndeks) {
        this.ansatteIndeks = ansatteIndeks;
    }

    @GET
    @Produces({ "application/json;charset=UTF-8" })
    @Path("/oidc/userinfo")
    public Response userinfo(@HeaderParam("Authorization") String auth) {
        if (!auth.startsWith("Bearer access:")) {
            return Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Bad mock access token; must be on format Bearer access:<userid>")
                    .build();
        }

        String accessToken = auth.substring(14);
        String ident = accessToken.split(";")[0];

        NAVAnsatt ansatt = ansatteIndeks.hentNAVAnsatt(ident).orElseThrow(() -> new RuntimeException("Ansatt med ident " + ident + " ikke funnet i VTP."));

        Map<String, String> response = new HashMap<>();
        response.put("sub", ident);
        response.put("name", ansatt.displayName);
        response.put("family_name", ansatt.cn);
        response.put("given_name", ansatt.givenname);
        response.put("picture", "http://example.com/picture.jpg");
        response.put("email", ansatt.email);
        return Response.ok(response).build();
    }

    @GET
    @Produces({ "application/json;charset=UTF-8" })
    @Path("/v1.0/me")
    public Response me(@HeaderParam("Authorization") String auth, @QueryParam("select") String select) {
        if (!auth.startsWith("Bearer access:")) {
            return Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Bad mock access token; must be on format Bearer access:<userid>")
                    .build();
        }

        String accessToken = auth.substring(14);
        String ident = accessToken.split(";")[0];

        final List<Group> memberOf = ansatteIndeks.hentNAVAnsatt(ident).orElseThrow(() -> new RuntimeException("Fant ikke NAV-ansatt med brukernavn " + ident))
                .groups
                .stream()
                .map(Group::new)
                .collect(toList());

        final User user = new User("https://graph.microsoft.com/v1.0/$metadata#users(" + select + ")/$entity", ident, memberOf);

        return Response.ok(user).build();
    }

    public static class User {
        @JsonProperty("@odata.context")
        public String context;
        public String onPremisesSamAccountName;

        public List<Group> memberOf;

        public User(String context, String onPremisesSamAccountName, List<Group> memberOf) {
            this.context = context;
            this.onPremisesSamAccountName = onPremisesSamAccountName;
            this.memberOf = memberOf;
        }

        @Override
        public String toString() {
            return "User{" +
                    "context='" + context + '\'' +
                    ", onPremisesSamAccountName='" + onPremisesSamAccountName + '\'' +
                    '}';
        }
    }

    public static class Group {
        public String displayName;

        public Group(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return "Group{" +
                    "displayName='" + displayName + '\'' +
                    '}';
        }
    }
}
