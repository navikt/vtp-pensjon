package no.nav.pensjon.vtp.testmodell.repo;

import java.util.Map;

public interface TestscenarioRepository {
    Map<String, Testscenario> getTestscenarios();

    Testscenario getTestscenario(String id);

    Testscenario opprettTestscenario(TestscenarioTemplate template);

    Testscenario opprettTestscenario(TestscenarioTemplate template, Map<String, String> userSuppliedVariables);

    Testscenario opprettTestscenarioFraJsonString(String testscenarioJson, Map<String, String> userSuppliedVariables);

    Boolean slettScenario(String id);
}
