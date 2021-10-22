package no.nav.pensjon.vtp.testmodell.repo

import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModell
import no.nav.pensjon.vtp.testmodell.scenario.pensjon.PensjonTestScenario

interface TestscenarioService {
    fun findAll(): List<Testscenario>
    fun getTestscenario(id: String): Testscenario?
    fun opprettTestscenario(template: TestscenarioTemplate): Testscenario
    fun opprettTestscenario(template: TestscenarioTemplate, userSuppliedVariables: Map<String, String> = mapOf()): Testscenario
    fun opprettTestscenarioFraJsonString(testscenarioJson: String, userSuppliedVariables: Map<String, String> = mapOf()): Testscenario
    fun slettScenario(id: String)
    fun opprettPensjonTestScenario(templateId: String, input: PensjonTestScenario): PensjonTestScenario?
    fun getPensjonTestScenario(person: PersonModell): PensjonTestScenario
}
