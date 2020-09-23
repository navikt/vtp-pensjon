package no.nav.foreldrepenger.vtp.testmodell;

import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.SøkerModell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioTemplate;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioTemplateRepositoryImpl;
import org.junit.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class ScenariosTest {

    private static final String TEST_SCENARIO_NAVN = "100-mor-og-far-bosatt-i-norge";

    @Test
    public void skal_laste_scenarios() throws Exception {
        TestscenarioTemplateRepositoryImpl templateRepository = TestscenarioTemplateRepositoryImpl.getInstance();
        templateRepository.load();

        Collection<TestscenarioTemplate> scenarioTemplates = templateRepository.getTemplates();

        TestscenarioRepositoryImpl testScenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());
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
        sc.getIdenter().getAlleIdenter().entrySet().forEach(e -> {
            System.out.println(e);
        });
        System.out.println("--------------");
    }

}
