package no.nav.pensjon.vtp.mocks.sigrun;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import no.nav.pensjon.vtp.core.annotations.JaxrsResource;
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks;
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.pensjon.vtp.testmodell.inntektytelse.sigrun.Inntektsår;
import no.nav.pensjon.vtp.testmodell.inntektytelse.sigrun.Oppføring;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonIndeks;

@JaxrsResource
@Api(tags = {"Sigrun/beregnetskatt"})
@Path("/api/beregnetskatt")
public class SigrunMock {
    private static final Logger LOG = LoggerFactory.getLogger(SigrunMock.class);

    private final InntektYtelseIndeks inntektYtelseIndeks;
    private final PersonIndeks personIndeks;

    public SigrunMock(InntektYtelseIndeks inntektYtelseIndeks, PersonIndeks personIndeks) {
        this.inntektYtelseIndeks = inntektYtelseIndeks;
        this.personIndeks = personIndeks;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "beregnetskatt", notes = ("Returnerer beregnetskatt fra Sigrun"))
    public Response buildPermitResponse(@Context UriInfo ui, @Context HttpHeaders httpHeaders) {

        String brukerFnr = null;
        String inntektsAar = null;
        String aktørId = null;

        if (httpHeaders.getRequestHeader("x-naturligident").size() > 0) {
            brukerFnr = httpHeaders.getRequestHeader("x-naturligident").get(0);
        }
        if (httpHeaders.getRequestHeader("x-inntektsaar").size() > 0) {
            inntektsAar = httpHeaders.getRequestHeader("x-inntektsaar").get(0);
        }
        if (httpHeaders.getRequestHeader("x-aktoerid").size() > 0) {
            aktørId = httpHeaders.getRequestHeader("x-aktoerid").get(0);
        }

        LOG.info("Sigrun for aktørId: {} ", aktørId);

        if (brukerFnr == null && aktørId != null) {
            brukerFnr = personIndeks.finnByAktørIdent(aktørId).getIdent();
        } else if (brukerFnr == null) {
            LOG.info("sigrun. fnr eller aktoerid må være oppgitt.");
            return Response.status(400, "Kan ikke ha tomt felt for både aktoerid og naturligident.").build();
        } else if (inntektsAar == null) {
            LOG.info("sigrun. Inntektsår må være oppgitt.");
            Response.status(404, "Det forespurte inntektsåret er ikke støttet.");
        }

        Optional<InntektYtelseModell> inntektYtelseModell = inntektYtelseIndeks.getInntektYtelseModell(brukerFnr);
        String response;

        if (inntektYtelseModell.isPresent() && !inntektYtelseModell.get().getSigrunModell().getInntektsår().isEmpty()) {
            List<Inntektsår> inntektsår = inntektYtelseModell.get().getSigrunModell().getInntektsår();
            String test = inntektsår.get(0).getOppføring().stream().map(Oppføring::toString).collect(Collectors.joining(",\n"));

            response = String.format("[\n%s\n]", test);
        } else if (inntektYtelseModell.isEmpty()){
            LOG.info("Kunne ikke finne bruker.");
            return Response.status(404,"Kunne ikke finne bruker").build();
        } else {
            LOG.info("Kunne ikke finne inntektsår");
            return Response.status(404,"Kunne ikke finne inntektsår").build();
        }
        return Response.status(200).entity(response).build();
    }

}