package no.nav.pensjon.vtp.testmodell;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import no.nav.pensjon.vtp.testmodell.identer.IdenterIndeks;
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonRepository;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonRepositoryInMemory;
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.BrukerModelRepositoryInMemory;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonIdentFooRepositoryInMemory;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.pensjon.vtp.testmodell.personopplysning.SøkerModell;
import no.nav.pensjon.vtp.testmodell.repo.Testscenario;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.pensjon.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioFraTemplateMapper;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioServiceImpl;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioTemplateLoader;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioTemplateRepositoryImpl;
import no.nav.pensjon.vtp.testmodell.virksomhet.VirksomhetIndeks;

public class ScenariosTest {

    @Test
    public void skal_laste_scenarios() throws Exception {
        final TestscenarioTemplateLoader loader = new TestscenarioTemplateLoader();
        TestscenarioTemplateRepositoryImpl templateRepository = new TestscenarioTemplateRepositoryImpl(loader.load());

        AdresseIndeks adresseIndeks = BasisdataProviderFileImpl.loadAdresser();
        VirksomhetIndeks virksomhetIndeks = BasisdataProviderFileImpl.loadVirksomheter();
        PersonIndeks personIndeks = new PersonIndeks(new PersonIdentFooRepositoryInMemory());
        InntektYtelseIndeks inntektYtelseIndeks = new InntektYtelseIndeks();
        OrganisasjonRepository organisasjonRepository = new OrganisasjonRepositoryInMemory();

        TestscenarioFraTemplateMapper testscenarioFraTemplateMapper = new TestscenarioFraTemplateMapper(adresseIndeks, new IdenterIndeks(), virksomhetIndeks);
        TestscenarioRepository testscenarioRepository = new TestscenarioRepositoryImpl();
        TestscenarioServiceImpl testScenarioRepository = new TestscenarioServiceImpl(testscenarioFraTemplateMapper, testscenarioRepository, personIndeks, inntektYtelseIndeks,
                organisasjonRepository, new BrukerModelRepositoryInMemory());

        templateRepository.getTemplates().forEach(sc -> {
            Testscenario testScenario = testScenarioRepository.opprettTestscenario(sc);
            sjekkIdenterErInjisert(testScenario);
            Personopplysninger pers = testScenario.getPersonopplysninger();
            assertThat(pers).isNotNull();
            SøkerModell søker = pers.getSøker();
            assertThat(pers.getFamilierelasjoner()).isNotEmpty();
            assertThat(søker.getGeografiskTilknytning()).isNotNull();
        });
    }

    private void sjekkIdenterErInjisert(Testscenario sc) {
        sc.getIdenter().getAlleIdenter().entrySet().forEach(System.out::println);
        System.out.println("--------------");
    }

}
