package no.nav.pensjon.vtp.mocks.samboer

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository
import no.nav.pensjon.vtp.testmodell.personopplysning.SamboerforholdModell
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@RestController
@Tag(name = "Samboerforhold")
@RequestMapping("/rest/samboer")
class SamboerforholdController(
    private val personModellRepository: PersonModellRepository,
) {

    @GetMapping("api/samboer/{pid}")
    @Operation(summary = "Hent samboerforhold")
    fun hentSamboerforhold(
        @PathVariable("pid") pid: String
    ): ResponseEntity<SamboerDTO> = personModellRepository.findById(pid)
        ?.run {
            samboerforhold.filter { !it.annullert }.map {
                SamboerDTO(
                    pidBruker = pid,
                    pidSamboer = it.pidSamboer,
                    datoFom = it.datoFom,
                    datoTom = it.datoTom,
                    opprettetAv = it.opprettetAv
                ).apply {
                    add(linkTo<SamboerforholdController> { hentSamboerforhold(pid) }.withSelfRel())
                    add(linkTo<SamboerforholdController> { avsluttForhold(it.id, "{datoTom}") }.withRel("avslutt"))
                    add(linkTo<SamboerforholdController> { annullerForhold(it.id) }.withRel("annuller"))
                }
            }.firstOrNull { it.datoTom == null }
        }
        ?.let { ResponseEntity.ok(it) }
        ?: noContent().build()

    @PostMapping("/api/samboer")
    @Operation(summary = "Registrer samboerforhold")
    fun registrerForhold(
        @RequestBody request: SamboerDTO,
    ) = personModellRepository.findById(request.pidBruker)?.apply {
        copy(
            samboerforhold = listOf(
                SamboerforholdModell(
                    id = UUID.randomUUID().toString(),
                    pidSamboer = request.pidSamboer,
                    datoFom = request.datoFom,
                    datoTom = request.datoTom,
                    opprettetAv = request.opprettetAv
                )
            )
        ).let(personModellRepository::save)
        ResponseEntity.status(CREATED).build<Any>()
    } ?: ResponseEntity.status(NOT_FOUND).build<Any>()

    @PutMapping("/api/samboer/forhold/{forholdId}/avslutt")
    @Operation(summary = "Avslutt samboerforhold")
    fun avsluttForhold(
        @PathVariable forholdId: String,
        @RequestParam datoTom: String
    ) = personModellRepository.findBySamboerforholdId(forholdId)
        ?.run {
            copy(samboerforhold = samboerforhold.also {
                it.find { it.id == forholdId }?.datoTom = LocalDate.parse(datoTom, DateTimeFormatter.ISO_DATE)
            }).let(personModellRepository::save)
            ResponseEntity.status(OK).build<Any>()
        }
        ?: ResponseEntity.status(NOT_FOUND).build<Any>()

    @PutMapping("/api/samboer/forhold/{forholdId}/annuller")
    @Operation(summary = "Annuller samboerforhold")
    fun annullerForhold(
        @PathVariable forholdId: String
    ) = personModellRepository.findBySamboerforholdId(forholdId)
        ?.run {
            copy(samboerforhold = samboerforhold.also {
                it.find { it.id == forholdId }?.annullert = true
            }).let(personModellRepository::save)
            ResponseEntity.status(OK).build<Any>()
        }
        ?: ResponseEntity.status(NOT_FOUND).build<Any>()
}
