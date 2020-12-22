package no.nav.pensjon.vtp.mocks.oppgave.rest

import com.fasterxml.jackson.databind.node.ObjectNode
import io.swagger.annotations.*
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.concurrent.ConcurrentHashMap
import javax.validation.Valid

@RestController
@Api(tags = ["Oppgave Mock"])
@RequestMapping(
    value = ["oppgave/api/v1/oppgaver"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
)
class OppgaveMockImpl {
    private val oppgaver: MutableMap<Long, ObjectNode?> = ConcurrentHashMap()

    @PostMapping
    @ApiOperation(value = "Opprett oppgave", response = OppgaveJson::class)
    @ApiImplicitParams(
        ApiImplicitParam(
            name = "X-Correlation-ID",
            required = true,
            dataType = "string",
            paramType = "header"
        ),
        ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header")
    )
    fun opprettOppgave(
        @ApiParam(value = "Oppgaven som opprettes", required = true) oppgave: @Valid ObjectNode?,
        @RequestHeader httpHeaders: HttpHeaders?
    ): ResponseEntity<*> {
        val id = (oppgaver.size + 1).toLong()
        oppgave!!.put("id", id)
        oppgaver[id] = oppgave
        return ResponseEntity.status(HttpStatus.CREATED).body(oppgave)
    }

    @GetMapping
    @ApiOperation(value = "Hent oppgaver", response = HentOppgaverResponse::class)
    @ApiImplicitParams(
        ApiImplicitParam(
            name = "X-Correlation-ID",
            required = true,
            dataType = "string",
            paramType = "header"
        ),
        ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header"),
        ApiImplicitParam(name = "tema", dataType = "string", paramType = "query"),
        ApiImplicitParam(name = "oppgavetype", dataType = "string", paramType = "query"),
        ApiImplicitParam(name = "journalpostId", dataType = "string", paramType = "query"),
        ApiImplicitParam(name = "aktoerId", dataType = "string", paramType = "query")
    )
    fun hentOppgaver(httpHeaders: HttpHeaders?) =
        HentOppgaverResponse(oppgaver.values.toList())
}
