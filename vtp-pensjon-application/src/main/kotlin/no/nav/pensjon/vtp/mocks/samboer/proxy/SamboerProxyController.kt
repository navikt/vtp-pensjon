package no.nav.pensjon.vtp.mocks.samboer.proxy

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository
import no.nav.pensjon.vtp.testmodell.personopplysning.SamboerforholdModell
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@Api(tags = ["Samboerforhold TPS proxy"])
@RequestMapping("/rest/")
class SamboerProxyController(
    private val personModellRepository: PersonModellRepository
) {

    @GetMapping("api/proxy/samboer/{pid}")
    @ApiOperation(value = "Hent samboerforhold")
    fun hentSamboerforhold(
        @PathVariable("pid") pid: String
    ) = personModellRepository.findById(pid).let {
        it?.samboerforhold?.map {
            SamboerProxyDTO(
                fnrInnmelder = it.innmelder,
                fnrMotpart = it.motpart,
                gyldigFraOgMed = it.fraOgMed,
                gyldigTilOgMed = it.tilOgMed,
                opprettetAv = it.opprettetAv
            )
        }?.firstOrNull() ?: ResponseEntity.status(HttpStatus.NO_CONTENT).build<Any>()
    }

    @PostMapping("api/proxy/samboer")
    @ApiOperation(value = "Opprett samboerforhold")
    fun opprettSamboerforhold(
        @RequestBody request: SamboerProxyDTO
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
        ResponseEntity.status(HttpStatus.CREATED).build<Any>()
    } ?: ResponseEntity.status(HttpStatus.NOT_FOUND).build<Any>()
}
