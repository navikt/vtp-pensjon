package no.nav.pensjon.vtp.mocks.oppgave;

import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "Oppgave Mock")
@RequestMapping("oppgave/api/v1/internal/alive")
public class OppgaveInternalAliveMockImpl {
    @GetMapping
    public ResponseEntity isAlive() {
        return ResponseEntity.ok().build();
    }
}
