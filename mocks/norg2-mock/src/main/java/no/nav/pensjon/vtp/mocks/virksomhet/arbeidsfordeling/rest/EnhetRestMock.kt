package no.nav.pensjon.vtp.mocks.virksomhet.arbeidsfordeling.rest

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import no.nav.norg2.model.Norg2RsArbeidsfordeling
import no.nav.norg2.model.Norg2RsEnhet
import no.nav.norg2.model.Norg2RsOrganisering
import no.nav.norg2.model.Norg2RsSimpleEnhet
import no.nav.pensjon.vtp.testmodell.enheter.EnheterIndeks
import no.nav.pensjon.vtp.testmodell.enheter.Norg2Modell
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.stream.Collectors

@RestController
@Api(tags = ["Norg2 enheter"])
@RequestMapping("/rest/norg2/api/v1")
class EnhetRestMock(private val enheterIndeks: EnheterIndeks) {
    @PostMapping(value = ["/arbeidsfordelinger"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiOperation(
        value = "Returnerer alle arbeidsfordelinger basert på multiple set av søkekriterier",
        response = Fordeling::class,
        responseContainer = "List",
        tags = ["arbeidsfordeling"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                code = 200,
                message = "Success",
                response = Fordeling::class,
                responseContainer = "List"
            ), ApiResponse(code = 400, message = "Bad request"), ApiResponse(
                code = 404,
                message = "Not found"
            ), ApiResponse(code = 500, message = "Internal Server Error")
        ]
    )
    fun getArbeidsFordelinger(
        @RequestParam(
            value = "fordelinger",
            required = false,
            defaultValue = ""
        ) fordelinger: List<Fordeling?>?,
        @RequestParam("skjermet") skjermet: Boolean
    ) = norg2RsArbeidsfordelinger(
        enheterIndeks.alleEnheter
    )

    @GetMapping(value = ["/enhet"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiOperation(
        value = "Returnerer en filtrert liste av alle enheter",
        response = Norg2RsEnhet::class,
        responseContainer = "List",
        tags = ["enhet"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                code = 200,
                message = "Success",
                response = Norg2RsEnhet::class,
                responseContainer = "List"
            ), ApiResponse(code = 400, message = "Bad request"), ApiResponse(
                code = 404,
                message = "Not found"
            ), ApiResponse(code = 500, message = "Internal Server Error")
        ]
    )
    fun getAllEnheterUsingGET(
        @RequestParam(value = "enhetStatusListe", required = false, defaultValue = "") enhetStatusListe: List<String?>,
        @RequestParam(
            value = "enhetsnummerListe",
            required = false,
            defaultValue = ""
        ) enhetsnummerListe: List<String?>,
        @RequestParam(
            value = "oppgavebehandlerFilter",
            required = false,
            defaultValue = ""
        ) oppgavebehandlerFilter: String?
    ): ResponseEntity<*> {
        val norg2RsEnheter = norg2RsEnheter(enheterIndeks.alleEnheter).stream()
            .filter { e: Norg2RsEnhet -> enhetsnummerListe.isEmpty() || enhetsnummerListe.contains(e.enhetNr) }
            .filter { e: Norg2RsEnhet -> enhetStatusListe.isEmpty() || enhetStatusListe.contains(e.status) }
            .collect(Collectors.toList())
        return ResponseEntity.ok(norg2RsEnheter)
    }

    @GetMapping(value = ["/enhet/navkontor/{geografiskOmraade}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiOperation(
        value = "Returnerer en NAV kontor som er en lokalkontor for spesifisert geografisk område",
        response = Norg2RsEnhet::class,
        tags = ["enhet"]
    )
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Success", response = Norg2RsEnhet::class), ApiResponse(
                code = 404,
                message = "Not Found"
            ), ApiResponse(code = 500, message = "Internal Server Error")
        ]
    )
    fun getEnhetByGeografiskOmraadeUsingGET(
        @PathVariable("geografiskOmraade") geografiskOmraade: String,
        @RequestParam("disk") disk: String?
    ) = enEnhet(4407L)

