package no.nav.pensjon.vtp.testmodell;

import static java.util.stream.Collectors.toList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;

import static no.nav.pensjon.vtp.testmodell.repo.impl.BasisdataProviderFileImpl.loadAdresser;
import static no.nav.pensjon.vtp.testmodell.repo.impl.BasisdataProviderFileImpl.loadVirksomheter;

import java.util.List;
import java.util.Objects;

import org.junit.Test;

import no.nav.pensjon.vtp.testmodell.identer.IdenterIndeks;
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonRepository;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonRepositoryInMemory;
import no.nav.pensjon.vtp.testmodell.personopplysning.BrukerModelRepositoryInMemory;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonIndeks;
import no.nav.pensjon.vtp.testmodell.repo.Testscenario;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplate;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioFraTemplateMapper;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioServiceImpl;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioTemplateLoader;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioTemplateRepositoryImpl;

public class MuterScenarioTest {

    @Test
    public void slettScenarioTest() throws Exception{
        TestscenarioTemplateLoader loader = new TestscenarioTemplateLoader();
        TestscenarioTemplateRepositoryImpl templateRepository = new TestscenarioTemplateRepositoryImpl(loader.load());

        PersonIndeks personIndeks = new PersonIndeks(new BrukerModelRepositoryInMemory());
        InntektYtelseIndeks inntektYtelseIndeks = new InntektYtelseIndeks();
        OrganisasjonRepository organisasjonRepository = new OrganisasjonRepositoryInMemory();

        TestscenarioFraTemplateMapper testscenarioFraTemplateMapper = new TestscenarioFraTemplateMapper(loadAdresser(), new IdenterIndeks(), loadVirksomheter());
        TestscenarioRepository testscenarioRepository = new TestscenarioRepositoryImpl();
        TestscenarioServiceImpl testScenarioRepository = new TestscenarioServiceImpl(testscenarioFraTemplateMapper, testscenarioRepository, personIndeks, inntektYtelseIndeks,
                organisasjonRepository);

        List<TestscenarioTemplate> templates = templateRepository.getTemplates().collect(toList());
        assertFalse(templates.isEmpty());
        Testscenario testScenario = testScenarioRepository.opprettTestscenario(templates.get(0));

        assertThat(testscenarioRepository.findAll())
                .isNotEmpty();

        testscenarioRepository.delete(testScenario.getId());

        assertThat(testscenarioRepository.findAll())
                .noneMatch(ts -> Objects.equals(ts.getId(), testScenario.getId()));
    }
}
