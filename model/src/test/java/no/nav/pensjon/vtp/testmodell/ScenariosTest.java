package no.nav.pensjon.vtp.testmodell;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import no.nav.pensjon.vtp.testmodell.identer.IdenterIndeks;
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.pensjon.vtp.testmodell.personopplysning.SøkerModell;
import no.nav.pensjon.vtp.testmodell.repo.Testscenario;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.pensjon.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioBuilderRepositoryImpl;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioFraTemplateMapper;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
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
        PersonIndeks personIndeks = new PersonIndeks();
        InntektYtelseIndeks inntektYtelseIndeks = new InntektYtelseIndeks();
        OrganisasjonIndeks organisasjonIndeks = new OrganisasjonIndeks();

        TestscenarioFraTemplateMapper testscenarioFraTemplateMapper = new TestscenarioFraTemplateMapper(adresseIndeks, new IdenterIndeks(), virksomhetIndeks);
        TestscenarioBuilderRepository testscenarioBuilderRepository = new TestscenarioBuilderRepositoryImpl(personIndeks, inntektYtelseIndeks, organisasjonIndeks);
        TestscenarioRepositoryImpl testScenarioRepository = new TestscenarioRepositoryImpl(testscenarioFraTemplateMapper, testscenarioBuilderRepository);

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