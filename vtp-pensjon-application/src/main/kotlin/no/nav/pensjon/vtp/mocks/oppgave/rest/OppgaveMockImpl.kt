package no.nav.pensjon.vtp.mocks.oppgave.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "Oppgave Mock")
@RequestMapping(
    value = ["/rest/oppgave/api/v1/oppgaver"]
)
class OppgaveMockImpl {
    private val oppgaver = mutableListOf<Oppgave>()
    private var oppgaveId = 1L

    private val oppfolginger = mutableListOf<Oppfolging>()
    private var oppfolgingId = 1L

    @PostMapping
    @Operation(summary = "Opprett oppgave")
    @Parameters(
        Parameter(
            name = "X-Correlation-ID",
            required = true,
        ),
        Parameter(name = "Authorization", required = true)
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
    @Operation(summary = "Oppdater oppgave")
    @Parameters(
        Parameter(
            name = "X-Correlation-ID",
            required = true,
        ),
        Parameter(name = "Authorization", required = true)
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
    @Operation(summary = "Hent oppgaver")
    @Parameters(
        Parameter(
            name = "X-Correlation-ID",
            required = true,
        ),
        Parameter(name = "Authorization", required = true),
        Parameter(name = "tema"),
        Parameter(name = "oppgavetype"),
        Parameter(name = "journalpostId"),
        Parameter(name = "aktoerId")
    )
    fun hentOppgaver(@RequestHeader httpHeaders: HttpHeaders?) =
        HentOppgaverResponse(antallTreffTotalt = oppgaver.size, oppgaver = oppgaver)

    @GetMapping("/{oppgaveid}")
    @Operation(summary = "Hent oppgave")
    @Parameters(
        Parameter(
            name = "X-Correlation-ID",
            required = true,
        ),
        Parameter(name = "Authorization", required = true)
    )
    fun hentOppgave(
        @RequestHeader httpHeaders: HttpHeaders?,
        @PathVariable("oppgaveid") oppgaveId: Long
    ) =
        oppgaver.firstOrNull { it.id == oppgaveId }

    @PatchMapping
    @Operation(summary = "Ferdigstille oppgaver")
    @Parameters(
        Parameter(
            name = "X-Correlation-ID",
            required = true,
        ),
        Parameter(name = "Authorization", required = true)
    )
    fun bulkUpdateOppgaver(): FerdigstillOppgaverResponse {
        val antallOppdaterte = oppgaver.size
        // Pesys benytter dette bare til å ferdigstille oppgaver
        oppgaver.clear()
        oppfolginger.clear()
        return FerdigstillOppgaverResponse(suksess = antallOppdaterte, totalt = antallOppdaterte)
    }

    @GetMapping("/{oppgaveid}/oppfolging")
    @Operation(summary = "Hent oppfølginger for oppgave")
    @Parameters(
        Parameter(
            name = "X-Correlation-ID",
            required = true,
        ),
        Parameter(name = "Authorization", required = true)
    )
    fun hentOppfolginger(
        @PathVariable("oppgaveid") oppgaveId: Long
    ): ResponseEntity<*> {
        val oppfolginger = oppfolginger.filter { it.oppgaveId == oppgaveId }
        return ResponseEntity.status(HttpStatus.OK).body(oppfolginger)
    }

    @PostMapping("/{oppgaveid}/oppfolging")
    @Operation(summary = "Opprett oppfølging")
    @Parameters(
        Parameter(
            name = "X-Correlation-ID",
            required = true,
        ),
        Parameter(name = "Authorization", required = true)
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
    @Operation(summary = "Oppdater oppfølging")
    @Parameters(
        Parameter(
            name = "X-Correlation-ID",
            required = true,
        ),
        Parameter(name = "Authorization", required = true)
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
