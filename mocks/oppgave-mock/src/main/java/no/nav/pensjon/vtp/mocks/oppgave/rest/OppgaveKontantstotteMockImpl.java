package no.nav.pensjon.vtp.mocks.oppgave.rest;

import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "oppgave-kontantstotte/api/v1/oppgaver", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class OppgaveKontantstotteMockImpl {
    private static final Logger LOG = LoggerFactory.getLogger(OppgaveKontantstotteMockImpl.class);

    //TODO (TEAM FAMILIE) Lag mock-responser fra scenario NOSONAR
    private Oppgave.Builder oppgaveMock = new Oppgave.Builder()
            .medTema("KON")
            .medAktivDato(LocalDate.now())
            .medVersjon(1)
            .medStatus(Oppgavestatus.OPPRETTET)
            .medPrioritet(Prioritet.NORM)
            .medOppgavetype("BEH_SAK");

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Henter oppgave for en gitt id", response = OppgaveJson.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "X-Correlation-ID", required = true, dataType = "string", paramType = "header")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Konsument mangler gyldig token"),
            @ApiResponse(code = 403, message = "Bruker er ikke autorisert for denne operasjonen"),
            @ApiResponse(code = 404, message = "Det finnes ingen oppgave for angitt id"),
            @ApiResponse(code = 409, message = "Konflikt"),
            @ApiResponse(code = 500, message = "Ukjent feilsituasjon har oppstått i Oppgave")
    })
    public ResponseEntity hentOppgave(@PathVariable("id") Long id) {
        return ResponseEntity.ok(new OppgaveJson(oppgaveMock.medId(id).build()));
    }


    @GetMapping()
    @ApiOperation(value = "Søk etter oppgaver", response = FinnOppgaveResponse.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "X-Correlation-ID", required = true, dataType = "string", paramType = "header")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Ugyldig input"),
            @ApiResponse(code = 401, message = "Konsument mangler gyldig token"),
            @ApiResponse(code = 403, message = "Bruker er ikke autorisert for denne operasjonen"),
            @ApiResponse(code = 500, message = "Ukjent feilsituasjon har oppstått i Oppgave")
    })
    public ResponseEntity finnOppgaver() {
        return ResponseEntity.ok()
                .body(new FinnOppgaveResponse(
                        List.of(new OppgaveJson(oppgaveMock.medId(123456789L).build())),
                        1));
    }


    @PutMapping("/{id}")
    @ApiOperation(value = "Endrer en eksisterende oppgave", response = OppgaveJson.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "X-Correlation-ID", required = true, dataType = "string", paramType = "header")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Oppgave endret", responseHeaders = @ResponseHeader(name = "location", description = "Angir URI til den endrede oppgaven")),
            @ApiResponse(code = 400, message = "Ugyldig input"),
            @ApiResponse(code = 401, message = "Konsument mangler gyldig token"),
            @ApiResponse(code = 403, message = "Bruker er ikke autorisert for denne operasjonen"),
            @ApiResponse(code = 409, message = "Konflikt"),
            @ApiResponse(code = 500, message = "Ukjent feilsituasjon har oppstått i Oppgave")
    })
    public ResponseEntity endreOppgave(@Valid @ApiParam(value = "Oppgaven som skal endres", required = true) OppgaveJson oppgaveJson,
                                 @PathVariable("id") Long id,
                                 @RequestParam("brukerId") @ApiParam(hidden = true) String brukerId) {

        LOG.info("Mottatt endringsrequest: {}", oppgaveJson);
        return ResponseEntity.ok(oppgaveJson);
    }
}
