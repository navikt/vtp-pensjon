package no.nav.pensjon.vtp.mocks.popp.beholdning

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import no.nav.pensjon.vtp.mocks.popp.kjerne.Pid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/rest/popp/api/beholdning")
class BeholdningController {

    @PostMapping
    fun hentBeholdningListe(@RequestBody(required = false) body: BeholdningListeRequest): BeholdningListeResponse {
        return BeholdningListeResponse()
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class BeholdningListeRequest {
        private val fnr: Pid? = null
        private val beholdningType: String? = null
        private val serviceDirectiveTPOPP006: String? = null
        private val fomDato: Date? = null
        private val tomDato: Date? = null
    }

    class BeholdningListeResponse {
        val beholdninger = emptyList<String>()
    }
}
