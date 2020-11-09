package no.nav.pensjon.vtp.application.configuration;

import static no.nav.pensjon.vtp.testmodell.repo.impl.BasisdataProviderFileImpl.loadAdresser;
import static no.nav.pensjon.vtp.testmodell.repo.impl.BasisdataProviderFileImpl.loadAnsatte;
import static no.nav.pensjon.vtp.testmodell.repo.impl.BasisdataProviderFileImpl.loadEnheter;
import static no.nav.pensjon.vtp.testmodell.repo.impl.BasisdataProviderFileImpl.loadVirksomheter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import no.nav.pensjon.vtp.miscellaneous.api.pensjon_testdata.PensjonTestdataService;
import no.nav.pensjon.vtp.miscellaneous.api.pensjon_testdata.PensjonTestdataServiceImpl;
import no.nav.pensjon.vtp.testmodell.ansatt.AnsatteIndeks;
import no.nav.pensjon.vtp.testmodell.enheter.EnheterIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseIndeks;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplateRepository;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioTemplateLoader;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioTemplateRepositoryImpl;
import no.nav.pensjon.vtp.testmodell.virksomhet.VirksomhetIndeks;

@Configuration
public class RepositoryConfiguration {
    @Bean
    public AdresseIndeks adresseIndeks() throws IOException {
        return loadAdresser();
    }

    @Bean
    public AnsatteIndeks ansatteIndeks() {
        return loadAnsatte();
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
    public EnheterIndeks enheterIndeks() throws IOException {
        return loadEnheter();
    }

    @Bean
    public VirksomhetIndeks virksomhetIndeks() throws IOException {
        return loadVirksomheter();
    }
}
