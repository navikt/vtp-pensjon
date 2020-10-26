package no.nav.pensjon.vtp.mocks.virksomhet.inntekt.hentinntektlistebolk;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import no.nav.pensjon.vtp.core.annotations.JaxrsResource;
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks;
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.pensjon.vtp.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell;
import no.nav.tjenester.aordningen.inntektsinformasjon.Aktoer;
import no.nav.tjenester.aordningen.inntektsinformasjon.ArbeidsInntektIdent;
import no.nav.tjenester.aordningen.inntektsinformasjon.request.HentInntektListeBolkRequest;
import no.nav.tjenester.aordningen.inntektsinformasjon.response.HentInntektListeBolkResponse;
import no.nav.pensjon.vtp.mocks.virksomhet.inntekt.hentinntektlistebolk.modell.HentInntektlisteBolkMapperRest;

@JaxrsResource
@Api("/inntektskomponenten-ws/rs/api/v1/hentinntektlistebolk")
@Path("/inntektskomponenten-ws/rs/api/v1/hentinntektlistebolk")
public class HentInntektlisteBolkREST {
    private static final Logger LOG = LoggerFactory.getLogger(HentInntektlisteBolkREST.class);

    private final InntektYtelseIndeks inntektYtelseIndeks;

    public HentInntektlisteBolkREST(InntektYtelseIndeks inntektYtelseIndeks) {
        this.inntektYtelseIndeks = inntektYtelseIndeks;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "HentInntektlisteBolk", notes = ("Returnerer inntektliste fra Inntektskomponenten"))
    public HentInntektListeBolkResponse hentInntektlisteBolk(HentInntektListeBolkRequest request){

        List<Aktoer> identListe = request.getIdentListe();
        LOG.info("Henter inntekter for personer: {}", identListe.stream().map(Aktoer::getIdentifikator).collect(Collectors.joining(",")));

        YearMonth fom = request.getMaanedFom() != null ? request.getMaanedFom() : YearMonth.of(1990,1);
        YearMonth tom = request.getMaanedTom() != null ? request.getMaanedTom() : YearMonth.of(1990,1);
        request.getMaanedTom();

        HentInntektListeBolkResponse response = new HentInntektListeBolkResponse();
        response.setArbeidsInntektIdentListe(new ArrayList<>());

        for(Aktoer aktoer : identListe){
            Optional<InntektYtelseModell> inntektYtelseModell = inntektYtelseIndeks.getInntektYtelseModellFraAktørId(aktoer.getIdentifikator());
            if(inntektYtelseModell.isPresent()) {
                InntektskomponentModell inntektskomponentModell = inntektYtelseModell.get().getInntektskomponentModell();
                ArbeidsInntektIdent arbeidsInntektIdent = HentInntektlisteBolkMapperRest.makeArbeidsInntektIdent(
                        inntektskomponentModell
                        , aktoer
                        , fom
                        , tom);
                response.getArbeidsInntektIdentListe().add(arbeidsInntektIdent);
            }

        }

        return response;

    }

}