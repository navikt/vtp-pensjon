package no.nav.pensjon.vtp.miscellaneous.api.scenario;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplateRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.TreeMap;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@RestController
@Api(tags = { "Testscenario/templates" })
@RequestMapping("/api/testscenario/templates")
public class TestscenarioTemplateRestTjeneste {
    private final TestscenarioTemplateRepository templateRepository;

    public TestscenarioTemplateRestTjeneste(TestscenarioTemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "templates", notes = ("Liste av tilgjengelig Testscenario Templates"), response = TemplateReferanse.class, responseContainer = "List")
    public List<TemplateReferanse> listTestscenarioTemplates() {
        return templateRepository.getTemplates()
            .map(TemplateReferanse::fromTestscenarioTemplate)
                .sorted(comparing(TemplateReferanse::getNavn))
            .collect(toList());
    }

    @GetMapping(value = "/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "templates", notes = ("Beskrivelse av template, inklusiv påkrevde variable (og evt. default verdier"), response = TemplateDto.class)
    public ResponseEntity beskrivTestscenarioTemplate(@PathVariable("key") String key) {
        return templateRepository.finn(key)
                .map(template -> {
                    final TreeMap<String, String> map = new TreeMap<>();
                    template.getDefaultVars().forEach(map::putIfAbsent);
                    template.getExpectedVars().forEach(v -> map.putIfAbsent(v.getName(), null));
                    return ResponseEntity.ok(new TemplateDto(key, template.getTemplateNavn(), map));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
