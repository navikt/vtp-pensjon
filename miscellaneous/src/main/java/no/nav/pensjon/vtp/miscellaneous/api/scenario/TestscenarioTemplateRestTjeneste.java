package no.nav.pensjon.vtp.miscellaneous.api.scenario;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.status;

import java.util.List;
import java.util.TreeMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import no.nav.pensjon.vtp.core.annotations.JaxrsResource;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplateRepository;

@JaxrsResource
@Api(tags = { "Testscenario/templates" })
@Path("/api/testscenario/templates")
public class TestscenarioTemplateRestTjeneste {
    private final TestscenarioTemplateRepository templateRepository;

    public TestscenarioTemplateRestTjeneste(TestscenarioTemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "templates", notes = ("Liste av tilgjengelig Testscenario Templates"), response = TemplateReferanse.class, responseContainer = "List")
    public List<TemplateReferanse> listTestscenarioTemplates() {
        return templateRepository.getTemplates()
            .map(TemplateReferanse::fromTestscenarioTemplate)
                .sorted(comparing(TemplateReferanse::getNavn))
            .collect(toList());
    }

    @GET
    @Path("/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "templates", notes = ("Beskrivelse av template, inklusiv påkrevde variable (og evt. default verdier"), response = TemplateDto.class)
    public Response beskrivTestscenarioTemplate(@PathParam("key") String key) {
        return templateRepository.finn(key)
                .map(template -> {
                    final TreeMap<String, String> map = new TreeMap<>();
                    template.getDefaultVars().forEach(map::putIfAbsent);
                    template.getExpectedVars().forEach(v -> map.putIfAbsent(v.getName(), null));
                    return ok(new TemplateDto(key, template.getTemplateNavn(), map)).build();
                })
                .orElse(status(NOT_FOUND.getStatusCode(), "Template " + key + " ikke funnet").build());
    }
}
