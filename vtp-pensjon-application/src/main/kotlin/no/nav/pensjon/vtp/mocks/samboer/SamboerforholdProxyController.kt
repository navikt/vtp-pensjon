package no.nav.pensjon.vtp.mocks.samboer

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository
import no.nav.pensjon.vtp.testmodell.personopplysning.SamboerforholdModell
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@RestController
@Tag(name = "Samboerforhold proxy")
@RequestMapping("/rest/samboer")
class SamboerforholdProxyController(
    private val personModellRepository: PersonModellRepository
) {

    @GetMapping("proxy/samboer/{pid}")
    @Operation(summary = "Hent samboerforhold")
    fun hentSamboerforhold(
        @PathVariable("pid") pid: String
    ) = personModellRepository.findById(pid).let {
        it?.samboerforhold?.map {
            SamboerDTO(
                pidBruker = pid,
                pidSamboer = it.pidSamboer,
                datoFom = it.datoFom,
                datoTom = it.datoTom,
                registrertAv = it.opprettetAv
            )
        }?.firstOrNull { it.datoTom == null } ?: ResponseEntity.status(HttpStatus.NO_CONTENT).build<Any>()
    }

    @GetMapping("proxy/samboer/historikk/{pid}")
    @Operation(summary = "Hent alle samboerforhold registert på bruker")
    fun hentSamboerhistorikk(
        @PathVariable("pid") pid: String
    ) = personModellRepository.findById(pid).let {
        it?.samboerforhold?.map {
            SamboerDTO(
                pidBruker = pid,
                pidSamboer = it.pidSamboer,
                datoFom = it.datoFom,
                datoTom = it.datoTom,
                registrertAv = it.opprettetAv
            )
        } ?: ResponseEntity.status(HttpStatus.NO_CONTENT).build<Any>()
    }

    @PostMapping("proxy/samboer")
    @Operation(summary = "Opprett samboerforhold")
    fun opprettSamboerforhold(
        @RequestBody request: SamboerHateoasDTO
    ) = personModellRepository.findById(request.pidBruker)?.apply {
        copy(
            samboerforhold = samboerforhold + SamboerforholdModell(
                    id = UUID.randomUUID().toString(),
                    pidSamboer = request.pidSamboer,
                    datoFom = request.datoFom,
                    datoTom = request.datoFom,
                    opprettetAv = request.registrertAv
            )
        ).let(personModellRepository::save)
        ResponseEntity.status(HttpStatus.CREATED).build<Any>()
    } ?: ResponseEntity.status(HttpStatus.NOT_FOUND).build<Any>()

    @PutMapping("proxy/samboer/{pid}")
    @Operation(summary = "Avslutt samboerforhold")
    fun avsluttSamboerforhold(
        @PathVariable("pid") pid: String,
        @RequestParam("datoTom") datoTom: String
    ) = personModellRepository.findById(pid)?.apply {
        val parsedDatoTom = LocalDate.parse(datoTom, DateTimeFormatter.ISO_LOCAL_DATE)
        copy(samboerforhold = samboerforhold.also {
            it.sortedBy { it.datoTom }.first().datoTom = parsedDatoTom
        }).let(personModellRepository::save)
        ResponseEntity.status(HttpStatus.OK).build<Any>()
    } ?: ResponseEntity.status(HttpStatus.NOT_FOUND).build<Any>()
}
