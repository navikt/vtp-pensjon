package no.nav.pensjon.vtp.application

import no.nav.pensjon.vtp.testmodell.personopplysning.Personopplysninger
import no.nav.pensjon.vtp.testmodell.repo.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest
@TestPropertySource("classpath:disable-ldap.properties")
class ScenariosTest @Autowired constructor(
    private val testscenarioTemplateRepository: TestscenarioTemplateRepository,
    private val testscenarioService: TestscenarioService,
    private val testscenarioRepository: TestscenarioRepository
) {
    @Test
    fun validates_that_all_scenarios_can_be_loaded_from_disk_created_and_persisted() {
        testscenarioTemplateRepository.templates()
            .map(testscenarioService::opprettTestscenario)
            .map(Testscenario::personopplysninger)
            .forEach { personopplysninger: Personopplysninger? ->
                assertNotNull(personopplysninger?.søker?.geografiskTilknytning)
                val actual = personopplysninger?.familierelasjoner?.isEmpty()
                assertNotNull(actual)
                assertTrue(personopplysninger.annenPart == null || !actual)
            }
    }

    @Test
    fun scenarios_are_removed_from_TestscenarioRepository_when_deleted() {
        val templates = testscenarioTemplateRepository.templates()
        assertFalse(templates.isEmpty())

        val (id) = testscenarioService.opprettTestscenario(templates.iterator().next())

        assertFalse(testscenarioRepository.findAll().isEmpty())
        testscenarioRepository.deleteById(id)

        assertTrue(testscenarioRepository.findAll().none { it.id == id })
    }
}
