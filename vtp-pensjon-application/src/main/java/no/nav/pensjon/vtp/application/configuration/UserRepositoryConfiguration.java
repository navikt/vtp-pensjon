package no.nav.pensjon.vtp.application.configuration;

import java.util.Hashtable;

import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import no.nav.pensjon.vtp.auth.UserRepository;

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
}
