package no.nav.pensjon.vtp.testmodell.repo;

import java.util.Map;

public interface TestscenarioBuilderRepository {
    Map<String, Testscenario> getTestscenarios();

    Testscenario getTestscenario(String id);

    Boolean slettScenario(String id);

    void indekser(Testscenario testscenario);
}
