package no.nav.pensjon.vtp.testmodell;

import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonIndeks;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioImpl;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplate;
import no.nav.pensjon.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioTemplateRepositoryImpl;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.Objects;

public class MuterScenarioTest {

    @Test
    public void slettScenarioTest() throws Exception{
        TestscenarioTemplateRepositoryImpl templateRepository = new TestscenarioTemplateRepositoryImpl();
        templateRepository.load();
        Collection<TestscenarioTemplate> scenarioTemplates = templateRepository.getTemplates();
        TestscenarioRepositoryImpl testScenarioRepository = new TestscenarioRepositoryImpl(new BasisdataProviderFileImpl(), new PersonIndeks(), new InntektYtelseIndeks(), new OrganisasjonIndeks());

        TestscenarioImpl testScenario = testScenarioRepository.opprettTestscenario(scenarioTemplates.stream().findFirst().get());

        Assert.assertTrue(testScenarioRepository.getTestscenarios().size() > 0);
        testScenarioRepository.slettScenario(testScenario.getId());

        Assert.assertEquals(0, testScenarioRepository.getTestscenarios().values().stream().filter(ts -> (Objects.equals(ts.getId(), testScenario.getId()))).count());
    }
}
