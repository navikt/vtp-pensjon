package no.nav.pensjon.vtp.mocks.axsys

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.axsys.model.AxsysEnhetDTO
import no.nav.pensjon.vtp.testmodell.ansatt.AnsatteIndeks
import no.nav.pensjon.vtp.testmodell.enheter.EnheterIndeks
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "AxsysMock")
@RequestMapping("/rest/axsys/api/v1")
class AxsysMock(
    private val ansatteIndeks: AnsatteIndeks,
    private val enheterIndeks: EnheterIndeks
) {

    data class TilgangerResponse(
        val enheter: List<AxsysEnhetDTO>
    )
    @GetMapping(
        value = ["/tilgang/{ident}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "hentTilgangerForIdent", description = "Hent alle tilganger for en ident")
    fun hentTilgangerForIdent(@PathVariable("ident") ident: String): TilgangerResponse {
        val enheter = ansatteIndeks.findByCn(ident)?.enheter?.let {
            enheterIndeks.findByEnhetIdIn(it)
        }?.toList() ?: emptyList()

        return TilgangerResponse(
            enheter = enheter.map {
                AxsysEnhetDTO(
                    enhetId = it.enhetId.toString(),
                    fagomrader = setOf("PEN", "UFO", "GOS"),
                    navn = it.navn
                )
            }
        )
    }

    data class EnhetBruker(
        val appIdent: String,
        val historiskIdent: Int
    )
    @GetMapping(
        value = ["/enhet/{id}/brukere"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "hentAlleBrukereForEnhet", description = "Hent alle brukere for en NAV-enhet")
    fun hentAlleBrukereForEnhet(@PathVariable("id") enhetsId: String): List<EnhetBruker> {
        return ansatteIndeks.findByEnhetsId(enhetsId = enhetsId.toLong()).map {
            EnhetBruker(
                appIdent = it.cn,
                historiskIdent = it.cn.toByteArray().sum() // arbitrary but deterministic id
            )
        }
    }
}