    @GetMapping(value = ["/enhet/{enhetsnummer}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiOperation(
        value = "Returnerer en enhet basert på enhetsnummer",
        response = Norg2RsEnhet::class,
        tags = ["enhet"]
    )
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Success", response = Norg2RsEnhet::class), ApiResponse(
                code = 404,
                message = "Not Found"
            ), ApiResponse(code = 500, message = "Internal Server Error")
        ]
    )
    fun getEnhetUsingGET(@PathVariable("enhetsnummer") enhetsnummer: String) =
        norg2RsEnheter(enheterIndeks.alleEnheter)
            .firstOrNull { it.enhetNr.equals(enhetsnummer, ignoreCase = true) }
            ?: enEnhet(enhetsnummer.toLong())

    @GetMapping(value = ["/enhet/{enhetsnummer}/organisering"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiOperation(
        value = "Returnerer en organiseringsliste for enhetsnummer",
        response = Array<Norg2RsOrganisering>::class,
        tags = ["enhet"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                code = 200,
                message = "Success",
                response = Array<Norg2RsOrganisering>::class
            ), ApiResponse(code = 404, message = "Not Found"), ApiResponse(code = 500, message = "Internal Server Error")
        ]
    )
    fun getOrganiseringListeForEnhet(@PathVariable("enhetsnummer") enhetsnummer: String): List<Norg2RsOrganisering> {
        val norg2RsEnhet = getEnhetUsingGET(enhetsnummer)

        val validFrom = Optional.ofNullable(norg2RsEnhet.aktiveringsdato).orElse(
            LocalDate.of(1950, 1, 1).format(
                DateTimeFormatter.ISO_LOCAL_DATE
            )
        )

        return listOf(
            Norg2RsOrganisering().apply {
                organiserer = Norg2RsSimpleEnhet().apply {
                    id = norg2RsEnhet.enhetId
                    navn = norg2RsEnhet.navn
                    nr = norg2RsEnhet.enhetNr
                    gyldigFra = validFrom
                }
                organisertUnder = Norg2RsSimpleEnhet().apply {
                    id = 99768L
                    navn = "Enhet over " + norg2RsEnhet.navn
                    nr = "99768"
                    gyldigFra = validFrom
                }
                fra = validFrom
                til = LocalDate.of(2036, 3, 18).format(DateTimeFormatter.ISO_LOCAL_DATE)
                id = 123456123456L
                orgType = "FORV"
            }
        )
    }

    private fun norg2RsEnheter(enheter: Collection<Norg2Modell>): List<Norg2RsEnhet> {
        return enheter
            .map { convert(it) }
    }

    private fun convert(modell: Norg2Modell): Norg2RsEnhet {
        return Norg2RsEnhet().apply {
            enhetNr = modell.enhetId
            enhetNr = modell.enhetId
            navn = modell.navn
            status = modell.status
            type = modell.type
            isOppgavebehandler = true
        }
    }

    private fun enEnhet(id: Long): Norg2RsEnhet {
        return Norg2RsEnhet().apply {
            enhetId = id
            enhetNr = id.toString()
            navn = "NAV Arbeid og ytelser Tønsberg"
            isOppgavebehandler = true
        }
    }

    private fun norg2RsArbeidsfordelinger(enheter: Collection<Norg2Modell>): List<Norg2RsArbeidsfordeling> {
        return enheter
            .map { convertToArbeidsfordeling(it) }
    }

    private fun convertToArbeidsfordeling(modell: Norg2Modell): Norg2RsArbeidsfordeling {
        return Norg2RsArbeidsfordeling().apply {
            enhetId = modell.enhetId.toLong()
            enhetNr = modell.enhetId
            enhetNavn = modell.navn
            diskresjonskode = modell.diskresjonskode
        }
    }
}
