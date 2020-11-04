package no.nav.pensjon.vtp.mocks.oppgave.rest;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@Api(tags = "Oppgave Mock")
@RequestMapping(value = "oppgave/api/v1/oppgaver",
        produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
        consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
public class OppgaveMockImpl {

    private static final Logger LOG = LoggerFactory.getLogger(OppgaveMockImpl.class);

    private static final Map<Long, ObjectNode> oppgaver = new ConcurrentHashMap<>();

    @PostMapping
    @ApiOperation(value = "Opprett oppgave", response = OppgaveJson.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-Correlation-ID", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header")

    })
    public ResponseEntity opprettOppgave(
            @Valid @ApiParam(value = "Oppgaven som opprettes", required = true) ObjectNode oppgave,
            HttpHeaders httpHeaders) {
        Long id = (long) (oppgaver.size() + 1);
        oppgave.put("id", id);

        oppgaver.put(id, oppgave);

        LOG.info("Opprettet oppgave: {}", oppgave);

        return ResponseEntity.status(HttpStatus.CREATED).body(oppgave);
    }

    @GetMapping
    @ApiOperation(value = "Hent oppgaver", response = HentOppgaverResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-Correlation-ID", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "tema", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "oppgavetype", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "journalpostId", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "aktoerId", dataType = "string", paramType = "query")
    })
    public ResponseEntity hentOppgaver(HttpHeaders httpHeaders) {
        return ResponseEntity.ok(new HentOppgaverResponse(new ArrayList<>(oppgaver.values())));
    }
}
