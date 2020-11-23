package no.nav.pensjon.vtp.testmodell.repo.impl

import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplate
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplateRepository

/**
 * TestscenarioRepository av alle test templates.
 */
class TestscenarioTemplateRepositoryImpl(private val testTemplates: Map<String, TestscenarioTemplate>) : TestscenarioTemplateRepository {
    override fun templates() = testTemplates.values

    override fun finn(templateKey: String): TestscenarioTemplate? = testTemplates[templateKey]
}
