package no.nav.pensjon.vtp.ldap;

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldif.LDIFAddChangeRecord;
import com.unboundid.ldif.LDIFChangeRecord;
import com.unboundid.ldif.LDIFReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import no.nav.pensjon.vtp.testmodell.ansatt.AnsatteIndeks;
import no.nav.pensjon.vtp.testmodell.ansatt.NAVAnsatt;

@Component
@ConditionalOnProperty("ldap.server.enabled")
public class LdapServer {

    private static final Logger LOG = LoggerFactory.getLogger(LdapServer.class);
    private static final String BASEDATA_USERS_LDIF = "basedata/ldap_setup.ldif";

    private final InMemoryDirectoryServer directoryServer;

    private final AnsatteIndeks ansatteIndeks;

    public LdapServer(
            AnsatteIndeks ansatteIndeks,
            @Value("${ldap.server.port}") int listenerPortLdap,
            @Value("${ldap.server.securePort}") int listenerPortLdaps,
            SSLContext tlsContext) throws Exception {
        this.ansatteIndeks = ansatteIndeks;
        InMemoryDirectoryServerConfig cfg = new InMemoryDirectoryServerConfig("DC=local");

        cfg.setEnforceAttributeSyntaxCompliance(false);
        cfg.setEnforceSingleStructuralObjectClass(false);
        cfg.setSchema(null); // dropper valider schema slik at vi slipper å definere alle object classes

        InMemoryListenerConfig ldapsConfig = InMemoryListenerConfig.createLDAPSConfig("LDAPS", listenerPortLdaps, tlsContext.getServerSocketFactory());
        InMemoryListenerConfig ldapConfig = InMemoryListenerConfig.createLDAPConfig("LDAP",listenerPortLdap );

        cfg.setListenerConfigs(ldapsConfig,ldapConfig);

        directoryServer = new InMemoryDirectoryServer(cfg);
        readLdifFilesFromClasspath(directoryServer);
        readNAVAnsatte(directoryServer);
    }


    private void readNAVAnsatte(InMemoryDirectoryServer server) throws Exception {
        for (NAVAnsatt ansatt : (Iterable<NAVAnsatt>) ansatteIndeks.hentAlleAnsatte()::iterator) {
            Entry entry = new Entry("CN=" + ansatt.cn + "_xxx,OU=Users,OU=NAV,OU=BusinessUnits,DC=test,DC=local");
            entry.addAttribute("objectClass", "user", "organizationalPerson", "person", "top");
            entry.addAttribute("objectCategory", "CN=Person,CN=Schema,CN=Configuration,DC=test,DC=local");
            entry.addAttribute("cn", ansatt.cn);
            entry.addAttribute("samaccountname", ansatt.cn);
            entry.addAttribute("displayName", ansatt.displayName);
            entry.addAttribute("givenname", ansatt.givenname);
            entry.addAttribute("sn", ansatt.sn);
            entry.addAttribute("mail", ansatt.email);
            entry.addAttribute("memberOf", ansatt.groups.stream().map(group -> "CN=" + group + ",OU=AccountGroups,OU=Groups,OU=NAV,OU=BusinessUnits,DC=test,DC=local").collect(Collectors.toList()));

            LOG.debug("Added NAV-ansatt to LDAP: {}", Arrays.toString(entry.toLDIF()));
            new LDIFAddChangeRecord(entry).processChange(server);
        }
    }

    @SuppressWarnings("resource")
    private void readLdifFilesFromClasspath(InMemoryDirectoryServer server) throws Exception {
        Enumeration<URL> ldifs = getClass().getClassLoader().getResources(BASEDATA_USERS_LDIF);
        while(ldifs.hasMoreElements()) {
            URL ldif = ldifs.nextElement();
            try(InputStream is = ldif.openStream()){
                LDIFReader r = new LDIFReader(is);
                LDIFChangeRecord readEntry;
                while ((readEntry = r.readChangeRecord()) != null) {

                    LOG.info("Read entry from path {} LDIF: {}", ldif.getPath(), Arrays.toString(readEntry.toLDIF()));
                  readEntry.processChange(server);
                }
            }
        }
    }

    @PostConstruct
    public void start() {
        try {
            directoryServer.startListening();
        } catch (LDAPException e) {
            throw new IllegalStateException("Kunne ikke starte LdapServer", e);
        }
    }
}
