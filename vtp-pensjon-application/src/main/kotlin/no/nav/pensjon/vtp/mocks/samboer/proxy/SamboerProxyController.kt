package no.nav.pensjon.vtp.mocks.samboer.proxy

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import no.nav.pensjon.vtp.mocks.samboer.SamboerDTO
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@Api(tags = ["Samboerforhold TPS proxy"])
@RequestMapping("/rest/")
class SamboerProxyController {

    @GetMapping("api/samboer/proxy/{pid}")
    @ApiOperation(value = "Hent samboerforhold")
    fun hentSamboerforhold(
        @PathVariable("pid") pid: String
    ) = SamboerDTO(
        fnrInnmelder = pid,
        fnrMotpart = "TODO",
        gyldigFraOgMed = LocalDate.now(),
        gyldigTilOgMed = null,
        opprettetAv = "VTP"
    )
}
