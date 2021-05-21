package no.nav.pensjon.vtp.mocks.oppgave.rest

import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Api(tags = ["Oppgave Mock"])
@RequestMapping(
    value = ["/rest/oppgave/api/v1/oppgaver"]
)
class OppgaveMockImpl {
    private val oppgaver = mutableListOf<Oppgave>()
    private var oppgaveId = 1L

    private val oppfolginger = mutableListOf<Oppfolging>()
    private var oppfolgingId = 1L

    @PostMapping
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

    @PutMapping("/{oppgaveid}")
    @ApiOperation(value = "Oppdater oppgave")
    @ApiImplicitParams(
        ApiImplicitParam(
            name = "X-Correlation-ID",
            required = true,
            dataType = "string",
            paramType = "header"
        ),
        ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header")
    )
    fun oppdaterOppgave(
        @PathVariable("oppgaveid") oppgaveId: Long,
        @RequestBody oppgave: Oppgave,
        @RequestHeader httpHeaders: HttpHeaders?
    ): ResponseEntity<*> {
        oppgaver.removeIf { it.id == oppgaveId }
        oppgaver.add(oppgave)
        return ResponseEntity.status(HttpStatus.OK).build<Any>()
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
    @ApiOperation(value = "Hent oppgave", response = Oppgave::class)
    @ApiImplicitParams(
        ApiImplicitParam(
            name = "X-Correlation-ID",
            required = true,
            dataType = "string",
            paramType = "header"
        ),
        ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header")
    )
    fun hentOppgave(
        @RequestHeader httpHeaders: HttpHeaders?,
        @PathVariable("oppgaveid") oppgaveId: Long
    ) =
        oppgaver.firstOrNull { it.id == oppgaveId }

    @PatchMapping
    @ApiOperation(value = "Ferdigstille oppgaver", response = FerdigstillOppgaverResponse::class)
    @ApiImplicitParams(
        ApiImplicitParam(
            name = "X-Correlation-ID",
            required = true,
            dataType = "string",
            paramType = "header"
        ),
        ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header")
    )
    fun bulkUpdateOppgaver(): FerdigstillOppgaverResponse {
        val antallOppdaterte = oppgaver.size
        // Pesys benytter dette bare til å ferdigstille oppgaver
        oppgaver.clear()
        oppfolginger.clear()
        return FerdigstillOppgaverResponse(suksess = antallOppdaterte, totalt = antallOppdaterte)
    }

    @GetMapping("/{oppgaveid}/oppfolging")
    @ApiOperation(value = "Hent oppfølginger for oppgave")
    @ApiImplicitParams(
        ApiImplicitParam(
            name = "X-Correlation-ID",
            required = true,
            dataType = "string",
            paramType = "header"
        ),
        ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header")
    )
    fun hentOppfolginger(
        @PathVariable("oppgaveid") oppgaveId: Long
    ): ResponseEntity<*> {
        val oppfolginger = oppfolginger.filter { it.oppgaveId == oppgaveId }
        return ResponseEntity.status(HttpStatus.OK).body(oppfolginger)
    }

    @PostMapping("/{oppgaveid}/oppfolging")
    @ApiOperation(value = "Opprett oppfølging")
    @ApiImplicitParams(
        ApiImplicitParam(
            name = "X-Correlation-ID",
            required = true,
            dataType = "string",
            paramType = "header"
        ),
        ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header")
    )
    fun opprettOppfolging(
        @PathVariable("oppgaveid") oppgaveId: Long,
        @RequestBody oppfolging: Oppfolging,
        @RequestHeader httpHeaders: HttpHeaders?
    ): ResponseEntity<*> {
        oppfolging.id = oppfolgingId++
        oppfolginger.add(oppfolging.apply { this.oppgaveId = oppgaveId })
        return ResponseEntity.status(HttpStatus.CREATED).body(oppfolging)
    }

    @PutMapping("/{oppgaveid}/oppfolging/{oppfolgingid}")
    @ApiOperation(value = "Oppdater oppfølging")
    @ApiImplicitParams(
        ApiImplicitParam(
            name = "X-Correlation-ID",
            required = true,
            dataType = "string",
            paramType = "header"
        ),
        ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header")
    )
    fun oppdaterOppfolging(
        @PathVariable("oppgaveid") oppgaveId: Long,
        @RequestBody oppfolging: Oppfolging,
        @RequestHeader httpHeaders: HttpHeaders?
    ) {
        oppfolginger.removeIf { e -> e.id == oppfolging.id }
        oppfolginger.add(oppfolging.apply { this.oppgaveId = oppgaveId })
    }
}
