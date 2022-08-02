package no.nav.pensjon.vtp.mocks.samboer.proxy

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository
import no.nav.pensjon.vtp.testmodell.personopplysning.SamboerforholdModell
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@Tag(name = "Samboerforhold TPS proxy")
@RequestMapping("/rest/")
class SamboerProxyController(
    private val personModellRepository: PersonModellRepository
) {

    @GetMapping("api/proxy/samboer/{pid}")
    @Operation(summary = "Hent samboerforhold")
    fun hentSamboerforhold(
        @PathVariable("pid") pid: String
    ) = personModellRepository.findById(pid).let {
        it?.samboerforhold?.map {
            SamboerProxyDTO(
                pidBruker = it.innmelder,
                pidSamboer = it.motpart,
                datoFom = it.fraOgMed,
                datoTom = it.tilOgMed,
                registrertAv = it.opprettetAv
            )
        }?.firstOrNull() ?: ResponseEntity.status(HttpStatus.NO_CONTENT).build<Any>()
    }

    @PostMapping("api/proxy/samboer")
    @Operation(summary = "Opprett samboerforhold")
    fun opprettSamboerforhold(
        @RequestBody request: SamboerProxyDTO
    ) = personModellRepository.findById(request.pidBruker)?.apply {
        copy(
            samboerforhold = listOf(
                SamboerforholdModell(
                    id = UUID.randomUUID().toString(),
                    innmelder = request.pidBruker,
                    motpart = request.pidSamboer,
                    fraOgMed = request.datoFom,
                    tilOgMed = request.datoTom,
                    opprettetAv = request.registrertAv
                )
            )
        ).let(personModellRepository::save)
        ResponseEntity.status(HttpStatus.CREATED).build<Any>()
    } ?: ResponseEntity.status(HttpStatus.NOT_FOUND).build<Any>()
}
