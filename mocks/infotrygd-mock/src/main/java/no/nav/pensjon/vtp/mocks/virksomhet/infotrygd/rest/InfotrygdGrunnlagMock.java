package no.nav.pensjon.vtp.mocks.virksomhet.infotrygd.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks;
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.pensjon.vtp.testmodell.inntektytelse.trex.Grunnlag;
import no.nav.pensjon.vtp.testmodell.inntektytelse.trex.TRexModell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = {"Infotrygdmock/grunnlag"})
@RequestMapping("/infotrygd/grunnlag")
public class InfotrygdGrunnlagMock {
    private static final Logger LOG = LoggerFactory.getLogger(InfotrygdGrunnlagMock.class);
    private static final String LOG_PREFIX = "InfotrygdGrunnlag Rest kall til {}";

    private final InntektYtelseIndeks inntektYtelseIndeks;

    public InfotrygdGrunnlagMock(InntektYtelseIndeks inntektYtelseIndeks) {
        this.inntektYtelseIndeks = inntektYtelseIndeks;
    }

    @GetMapping(value = "/foreldrepenger", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "foreldrepenger", notes = ("Returnerer foreldrepenger fra Infotrygd"))
    public Grunnlag[] getForeldrepenger(@RequestParam("fnr") String fnr,
                                        @RequestParam("fom") String fom,
                                        @RequestParam("tom") String tom) {
        LOG.info(LOG_PREFIX, "foreldrepenger");
        List<Grunnlag> tomresponse = new ArrayList<>();
        return inntektYtelseIndeks.getInntektYtelseModell(fnr)
                .map(InntektYtelseModell::gettRexModell)
                .map(TRexModell::getForeldrepenger).orElse(tomresponse)
                .toArray(Grunnlag[]::new);
    }

    @GetMapping(value = "/svangerskapspenger", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "foreldrepenger", notes = ("Returnerer svangerskapspenger fra Infotrygd"))
    public Grunnlag[] getSvangerskapspenger(@RequestParam("fnr") String fnr,
                                        @RequestParam("fom") String fom,
                                        @RequestParam("tom") String tom) {
        LOG.info(LOG_PREFIX, "svangerskapspenger");
        List<Grunnlag> tomresponse = new ArrayList<>();
        return inntektYtelseIndeks.getInntektYtelseModell(fnr)
                .map(InntektYtelseModell::gettRexModell)
                .map(TRexModell::getSvangerskapspenger).orElse(tomresponse)
                .toArray(Grunnlag[]::new);
    }

    @GetMapping(value = "/sykepenger", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "foreldrepenger", notes = ("Returnerer sykepenger fra Infotrygd"))
    public Grunnlag[] getSykepenger(@RequestParam("fnr") String fnr,
                                            @RequestParam("fom") String fom,
                                            @RequestParam("tom") String tom) {
        LOG.info(LOG_PREFIX, "sykepenger");
        List<Grunnlag> tomresponse = new ArrayList<>();
        return inntektYtelseIndeks.getInntektYtelseModell(fnr)
                .map(InntektYtelseModell::gettRexModell)
                .map(TRexModell::getSykepenger).orElse(tomresponse)
                .toArray(Grunnlag[]::new);
    }

    @GetMapping(value = "/paaroerende-sykdom", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "foreldrepenger", notes = ("Returnerer barns sykdom fra Infotrygd"))
    public Grunnlag[] getPaaroerendeSykdom(@RequestParam("fnr") String fnr,
                                    @RequestParam("fom") String fom,
                                    @RequestParam("tom") String tom) {
        LOG.info(LOG_PREFIX, "pårørendesykdom");
        List<Grunnlag> tomresponse = new ArrayList<>();
        return inntektYtelseIndeks.getInntektYtelseModell(fnr)
                .map(InntektYtelseModell::gettRexModell)
                .map(TRexModell::getBarnsykdom).orElse(tomresponse)
                .toArray(Grunnlag[]::new);
    }

}
