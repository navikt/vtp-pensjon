package no.nav.pensjon.vtp.miscellaneous.api.pensjon_testdata;

import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.pensjon.vtp.testmodell.repo.Testscenario;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static java.util.Optional.of;
import static org.slf4j.LoggerFactory.getLogger;

public class PensjonTestdataServiceImpl implements PensjonTestdataService {
    private final Logger logger = getLogger(getClass());
    private final RestTemplate client = new RestTemplate();
    private final String baseUrl;

    public PensjonTestdataServiceImpl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public void opprettData(final Testscenario testscenario) {
        of(testscenario)
                .map(Testscenario::getPersonopplysninger)
                .map(Personopplysninger::getSøker)
                .map(PersonModell::getIdent)
                .ifPresent(this::lagrePerson);

        of(testscenario)
                .map(Testscenario::getPersonopplysninger)
                .map(Personopplysninger::getAnnenPart)
                .map(PersonModell::getIdent)
                .ifPresent(this::lagrePerson);
    }

    public void lagrePerson(final String ident) {
        String url = baseUrl + "/api/person/" + ident;
        HttpEntity<Object> request = new HttpEntity<>(null, new HttpHeaders());
        final ResponseEntity<String> response = client.postForEntity(url, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            logger.error("Failed to create person (ident={}, uri={}, responseCode={})", ident, baseUrl, response.getStatusCode());
            throw new RuntimeException("Failed to create person with ident=" + ident + " in pensjon-testdata using Uri=" + baseUrl);
        }
    }
}
