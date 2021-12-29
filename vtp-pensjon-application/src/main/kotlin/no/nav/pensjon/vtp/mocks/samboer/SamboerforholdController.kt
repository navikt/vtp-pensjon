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
import java.util.*

@RestController
@Tag(name = "Samboerforhold")
@RequestMapping("/rest/")
class SamboerforholdController(
    private val personModellRepository: PersonModellRepository,
) {

    @GetMapping("api/samboer/{pid}")
    @Operation(summary = "Henter alle samboerforhold")
    fun hentSamboer(
        @PathVariable("pid") pid: String
    ): ResponseEntity<SamboerDTO> = personModellRepository.findById(pid)
        ?.run {
            samboerforhold.map {
                SamboerDTO(
                    fnrInnmelder = it.innmelder,
                    fnrMotpart = it.motpart,
                    gyldigFraOgMed = it.fraOgMed,
                    gyldigTilOgMed = it.tilOgMed,
                    opprettetAv = it.opprettetAv
                ).apply {
                    add(linkTo<SamboerforholdController> { hentSamboer(pid) }.withSelfRel())
                    // add(linkTo<SamboerforholdController> { avsluttForhold(it.id) }.withRel("avslutt"))
                }
            }.firstOrNull()
        }
        ?.let { ResponseEntity.ok(it) }
        ?: noContent().build()

    @PostMapping("/api/samboer")
    @Operation(summary = "Registrer samboerforhold")
    fun registrerForhold(
        @RequestBody request: SamboerDTO,
    ) = personModellRepository.findById(request.fnrInnmelder)?.apply {
        copy(
            samboerforhold = listOf(
                SamboerforholdModell(
                    id = UUID.randomUUID().toString(),
                    innmelder = request.fnrInnmelder,
                    motpart = request.fnrMotpart,
                    fraOgMed = request.gyldigFraOgMed,
                    tilOgMed = request.gyldigTilOgMed,
                    opprettetAv = request.opprettetAv
                )
            )
        ).let(personModellRepository::save)
        ResponseEntity.status(CREATED).build<Any>()
    } ?: ResponseEntity.status(NOT_FOUND).build<Any>()

    @PutMapping("/api/forhold/{forholdId}/avslutt")
    @Operation(summary = "Avslutt samboerforhold")
    fun avsluttForhold(
        @PathVariable forholdId: String
    ) = personModellRepository.findBySamboerforholdId(forholdId)
        ?.run {
            copy(samboerforhold = samboerforhold.filterNot { it.id == forholdId })
                .let(personModellRepository::save)
            ResponseEntity.status(NO_CONTENT).build<Any>()
        }
        ?: ResponseEntity.status(NOT_FOUND).build<Any>()
}
