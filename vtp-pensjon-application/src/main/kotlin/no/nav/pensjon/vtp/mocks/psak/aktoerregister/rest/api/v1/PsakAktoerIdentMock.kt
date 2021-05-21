package no.nav.pensjon.vtp.mocks.psak.aktoerregister.rest.api.v1

import io.swagger.annotations.Api
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
@Api(tags = ["aktoerregister"])
@RequestMapping("/rest/psak/aktoerregister/api/v1/identer")
class PsakAktoerIdentMock {
    private val personIdentMock = "01015746161"
    private val aktoerIdMock = "1234567891011"

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getIdenter(
        @RequestHeader(NAV_IDENTER_HEADER_KEY) requestIdenter: Set<String>,
        @RequestParam(IDENTGRUPPE) identgruppe: @NotNull String?,
        @RequestParam(GJELDENDE) gjeldende: @NotNull Boolean
    ): Map<String, IdentinfoForAktoer> {
        validateRequest(requestIdenter)

        val identinfo: Identinfo = if (AKTOERID_IDENTGRUPPE == identgruppe) {
            Identinfo(
                ident = aktoerIdMock,
                identgruppe = AKTOERID_IDENTGRUPPE,
                gjeldende = true
            )
        } else {
            Identinfo(
                ident = personIdentMock,
                identgruppe = PERSONIDENT_IDENTGRUPPE,
                gjeldende = true
            )
        }

        return mapOf(
            requestIdenter.first() to IdentinfoForAktoer(identer = listOf(identinfo))
        )
    }

    private fun validateRequest(identer: Set<String>) {
        require(identer.isNotEmpty()) { "Ville kastet \"MissingIdenterException\"" }
        require(identer.size <= NAV_IDENTER_MAX_SIZE) { "Ville kastet \"RequestIdenterMaxSizeException\"" }
    }
}
