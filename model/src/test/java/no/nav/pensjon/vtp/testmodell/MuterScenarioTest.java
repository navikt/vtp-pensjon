package no.nav.pensjon.vtp.testmodell;

import static java.util.stream.Collectors.toList;

import static org.junit.Assert.assertFalse;

import static no.nav.pensjon.vtp.testmodell.repo.impl.BasisdataProviderFileImpl.loadAdresser;
import static no.nav.pensjon.vtp.testmodell.repo.impl.BasisdataProviderFileImpl.loadVirksomheter;

import java.util.List;
import java.util.Objects;

import org.junit.Assert;
import org.junit.Test;

import no.nav.pensjon.vtp.testmodell.identer.IdenterIndeks;
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonIndeks;
import no.nav.pensjon.vtp.testmodell.repo.Testscenario;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplate;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioBuilderRepositoryImpl;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioFraTemplateMapper;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioTemplateLoader;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioTemplateRepositoryImpl;

public class MuterScenarioTest {

    @Test
    public void slettScenarioTest() throws Exception{
        TestscenarioTemplateLoader loader = new TestscenarioTemplateLoader();
        TestscenarioTemplateRepositoryImpl templateRepository = new TestscenarioTemplateRepositoryImpl(loader.load());

        PersonIndeks personIndeks = new PersonIndeks();
        InntektYtelseIndeks inntektYtelseIndeks = new InntektYtelseIndeks();
        OrganisasjonIndeks organisasjonIndeks = new OrganisasjonIndeks();

        TestscenarioFraTemplateMapper testscenarioFraTemplateMapper = new TestscenarioFraTemplateMapper(loadAdresser(), new IdenterIndeks(), loadVirksomheter());
        TestscenarioBuilderRepository testscenarioBuilderRepository = new TestscenarioBuilderRepositoryImpl(personIndeks, inntektYtelseIndeks, organisasjonIndeks);
        TestscenarioRepositoryImpl testScenarioRepository = new TestscenarioRepositoryImpl(testscenarioFraTemplateMapper, testscenarioBuilderRepository);

        List<TestscenarioTemplate> templates = templateRepository.getTemplates().collect(toList());
        assertFalse(templates.isEmpty());
        Testscenario testScenario = testScenarioRepository.opprettTestscenario(templates.get(0));

        Assert.assertTrue(testscenarioBuilderRepository.getTestscenarios().size() > 0);
        testscenarioBuilderRepository.slettScenario(testScenario.getId());

        Assert.assertEquals(0, testscenarioBuilderRepository.getTestscenarios().values().stream().filter(ts -> (Objects.equals(ts.getId(), testScenario.getId()))).count());
    }
}
