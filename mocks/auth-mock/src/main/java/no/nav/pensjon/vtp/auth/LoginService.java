package no.nav.pensjon.vtp.auth;

import io.swagger.annotations.Api;
import no.nav.pensjon.vtp.felles.JsonWebKeySupport;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.lang.JoseException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(tags = {"LoginService"})
@RequestMapping("/rest/loginservice")
public class LoginService {
    private final JsonWebKeySupport jsonWebKeySupport;
    private final PersonModellRepository personModellRepository;

    public LoginService(JsonWebKeySupport jsonWebKeySupport, PersonModellRepository personModellRepository) {
        this.jsonWebKeySupport = jsonWebKeySupport;
        this.personModellRepository = personModellRepository;
    }

    @GetMapping(value = "/login")
    public ResponseEntity<String> login(@RequestParam("redirect") String redirect) {
        List<String> rows = personModellRepository.findAll().stream()
                .map(p -> {
                    String fnr = p.getIdent();
                    String navn = p.getFornavn() + " " + p.getEtternavn();
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
                "<input type=\"hidden\" name=\"redirect\" value=\""+ HtmlUtils.htmlEscape(redirect) +"\">" +
                "<input name=\"fnr\" placeholder=\"Fyll inn et annet fødselsnummer\" style=\"width: 200px\"><input type=\"submit\"></form>" +
                "</div>" +
                "</body>" +
                "</html>";

        return ResponseEntity.ok(html);
    }

    @GetMapping(value = "/login-redirect-with-cookie")
    public ResponseEntity<?> doLogin(@RequestParam("redirect") String redirect, @RequestParam("fnr") String fnr) throws URISyntaxException, JoseException {
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

        String token = jsonWebKeySupport.createRS256Token(claims.toJson()).getCompactSerialization();

        HttpCookie cookie = ResponseCookie.from("selvbetjening-idtoken", token)
                .path("/").maxAge(-1L).httpOnly(false).secure(false).build();

        return ResponseEntity
                .status(HttpStatus.TEMPORARY_REDIRECT)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .location(new URI(redirect))
                .build();
    }
}
