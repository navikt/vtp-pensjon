package no.nav.pensjon.vtp.application.configuration

import no.nav.pensjon.vtp.testmodell.ansatt.AnsatteIndeks
import no.nav.pensjon.vtp.ldap.LdapServer
import no.nav.pensjon.vtp.felles.KeystoreUtils.getKeyStorePassword
import no.nav.pensjon.vtp.felles.KeystoreUtils.getKeystoreFilePath
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File
import java.lang.Exception

@Configuration
class LdapServerConfiguration {
    @Bean(initMethod = "start")
    @ConditionalOnProperty("ldap.server.enabled")
    @Throws(Exception::class)
    fun ldapServer(ansatteIndeks: AnsatteIndeks, @Value("\${ldap.server.port}") port: Int, @Value("\${ldap.server.securePort}") securePort: Int): LdapServer =
            LdapServer(ansatteIndeks, File(getKeystoreFilePath()), getKeyStorePassword().toCharArray(), port, securePort)
}
