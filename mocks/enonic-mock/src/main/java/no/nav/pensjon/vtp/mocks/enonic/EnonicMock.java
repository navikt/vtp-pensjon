package no.nav.pensjon.vtp.mocks.enonic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/enonic")
public class EnonicMock {
    private static final Logger logger = LoggerFactory.getLogger(EnonicMock.class);

    @GetMapping(value = "{var:.+}")
    public ResponseEntity get(@PathVariable("var") String path) {
        logger.info("Fikk en forespørsel på {}", path);
        return ResponseEntity.ok().build();
    }
}