package no.nav.pensjon.vtp.mocks.inntektskomponenten

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "inntektskomponenten rest")
@RequestMapping("rest/inntektskomponenten-ws/rs/api")
class InntektControllerMockImpl {

    @PostMapping("/v1/abonnement")
    fun opprettAbonnement() = OpprettAbonnementResponse(100000000)

    @DeleteMapping("/v1/abonnement/{abonnementId}")
    fun opphorAbonnement(@PathVariable abonnementId: Int) = Unit

    @PostMapping("/v1/forventetinntekt")
    fun lagreForventetInntekt() = Unit

    @GetMapping("/v1/forventetinntekt")
    fun hentForventetInntekt(
        @RequestHeader(value = "norskident") fnr: String,
        @RequestHeader(value = "aar-list") aarList: List<String>
    ): ForventetInntektBruker =
        opprettForventetInntektBruker(fnr, aarList)

    @PostMapping("/v1/hentdetaljerteabonnerteinntekter")
    fun hentDetaljerteAbonnerteInntekter(@RequestBody body: HentInntekterRequest): HentInntekterResponse {
        return hentInntekterResponseApi(body.ident.identifikator, body.maanedFom, body.maanedTom)
    }

    @PostMapping("/v1/hentabonnerteinntekterbolk")
    fun hentAbonnerteInntekterBolk(@RequestBody body: HentInntekterBolkRequest): HentAbonnerteInntekterBolkResponse =
        hentDetaljerteInntekterBolkResponseApi(body.abonnerteInntekterIdentOgPeriodeListe)

    @PostMapping("/v1/hentinntektliste")
    fun hentInntektListe(@RequestBody body: HentInntekterRequest): HentInntekterResponse =
        hentInntekterResponseApi(body.ident.identifikator, body.maanedFom, body.maanedTom)

    @GetMapping("/ping")
    fun ping() = "ping pong"
}
