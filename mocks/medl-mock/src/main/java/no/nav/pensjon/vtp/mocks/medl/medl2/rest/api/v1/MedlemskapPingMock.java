package no.nav.pensjon.vtp.mocks.medl.medl2.rest.api.v1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("medl2/api/ping")
public class MedlemskapPingMock {
    @GetMapping
    public ResponseEntity ping() {
        return ResponseEntity.ok("pong");
    }
}

