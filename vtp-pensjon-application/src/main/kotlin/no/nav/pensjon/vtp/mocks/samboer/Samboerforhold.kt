package no.nav.pensjon.vtp.mocks.samboer

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository
import no.nav.pensjon.vtp.testmodell.personopplysning.SamboerforholdModell
import no.nav.pensjon.vtp.testmodell.util.SamboerId
import no.nav.pensjon.vtp.util.asResponseEntity
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Api(tags = ["Samboerforhold"])
@RequestMapping("/rest/")
class Samboerforhold(
    private val personModellRepository: PersonModellRepository
) {

    @GetMapping("api/samboer/{pid}")
    @ApiOperation(value = "Henter alle samboerforhold")
    fun hentSamboer(
        @PathVariable("pid") pid: String
    ): ResponseEntity<List<SamboerDTO>> = (
        personModellRepository.findById(pid).let {
            it?.samboerforhold?.map {
                SamboerDTO(
                    fnrInnmelder = it.innmelder,
                    fnrMotpart = it.motpart,
                    gyldigFraOgMed = it.fraOgMed,
                    gyldigTilOgMed = it.tilOgMed,
                    opprettetAv = it.opprettetAv
                ).apply {
                    add(linkTo<Samboerforhold> { hentSamboer(pid) }.withSelfRel())
                    add(linkTo<Samboerforhold> { avsluttForhold(it.id) }.withRel("avslutt"))
                }
            }
        } ?: emptyList()
        ).asResponseEntity()

    @PostMapping("/api/samboer")
    fun registrerForhold(
        @RequestBody request: SamboerDTO,
    ) = (
        personModellRepository.findById(request.fnrInnmelder)?.apply {
            samboerforhold.add(
                SamboerforholdModell(
                    id = SamboerId.nextId(),
                    innmelder = request.fnrInnmelder,
                    motpart = request.fnrMotpart,
                    fraOgMed = request.gyldigFraOgMed,
                    tilOgMed = request.gyldigTilOgMed,
                    opprettetAv = request.opprettetAv
                )
            )
        }?.let {
            personModellRepository.save(it)
        }?.run { HttpStatus.OK } ?: HttpStatus.NOT_FOUND
        ).asResponseEntity()

    @PutMapping("/api/forhold/{forholdId}/avslutt")
    @ApiOperation(value = "Avslutt samboerforhold")
    fun avsluttForhold(
        @PathVariable forholdId: String
    ) = (
        personModellRepository.findById(
            personModellRepository.findAll().find {
                it.samboerforhold.map { it.id }.contains(forholdId)
            }?.ident ?: "UKJENT"
        )?.let {
            it.samboerforhold.removeIf {
                it.id == forholdId
            }
            personModellRepository.save(it)
        }?.run { HttpStatus.OK } ?: HttpStatus.NOT_FOUND
        ).asResponseEntity()
}
