package no.nav.pensjon.vtp.mocks.dkif

import io.swagger.annotations.Api
import no.nav.pensjon.vtp.testmodell.dkif.*
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(tags = ["Digital kontaktinformasjon"])
@RequestMapping("/rest/dkif")
class DkifMock(private val dkifRepository: DkifRepository) {

    @GetMapping("/v1/personer/kontaktinformasjon")
    fun getIdenter(@RequestHeader("Nav-Personidenter") requestIdenter: List<String>): DkifResponse {
        val kontaktinfoMap = hashMapOf<String, Kontaktinfo>()
        val feilMap = hashMapOf<String, Feil>()

        requestIdenter.forEach {
            val kontaktinfo = dkifRepository.findById(it)
            if (kontaktinfo != null) {
                kontaktinfoMap.put(it, kontaktinfo)
            } else {
                feilMap.put(it, Feil("Ingen kontaktinformasjon er registrert på personen"))
            }
        }

        return DkifResponse(
            if (kontaktinfoMap.isEmpty()) null else kontaktinfoMap,
            if (feilMap.isEmpty()) null else feilMap
        )
    }

    @GetMapping("/ping")
    fun ping() = ok("pong")
}
