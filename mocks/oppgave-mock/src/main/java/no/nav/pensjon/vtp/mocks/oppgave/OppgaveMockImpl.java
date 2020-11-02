package no.nav.pensjon.vtp.mocks.oppgave;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

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
            @Context HttpHeaders httpHeaders) {
        Optional<ResponseEntity> validert = validerIkkeFunksjonelt(httpHeaders);
        if (validert.isPresent()) {
            return validert.get();
        }

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
    public ResponseEntity hentOppgaver(
            @Context HttpHeaders httpHeaders,
            @Context UriInfo uriInfo) {
        Optional<ResponseEntity> validert = validerIkkeFunksjonelt(httpHeaders);
        if (validert.isPresent()) {
            return validert.get();
        }

        MultivaluedMap<String, String> queries = uriInfo.getQueryParameters();

        List<ObjectNode> matching = new ArrayList<>();

        oppgaver.forEach((id, oppgave) -> {
            AtomicInteger matches = new AtomicInteger();
            queries.forEach((queryKey, queryValues) -> {
                if (oppgave.hasNonNull(queryKey) && !queryValues.isEmpty()) {
                    JsonNode oppgaveValue = oppgave.get(queryKey);
                    String firstQueryValue = queryValues.get(0);
                    switch (oppgaveValue.getNodeType()) {
                        case STRING:
                            if (oppgaveValue.asText().equals(firstQueryValue)) matches.getAndIncrement();
                            break;
                        case NUMBER:
                            if (oppgaveValue.asLong() == Long.parseLong(firstQueryValue)) matches.getAndIncrement();
                            break;
                        case BOOLEAN:
                            if (oppgaveValue.asBoolean() == firstQueryValue.equals("true")) matches.getAndIncrement();
                            break;
                        default:
                            throw new IllegalStateException("Ikke støttet NodeType " + oppgaveValue.getNodeType().name());
                    }
                }
            });
            if (matches.get() == queries.size()) matching.add(oppgave);
        });

        return ResponseEntity.ok(new HentOppgaverResponse(matching));
    }

    private Optional<ResponseEntity> validerIkkeFunksjonelt(HttpHeaders httpHeaders) {
        // Validerer token på rett format
        String jwt = httpHeaders.getHeaderString("Authorization");
        if (jwt == null || !jwt.startsWith("Bearer")) {
            LOG.error("Ugyldig/manglende Authorization header");
            return Optional.of(ResponseEntity.badRequest().build());

        }
        // Caliderer at correlation id er satt
        String correlationId = httpHeaders.getHeaderString("X-Correlation-ID");
        if (correlationId == null || correlationId.isBlank()) {
            return Optional.of(ResponseEntity.status(HttpStatus.BAD_GATEWAY).build());
        }

        return Optional.empty();
    }
}
