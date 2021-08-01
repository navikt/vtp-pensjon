package no.nav.pensjon.vtp.auth.abac

import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Api(tags = ["ABAC-PDP-Mock"])
@RequestMapping("/rest/asm-pdp/authorize")
class PdpRestTjeneste {
    @GetMapping(value = ["/ping"], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun ping(): ResponseEntity<*> = ResponseEntity.ok().build<Any>()

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiOperation(value = "asm-pdp/authorize", notes = "Mock impl av ABAC PDP authorize")
    fun authorize(@RequestBody entity: String) =
        ResponseEntity.ok(buildPermitResponse(getPermits(entity)))

    private fun getPermits(entity: String): Int {
        var permits = 0
        val xacmlJson = ObjectMapper().reader().readTree(entity)
        // tell antall Request/Resource/Attribute for å bestemme antall responser
        val request = xacmlJson["Request"]
        if (request != null && !request.isNull) {
            val resource = request["Resource"]
            if (resource != null && !resource.isNull) {
                val values = resource.findValues("Attribute")
                permits = values.size
            }
        }
        return permits
    }

    private fun buildPermitResponse(antallPermits: Int): String {
        val permit = "{\"Decision\":\"Permit\",\"Status\":" +
            "{\"StatusCode\":{\"Value\":\"urn:oasis:names:tc:xacml:1.0:status:ok\",\"StatusCode\":{\"Value\":\"urn:oasis:names:tc:xacml:1.0:status:ok\"}}}}"
        return if (antallPermits > 1) {
            var genPermits = antallPermits
            val permitResult = StringBuilder(permit)
            while (genPermits-- > 1) {
                permitResult.append(", ").append(permit)
            }
            " { \"Response\" : [$permitResult] }"
        } else {
            " { \"Response\" : $permit}"
        }
    }
}
