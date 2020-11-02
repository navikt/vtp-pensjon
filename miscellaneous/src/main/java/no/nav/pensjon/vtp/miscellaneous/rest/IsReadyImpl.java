package no.nav.pensjon.vtp.miscellaneous.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = { "isReady" })
@RequestMapping("/isReady")
public class IsReadyImpl {

    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    @ApiOperation(value = "isReady", notes = ("Sjekker om systemet er ready for NAIS"))
    public String buildPermitResponse() {
        return "OK";
    }
}
