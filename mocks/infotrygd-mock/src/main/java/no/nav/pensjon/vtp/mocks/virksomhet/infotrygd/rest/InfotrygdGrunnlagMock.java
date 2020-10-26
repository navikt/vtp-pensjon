package no.nav.pensjon.vtp.mocks.virksomhet.infotrygd.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import no.nav.pensjon.vtp.core.annotations.JaxrsResource;
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks;
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.pensjon.vtp.testmodell.inntektytelse.trex.Grunnlag;
import no.nav.pensjon.vtp.testmodell.inntektytelse.trex.TRexModell;

@JaxrsResource
@Api(tags = {"Infotrygdmock/grunnlag"})
@Path("/infotrygd/grunnlag")
public class InfotrygdGrunnlagMock {
    private static final Logger LOG = LoggerFactory.getLogger(InfotrygdGrunnlagMock.class);
    private static final String LOG_PREFIX = "InfotrygdGrunnlag Rest kall til {}";

    private final InntektYtelseIndeks inntektYtelseIndeks;

    public InfotrygdGrunnlagMock(InntektYtelseIndeks inntektYtelseIndeks) {
        this.inntektYtelseIndeks = inntektYtelseIndeks;
    }

    @GET
    @Path("/foreldrepenger")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "foreldrepenger", notes = ("Returnerer foreldrepenger fra Infotrygd"))
    public Grunnlag[] getForeldrepenger(@QueryParam("fnr") String fnr,
                                        @QueryParam("fom") String fom,
                                        @QueryParam("tom") String tom) {
        LOG.info(LOG_PREFIX, "foreldrepenger");
        List<Grunnlag> tomresponse = new ArrayList<>();
        return inntektYtelseIndeks.getInntektYtelseModell(fnr)
                .map(InntektYtelseModell::gettRexModell)
                .map(TRexModell::getForeldrepenger).orElse(tomresponse)
                .toArray(Grunnlag[]::new);
    }

    @GET
    @Path("/svangerskapspenger")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "foreldrepenger", notes = ("Returnerer svangerskapspenger fra Infotrygd"))
    public Grunnlag[] getSvangerskapspenger(@QueryParam("fnr") String fnr,
                                        @QueryParam("fom") String fom,
                                        @QueryParam("tom") String tom) {
        LOG.info(LOG_PREFIX, "svangerskapspenger");
        List<Grunnlag> tomresponse = new ArrayList<>();
        return inntektYtelseIndeks.getInntektYtelseModell(fnr)
                .map(InntektYtelseModell::gettRexModell)
                .map(TRexModell::getSvangerskapspenger).orElse(tomresponse)
                .toArray(Grunnlag[]::new);
    }

    @GET
    @Path("/sykepenger")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "foreldrepenger", notes = ("Returnerer sykepenger fra Infotrygd"))
    public Grunnlag[] getSykepenger(@QueryParam("fnr") String fnr,
                                            @QueryParam("fom") String fom,
                                            @QueryParam("tom") String tom) {
        LOG.info(LOG_PREFIX, "sykepenger");
        List<Grunnlag> tomresponse = new ArrayList<>();
        return inntektYtelseIndeks.getInntektYtelseModell(fnr)
                .map(InntektYtelseModell::gettRexModell)
                .map(TRexModell::getSykepenger).orElse(tomresponse)
                .toArray(Grunnlag[]::new);
    }

    @GET
    @Path("/paaroerende-sykdom")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "foreldrepenger", notes = ("Returnerer barns sykdom fra Infotrygd"))
    public Grunnlag[] getPaaroerendeSykdom(@QueryParam("fnr") String fnr,
                                    @QueryParam("fom") String fom,
                                    @QueryParam("tom") String tom) {
        LOG.info(LOG_PREFIX, "pårørendesykdom");
        List<Grunnlag> tomresponse = new ArrayList<>();
        return inntektYtelseIndeks.getInntektYtelseModell(fnr)
                .map(InntektYtelseModell::gettRexModell)
                .map(TRexModell::getBarnsykdom).orElse(tomresponse)
                .toArray(Grunnlag[]::new);
    }

}