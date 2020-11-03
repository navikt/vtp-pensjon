package no.nav.pensjon.vtp.mocks.infotrygdks;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = {"infotrygd-kontantstotte"})
@RequestMapping("/rest/infotrygd-kontantstotte/v1/harBarnAktivKontantstotte")
public class InfotrygdKontantstotteMock {
    private static final Logger LOG = LoggerFactory.getLogger(InfotrygdKontantstotteMock.class);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "infotrygd-kontantstotte")
    public ResponseEntity harBarnAktivKontantstøtte(@RequestHeader("fnr") String fnr) {
        LOG.info("infotrygd-kontantstotte. fnr: {}", fnr);

        return ResponseEntity.ok("{ \"harAktivKontantstotte\": false }");
    }
}
