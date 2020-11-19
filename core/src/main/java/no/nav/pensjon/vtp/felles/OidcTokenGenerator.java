package no.nav.pensjon.vtp.felles;

import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.lang.JoseException;

import com.google.common.base.Strings;


public class OidcTokenGenerator {

    private final KeyStoreTool keyStoreTool;
    private List<String> aud = singletonList("OIDC");
    private NumericDate expiration = NumericDate.fromSeconds(NumericDate.now().getValue() + 3600*6);
    private String issuer;
    private NumericDate issuedAt = NumericDate.now();
    private final String subject;
    private String nonce;
    private Map<String, String> additionalClaims = new HashMap<>();

    public OidcTokenGenerator(KeyStoreTool keyStoreTool, String brukerId, String nonce) {
        this.keyStoreTool = keyStoreTool;
        additionalClaims.put("azp", "OIDC");
        additionalClaims.put("acr", "Level4");
        this.subject = brukerId;
        this.nonce = nonce;

    }

    public void addAud(String e) {
        this.aud = new ArrayList<>(aud);
        this.aud.add(e);
    }

    OidcTokenGenerator withoutAzp() {
        additionalClaims.remove("azp");
        return this;
    }

    OidcTokenGenerator withExpiration(NumericDate expiration) {
        this.expiration = expiration;
        return this;
    }

    public OidcTokenGenerator withIssuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    public String create() {
        JwtClaims claims = new JwtClaims();
        claims.setIssuer(issuer);
        claims.setExpirationTime(expiration);
        claims.setGeneratedJwtId();
        claims.setIssuedAt(issuedAt);
        claims.setSubject(subject);
        if (!Strings.isNullOrEmpty(nonce)) {
            claims.setClaim("nonce", nonce);
        }
        if (aud.size() == 1) {
            claims.setAudience(aud.get(0));
        } else {
            claims.setAudience(aud);
        }
        for (Map.Entry<String, String> entry : additionalClaims.entrySet()) {
            claims.setStringClaim(entry.getKey(), entry.getValue());
        }
        try {
            return keyStoreTool.createRS256Token(claims.toJson()).getCompactSerialization();
        } catch (JoseException e) {
            throw new RuntimeException(e);
        }
    }
}
