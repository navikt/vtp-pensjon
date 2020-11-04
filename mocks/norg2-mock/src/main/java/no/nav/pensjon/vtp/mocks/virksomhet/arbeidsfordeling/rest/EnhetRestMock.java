package no.nav.pensjon.vtp.mocks.virksomhet.arbeidsfordeling.rest;

import io.swagger.annotations.Api;
import no.nav.norg2.model.Norg2RsEnhet;
import no.nav.norg2.model.Norg2RsOrganisering;
import no.nav.norg2.model.Norg2RsSimpleEnhet;
import no.nav.pensjon.vtp.testmodell.enheter.EnheterIndeks;
import no.nav.pensjon.vtp.testmodell.enheter.Norg2Modell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Api(tags = {"Norg2 enheter"})
@RequestMapping("/rest/norg2/api/v1")
public class EnhetRestMock {
    private static final Logger LOG = LoggerFactory.getLogger(EnhetRestMock.class);
    private final EnheterIndeks enheterIndeks;

    public EnhetRestMock(EnheterIndeks enheterIndeks) {
        this.enheterIndeks = enheterIndeks;
    }

    @PostMapping(value = "/arbeidsfordelinger", produces = MediaType.APPLICATION_JSON_VALUE)
    @io.swagger.annotations.ApiOperation(value = "Returnerer alle arbeidsfordelinger basert på multiple set av søkekriterier", response = Fordeling.class, responseContainer = "List", tags = {"arbeidsfordeling",})
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Fordeling.class, responseContainer = "List"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Not found"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity getArbeidsFordelinger(List<Fordeling> fordelinger, @RequestParam("skjermet") boolean skjermet) {
        LOG.info("kall på /norg2/api/v1/arbeidsfordelinger med entitites:" + fordelinger);
        return ResponseEntity.ok(fordelinger);
    }

    @GetMapping(value = "/enhet", produces = MediaType.APPLICATION_JSON_VALUE)
    @io.swagger.annotations.ApiOperation(value = "Returnerer en filtrert liste av alle enheter", response = Norg2RsEnhet.class, responseContainer = "List", tags = {"enhet",})
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Norg2RsEnhet.class, responseContainer = "List"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Not found"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity getAllEnheterUsingGET(@RequestParam(value = "enhetStatusListe", required = false, defaultValue = "") List<String> enhetStatusListe, @RequestParam(value = "enhetsnummerListe", required = false, defaultValue = "") List<String> enhetsnummerListe, @RequestParam(value = "oppgavebehandlerFilter", required = false, defaultValue = "") String oppgavebehandlerFilter
            ) {
        List<Norg2RsEnhet> norg2RsEnheter = norg2RsEnheter(enheterIndeks.getAlleEnheter()).stream()
                .filter(e -> enhetsnummerListe.isEmpty() || enhetsnummerListe.contains(e.getEnhetNr()))
                .filter(e -> enhetStatusListe.isEmpty() || enhetStatusListe.contains(e.getStatus()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(norg2RsEnheter);
    }

    @GetMapping(value = "/enhet/navkontor/{geografiskOmraade}", produces = MediaType.APPLICATION_JSON_VALUE)
    @io.swagger.annotations.ApiOperation(value = "Returnerer en NAV kontor som er en lokalkontor for spesifisert geografisk område", response = Norg2RsEnhet.class, tags = {"enhet",})
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Norg2RsEnhet.class),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity getEnhetByGeografiskOmraadeUsingGET(@PathVariable("geografiskOmraade") String geografiskOmraade, @RequestParam("disk") String disk){
        LOG.info("kall på /norg2/api/v1/enhet/navkontor/ " + geografiskOmraade + " returnerer default enhet 4407");
        return ResponseEntity.ok(enEnhet(4407L));
    }

    @GetMapping(value = "/enhet/{enhetsnummer}", produces = MediaType.APPLICATION_JSON_VALUE)
    @io.swagger.annotations.ApiOperation(value = "Returnerer en enhet basert på enhetsnummer", response = Norg2RsEnhet.class, tags = {"enhet",})
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Norg2RsEnhet.class),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity getEnhetUsingGET(@PathVariable("enhetsnummer") String enhetsnummer){
            LOG.info("kall på /norg2/api/v1/enhet/" + enhetsnummer);

            Norg2RsEnhet norg2RsEnhet = norg2RsEnheter(enheterIndeks.getAlleEnheter()).stream()
                    .filter(e -> e.getEnhetNr().equalsIgnoreCase(enhetsnummer))
                    .findAny().orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Could not find enhet with id " + enhetsnummer));
            return ResponseEntity.ok(norg2RsEnhet);
    }

    @GetMapping(value = "/enhet/{enhetsnummer}/organisering", produces = MediaType.APPLICATION_JSON_VALUE)
    @io.swagger.annotations.ApiOperation(value = "Returnerer en organiseringsliste for enhetsnummer", response = Norg2RsOrganisering[].class, tags = {"enhet",})
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Norg2RsOrganisering[].class),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),
            @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity getOrganiseringListeForEnhet(@PathVariable("enhetsnummer") String enhetsnummer){
        LOG.info(String.format("kall på /norg2/api/v1/enhet/%s/organisering", enhetsnummer));

        Norg2RsEnhet norg2RsEnhet = norg2RsEnheter(enheterIndeks.getAlleEnheter()).stream()
                .filter(e -> e.getEnhetNr().equalsIgnoreCase(enhetsnummer))
                .findAny().orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Could not find enhet with id " + enhetsnummer));

        Norg2RsOrganisering organisering = new Norg2RsOrganisering();
        Norg2RsSimpleEnhet organisererEnhet = new Norg2RsSimpleEnhet();
        organisererEnhet.setId(norg2RsEnhet.getEnhetId());
        organisererEnhet.setNavn(norg2RsEnhet.getNavn());
        organisererEnhet.setNr(norg2RsEnhet.getEnhetNr());
        organisererEnhet.setGyldigFra(Optional.ofNullable(norg2RsEnhet.getAktiveringsdato()).orElse(LocalDate.of(1950, 1, 1).format(DateTimeFormatter.ISO_LOCAL_DATE)));

        Norg2RsSimpleEnhet organisertUnderEnhet = new Norg2RsSimpleEnhet();
        organisertUnderEnhet.setId(99768L);
        organisertUnderEnhet.setNavn("Enhet over " + organisererEnhet.getNavn());
        organisertUnderEnhet.setNr("99768");
        organisertUnderEnhet.setGyldigFra(organisererEnhet.getGyldigFra());

        organisering.setOrganiserer(organisererEnhet);
        organisering.setOrganisertUnder(organisertUnderEnhet);
        organisering.setFra(Optional.ofNullable(organisererEnhet.getGyldigFra()).orElse(LocalDate.of(1950, 1, 1).format(DateTimeFormatter.ISO_LOCAL_DATE)));
        organisering.setTil(LocalDate.of(2036, 3, 18).format(DateTimeFormatter.ISO_LOCAL_DATE));
        organisering.setId(123456123456L);
        organisering.setOrgType("FORV");

        return ResponseEntity.ok(Collections.singletonList(organisering));
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
