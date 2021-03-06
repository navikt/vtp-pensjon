package no.nav.pensjon.vtp.mocks.oppgave.rest

import io.swagger.annotations.Api
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(tags = ["Oppgave Mock"])
@RequestMapping("/rest/oppgave/api/v1/internal/alive")
class OppgaveInternalAliveMockImpl {
    @GetMapping
    fun isAlive() = ResponseEntity.ok().build<Any>()
}
