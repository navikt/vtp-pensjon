package no.nav.pensjon.vtp.mocks.psak;

import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * VTP-teamet jobber med å lage mock for PDL, og dette er midlertidig løsning frem til vi merger inn i vtp-en
 */

@RestController
@Api(tags = {"PDL"})
@RequestMapping("/graphql")
public class PDLMock {
    private static final Logger LOG = LoggerFactory.getLogger(PDLMock.class);

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
        public Map<String, List<Map<String,String>>> hentFullPerson(String data){
        LOG.info("Nytt kall mot graphQL med data: " + data);
        Map<String,String> errors = new HashMap<>();
        errors.put("locations",null);
        errors.put("path",null);
        errors.put("extensions",null);
        errors.put("message","Ikke implementert");

        return Map.of("errors", Collections.singletonList(errors));
    }
}
