package no.nav.pensjon.vtp.felles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.lang.JoseException;
import org.springframework.util.ObjectUtils;

public class AzureOidcTokenGenerator {


    private List<String> aud = Collections.emptyList();
    private List<String> groups = new ArrayList<>();

    private NumericDate expiration = NumericDate.fromSeconds(NumericDate.now().getValue() + 3600*6);
    private String issuer;
    private NumericDate issuedAt = NumericDate.now();
    private final String subject;
    private final KeyStoreTool keyStoreTool;
    private final String nonce;
    private Map<String, String> additionalClaims = new HashMap<>();

    public AzureOidcTokenGenerator(final KeyStoreTool keyStoreTool, String brukerId, String nonce) {
        this.keyStoreTool = keyStoreTool;
        this.subject = brukerId;
        this.nonce = nonce;
    }

    public void setAud(String e) {
        this.aud = new ArrayList<>();
        this.aud.add(e);
    }

    AzureOidcTokenGenerator withExpiration(NumericDate expiration) {
        this.expiration = expiration;
        return this;
    }

    public AzureOidcTokenGenerator withIssuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    AzureOidcTokenGenerator withIssuedAt(NumericDate issuedAt) {
        this.issuedAt = issuedAt;
        return this;
    }

    public AzureOidcTokenGenerator withGroups(List<String> groups) {
        this.groups = groups;
        return this;
    }

    public AzureOidcTokenGenerator withClaim(String name, String value) {
        additionalClaims.put(name, value);
        return this;
    }

    public String create() {
        JwtClaims claims = new JwtClaims();
        claims.setIssuer(issuer);
        claims.setExpirationTime(expiration);
        claims.setGeneratedJwtId();
        claims.setIssuedAt(issuedAt);
        claims.setNotBefore(issuedAt);
        claims.setSubject(subject);
        claims.setClaim("ver", "2.0");
        if (!ObjectUtils.isEmpty(nonce)) {
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
        claims.setClaim("groups", groups);

        try {
            return keyStoreTool.createRS256Token(claims.toJson()).getCompactSerialization();
        } catch (JoseException e) {
            throw new RuntimeException(e);
        }
    }
}
