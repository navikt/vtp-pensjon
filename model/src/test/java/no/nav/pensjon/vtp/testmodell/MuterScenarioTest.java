package no.nav.pensjon.vtp.testmodell;

import static no.nav.pensjon.vtp.testmodell.repo.impl.BasisdataProviderFileImpl.loadAdresser;
import static no.nav.pensjon.vtp.testmodell.repo.impl.BasisdataProviderFileImpl.loadVirksomheter;

import java.util.Collection;
import java.util.Objects;

import org.junit.Assert;
import org.junit.Test;

import no.nav.pensjon.vtp.testmodell.identer.IdenterIndeks;
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonIndeks;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioImpl;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplate;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioFraTemplateMapper;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioTemplateRepositoryImpl;

public class MuterScenarioTest {

    @Test
    public void slettScenarioTest() throws Exception{
        TestscenarioTemplateRepositoryImpl templateRepository = new TestscenarioTemplateRepositoryImpl();
        templateRepository.load();
        Collection<TestscenarioTemplate> scenarioTemplates = templateRepository.getTemplates();
        TestscenarioFraTemplateMapper testscenarioFraTemplateMapper = new TestscenarioFraTemplateMapper(loadAdresser(), new IdenterIndeks(), loadVirksomheter());
        TestscenarioRepositoryImpl testScenarioRepository = new TestscenarioRepositoryImpl(new PersonIndeks(), new InntektYtelseIndeks(), new OrganisasjonIndeks(), testscenarioFraTemplateMapper);

        TestscenarioImpl testScenario = testScenarioRepository.opprettTestscenario(scenarioTemplates.stream().findFirst().get());

        Assert.assertTrue(testScenarioRepository.getTestscenarios().size() > 0);
        testScenarioRepository.slettScenario(testScenario.getId());

        Assert.assertEquals(0, testScenarioRepository.getTestscenarios().values().stream().filter(ts -> (Objects.equals(ts.getId(), testScenario.getId()))).count());
    }
}
