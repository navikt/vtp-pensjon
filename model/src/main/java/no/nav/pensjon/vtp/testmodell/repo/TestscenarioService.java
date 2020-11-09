package no.nav.pensjon.vtp.testmodell.repo;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public interface TestscenarioService {
    Stream<Testscenario> findAll();

    Optional<Testscenario> getTestscenario(String id);

    Testscenario opprettTestscenario(TestscenarioTemplate template);

    Testscenario opprettTestscenario(TestscenarioTemplate template, Map<String, String> userSuppliedVariables);

    Testscenario opprettTestscenarioFraJsonString(String testscenarioJson, Map<String, String> userSuppliedVariables);

    void slettScenario(String id);
}
