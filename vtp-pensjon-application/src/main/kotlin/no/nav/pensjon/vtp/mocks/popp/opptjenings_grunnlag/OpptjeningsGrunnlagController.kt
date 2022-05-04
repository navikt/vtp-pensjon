package no.nav.pensjon.vtp.mocks.popp.opptjenings_grunnlag

import no.nav.pensjon.vtp.mocks.popp.kjerne.OpptjeningsGrunnlag
import no.nav.pensjon.vtp.mocks.popp.kjerne.Pid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/popp/api/opptjeningsgrunnlag")
class OpptjeningsGrunnlagController {
    @GetMapping("/{pid}")
    fun hentOpptjeningsGrunnlag(@PathVariable("pid") pid: String) = HentOpptjeningsGrunnlagResponse(
        OpptjeningsGrunnlag(
            fnr = Pid(pid),
        )
    )
}
