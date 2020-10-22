package no.nav.pensjon.vtp.application.configuration;

import java.io.IOException;
import java.util.Hashtable;

import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import no.nav.pensjon.vtp.server.api.pensjon_testdata.PensjonTestdataService;
import no.nav.pensjon.vtp.server.api.pensjon_testdata.PensjonTestdataServiceImpl;
import no.nav.pensjon.vtp.testmodell.repo.BasisdataProvider;
import no.nav.pensjon.vtp.testmodell.repo.JournalRepository;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplateRepository;
import no.nav.pensjon.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.pensjon.vtp.testmodell.repo.impl.JournalRepositoryImpl;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioTemplateRepositoryImpl;
import no.nav.pensjon.vtp.auth.UserRepository;
import no.nav.tjeneste.virksomhet.sak.v1.GsakRepo;

@Configuration
public class UserRepositoryConfiguration {
    @Bean
    @DependsOn("ldapServer")
    public UserRepository userRepository() throws NamingException {
        Hashtable<String, String> props = new Hashtable<>();
        props.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        props.put(javax.naming.Context.PROVIDER_URL, "ldap://localhost:8389/");
        props.put(javax.naming.Context.SECURITY_AUTHENTICATION, "none");
        return new UserRepository(new InitialLdapContext(props, null), UserRepository.defaultLdapName());
    }

    @Bean
    public BasisdataProvider basisdataProvider() throws IOException {
        return BasisdataProviderFileImpl.getInstance();
    }

    @Bean
    public TestscenarioTemplateRepository testscenarioTemplateRepository() {
        TestscenarioTemplateRepositoryImpl templateRepositoryImpl = TestscenarioTemplateRepositoryImpl.getInstance();
        templateRepositoryImpl.load();
        return templateRepositoryImpl;
    }

    @Bean
    public TestscenarioRepository testscenarioRepository(BasisdataProvider basisdataProvider) {
        return TestscenarioRepositoryImpl.getInstance(basisdataProvider);
    }

    @Bean
    public PensjonTestdataService pensjonTestdataService(@Value("${pensjon.testdata.url}") String pensjonTestdataUrl) {
        return new PensjonTestdataServiceImpl(pensjonTestdataUrl);
    }

    @Bean
    public JournalRepository journalRepository() {
        return JournalRepositoryImpl.getInstance();
    }

    @Bean
    public GsakRepo gsakRepo() {
        return new GsakRepo();
    }
}
