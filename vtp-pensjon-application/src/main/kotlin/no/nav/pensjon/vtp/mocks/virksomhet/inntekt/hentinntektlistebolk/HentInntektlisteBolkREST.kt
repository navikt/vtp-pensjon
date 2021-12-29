package no.nav.pensjon.vtp.mocks.virksomhet.inntekt.hentinntektlistebolk

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.pensjon.vtp.mocks.virksomhet.inntekt.hentinntektlistebolk.modell.HentInntektlisteBolkMapperRest
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks
import no.nav.tjenester.aordningen.inntektsinformasjon.request.HentInntektListeBolkRequest
import no.nav.tjenester.aordningen.inntektsinformasjon.response.HentInntektListeBolkResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.YearMonth

@RestController
@Tag(name = "/inntektskomponenten-ws/rs/api/v1/hentinntektlistebolk")
@RequestMapping("/rest/inntektskomponenten-ws/rs/api/v1/hentinntektlistebolk")
class HentInntektlisteBolkREST(
    private val hentInntektlisteBolkMapperRest: HentInntektlisteBolkMapperRest,
    private val inntektYtelseIndeks: InntektYtelseIndeks
) {
    @PostMapping
    @Operation(summary = "HentInntektlisteBolk", description = "Returnerer inntektliste fra Inntektskomponenten")
    fun hentInntektlisteBolk(@RequestBody request: HentInntektListeBolkRequest) =
        HentInntektListeBolkResponse().apply {
            arbeidsInntektIdentListe = request.identListe.mapNotNull { aktoer ->
                inntektYtelseIndeks.getInntektYtelseModellFraAktørId(aktoer.identifikator)
                    ?.let { inntektYtelseModell ->
                        hentInntektlisteBolkMapperRest.makeArbeidsInntektIdent(
                            inntektYtelseModell.inntektskomponentModell,
                            aktoer,
                            request.maanedFom ?: YearMonth.of(1990, 1),
                            request.maanedTom ?: YearMonth.of(1990, 1)
                        )
                    }
            }
        }
}
