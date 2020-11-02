package no.nav.pensjon.vtp.miscellaneous.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = { "isAlive" })
public class IsAliveImpl {

    @ApiOperation(value = "isAlive", notes = ("Sjekker om systemet er alive for NAIS"))
    @GetMapping(value = "/isAlive", produces = MediaType.TEXT_PLAIN_VALUE)
    public String buildPermitResponse() {
        return "OK";
    }
}
