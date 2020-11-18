package no.nav.pensjon.vtp.testmodell;

import static java.util.stream.Collectors.toList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;

import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.pensjon.vtp.testmodell.repo.Testscenario;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioService;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplate;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplateRepository;

@SpringBootTest()
@TestPropertySource("classpath:disable-ldap.properties")
public class ScenariosTest {
    private final TestscenarioTemplateRepository testscenarioTemplateRepository;
    private final TestscenarioService testscenarioService;
    private final TestscenarioRepository testscenarioRepository;

    @Autowired
    public ScenariosTest(TestscenarioTemplateRepository testscenarioTemplateRepository, TestscenarioService testscenarioService,
            TestscenarioRepository testscenarioRepository) {
        this.testscenarioTemplateRepository = testscenarioTemplateRepository;
        this.testscenarioService = testscenarioService;
        this.testscenarioRepository = testscenarioRepository;
    }

    @Test
    public void validates_that_all_scenarios_can_be_loaded_from_disk_created_and_persisted() {
        testscenarioTemplateRepository.getTemplates()
                .map(testscenarioService::opprettTestscenario)
                .map(Testscenario::getPersonopplysninger)
                .forEach(personopplysninger -> {
                    assertThat(personopplysninger)
                            .extracting(Personopplysninger::getSøker)
                            .extracting(PersonModell::getGeografiskTilknytning)
                            .isNotNull();

                    assertThat(personopplysninger)
                            .extracting(Personopplysninger::getFamilierelasjoner).asList()
                            .isNotEmpty();
                });
    }

    @Test
    public void scenarios_are_removed_from_TestscenarioRepository_when_deleted() {
        List<TestscenarioTemplate> templates = testscenarioTemplateRepository.getTemplates().collect(toList());
        assertFalse(templates.isEmpty());
        Testscenario testScenario = testscenarioService.opprettTestscenario(templates.get(0));

        assertThat(testscenarioRepository.findAll())
                .isNotEmpty();

        testscenarioRepository.deleteById(testScenario.getId());

        assertThat(testscenarioRepository.findAll())
                .noneMatch(ts -> Objects.equals(ts.getId(), testScenario.getId()));
    }

}
