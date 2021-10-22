package no.nav.pensjon.vtp.testmodell.scenario

import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioServiceImpl
import no.nav.pensjon.vtp.testmodell.scenario.pensjon.PensjonTestScenario
import no.nav.pensjon.vtp.util.asResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/pensjon/testscenario")
class PensjonTestScenarioController(
    private val testscenarioService: TestscenarioServiceImpl,
) {
    @PostMapping("/{template}")
    fun create(@PathVariable("template") template: String, @RequestBody pensjonTestScenario: PensjonTestScenario) =
        testscenarioService.opprettPensjonTestScenario(template, pensjonTestScenario).asResponseEntity()
}
