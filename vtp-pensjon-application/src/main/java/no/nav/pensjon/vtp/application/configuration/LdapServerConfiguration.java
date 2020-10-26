package no.nav.pensjon.vtp.application.configuration;

import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import no.nav.pensjon.vtp.felles.KeystoreUtils;
import no.nav.pensjon.vtp.ldap.LdapServer;
import no.nav.pensjon.vtp.testmodell.ansatt.AnsatteIndeks;

@Configuration
public class LdapServerConfiguration {
    @Bean(initMethod = "start")
    public LdapServer ldapServer(AnsatteIndeks ansatteIndeks) throws Exception {
        return new LdapServer(ansatteIndeks, new File(KeystoreUtils.getKeystoreFilePath()), KeystoreUtils.getKeyStorePassword().toCharArray(), 8389, 8636);
    }
}
