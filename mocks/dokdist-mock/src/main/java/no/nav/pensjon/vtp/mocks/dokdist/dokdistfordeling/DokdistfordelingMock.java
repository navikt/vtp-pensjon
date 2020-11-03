package no.nav.pensjon.vtp.mocks.dokdist.dokdistfordeling;


import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Api(tags = {"Dokdist"})
@RequestMapping("dokdist/v1/distribuerjournalpost")
public class DokdistfordelingMock {
    private static final Logger LOG = LoggerFactory.getLogger(DokdistfordelingMock.class);

    @PostMapping
    public ResponseEntity lagJournalpost(String request) {
        LOG.info("Distribuer dokument request: [{}]", request);

        return ResponseEntity.ok("{ \"bestillingsId\": \""+ UUID.randomUUID().toString() +"\"}");
    }

}
