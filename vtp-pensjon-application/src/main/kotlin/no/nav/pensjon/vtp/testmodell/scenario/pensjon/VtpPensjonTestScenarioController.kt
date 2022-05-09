package no.nav.pensjon.vtp.testmodell.scenario.pensjon

import no.nav.pensjon.vtp.common.testscenario.VtpPensjonTestScenario
import no.nav.pensjon.vtp.util.asResponseEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Controller for creating and fetching test scenarios used by vtp-pensjon-client
 */
@RestController
@RequestMapping("/api/vtp-pensjon/testscenario")
class VtpPensjonTestScenarioController(
    private val vtpPensjonTestScenarioService: VtpPensjonTestScenarioService,
) {
    @GetMapping("/{scenarioId}")
    fun get(@PathVariable("scenarioId") scenarioId: String) =
        vtpPensjonTestScenarioService.getSamTestScenario(scenarioId)

    @PostMapping()
    fun create(@RequestBody vtpPensjonTestScenario: VtpPensjonTestScenario): ResponseEntity<VtpPensjonTestScenario> =
        vtpPensjonTestScenarioService.opprettVtpPensjonTestScenario(vtpPensjonTestScenario).asResponseEntity()
}
