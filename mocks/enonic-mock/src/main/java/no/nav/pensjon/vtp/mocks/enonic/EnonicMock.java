package no.nav.pensjon.vtp.mocks.enonic;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

@RestController
@RequestMapping("/rest/enonic")
public class EnonicMock {
    private static final Logger logger = LoggerFactory.getLogger(EnonicMock.class);

    @GetMapping(value = "/**")
    public ResponseEntity get(HttpServletRequest request) {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        logger.info("Fikk en forespørsel på {}", path);

        return ResponseEntity.ok().build();
    }
}