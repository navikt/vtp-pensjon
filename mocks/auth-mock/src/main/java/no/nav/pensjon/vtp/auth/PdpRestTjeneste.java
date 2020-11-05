package no.nav.pensjon.vtp.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
@RestController
@Api(tags = { "ABAC-PDP-Mock" })
@RequestMapping("/rest/asm-pdp/authorize")
public class PdpRestTjeneste {
    private final static Logger LOG = LoggerFactory.getLogger(PdpRestTjeneste.class);

    @GetMapping(value = "/ping", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity ping() {
        return ResponseEntity.ok().build();
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "asm-pdp/authorize", notes = ("Mock impl av ABAC PDP authorize"))
    public ResponseEntity authorize(@RequestBody String entity) throws IOException {
        LOG.info("Invoke: autorize with entry: {}", entity);
        int permits = getPermits(entity);
        return ResponseEntity.ok(buildPermitResponse(permits));
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
