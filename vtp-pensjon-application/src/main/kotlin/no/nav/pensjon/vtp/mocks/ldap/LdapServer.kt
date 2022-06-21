package no.nav.pensjon.vtp.mocks.ldap

import com.unboundid.ldap.listener.InMemoryDirectoryServer
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig
import com.unboundid.ldap.listener.InMemoryListenerConfig.createLDAPConfig
import com.unboundid.ldap.listener.InMemoryListenerConfig.createLDAPSConfig
import com.unboundid.ldap.sdk.Attribute
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
    private val snitchInMemoryOperationInterceptor: SnitchInMemoryOperationInterceptor,
    @Value("\${ldap.server.port}") private val listenerPortLdap: Int,
    @Value("\${ldap.server.securePort}") private val listenerPortLdaps: Int,
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

    private fun NAVAnsatt.mapToEntry() = Entry(
        "CN=${cn},OU=Users,OU=NAV,OU=BusinessUnits,DC=test,DC=local",
        Attribute("objectClass", "user", "organizationalPerson", "person", "top"),
        Attribute("objectCategory", "CN=Person,CN=Schema,CN=Configuration,DC=test,DC=local"),
        Attribute("cn", cn),
        Attribute("samaccountname", cn),
        Attribute("displayName", displayName),
        Attribute("givenname", givenname),
        Attribute("sn", sn),
        Attribute("mail", email),
        Attribute("userPrincipalName", cn),
        Attribute("userPassword", "dummy"),
        Attribute("memberOf", groups.map { "CN=$it,OU=AccountGroups,OU=Groups,OU=NAV,OU=BusinessUnits,DC=test,DC=local" }),
    )

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
                    createLDAPSConfig(
                        "LDAPS",
                        listenerPortLdaps,
                        tlsContext.serverSocketFactory
                    ),

                    createLDAPConfig("LDAP", listenerPortLdap)
                )

                addInMemoryOperationInterceptor(snitchInMemoryOperationInterceptor)
            }
        )

        readLdifFilesFromClasspath()
        readNAVAnsatte()
    }
}
