package no.nav.pensjon.vtp.application.configuration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import no.nav.pensjon.vtp.miscellaneous.api.pensjon_testdata.PensjonTestdataService;
import no.nav.pensjon.vtp.miscellaneous.api.pensjon_testdata.PensjonTestdataServiceImpl;
import no.nav.pensjon.vtp.mocks.virksomhet.sak.v1.GsakRepo;
import no.nav.pensjon.vtp.testmodell.ansatt.AnsatteIndeks;
import no.nav.pensjon.vtp.testmodell.enheter.EnheterIndeks;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseIndeks;
import no.nav.pensjon.vtp.testmodell.repo.JournalRepository;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplateRepository;
import no.nav.pensjon.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.pensjon.vtp.testmodell.repo.impl.JournalRepositoryImpl;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioTemplateLoader;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioTemplateRepositoryImpl;
import no.nav.pensjon.vtp.testmodell.virksomhet.VirksomhetIndeks;

@Configuration
public class UserRepositoryConfiguration {
    @Bean
    public AdresseIndeks adresseIndeks() throws IOException {
        return BasisdataProviderFileImpl.loadAdresser();
    }

    @Bean
    public AnsatteIndeks ansatteIndeks() {
        return BasisdataProviderFileImpl.loadAnsatte();
    }

    @Bean
    public OrganisasjonIndeks organisasjonIndeks() throws IOException {
        return BasisdataProviderFileImpl.loadOrganisasjoner();
    }

    @Bean
    public TestscenarioTemplateRepository testscenarioTemplateRepository() {
        return new TestscenarioTemplateRepositoryImpl(new TestscenarioTemplateLoader().load());
    }

    @Bean
    public PensjonTestdataService pensjonTestdataService(@Value("${pensjon.testdata.url}") String pensjonTestdataUrl) {
        return new PensjonTestdataServiceImpl(pensjonTestdataUrl);
    }

    @Bean
    public JournalRepository journalRepository() {
        return new JournalRepositoryImpl();
    }

    @Bean
    public EnheterIndeks enheterIndeks() throws IOException {
        return BasisdataProviderFileImpl.loadEnheter();
    }

    @Bean
    public GsakRepo gsakRepo() {
        return new GsakRepo();
    }

    @Bean
    public VirksomhetIndeks virksomhetIndeks() throws IOException {
        return BasisdataProviderFileImpl.loadVirksomheter();
    }
}
