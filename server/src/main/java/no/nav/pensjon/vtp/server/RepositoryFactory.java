package no.nav.pensjon.vtp.server;

import static java.util.Optional.ofNullable;

import java.util.Hashtable;

import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;

import no.nav.pensjon.vtp.auth.UserRepository;

public interface RepositoryFactory {
    static UserRepository createUserRepository() {
        try {
            Hashtable<String, String> props = new Hashtable<>();
            props.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            props.put(javax.naming.Context.SECURITY_AUTHENTICATION, "none");

            props.put(javax.naming.Context.PROVIDER_URL,
                    ofNullable(System.getenv("LDAP_PROVIDER_URL"))
                            .orElse("ldap://localhost:8389/")
            );

            return new UserRepository(new InitialLdapContext(props, null), UserRepository.defaultLdapName());
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
