package no.nav.pensjon.vtp.mocks.dkif

import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.pensjon.vtp.testmodell.dkif.*
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Digital kontaktinformasjon")
@RequestMapping("/rest/dkif")
class DkifMock(private val dkifRepository: DkifRepository) {

    @GetMapping("/v1/personer/kontaktinformasjon")
    fun getIdenter(@RequestHeader("Nav-Personidenter") requestIdenter: List<String>): DkifResponse {
        val kontaktinfoMap = hashMapOf<String, Kontaktinfo>()
        val feilMap = hashMapOf<String, Feil>()

        requestIdenter.forEach {
            val kontaktinfo = dkifRepository.findById(it)
            if (kontaktinfo != null) {
                kontaktinfoMap[it] = kontaktinfo
            } else {
                feilMap[it] = Feil("Ingen kontaktinformasjon er registrert på personen")
            }
        }

        return DkifResponse(
            kontaktinfoMap.ifEmpty { null },
            feilMap.ifEmpty { null }
        )
    }

    @GetMapping("/ping")
    fun ping() = ok("pong")
}
