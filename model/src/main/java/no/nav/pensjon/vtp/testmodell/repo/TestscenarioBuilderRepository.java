package no.nav.pensjon.vtp.testmodell.repo;

import java.util.Map;

public interface TestscenarioBuilderRepository {
    Map<String, TestscenarioImpl> getTestscenarios();

    TestscenarioImpl getTestscenario(String id);

    Boolean slettScenario(String id);
}
