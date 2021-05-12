package no.nav.pensjon.vtp.mocks.oppgave.rest

import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Api(tags = ["Oppgave Mock"])
@RequestMapping(
    value = ["/rest/oppgave/api/v1/oppgaver"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
)
class OppgaveMockImpl {
    private val oppgaver = mutableListOf<Oppgave>()
    private var oppgaveId = 1L

    @PutMapping
    @ApiOperation(value = "Opprett oppgave")
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
        @RequestBody oppgave: Oppgave,
        @RequestHeader httpHeaders: HttpHeaders?
    ): ResponseEntity<*> {
        oppgave.id = oppgaveId++
        oppgaver.add(oppgave)
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
        ApiImplicitParam(name = "tema", dataType = "string", paramType = "query", allowMultiple = true),
        ApiImplicitParam(name = "oppgavetype", dataType = "string", paramType = "query"),
        ApiImplicitParam(name = "journalpostId", dataType = "string", paramType = "query"),
        ApiImplicitParam(name = "aktoerId", dataType = "string", paramType = "query")
    )
    fun hentOppgaver(@RequestHeader httpHeaders: HttpHeaders?) =
        HentOppgaverResponse(antallTreffTotalt = oppgaver.size, oppgaver = oppgaver)

    @GetMapping("/{oppgaveid}")
    @ApiOperation(value = "Hent oppgave")
    fun hentOppgave(
        @PathVariable("oppgaveid") oppgaveId: Long
    ) = oppgaver.first { oppgave -> oppgave.id == oppgaveId }
}
