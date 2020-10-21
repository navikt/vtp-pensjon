package no.nav.pensjon.vtp.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Api(tags = { "ABAC-PDP-Mock" })
@Path("/asm-pdp/authorize")
public class PdpRestTjeneste {
    private final static Logger LOG = LoggerFactory.getLogger(PdpRestTjeneste.class);

    @GET
    @Path("/ping")
    public Response ping() {
        return Response.ok().build();
    }

    @POST
    @Produces("application/xacml+json")
    @ApiOperation(value = "asm-pdp/authorize", notes = ("Mock impl av ABAC PDP authorize"))
    public Response authorize(String entity) throws IOException {
        LOG.info("Invoke: autorize with entry: {}", entity);
        int permits = getPermits(entity);
        return Response.ok(buildPermitResponse(permits)).build();
    }

    private int getPermits(String entity) throws IOException {
        int permits = 0;

        JsonNode xacmlJson = new ObjectMapper().reader().readTree(entity);
        // tell antall Request/Resource/Attribute for å bestemme antall responser

        JsonNode request = xacmlJson.get("Request");
        if (request != null && !request.isNull()) {
            JsonNode resource = request.get("Resource");
            if (resource != null && !resource.isNull()) {
                List<JsonNode> values = resource.findValues("Attribute");
                permits = values.size();
            }

        }
        return permits;
    }

    @SuppressWarnings("unused")
    private String buildDenyResponse() {
        return " { \"Response\" : {\"Decision\" : \"Deny\",\"InfotrygdSakStatus\" : {\"StatusCode\" : {\"Value\" : " +
            "\"urn:oasis:names:tc:xacml:1.0:status:ok\",\"StatusCode\" : {\"Value\" : " +
            "\"urn:oasis:names:tc:xacml:1.0:status:ok\"}}}}}";
    }

    private String buildPermitResponse(int antallPermits) {

        String permit = "{\"Decision\":\"Permit\",\"Status\":" +
                "{\"StatusCode\":{\"Value\":\"urn:oasis:names:tc:xacml:1.0:status:ok\",\"StatusCode\":{\"Value\":\"urn:oasis:names:tc:xacml:1.0:status:ok\"}}}}";

        if (antallPermits > 1) {
            int genPermits = antallPermits;
            StringBuilder permitResult = new StringBuilder(permit);
            while (genPermits-- > 1) {
                permitResult.append(", ").append(permit);
            }

            return " { \"Response\" : [" + permitResult.toString() + "] }";
        } else {
            return " { \"Response\" : " + permit + "}";
        }

    }
}
