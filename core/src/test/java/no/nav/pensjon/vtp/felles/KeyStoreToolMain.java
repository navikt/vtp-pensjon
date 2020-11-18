package no.nav.pensjon.vtp.felles;

import static java.lang.System.getProperty;
import static java.nio.file.Files.deleteIfExists;
import static java.nio.file.Paths.get;

import static no.nav.pensjon.vtp.felles.KeyStoreTool.KEYSTORE_FORMAT;

import java.io.IOException;

public class KeyStoreToolMain {

    /**
     * Dette brukes bare for å generere et nytt nøkkel-par.
     * 1. Generer nøkkel-par ved å kjøre main-metoden
     * 2. Eksporter sertifikat etter generering:
     * keytool -export -keystore ~/.modig/keystore.jks -alias localhost-ssl -file ~/.modig/ vtp-pensjon.cer
     * 3. Importer sertifikaten inn i PEN lokale truststores som ligger i pesys/local/pen-secrets:
     * keytool -import -alias localhost -file ~/.modig/vtp-pensjon.cer -storetype JKS -keystore ~/pesys/local/pen-secrets/truststore/truststore.jts
     * 4. Importer sertifikaten inn i POPP lokale truststores som ligger i pesys/local/popp-secrets:
     * keytool -import -alias localhost -file ~/.modig/vtp-pensjon.cer -storetype JKS -keystore ~/pesys/local/popp-secrets/truststore/truststore.jts
     * 5. Lag en Pull-Request til pesys-master med oppdaterte lokale truststores
     * 6. Kopier og erstatt vtp truststore og keystore i keystore.jks og truststore med nye genererte
     * 7. Lag Pull-Request til vtp-master med oppdaterte truststore/keystore
     */
    public static void main(String[] args) throws IOException {
        final String keystorePath = getProperty("user.home", ".") + "/.modig/keystore.jks";
        final String keystorePassword = "devillokeystore1234";

        final String truststorePath = getProperty("user.home", ".") + "/.modig/truststore.jks";
        final String truststorePassword = "changeit";

        final String keyAndCertAlias = "localhost-ssl";

        System.out.println("------------GENERATING A NEW KEY-PAIR------------");
        deleteIfExists(get(keystorePath));
        deleteIfExists(get(truststorePath));
        KeystoresGenerator.readKeystoresOrGenerateIfNotExists(keystorePath, keystorePassword, truststorePath, truststorePassword, KEYSTORE_FORMAT, keyAndCertAlias);
        System.out.println("------------DONE------------");
    }
}
