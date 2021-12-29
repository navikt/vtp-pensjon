package no.nav.pensjon.vtp.mocks.nom

import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "NOM")
@RequestMapping("/rest")
class NomMock(private val personModellRepository: PersonModellRepository) {

    @GetMapping(path = ["/skjermet"])
    fun skjermet(@RequestParam("personident", required = true) personident: String) =
        personModellRepository.findById(personident)?.egenansatt ?: false
}
