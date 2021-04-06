package no.nav.pensjon.vtp.mocks.journalpost.dokarkiv

import io.swagger.annotations.*
import no.nav.dokarkiv.generated.model.Sak
import no.nav.dokarkiv.generated.model.SakJson
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@Api(tags = ["Dokarkiv"])
@RequestMapping("/rest/api/v1")
class SakController {
    @ApiOperation(
        value = "Finner saker for angitte søkekriterier",
        nickname = "finnSakerUsingGET",
        notes = "",
        response = SakJson::class,
        responseContainer = "List",
        authorizations = [Authorization(value = "Authorization"), Authorization(value = "Basic"), Authorization(value = "Saml")],
        tags = ["sak-controller"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                code = 200,
                message = "OK",
                response = SakJson::class,
                responseContainer = "List"
            ), ApiResponse(code = 400, message = "Ugyldig input"), ApiResponse(
                code = 401,
                message = "Konsument mangler gyldig token"
            ), ApiResponse(code = 500, message = "Ukjent feilsituasjon har oppstått i Sak"), ApiResponse(
                code = 503,
                message = "En eller flere tjenester som sak er avhengig av er ikke tilgjengelige eller svarer ikke."
            )
        ]
    )
    @GetMapping("/saker")
    fun finnSakerUsingGET(
        @ApiParam(value = "") @RequestHeader(
            value = "X-Correlation-ID",
            required = false
        ) xCorrelationID: String?,
        @ApiParam(value = "Filtrering på saker opprettet for en aktør (person)") @RequestParam(value = "aktoerId") aktoerId: @Valid String?,
        @ApiParam(value = "Filtrering på applikasjon (iht felles kodeverk)") @RequestParam(
            value = "applikasjon",
            required = false
        ) applikasjon: @Valid String?,
        @ApiParam(value = "Filtrering på fagsakNr") @RequestParam(
            value = "fagsakNr",
            required = false
        ) fagsakNr: String?,
        @ApiParam(value = "Filtrering på saker opprettet for en organisasjon") @RequestParam(
            value = "orgnr",
            required = false
        ) orgnr: String?,
        @ApiParam(value = "Filtrering på tema (iht felles kodeverk)") @RequestParam(
            value = "tema",
            required = false
        ) tema: List<String?>?
    ): List<Sak> = emptyList()

    @ApiOperation(
        value = "Henter sak for en gitt id",
        nickname = "hentSakUsingGET",
        notes = "",
        response = SakJson::class,
        authorizations = [Authorization(value = "Authorization"), Authorization(value = "Basic"), Authorization(value = "Saml")],
        tags = ["sak-controller"]
    )
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "OK", response = SakJson::class), ApiResponse(
                code = 401,
                message = "Konsument mangler gyldig token"
            ), ApiResponse(code = 403, message = "Konsument har ikke tilgang til å gjennomføre handlingen"), ApiResponse(
                code = 404,
                message = "Det finnes ingen sak for angitt id"
            ), ApiResponse(code = 500, message = "Ukjent feilsituasjon har oppstått i Sak"), ApiResponse(
                code = 503,
                message = "En eller flere tjenester som sak er avhengig av er ikke tilgjengelige eller svarer ikke."
            )
        ]
    )
    @GetMapping("/{id}")
    fun hentSakUsingGET(
        @ApiParam(value = "", required = false) @RequestHeader(
            value = "X-Correlation-ID",
            required = false
        ) xCorrelationID: String?,
        @ApiParam(value = "id", required = true) @PathVariable("id") id: Long
    ) = Sak(
        fagsakId = id.toString(),
        arkivsaksnummer = id.toString(),
        arkivsaksystem = Sak.Arkivsaksystem.pSAK,
    )
}
