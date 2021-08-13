package no.nav.pensjon.vtp.mocks.ldap

import com.unboundid.ldap.listener.InMemoryDirectoryServer
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig
import com.unboundid.ldap.listener.InMemoryListenerConfig
import com.unboundid.ldap.sdk.Entry
import com.unboundid.ldif.LDIFAddChangeRecord
import com.unboundid.ldif.LDIFReader
import no.nav.pensjon.vtp.testmodell.ansatt.AnsatteIndeks
import no.nav.pensjon.vtp.testmodell.ansatt.NAVAnsatt
import no.nav.pensjon.vtp.testmodell.ansatt.NewAnsattEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct
import javax.net.ssl.SSLContext

@Component
@ConditionalOnProperty("ldap.server.enabled")
class LdapServer(
    private val ansatteIndeks: AnsatteIndeks,
    @Value("\${ldap.server.port}") listenerPortLdap: Int,
    @Value("\${ldap.server.securePort}") listenerPortLdaps: Int,
    tlsContext: SSLContext
) {
    private val directoryServer: InMemoryDirectoryServer

    @EventListener
    fun onNewAnsatt(newAnsattEvent: NewAnsattEvent) {
        addNavAnsatt(newAnsattEvent.ansatt)
    }

    private fun readNAVAnsatte() = ansatteIndeks.findAll().forEach(::addNavAnsatt)

    private fun addNavAnsatt(navAnsatt: NAVAnsatt) {
        val entry = navAnsatt.mapToEntry()

        LDIFAddChangeRecord(entry).processChange(directoryServer)
    }

    private fun NAVAnsatt.mapToEntry() = Entry("CN=${cn}_xxx,OU=Users,OU=NAV,OU=BusinessUnits,DC=test,DC=local").apply {
        addAttribute("objectClass", "user", "organizationalPerson", "person", "top")
        addAttribute("objectCategory", "CN=Person,CN=Schema,CN=Configuration,DC=test,DC=local")
        addAttribute("cn", cn)
        addAttribute("samaccountname", cn)
        addAttribute("displayName", displayName)
        addAttribute("givenname", givenname)
        addAttribute("sn", sn)
        addAttribute("mail", email)
        addAttribute(
            "memberOf",
            groups.map { group -> "CN=$group,OU=AccountGroups,OU=Groups,OU=NAV,OU=BusinessUnits,DC=test,DC=local" }
        )
    }

    private fun readLdifFilesFromClasspath() {
        javaClass.classLoader.getResources(BASEDATA_USERS_LDIF).iterator().forEach {
            LDIFReader(it.openStream()).use { ldifReader ->
                do {
                    val readChangeRecord = ldifReader.readChangeRecord()
                    readChangeRecord?.processChange(directoryServer)
                } while (readChangeRecord != null)
            }
        }
    }

    @PostConstruct
    fun start() {
        directoryServer.startListening()
    }

    companion object {
        private const val BASEDATA_USERS_LDIF = "basedata/ldap_setup.ldif"
    }

    init {
        directoryServer = InMemoryDirectoryServer(
            InMemoryDirectoryServerConfig("DC=local").apply {
                setEnforceAttributeSyntaxCompliance(false)
                setEnforceSingleStructuralObjectClass(false)
                schema = null // dropper valider schema slik at vi slipper å definere alle object classes

                setListenerConfigs(
                    InMemoryListenerConfig.createLDAPSConfig(
                        "LDAPS",
                        listenerPortLdaps,
                        tlsContext.serverSocketFactory
                    ),
                    InMemoryListenerConfig.createLDAPConfig("LDAP", listenerPortLdap)
                )
            }
        )

        readLdifFilesFromClasspath()
        readNAVAnsatte()
    }
}
