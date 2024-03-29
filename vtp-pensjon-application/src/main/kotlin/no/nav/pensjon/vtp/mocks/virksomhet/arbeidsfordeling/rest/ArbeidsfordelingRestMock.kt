package no.nav.pensjon.vtp.mocks.virksomhet.arbeidsfordeling.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.pensjon.vtp.testmodell.enheter.EnheterIndeks
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Norg2 enheter")
@RequestMapping("/rest/norg2/api/v1/arbeidsfordeling")
class ArbeidsfordelingRestMock(private val enheterIndeks: EnheterIndeks) {
    @PostMapping(
        value = ["/enheter"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "allenheter", description = "Returnerer enheter fra NORG2")
    fun hentAlleEnheter(@RequestBody request: ArbeidsfordelingRequest) =
        enheterIndeks.alleEnheter
            .filter {
                when {
                    request.tema == null -> true
                    "FOR".equals(request.tema, ignoreCase = true) && "YTA".equals(it.type, ignoreCase = true) -> false
                    "OMS".equals(request.tema, ignoreCase = true) && "FPY".equals(it.type, ignoreCase = true) -> false
                    else -> true
                }
            }
            .map {
                ArbeidsfordelingResponse(
                    enhetNr = it.enhetId.toString(),
                    enhetNavn = it.navn,
                    enhetType = it.status,
                    status = it.type,
                    oppgavebehandler = true
                )
            }
            .toTypedArray()

    @PostMapping(
        value = ["/enheter/bestmatch"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "finnenhet", description = "Returnerer  enheter fra NORG2")
    fun finnEnhet(@RequestBody request: ArbeidsfordelingRequest): Array<ArbeidsfordelingResponse> {
        val enhet =
            if (request.diskresjonskode != null && listOf("UFB", "SPSF", "SPFO").contains(request.diskresjonskode)) {
                enheterIndeks.finnByDiskresjonskode(request.diskresjonskode)
            } else {
                enheterIndeks.finnByDiskresjonskode(request.tema!!)
            }

        return enhet
            ?.let {
                arrayOf(
                    ArbeidsfordelingResponse(
                        enhetNr = enhet.enhetId.toString(),
                        enhetNavn = enhet.navn,
                        enhetType = enhet.status,
                        status = enhet.type,
                        oppgavebehandler = true
                    )
                )
            }
            ?: emptyArray()
    }
}
