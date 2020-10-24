package no.nav.pensjon.vtp.testmodell;

import no.nav.pensjon.vtp.testmodell.identer.IdenterIndeks;
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.pensjon.vtp.testmodell.personopplysning.SøkerModell;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioImpl;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplate;
import no.nav.pensjon.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioFraTemplateMapper;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioTemplateRepositoryImpl;
import no.nav.pensjon.vtp.testmodell.virksomhet.VirksomhetIndeks;

import org.junit.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class ScenariosTest {

    @Test
    public void skal_laste_scenarios() throws Exception {
        TestscenarioTemplateRepositoryImpl templateRepository = new TestscenarioTemplateRepositoryImpl();
        templateRepository.load();

        Collection<TestscenarioTemplate> scenarioTemplates = templateRepository.getTemplates();

        AdresseIndeks adresseIndeks = BasisdataProviderFileImpl.loadAdresser();
        VirksomhetIndeks virksomhetIndeks = BasisdataProviderFileImpl.loadVirksomheter();
        TestscenarioFraTemplateMapper testscenarioFraTemplateMapper = new TestscenarioFraTemplateMapper(adresseIndeks, new IdenterIndeks(), virksomhetIndeks);
        TestscenarioRepositoryImpl testScenarioRepository = new TestscenarioRepositoryImpl(new PersonIndeks(), new InntektYtelseIndeks(), new OrganisasjonIndeks(), testscenarioFraTemplateMapper);
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
