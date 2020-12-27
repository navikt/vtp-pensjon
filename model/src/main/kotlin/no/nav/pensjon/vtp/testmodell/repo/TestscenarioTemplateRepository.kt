package no.nav.pensjon.vtp.testmodell.repo

interface TestscenarioTemplateRepository {
    fun templates(): Collection<TestscenarioTemplate>
    fun finn(templateKey: String): TestscenarioTemplate?
}
