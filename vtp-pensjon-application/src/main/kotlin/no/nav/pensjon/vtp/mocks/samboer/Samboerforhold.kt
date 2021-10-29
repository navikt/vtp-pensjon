package no.nav.pensjon.vtp.mocks.samboer

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository
import no.nav.pensjon.vtp.util.asResponseEntity
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.servlet.http.HttpServletRequest

@RestController
@Api(tags = ["Samboerforhold"])
@RequestMapping("/rest/")
class Samboerforhold(
    private val personModellRepository: PersonModellRepository
) {

    @GetMapping("api/samboer/{pid}")
    @ApiOperation(value = "Henter alle samboerforhold")
    fun hentSamboer(
        request: HttpServletRequest,
        @PathVariable("pid") pid: String
    ) = (personModellRepository.findById(pid).let {
        it?.samboerforhold?.map {
            SamboerResponse(
                fnrInnmelder = it.innmelder,
                fnrMotpart = it.motpart,
                gyldigFraOgMed = it.fraOgMed,
                gyldigTilOgMed = it.tilOgMed,
                opprettetAv = it.opprettetAv,
                _links = SamboerResponse.Links(
                    //TODO: workaround for recursive linkTo self error
                    self = Link(URI("http://localhost:8060/rest/api/samboer/${pid}")),
                    avslutt = Link(linkTo<Samboerforhold> { avsluttForhold(it.id) }.toUri()),
                ),
            )
        }
    } ?: emptyList()).asResponseEntity()

    @PostMapping("/api/samboer")
    fun registrerForhold(
        @RequestBody request: SamboerRequest,
    ): HttpStatus {
        println(request)
        return HttpStatus.CREATED
    }

    @PutMapping("/api/forhold/{forholdId}/avslutt")
    @ApiOperation(value = "Avslutt samboerforhold")
    fun avsluttForhold(
        @PathVariable forholdId: String
    ) = personModellRepository.findAll().find {
        it.samboerforhold?.map { it.id }?.contains(forholdId) ?: false
    }
}
