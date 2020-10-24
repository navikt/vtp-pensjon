package no.nav.pensjon.vtp.testmodell;

import no.nav.pensjon.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.pensjon.vtp.testmodell.personopplysning.SøkerModell;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioImpl;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplate;
import no.nav.pensjon.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioTemplateRepositoryImpl;

import org.junit.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class ScenariosTest {

    @Test
    public void skal_laste_scenarios() throws Exception {
        TestscenarioTemplateRepositoryImpl templateRepository = new TestscenarioTemplateRepositoryImpl();
        templateRepository.load();

        Collection<TestscenarioTemplate> scenarioTemplates = templateRepository.getTemplates();

        TestscenarioRepositoryImpl testScenarioRepository = new TestscenarioRepositoryImpl(new BasisdataProviderFileImpl());
        for (TestscenarioTemplate sc : scenarioTemplates) {
            TestscenarioImpl testScenario = testScenarioRepository.opprettTestscenario(sc);
            sjekkIdenterErInjisert(testScenario);
            Personopplysninger pers = testScenario.getPersonopplysninger();
            assertThat(pers).isNotNull();
            SøkerModell søker = pers.getSøker();
            assertThat(pers.getFamilierelasjoner()).isNotEmpty();
            assertThat(søker.getGeografiskTilknytning()).isNotNull();
        }
    }

    private void sjekkIdenterErInjisert(TestscenarioImpl sc) {
        sc.getIdenter().getAlleIdenter().entrySet().forEach(System.out::println);
        System.out.println("--------------");
    }

}
