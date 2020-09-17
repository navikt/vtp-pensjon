package no.nav.tjeneste.virksomhet.arbeidsfordeling.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import no.nav.foreldrepenger.vtp.testmodell.enheter.Norg2Modell;
import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.foreldrepenger.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.norg2.model.Norg2RsEnhet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"Norg2 enheter"})
@Path("/norg2/api/v1/enhet")
public class EnhetRestMock {
    private static final Logger LOG = LoggerFactory.getLogger(EnhetRestMock.class);
    private TestscenarioBuilderRepository scenarioRepository = TestscenarioRepositoryImpl.getInstance(BasisdataProviderFileImpl.getInstance());

    public EnhetRestMock() throws IOException {
    }

    @GET
    @Produces({"application/json;charset=UTF-8"})
    @io.swagger.annotations.ApiOperation(value = "Returnerer en filtrert liste av alle enheter", response = Norg2RsEnhet.class, responseContainer = "List", tags = {"enhet",})
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Norg2RsEnhet.class, responseContainer = "List"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Not found"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error")})
    public Response getAllEnheterUsingGET(@ApiParam(value = "Enhetsstatus resulterende enheter skal filtreres på", allowableValues = "UNDER_ETABLERING, AKTIV, UNDER_AVVIKLING, NEDLAGT") @QueryParam("enhetStatusListe") List<String> enhetStatusListe
            , @ApiParam(value = "Enhetsnumre for filtrering av enheter") @QueryParam("enhetsnummerListe") List<String> enhetsnummerListe
            , @ApiParam(value = "Hvorvidt enheter skal være oppgavebehandlende", allowableValues = "UFILTRERT, KUN_OPPGAVEBEHANDLERE, INGEN_OPPGAVEBEHANDLERE") @QueryParam("oppgavebehandlerFilter") String oppgavebehandlerFilter
            , @Context SecurityContext securityContext)
            throws NotFoundException {
        LOG.info("kall på /norg2/api/v1/enhet med enhetIDs:" + enhetsnummerListe.parallelStream().collect(Collectors.joining(",")));

        List<Norg2RsEnhet> norg2RsEnheter = norg2RsEnheter(scenarioRepository.getEnheterIndeks().getAlleEnheter()).stream()
                .filter(e -> enhetsnummerListe.isEmpty() || enhetsnummerListe.contains(e.getEnhetNr()))
                .filter(e -> enhetStatusListe.isEmpty() || enhetStatusListe.contains(e.getStatus()))
                .collect(Collectors.toList());

        return Response.ok(norg2RsEnheter).build();
    }

    @GET
    @Path("/navkontor/{geografiskOmraade}")
    @Produces({"application/json;charset=UTF-8"})
    @io.swagger.annotations.ApiOperation(value = "Returnerer en NAV kontor som er en lokalkontor for spesifisert geografisk område", response = Norg2RsEnhet.class, tags = {"enhet",})
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Norg2RsEnhet.class),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error")})
    public Response getEnhetByGeografiskOmraadeUsingGET(@ApiParam(value = "Geografisk identifikator for NAV kontoret", required = true) @PathParam("geografiskOmraade") String geografiskOmraade
            , @ApiParam(value = "Diskresjonskode på saker et NAV kontor kan behandle", allowableValues = "SPFO, SPSF, ANY") @QueryParam("disk") String disk
            , @Context SecurityContext securityContext)
            throws NotFoundException {
        LOG.info("kall på /norg2/api/v1/enhet/navkontor/ " + geografiskOmraade + " returnerer default enhet 4407");
        return Response.ok(enEnhet(4407L)).build();
    }

    @GET
    @Path("/{enhetsnummer}")
    @Produces({"application/json;charset=UTF-8"})
    @io.swagger.annotations.ApiOperation(value = "Returnerer en enhet basert på enhetsnummer", response = Norg2RsEnhet.class, tags = {"enhet",})
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Norg2RsEnhet.class),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error")})
    public Response getEnhetUsingGET(@ApiParam(value = "Enhetsnummeret til enheten oppslaget gjelder for", required = true) @PathParam("enhetsnummer") String enhetsnummer,
                                     @Context SecurityContext securityContext) throws NotFoundException {
            LOG.info("kall på /norg2/api/v1/enhet/" + enhetsnummer);

            Norg2RsEnhet norg2RsEnhet = norg2RsEnheter(scenarioRepository.getEnheterIndeks().getAlleEnheter()).stream()
                    .filter(e -> e.getEnhetNr().equalsIgnoreCase(enhetsnummer))
                    .findAny().orElseThrow(() -> new InvalidParameterException("Could not find enhet with id " + enhetsnummer));
            return Response.ok(norg2RsEnhet).build();
    }

    private List<Norg2RsEnhet> norg2RsEnheter(Collection<Norg2Modell> enheter){
        return enheter.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    private Norg2RsEnhet convert(Norg2Modell modell) {
        Norg2RsEnhet norg2RsEnhet = new Norg2RsEnhet();
        norg2RsEnhet.setEnhetNr(modell.getEnhetId());
        norg2RsEnhet.setEnhetNr(modell.getEnhetId());
        norg2RsEnhet.setNavn(modell.getNavn());
        norg2RsEnhet.setStatus(modell.getStatus());
        norg2RsEnhet.setType(modell.getType());
        norg2RsEnhet.setOppgavebehandler(true);
        return norg2RsEnhet;
    }

    private Norg2RsEnhet enEnhet(long enhetId) {
        Norg2RsEnhet norg2RsEnhet = new Norg2RsEnhet();
        norg2RsEnhet.setEnhetId(enhetId);
        norg2RsEnhet.setEnhetNr(String.valueOf(enhetId));
        norg2RsEnhet.setNavn("NAV Arbeid og ytelser Tønsberg");
        norg2RsEnhet.setOppgavebehandler(true);
        return norg2RsEnhet;
    }
}
