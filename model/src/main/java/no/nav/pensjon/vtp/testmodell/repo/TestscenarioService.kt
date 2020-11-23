package no.nav.pensjon.vtp.testmodell.repo

interface TestscenarioService {
    fun findAll(): List<Testscenario>
    fun getTestscenario(id: String): Testscenario?
    fun opprettTestscenario(template: TestscenarioTemplate): Testscenario
    fun opprettTestscenario(template: TestscenarioTemplate, userSuppliedVariables: Map<String, String> = mapOf()): Testscenario
    fun opprettTestscenarioFraJsonString(testscenarioJson: String, userSuppliedVariables: Map<String, String> = mapOf()): Testscenario
    fun slettScenario(id: String)
}
