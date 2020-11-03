package no.nav.pensjon.vtp.auth.azuread;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.Api;
import no.nav.pensjon.vtp.testmodell.ansatt.AnsatteIndeks;
import no.nav.pensjon.vtp.testmodell.ansatt.NAVAnsatt;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RestController
@Api(tags = {"AzureAd"})
@RequestMapping("/rest/MicrosoftGraphApi")
public class MicrosoftGraphApiMock {
    private final AnsatteIndeks ansatteIndeks;

    public MicrosoftGraphApiMock(AnsatteIndeks ansatteIndeks) {
        this.ansatteIndeks = ansatteIndeks;
    }

    @GetMapping(value= "/oidc/userinfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity userinfo(@RequestHeader("Authorization") String auth) {
        if (!auth.startsWith("Bearer access:")) {
            return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                    .body("Bad mock access token; must be on format Bearer access:<userid>");
        }

        String accessToken = auth.substring(14);
        String ident = accessToken.split(";")[0];

        NAVAnsatt ansatt = ansatteIndeks.hentNAVAnsatt(ident).orElseThrow(() -> new RuntimeException("Ansatt med ident " + ident + " ikke funnet i VTP."));

        Map<String, String> response = new HashMap<>();
        response.put("sub", ident);
        response.put("name", ansatt.displayName);
        response.put("family_name", ansatt.cn);
        response.put("given_name", ansatt.givenname);
        response.put("picture", "http://example.com/picture.jpg");
        response.put("email", ansatt.email);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value= "/v1.0/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity me(@RequestHeader("Authorization") String auth, @RequestParam String select) {
        if (!auth.startsWith("Bearer access:")) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Bad mock access token; must be on format Bearer access:<userid>");
        }

        String accessToken = auth.substring(14);
        String ident = accessToken.split(";")[0];

        final List<Group> memberOf = ansatteIndeks.hentNAVAnsatt(ident).orElseThrow(() -> new RuntimeException("Fant ikke NAV-ansatt med brukernavn " + ident))
                .groups
                .stream()
                .map(Group::new)
                .collect(toList());

        final User user = new User("https://graph.microsoft.com/v1.0/$metadata#users(" + select + ")/$entity", ident, memberOf);

        return ResponseEntity.ok(user);
    }

    public static class User {
        @JsonProperty("@odata.context")
        public String context;
        public String onPremisesSamAccountName;

        public List<Group> memberOf;

        public User(String context, String onPremisesSamAccountName, List<Group> memberOf) {
            this.context = context;
            this.onPremisesSamAccountName = onPremisesSamAccountName;
            this.memberOf = memberOf;
        }

        @Override
        public String toString() {
            return "User{" +
                    "context='" + context + '\'' +
                    ", onPremisesSamAccountName='" + onPremisesSamAccountName + '\'' +
                    '}';
        }
    }

    public static class Group {
        public String displayName;

        public Group(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return "Group{" +
                    "displayName='" + displayName + '\'' +
                    '}';
        }
    }
}
