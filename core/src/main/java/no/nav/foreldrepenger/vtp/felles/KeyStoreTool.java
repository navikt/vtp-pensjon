package no.nav.foreldrepenger.vtp.felles;

import org.jose4j.base64url.Base64Url;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.interfaces.RSAPublicKey;

public class KeyStoreTool {
    private static final Logger log = LoggerFactory.getLogger(KeyStoreTool.class);
    private static RsaJsonWebKey jwk = null;

    public static synchronized void init() {
        try {
            jwk = RsaJwkGenerator.generateJwk(2048);
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
        return "localhost-ssl";
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
