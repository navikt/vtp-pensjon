package no.nav.pensjon.vtp.testmodell.scenario.sam

import no.nav.pensjon.vtp.util.asResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Controller for creating and fetching test scenarios used by SAM (Tjenestepensjon - Samordning)
 */
@RestController
@RequestMapping("/api/sam/testscenario")
class SamTestScenarioController(
    private val samTestScenarioService: SamTestScenarioService,
) {
    @GetMapping("/{scenarioId}")
    fun get(@PathVariable("scenarioId") scenarioId: String) =
        samTestScenarioService.getSamTestScenario(scenarioId)

    @PostMapping()
    fun create(@RequestBody samTestScenario: SamTestScenario) =
        samTestScenarioService.opprettSamTestScenario(samTestScenario).asResponseEntity()
}
