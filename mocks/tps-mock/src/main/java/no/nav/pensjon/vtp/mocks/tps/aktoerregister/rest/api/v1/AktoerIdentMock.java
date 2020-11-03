package no.nav.pensjon.vtp.mocks.tps.aktoerregister.rest.api.v1;

import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@Api(tags = {"aktoerregister"})
@RequestMapping("/rest/aktoerregister/api/v1/identer")
public class AktoerIdentMock {

    private static final String NAV_IDENTER_HEADER_KEY = "Nav-Personidenter";
    private static final int NAV_IDENTER_MAX_SIZE = 1000;
    private static final String IDENTGRUPPE = "identgruppe";
    private static final String AKTOERID_IDENTGRUPPE = "AktoerId";
    private static final String PERSONIDENT_IDENTGRUPPE = "NorskIdent";
    private static final String GJELDENDE = "gjeldende";

    //TODO (TEAM FAMILIE) Lag mock-responser fra scenario NOSONAR
    private String personIdentMock = "12345678910";
    private String aktørIdMock = "1234567891011";
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, IdentinfoForAktoer> alleIdenterForIdenter(@RequestHeader(NAV_IDENTER_HEADER_KEY) Set<String> requestIdenter,
                                                                 @RequestParam(IDENTGRUPPE) String identgruppe,
                                                                 @RequestParam(GJELDENDE) boolean gjeldende,
                                                                 HttpServletResponse response) {

        response.setHeader("Cache-Control", "no-cache");

        validateRequest(requestIdenter);

        Map<String, IdentinfoForAktoer> resultMap = new HashMap<>();

        Identinfo identinfo;
        if (AKTOERID_IDENTGRUPPE.equals(identgruppe)) {
            identinfo = new Identinfo(personIdentMock, PERSONIDENT_IDENTGRUPPE, true);
        } else {
            identinfo = new Identinfo(aktørIdMock, AKTOERID_IDENTGRUPPE, true);
        }

        //noinspection OptionalGetWithoutIsPresent
        resultMap.put(requestIdenter.stream().findFirst().get(), new IdentinfoForAktoer(List.of(identinfo), null)); //NOSONAR

        return resultMap;
    }

    private void validateRequest(Set<String> identer) {
        if (identer.isEmpty()) {
            throw new IllegalArgumentException("Ville kastet \"MissingIdenterException\"");
        } else if (identer.size() > NAV_IDENTER_MAX_SIZE) {
            throw new IllegalArgumentException("Ville kastet \"RequestIdenterMaxSizeException\"");
        }
    }
}
