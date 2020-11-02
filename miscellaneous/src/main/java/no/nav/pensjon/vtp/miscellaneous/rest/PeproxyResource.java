package no.nav.pensjon.vtp.miscellaneous.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.HeaderParam;
import java.net.URI;

/**
 * Mock of https://github.com/navikt/peproxy
 */

@RestController
@RequestMapping("/peproxy")
public class PeproxyResource {
    private final RestTemplate client = new RestTemplate();

    @GetMapping
    public String proxy(@HeaderParam("target") String target) {
        return client.getForEntity(URI.create(target), String.class).getBody();
    }
}
