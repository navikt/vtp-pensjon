package no.nav.pensjon.vtp.felles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        System.out.println("------------GENERATING A NEW KEY-PAIR------------");
        Files.deleteIfExists(Paths.get(KeystoreUtils.getKeystoreFilePath()));
        Files.deleteIfExists(Paths.get(KeystoreUtils.getTruststoreFilePath()));
        KeystoresGenerator.readKeystoresOrGenerateIfNotExists(KeyStoreTool.KEYSTORE_FORMAT, KeyStoreTool.getKeyAndCertAlias());
        System.out.println("------------DONE------------");
    }
}
