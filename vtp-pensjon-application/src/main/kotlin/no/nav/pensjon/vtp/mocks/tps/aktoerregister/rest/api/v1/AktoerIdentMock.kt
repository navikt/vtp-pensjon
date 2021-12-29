package no.nav.pensjon.vtp.mocks.tps.aktoerregister.rest.api.v1

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

private const val NAV_IDENTER_HEADER_KEY = "Nav-Personidenter"
private const val NAV_IDENTER_MAX_SIZE = 1000
private const val IDENTGRUPPE = "identgruppe"
private const val AKTOERID_IDENTGRUPPE = "AktoerId"
private const val PERSONIDENT_IDENTGRUPPE = "NorskIdent"
private const val GJELDENDE = "gjeldende"

@RestController
@Tag(name = "aktoerregister")
@RequestMapping("/rest/aktoerregister/api/v1/identer")
class AktoerIdentMock {
    // TODO (TEAM FAMILIE) Lag mock-responser fra scenario NOSONAR
    private val personIdentMock = "12345678910"
    private val aktoerIdMock = "1234567891011"

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun alleIdenterForIdenter(
        @RequestHeader(
            NAV_IDENTER_HEADER_KEY
        ) requestIdenter: Set<String>,
        @RequestParam(IDENTGRUPPE) identgruppe: String,
        @RequestParam(GJELDENDE) gjeldende: Boolean,
        response: HttpServletResponse
    ): Map<String, IdentinfoForAktoer> {
        response.setHeader("Cache-Control", "no-cache")

        require(requestIdenter.isNotEmpty()) { "Ville kastet \"MissingIdenterException\"" }
        require(requestIdenter.size <= NAV_IDENTER_MAX_SIZE) { "Ville kastet \"RequestIdenterMaxSizeException\"" }

        return mapOf(
            requestIdenter.first() to IdentinfoForAktoer(
                listOf(
                    if (AKTOERID_IDENTGRUPPE == identgruppe) {
                        Identinfo(
                            personIdentMock,
                            PERSONIDENT_IDENTGRUPPE,
                            true
                        )
                    } else {
                        Identinfo(
                            aktoerIdMock,
                            AKTOERID_IDENTGRUPPE,
                            true
                        )
                    }
                ),
                null
            )
        )
    }
}
