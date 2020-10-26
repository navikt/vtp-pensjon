package no.nav.pensjon.vtp.testmodell.repo.impl;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.stream;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.type.TypeReference;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplate;
import no.nav.pensjon.vtp.testmodell.util.JsonMapper;
import no.nav.pensjon.vtp.testmodell.util.VariabelContainer;

public class TestscenarioTemplateLoader {
    public static final String LOCATION_PATTERN = "classpath:/scenarios/**/vars.json";

    private final JsonMapper jsonMapper = new JsonMapper();

    public Map<String, TestscenarioTemplate> load() {
        return locateVarsResources()
                .map(this::loadFileTestscenarioTemplate)
                .collect(toMap(TestscenarioTemplate::getTemplateKey, identity()));
    }

    private Stream<Resource> locateVarsResources() {
        try {
            return stream(new PathMatchingResourcePatternResolver().getResources(LOCATION_PATTERN));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private TestscenarioTemplate loadFileTestscenarioTemplate(Resource resource) {
        try {
            final String dir = resource.getURL().toString().substring(0, resource.getURL().toString().lastIndexOf("/"));
            final String templateName = URLDecoder.decode(dir.substring(dir.lastIndexOf("/") + 1), UTF_8);
            final VariabelContainer variables = loadTemplateVars(resource);
            return new FileTestscenarioTemplate(dir + "/", templateName, variables);
        } catch (final IOException e) {
            throw new IllegalStateException("Kunne ikke laste template fra " + resource, e);
        }
    }

    private VariabelContainer loadTemplateVars(Resource resource) throws IOException {
        try (InputStream is = resource.getInputStream()) {
            return new VariabelContainer(jsonMapper.lagObjectMapper().readValue(is, new TypeReference<HashMap<String, String>>() {}));
        }
    }
}
