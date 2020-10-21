package no.nav.pensjon.vtp.auth;

import io.swagger.annotations.Api;
import no.nav.foreldrepenger.vtp.felles.KeyStoreTool;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioRepository;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.lang.JoseException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"LoginService"})
@Path("/loginservice")
public class LoginService {
    @Context
    private TestscenarioRepository repo;

    @GET
    @Path("/login")
    public Response login(@QueryParam("redirect") String redirect) {
        List<String> rows = repo.getPersonIndeks().getAlleSøkere().parallelStream()
                .map(p -> {
                    String fnr = p.getSøker().getIdent();
                    String navn = p.getSøker().getFornavn() + " " + p.getSøker().getEtternavn();
                    return "<a href=\"login-redirect-with-cookie?fnr=" + fnr + "&redirect=" + URLEncoder.encode(redirect, StandardCharsets.UTF_8) + "\">" + navn + "</a> ("+fnr+")<br>";
                }).collect(Collectors.toList());

        String html = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<title>Velg bruker</title>" +
                "</head>" +
                "<body>" +
                "<div style=\"text-align:center;width:100%;\">" +
                "<h3>Velg bruker:</h3>" +
                String.join("", rows) +
                (rows.isEmpty() ? "Det finnes visst ingen personer i VTP akkurat nå. Prøv å <a href=\"/#/\">laste inn et scenario</a>!" : "") +
                "<form method=\"get\" action=\"login-redirect-with-cookie\">" +
                "<input type=\"hidden\" name=\"redirect\" value=\""+ StringEscapeUtils.escapeHtml4(redirect) +"\">" +
                "<input name=\"fnr\" placeholder=\"Fyll inn et annet fødselsnummer\" style=\"width: 200px\"><input type=\"submit\"></form>" +
                "</div>" +
                "</body>" +
                "</html>";

        return Response.ok(html, MediaType.TEXT_HTML).build();
    }

    @GET
    @Path("/login-redirect-with-cookie")
    public Response doLogin(@QueryParam("redirect") String redirect, @QueryParam("fnr") String fnr) throws URISyntaxException, JoseException {
        NumericDate now = NumericDate.now();
        JwtClaims claims = new JwtClaims();

        claims.setSubject(fnr);
        claims.setIssuer("https://login.microsoftonline.com/d38f25aa-eab8-4c50-9f28-ebf92c1256f2/v2.0/");
        claims.setIssuedAt(now);
        claims.setNotBefore(now);
        claims.setGeneratedJwtId();
        claims.setExpirationTime(NumericDate.fromSeconds(now.getValue() + 3600 * 6));
        claims.setAudience("0090b6e1-ffcc-4c37-bc21-049f7d1f0fe5");
        claims.setClaim("ver", "1.0");
        claims.setClaim("acr", "Level4");
        claims.setNumericDateClaim("auth_time", now);
        claims.setClaim("nonce", "hardcoded");
        claims.setClaim("at_hash", "unknown");

        RsaJsonWebKey senderJwk = KeyStoreTool.getJsonWebKey();
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKeyIdHeaderValue(KeyStoreTool.getJsonWebKey().getKeyId());
        jws.setAlgorithmHeaderValue("RS256");
        jws.setKey(senderJwk.getPrivateKey());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        String token = jws.getCompactSerialization();

        NewCookie cookie = new NewCookie(
                "selvbetjening-idtoken",
                token,
                "/",
                null,
                1,
                null,
                -1,
                null,
                false,
                false);

        return Response
                .temporaryRedirect(new URI(redirect))
                .cookie(cookie)
                .build();
    }
}
