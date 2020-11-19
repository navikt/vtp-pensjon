package no.nav.pensjon.vtp.felles;

import java.security.interfaces.RSAPublicKey;

import org.jose4j.base64url.Base64Url;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.springframework.stereotype.Component;

@Component
public class KeyStoreTool {
    private final RsaJsonWebKey jwk;

    public KeyStoreTool(final RsaJsonWebKey rsaJsonWebKey) {
        this.jwk = rsaJsonWebKey;
    }

    public String getJwks() {
        String kty = "RSA";
        String kid = "1";
        String use = "sig";
        String alg = "RS256";
        String e = Base64Url.encode(jwk.getRsaPublicKey().getPublicExponent().toByteArray());
        RSAPublicKey publicKey = (RSAPublicKey) jwk.getPublicKey();

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

    public JsonWebSignature createRS256Token(final String string) {
        final JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(string);
        jws.setKeyIdHeaderValue(jwk.getKeyId());
        jws.setAlgorithmHeaderValue("RS256");
        jws.setKey(jwk.getPrivateKey());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        return jws;
    }
}
