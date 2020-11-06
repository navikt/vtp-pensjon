package no.nav.pensjon.vtp.mocks.virksomhet.arbeidsfordeling.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.pensjon.vtp.testmodell.enheter.EnheterIndeks;
import no.nav.pensjon.vtp.testmodell.enheter.Norg2Modell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = {"ArbeidsfordelingMock"})
@RequestMapping("/rest/norg2/api/v1/arbeidsfordeling")
public class ArbeidsfordelingRestMock {

    private static final Logger LOG = LoggerFactory.getLogger(ArbeidsfordelingRestMock.class);
    private static final String LOG_PREFIX = "Arbeidsfordeling Rest kall til {}";
    private final EnheterIndeks enheterIndeks;

    public ArbeidsfordelingRestMock(EnheterIndeks enheterIndeks) {
        this.enheterIndeks = enheterIndeks;
    }

    @PostMapping(value = "/enheter", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "allenheter", notes = ("Returnerer enheter fra NORG2"))
    public ArbeidsfordelingResponse[] hentAlleEnheter(@RequestBody ArbeidsfordelingRequest request) {
        LOG.info(LOG_PREFIX, "allenheter");
        return enheterIndeks.getAlleEnheter().stream()
                .filter(e -> skalEnhetMed(e, request.getTema()))
                .map(e -> new ArbeidsfordelingResponse(e.getEnhetId(), e.getNavn(), e.getStatus(), e.getType()))
                .toArray(ArbeidsfordelingResponse[]::new);
    }

    private boolean skalEnhetMed(Norg2Modell enhet, String tema) {
        if (tema == null) return true;
        if ("FOR".equalsIgnoreCase(tema) && "YTA".equalsIgnoreCase(enhet.getType())) return false;
        if ("OMS".equalsIgnoreCase(tema) && "FPY".equalsIgnoreCase(enhet.getType())) return false;
        return true;
    }

    @PostMapping(value = "/enheter/bestmatch", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "finnenhet", notes = ("Returnerer  enheter fra NORG2"))
    public ArbeidsfordelingResponse[] finnEnhet(@RequestBody ArbeidsfordelingRequest request) {
        LOG.info(LOG_PREFIX, "bestmatch");
        List<String> spesielleDiskrKoder = List.of("UFB", "SPSF", "SPFO");
        List<Norg2Modell> resultat = new ArrayList<>();
        if (request.getDiskresjonskode() != null && spesielleDiskrKoder.contains(request.getDiskresjonskode())) {
            resultat.add(enheterIndeks.finnByDiskresjonskode(request.getDiskresjonskode()));
        } else {
            resultat.add(enheterIndeks.finnByDiskresjonskode("NORMAL-"+request.getTema()));
        }
        return resultat.stream()
                .map(e -> new ArbeidsfordelingResponse(e.getEnhetId(), e.getNavn(), e.getStatus(), e.getType()))
                .toArray(ArbeidsfordelingResponse[]::new);
    }

}
