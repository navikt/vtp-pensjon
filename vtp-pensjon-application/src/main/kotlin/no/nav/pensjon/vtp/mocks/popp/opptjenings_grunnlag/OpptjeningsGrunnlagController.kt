package no.nav.pensjon.vtp.mocks.popp.opptjenings_grunnlag

import no.nav.pensjon.vtp.mocks.popp.kjerne.ChangeStamp
import no.nav.pensjon.vtp.mocks.popp.kjerne.Inntekt
import no.nav.pensjon.vtp.mocks.popp.kjerne.OpptjeningsGrunnlag
import no.nav.pensjon.vtp.mocks.popp.kjerne.Pid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/rest/popp/api/opptjeningsgrunnlag")
class OpptjeningsGrunnlagController {
    @GetMapping("/{pid}")
    fun hentOpptjeningsGrunnlag(@PathVariable("pid") pid: String) = HentOpptjeningsGrunnlagResponse(
        OpptjeningsGrunnlag(
            fnr = Pid(pid),
            inntektListe = listOf(
                Inntekt(
                    changeStamp = ChangeStamp(
                        createdBy = "vtp-pensjon",
                        createdDate = Date(),
                        updatedBy = "vtp-pensjon",
                        updatedDate = Date(),
                    ),
                    inntektId = 1001L,
                    fnr = Pid(pid),
                    inntektAr = 2017,
                    kilde = "PEN",
                    kommune = "1337",
                    piMerke = null,
                    inntektType = "INN_LON",
                    belop = 300_000,
                ),
                Inntekt(
                    changeStamp = ChangeStamp(
                        createdBy = "vtp-pensjon",
                        createdDate = Date(),
                        updatedBy = "vtp-pensjon",
                        updatedDate = Date(),
                    ),
                    inntektId = 1002L,
                    fnr = Pid(pid),
                    inntektAr = 2018,
                    kilde = "POPP",
                    kommune = null,
                    piMerke = null,
                    inntektType = "SUM_PI",
                    belop = 300_001,
                ),
            )
        )
    )
}
