package no.nav.pensjon.vtp.testmodell.repo.impl;

import static java.util.Optional.ofNullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplate;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplateRepository;

/**
 * TestscenarioRepository av alle test templates.
 */
public class TestscenarioTemplateRepositoryImpl implements TestscenarioTemplateRepository {
    private final Map<String, TestscenarioTemplate> testTemplates;


    public TestscenarioTemplateRepositoryImpl(final Map<String, TestscenarioTemplate> testTemplates) {
        this.testTemplates = new HashMap<>(testTemplates);
    }

    @Override
    public Stream<TestscenarioTemplate> getTemplates() {
        return testTemplates.values().stream();
    }


    @Override
    public Optional<TestscenarioTemplate> finn(String templateKey) {
        return ofNullable(testTemplates.get(templateKey));
    }
}
