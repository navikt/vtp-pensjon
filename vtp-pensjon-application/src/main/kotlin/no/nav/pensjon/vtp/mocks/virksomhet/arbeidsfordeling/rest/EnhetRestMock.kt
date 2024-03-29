package no.nav.pensjon.vtp.mocks.virksomhet.arbeidsfordeling.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.norg2.model.Norg2RsArbeidsfordeling
import no.nav.norg2.model.Norg2RsEnhet
import no.nav.norg2.model.Norg2RsOrganisering
import no.nav.norg2.model.Norg2RsSimpleEnhet
import no.nav.pensjon.vtp.testmodell.enheter.EnheterIndeks
import no.nav.pensjon.vtp.testmodell.enheter.Norg2Modell
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate.of
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE

@RestController
@Tag(name = "Norg2 enheter")
@RequestMapping("/rest/norg2/api/v1")
class EnhetRestMock(private val enheterIndeks: EnheterIndeks) {
    @PostMapping(value = ["/arbeidsfordelinger"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        summary = "Returnerer alle arbeidsfordelinger basert på multiple set av søkekriterier",
        tags = ["arbeidsfordeling"]
    )
    fun getArbeidsFordelinger(
        @RequestBody(required = false) fordelinger: List<Fordeling?>?,
        @RequestParam("skjermet") skjermet: Boolean
    ) = norg2RsArbeidsfordelinger(
        enheterIndeks.alleEnheter
    )

    @GetMapping(value = ["/enhet"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        summary = "Returnerer en filtrert liste av alle enheter",
        tags = ["enhet"]
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
        val norg2RsEnheter = norg2RsEnheter(enheterIndeks.alleEnheter)
            .filter { e: Norg2RsEnhet -> enhetsnummerListe.isEmpty() || enhetsnummerListe.contains(e.enhetNr) }
            .filter { e: Norg2RsEnhet -> enhetStatusListe.isEmpty() || enhetStatusListe.contains(e.status) }
        return ResponseEntity.ok(norg2RsEnheter)
    }

    @GetMapping(value = ["/enhet/navkontor/{geografiskOmraade}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        summary = "Returnerer en NAV kontor som er en lokalkontor for spesifisert geografisk område",
        tags = ["enhet"]
    )
    fun getEnhetByGeografiskOmraadeUsingGET(
        @PathVariable("geografiskOmraade") geografiskOmraade: String,
        @RequestParam("disk") disk: String?
    ) = enEnhet(4407L)

    @GetMapping(value = ["/enhet/{enhetsnummer}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        summary = "Returnerer en enhet basert på enhetsnummer",
        tags = ["enhet"]
    )
    fun getEnhetUsingGET(@PathVariable("enhetsnummer") enhetsnummer: String) =
        norg2RsEnheter(enheterIndeks.alleEnheter)
            .firstOrNull { it.enhetNr.equals(enhetsnummer, ignoreCase = true) }
            ?: enEnhet(enhetsnummer.toLong())

    @GetMapping(value = ["/enhet/{enhetsnummer}/organisering"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        summary = "Returnerer en organiseringsliste for enhetsnummer",
        tags = ["enhet"]
    )
    fun getOrganiseringListeForEnhet(@PathVariable("enhetsnummer") enhetsnummer: String): List<Norg2RsOrganisering> {
        val norg2RsEnhet = getEnhetUsingGET(enhetsnummer)

        val validFrom = norg2RsEnhet.aktiveringsdato
            ?: of(1950, 1, 1).format(ISO_LOCAL_DATE)

        return listOf(
            Norg2RsOrganisering(
                organiserer = Norg2RsSimpleEnhet(
                    id = norg2RsEnhet.enhetId,
                    navn = norg2RsEnhet.navn,
                    nr = norg2RsEnhet.enhetNr,
                    gyldigFra = validFrom
                ),
                organisertUnder = Norg2RsSimpleEnhet(
                    id = 99768L,
                    navn = "Enhet over " + norg2RsEnhet.navn,
                    nr = "99768",
                    gyldigFra = validFrom
                ),
                fra = validFrom,
                til = of(2036, 3, 18).format(ISO_LOCAL_DATE),
                id = 123456123456L,
                orgType = "FORV"
            )
        )
    }

    private fun norg2RsEnheter(enheter: Collection<Norg2Modell>): List<Norg2RsEnhet> {
        return enheter
            .map { convert(it) }
    }

    private fun convert(modell: Norg2Modell): Norg2RsEnhet {
        return Norg2RsEnhet(
            enhetId = modell.enhetId,
            enhetNr = modell.enhetId.toString(),
            navn = modell.navn,
            status = modell.status,
            type = modell.type,
            oppgavebehandler = true
        )
    }

    private fun enEnhet(id: Long): Norg2RsEnhet {
        return Norg2RsEnhet(
            enhetId = id,
            enhetNr = id.toString(),
            navn = "NAV Arbeid og ytelser Tønsberg",
            oppgavebehandler = true
        )
    }

    private fun norg2RsArbeidsfordelinger(enheter: Collection<Norg2Modell>): List<Norg2RsArbeidsfordeling> {
        return enheter
            .map { convertToArbeidsfordeling(it) }
    }

    private fun convertToArbeidsfordeling(modell: Norg2Modell): Norg2RsArbeidsfordeling {
        return Norg2RsArbeidsfordeling(
            enhetId = modell.enhetId,
            enhetNr = modell.enhetId.toString(),
            enhetNavn = modell.navn,
            diskresjonskode = modell.diskresjonskode
        )
    }
}
