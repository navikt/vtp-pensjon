package no.nav.pensjon.vtp.application.configuration;

import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import no.nav.foreldrepenger.vtp.felles.KeystoreUtils;
import no.nav.foreldrepenger.vtp.ldap.LdapServer;

@Configuration
public class LdapServerConfiguration {
    @Bean
    public LdapServer ldapServer() throws Exception {
        LdapServer ldapServer = new LdapServer(new File(KeystoreUtils.getKeystoreFilePath()), KeystoreUtils.getKeyStorePassword().toCharArray(), 8389, 8636);
        ldapServer.start();
        return ldapServer;
    }
}
