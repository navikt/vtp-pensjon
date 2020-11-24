package no.nav.pensjon.vtp.mocks.psak

import io.swagger.annotations.Api
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * VTP-teamet jobber med å lage mock for PDL, og dette er midlertidig løsning frem til vi merger inn i vtp-en
 */
@RestController
@Api(tags = ["PDL"])
@RequestMapping("/rest")
class PDLMock {
    @PostMapping(path = ["/graphql"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun hentFullPerson(data: String?): Map<String, List<Map<String, String?>>> {
        LOG.info("Nytt kall mot graphQL med data: $data")
        val errors: MutableMap<String, String?> = HashMap()
        errors["locations"] = null
        errors["path"] = null
        errors["extensions"] = null
        errors["message"] = "Ikke implementert"
        return java.util.Map.of("errors", listOf<Map<String, String?>>(errors))
    }

    @GetMapping(path = ["/skjermet"])
    fun skjermet(@RequestParam("ident") ident: String?): Boolean {
        return false
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(PDLMock::class.java)
    }
}