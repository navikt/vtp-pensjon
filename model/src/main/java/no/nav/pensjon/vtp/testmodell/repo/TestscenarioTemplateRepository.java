package no.nav.pensjon.vtp.testmodell.repo;

import java.util.Optional;
import java.util.stream.Stream;

public interface TestscenarioTemplateRepository {

    Stream<TestscenarioTemplate> getTemplates();

    Optional<TestscenarioTemplate> finn(String templateKey);
}
