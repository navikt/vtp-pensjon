package no.nav.pensjon.vtp.felles;

import org.jose4j.base64url.Base64Url;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.lang.JoseException;
import org.opensaml.security.x509.impl.KeyStoreX509CredentialAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;

import static no.nav.pensjon.vtp.felles.KeystoresGenerator.copyKeystoreAndTruststore;
import static no.nav.pensjon.vtp.felles.KeystoresGenerator.readKeystoresOrGenerateIfNotExists;

public class KeyStoreTool {
    private static final Logger log = LoggerFactory.getLogger(KeyStoreTool.class);
    private static RsaJsonWebKey jwk = null;
    private static KeyStore keystore = null;
    static final String KEYSTORE_FORMAT = "JKS";

    public static synchronized void init() {
        if (keystore != null) {
            return;
        }


        org.apache.xml.security.Init.init();

        PublicKey myPublicKey;
        PrivateKey myPrivateKey;
        char[] keystorePassword = getKeyStorePassword();
        String keystorePath = getDefaultKeyStorePath();
        String keyAndCertAlias = getKeyAndCertAlias();

        try{
            copyKeystoreAndTruststore();
        }
        catch(IOException e){
            readKeystoresOrGenerateIfNotExists(KEYSTORE_FORMAT, keyAndCertAlias);
        }

        try (FileInputStream keystoreFile = new FileInputStream(new File(keystorePath))) {
            KeyStore ks = KeyStore.getInstance(KEYSTORE_FORMAT);
            ks.load(keystoreFile, keystorePassword);

            log.info("Henter tilgjengelige cert-aliases");
            ks.aliases().asIterator().forEachRemaining(log::info);

            KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(keystorePassword);
            KeyStore.PrivateKeyEntry pk = (KeyStore.PrivateKeyEntry) ks.getEntry(keyAndCertAlias, protParam);
            myPrivateKey = pk.getPrivateKey();
            Certificate cert = ks.getCertificate(keyAndCertAlias);
            myPublicKey = cert.getPublicKey();

            KeyStoreTool.keystore = ks;
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | UnrecoverableEntryException e) {
            log.error("Error during loading of keystore. Do you have your keystore in order, soldier?", e);
            throw new RuntimeException(e);
        }

        try {
            jwk = (RsaJsonWebKey) PublicJsonWebKey.Factory.newPublicJwk(myPublicKey);
            jwk.setPrivateKey(myPrivateKey);
            jwk.setKeyId("1");
        } catch (JoseException e) {
            log.error("Error during init of JWK: " + e);
            throw new RuntimeException(e);
        }

    }

    public static String getDefaultKeyStorePath() {
        return KeystoreUtils.getKeystoreFilePath();
    }

    public static char[] getKeyStorePassword() {
        return KeystoreUtils.getKeyStorePassword().toCharArray();
    }

    public static String getKeyAndCertAlias() {
        return System.getProperty("no.nav.modig.security.appkey", "localhost-ssl");
    }


    public static synchronized KeyStoreX509CredentialAdapter getDefaultCredential() {
        init();
        return new KeyStoreX509CredentialAdapter(keystore, getKeyAndCertAlias(), getKeyStorePassword());
    }

    public static synchronized RsaJsonWebKey getJsonWebKey() {
        init();
        return jwk;
    }

    public static String getJwks() {
        String kty = "RSA";
        String kid = "1";
        String use = "sig";
        String alg = "RS256";
        RsaJsonWebKey jsonWebKey = getJsonWebKey();
        String e = Base64Url.encode(jsonWebKey.getRsaPublicKey().getPublicExponent().toByteArray());
        RSAPublicKey publicKey = (RSAPublicKey) jsonWebKey.getPublicKey();

        byte[] bytes = publicKey.getModulus().toByteArray();
        String n = Base64Url.encode(bytes);

        return String.format("{\"keys\":[{" +
                "\"kty\":\"%s\"," +
                "\"alg\":\"%s\"," +
                "\"use\":\"%s\"," +
                "\"kid\":\"%s\"," +
                "\"n\":\"%s\"," +
                "\"e\":\"%s\"" +
                "}]}", kty, alg, use, kid, n, e);
    }

}
