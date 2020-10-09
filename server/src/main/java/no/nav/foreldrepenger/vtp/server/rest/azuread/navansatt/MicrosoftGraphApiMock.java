package no.nav.foreldrepenger.vtp.server.rest.azuread.navansatt;

import io.swagger.annotations.Api;
import no.nav.foreldrepenger.vtp.server.rest.auth.UserRepository;
import no.nav.foreldrepenger.vtp.testmodell.ansatt.AnsatteIndeks;
import no.nav.foreldrepenger.vtp.testmodell.ansatt.NAVAnsatt;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Api(tags = {"AzureAd"})
@Path("/MicrosoftGraphApi")
public class MicrosoftGraphApiMock {
    @GET
    @Produces({ "application/json;charset=UTF-8" })
    @Path("/oidc/userinfo")
    public Response userinfo(@HeaderParam("Authorization") String auth) throws IOException {
        if (!auth.startsWith("Bearer access:")) {
            return Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Bad mock access token; must be on format Bearer access:<userid>")
                    .build();
        }

        String accessToken = auth.substring(14);
        String ident = accessToken.split(";")[0];

        AnsatteIndeks ansatteIndeks = BasisdataProviderFileImpl.getInstance().getAnsatteIndeks();
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

        Map<String, String> response = new HashMap<>();
        response.put("@odata.context", "https://graph.microsoft.com/v1.0/$metadata#users(" + select + ")/$entity");
        response.put("onPremisesSamAccountName", ident);
        return Response.ok(response).build();
    }
}
