package no.nav.pensjon.vtp.mocks.psak.aktoerregister.rest.api.v1

import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModell
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.NotNull

private const val NAV_IDENTER_HEADER_KEY = "Nav-Personidenter"
private const val NAV_IDENTER_MAX_SIZE = 1000
private const val IDENTGRUPPE = "identgruppe"
private const val AKTOERID_IDENTGRUPPE = "AktoerId"
private const val PERSONIDENT_IDENTGRUPPE = "NorskIdent"
private const val GJELDENDE = "gjeldende"

/**
 * Dette er en kopi av AktoerIdentMock med endepunkt som forventer ingen payload.
 * Vi beholder den opprinnelige AktoerIdentMock for å ikke knekke FP-løsningen
 */
// TODO avklare denne med FP
@RestController
@Tag(name = "aktoerregister")
@RequestMapping("/rest/psak/aktoerregister/api/v1/identer")
class PsakAktoerIdentMock(private val personModellRepository: PersonModellRepository) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getIdenter(
        @RequestHeader(NAV_IDENTER_HEADER_KEY) requestIdenter: Set<String>,
        @RequestParam(IDENTGRUPPE) identgruppe: String?,
        @RequestParam(GJELDENDE) gjeldende: @NotNull Boolean
    ): Map<String, IdentinfoForAktoer> {
        validateRequest(requestIdenter)

        return requestIdenter.map { ident ->
            mapOf(
                ident to IdentinfoForAktoer(
                    identer = hentIdenter(ident)
                        .filter { identinfo -> identinfo.identgruppe == identgruppe || identgruppe == null }
                )
            )
        }.associate { it.entries.first().toPair() }
    }

    private fun hentIdenter(ident: String): List<Identinfo> {
        return personModellRepository.findByIdentOrAktørIdent(ident, ident)
            ?.let { createIdentInfoFromPerson(it) }
            .orEmpty()
    }

    private fun createIdentInfoFromPerson(person: PersonModell): List<Identinfo> {
        return listOf(
            Identinfo(
                ident = person.ident,
                identgruppe = PERSONIDENT_IDENTGRUPPE,
                gjeldende = true
            ),
            Identinfo(
                ident = person.aktørIdent,
                identgruppe = AKTOERID_IDENTGRUPPE,
                gjeldende = true
            )
        )
    }

    private fun validateRequest(identer: Set<String>) {
        require(identer.isNotEmpty()) { "Ville kastet \"MissingIdenterException\"" }
        require(identer.size <= NAV_IDENTER_MAX_SIZE) { "Ville kastet \"RequestIdenterMaxSizeException\"" }
    }
}
