package no.nav.foreldrepenger.vtp.felles;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v1CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

class KeystoresGenerator {
    private static final Logger log = LoggerFactory.getLogger(KeystoresGenerator.class);
    // TODO: Maybe use PKCS12? Java 9 and onwards uses that, Java 8 or below uses JKS.
    // String outputFormat = "PKCS12";

    static void generateKeystoresIfNotExists(String outputFormat, String keyAndCertAlias) {
        String keystorePath = KeystoreUtils.getKeystoreFilePath();
        String truststorePath = KeystoreUtils.getTruststoreFilePath();

        File keystoreFile = new File(keystorePath);
        keystoreFile.getParentFile().mkdirs();

        if (keystoreFile.length() == 0) {
            log.warn("Keystore keystoreFile {} does not exist - will auto-generate.", keystorePath);
            createKeystoreAndUpdateTrustStore(keyAndCertAlias, keystorePath, outputFormat, truststorePath);
            return;
        }

        KeyStore.PrivateKeyEntry certEntry = null;
        try (FileInputStream keystoreFileIS = new FileInputStream(new File(keystorePath))) {
            KeyStore keyStore = KeyStore.getInstance(outputFormat);
            keyStore.load(keystoreFileIS, KeystoreUtils.getKeyStorePassword().toCharArray());
            KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(KeystoreUtils.getKeyStorePassword().toCharArray());
            certEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(keyAndCertAlias, protParam);
        } catch (Exception e) {
            log.error("Could not read existing keystore. " + e.getMessage(), e);
        }
        if (certEntry == null) { //will not regenerate existing certificate
            createKeystoreAndUpdateTrustStore(keyAndCertAlias, keystorePath, outputFormat, truststorePath);
        }
    }

    private static void createKeystoreAndUpdateTrustStore(String keyAndCertAlias, String keystorePath, String outputFormat, String
            truststorePath) {
        log.info("Generating keystore {} with certificate for {}", keystorePath, keyAndCertAlias);
        Certificate selfCert = generateKeystore(keystorePath, KeystoreUtils.getKeyStorePassword(), outputFormat, keyAndCertAlias);
        updateTruststore(keyAndCertAlias, outputFormat, truststorePath, selfCert);
    }

    private static void updateTruststore(String keyAndCertAlias, String outputFormat, String truststorePath, Certificate selfCert) {
        try {
            if (!Files.exists(Paths.get(truststorePath))) {
                log.warn("Truststore file {} does not exist - will auto-generate.", truststorePath);
                File file = new File(truststorePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            // Create the truststore, add the certificate, save to file
            log.info("Adding certificate to the truststore {}", truststorePath);
            KeyStore trustStore = KeyStore.getInstance(outputFormat);
            try (FileInputStream trustStoreFileIS = new FileInputStream(new File(truststorePath))) {
                InputStream stream = new File(truststorePath).length() > 0 ? trustStoreFileIS : null;
                trustStore.load(stream, KeystoreUtils.getTruststorePassword().toCharArray());
            }
            trustStore.setCertificateEntry(keyAndCertAlias, selfCert);
            writeKeystoreToFile(trustStore, truststorePath, KeystoreUtils.getTruststorePassword());
        } catch (Exception e) {
            log.error("Could not add generated certificate to trustStore: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    static Certificate generateKeystore(String keystorePath, String keystorePassword, String outputFormat, String keyAndCertAlias) {
        Security.addProvider(new BouncyCastleProvider());
        java.security.cert.Certificate selfCert;

        try {
            // Generate public/private keypair and self-signed certificate
            java.security.KeyPairGenerator keyPairGenerator = KeyPairGenerator
                    .getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
            selfCert = createMasterCert(
                    publicKey, privateKey);

            // Create the keystore, add the two entries
            // (we just use the same private key twice, for simplicity...),
            // save to file
            java.security.cert.Certificate[] outChain = {selfCert};
            KeyStore outStore = KeyStore.getInstance(outputFormat);
            outStore.load(null, keystorePassword.toCharArray());
            outStore.setKeyEntry(keyAndCertAlias, privateKey, keystorePassword.toCharArray(),
                    outChain);
            outStore.setKeyEntry("app-key", privateKey, keystorePassword.toCharArray(),
                    outChain);
            writeKeystoreToFile(outStore, keystorePath, keystorePassword);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError(e.getMessage());
        }
        return selfCert;
    }

    private static void writeKeystoreToFile(KeyStore keyStore, String filePath, String password)
            throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException {
        OutputStream outputStream = new FileOutputStream(filePath);
        keyStore.store(outputStream, password.toCharArray());
        outputStream.flush();
        outputStream.close();
    }

    private static Certificate createMasterCert(
            PublicKey pubKey,
            PrivateKey privKey)
            throws Exception {
        String issuer = "CN=localhost";
        String subject = "CN=localhost";

        // Valid from January 1st 1970 until ten years from now
        Date validFrom = new Date(0);
        Date validUntil = new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 3650));

        X509v1CertificateBuilder builder = new JcaX509v1CertificateBuilder(
                new X500Name(issuer),
                BigInteger.valueOf(1),
                validFrom,
                validUntil,
                new X500Name(subject),
                pubKey
        );

        X509CertificateHolder holder = builder.build(
                new JcaContentSignerBuilder("SHA1WithRSA")
                        .setProvider("BC")
                        .build(privKey)
        );

        X509Certificate cert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(holder);
        cert.checkValidity(new Date());
        cert.verify(pubKey);

        return cert;
    }
}
