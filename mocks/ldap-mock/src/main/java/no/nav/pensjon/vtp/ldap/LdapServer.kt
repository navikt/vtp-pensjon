package no.nav.pensjon.vtp.ldap

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import no.nav.pensjon.vtp.testmodell.ansatt.AnsatteIndeks
import com.unboundid.ldap.listener.InMemoryDirectoryServer
import com.unboundid.ldif.LDIFAddChangeRecord
import com.unboundid.ldif.LDIFReader
import javax.annotation.PostConstruct
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig
import com.unboundid.ldap.listener.InMemoryListenerConfig
import com.unboundid.ldap.sdk.Entry
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.net.ssl.SSLContext

@Component
@ConditionalOnProperty("ldap.server.enabled")
class LdapServer(
        private val ansatteIndeks: AnsatteIndeks,
        @Value("\${ldap.server.port}") listenerPortLdap: Int,
        @Value("\${ldap.server.securePort}") listenerPortLdaps: Int,
        tlsContext: SSLContext) {
    private val directoryServer: InMemoryDirectoryServer

    private fun readNAVAnsatte(server: InMemoryDirectoryServer) {
        ansatteIndeks.findAll().forEach {
            with(it) {
                val entry = Entry("CN=${cn}_xxx,OU=Users,OU=NAV,OU=BusinessUnits,DC=test,DC=local").apply {
                    addAttribute("objectClass", "user", "organizationalPerson", "person", "top")
                    addAttribute("objectCategory", "CN=Person,CN=Schema,CN=Configuration,DC=test,DC=local")
                    addAttribute("cn", cn)
                    addAttribute("samaccountname", cn)
                    addAttribute("displayName", displayName)
                    addAttribute("givenname", givenname)
                    addAttribute("sn", sn)
                    addAttribute("mail", email)
                    addAttribute("memberOf", groups.map { group -> "CN=$group,OU=AccountGroups,OU=Groups,OU=NAV,OU=BusinessUnits,DC=test,DC=local" })
                }

                LDIFAddChangeRecord(entry).processChange(server)

                logger.debug("Added NAV-ansatt to LDAP: {}", entry.toLDIF().joinToString(prefix = "[", postfix = "]"))
            }
        }
    }

    private fun readLdifFilesFromClasspath(server: InMemoryDirectoryServer) {
        javaClass.classLoader.getResources(BASEDATA_USERS_LDIF).iterator().forEach {
            LDIFReader(it.openStream()).use { ldifReader ->
                do {
                    val readChangeRecord = ldifReader.readChangeRecord()
                    if (readChangeRecord != null) {
                        readChangeRecord.processChange(server)
                        logger.debug("Read entry from path {} LDIF: {}", it.path, readChangeRecord.toLDIF().joinToString(prefix = "[", postfix = "]"))
                    }
                } while (readChangeRecord != null)
            }
        }
    }

    @PostConstruct
    fun start() {
        directoryServer.startListening()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(LdapServer::class.java)
        private const val BASEDATA_USERS_LDIF = "basedata/ldap_setup.ldif"
    }

    init {
        val cfg = InMemoryDirectoryServerConfig("DC=local")
        cfg.setEnforceAttributeSyntaxCompliance(false)
        cfg.setEnforceSingleStructuralObjectClass(false)
        cfg.schema = null // dropper valider schema slik at vi slipper å definere alle object classes
        val ldapsConfig = InMemoryListenerConfig.createLDAPSConfig("LDAPS", listenerPortLdaps, tlsContext.serverSocketFactory)
        val ldapConfig = InMemoryListenerConfig.createLDAPConfig("LDAP", listenerPortLdap)
        cfg.setListenerConfigs(ldapsConfig, ldapConfig)
        directoryServer = InMemoryDirectoryServer(cfg)
        readLdifFilesFromClasspath(directoryServer)
        readNAVAnsatte(directoryServer)
    }
}
