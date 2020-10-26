package no.nav.pensjon.vtp.mocks.virksomhet.arbeidsfordeling.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import no.nav.pensjon.vtp.core.annotations.JaxrsResource;
import no.nav.pensjon.vtp.testmodell.enheter.EnheterIndeks;
import no.nav.pensjon.vtp.testmodell.enheter.Norg2Modell;

@JaxrsResource
@Api(tags = {"ArbeidsfordelingMock"})
@Path("/norg2/api/v1/arbeidsfordeling")
public class ArbeidsfordelingRestMock {

    private static final Logger LOG = LoggerFactory.getLogger(ArbeidsfordelingRestMock.class);
    private static final String LOG_PREFIX = "Arbeidsfordeling Rest kall til {}";
    private final EnheterIndeks enheterIndeks;

    public ArbeidsfordelingRestMock(EnheterIndeks enheterIndeks) {
        this.enheterIndeks = enheterIndeks;
    }

    @POST
    @Path("/enheter")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "allenheter", notes = ("Returnerer enheter fra NORG2"))
    public ArbeidsfordelingResponse[] hentAlleEnheter(ArbeidsfordelingRequest request) {
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

    @POST
    @Path("/enheter/bestmatch")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "finnenhet", notes = ("Returnerer  enheter fra NORG2"))
    public ArbeidsfordelingResponse[] finnEnhet(ArbeidsfordelingRequest request) {
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
