package no.nav.pensjon.vtp.mocks.virksomhet.arbeidsfordeling.rest

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import no.nav.pensjon.vtp.testmodell.enheter.EnheterIndeks
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(tags = ["ArbeidsfordelingMock"])
@RequestMapping("/rest/norg2/api/v1/arbeidsfordeling")
class ArbeidsfordelingRestMock(private val enheterIndeks: EnheterIndeks) {
    @PostMapping(
        value = ["/enheter"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ApiOperation(value = "allenheter", notes = "Returnerer enheter fra NORG2")
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
                    enhetNr = it.enhetId,
                    enhetNavn = it.navn,
                    enhetType = it.status,
                    status = it.type
                )
            }
            .toTypedArray()

    @PostMapping(
        value = ["/enheter/bestmatch"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ApiOperation(value = "finnenhet", notes = "Returnerer  enheter fra NORG2")
    fun finnEnhet(@RequestBody request: ArbeidsfordelingRequest): Array<ArbeidsfordelingResponse> {
        val enhet =
            if (request.diskresjonskode != null && listOf("UFB", "SPSF", "SPFO").contains(request.diskresjonskode)) {
                enheterIndeks.finnByDiskresjonskode(request.diskresjonskode)
            } else {
                enheterIndeks.finnByDiskresjonskode("NORMAL-" + request.tema)
            }

        return enhet
            ?.let {
                arrayOf(
                    ArbeidsfordelingResponse(
                        enhetNr = enhet.enhetId,
                        enhetNavn = enhet.navn,
                        enhetType = enhet.status,
                        status = enhet.type
                    )
                )
            }
            ?: emptyArray()
    }
}
