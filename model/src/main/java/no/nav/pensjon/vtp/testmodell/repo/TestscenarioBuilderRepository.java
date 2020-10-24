package no.nav.pensjon.vtp.testmodell.repo;

import java.util.Map;

import no.nav.pensjon.vtp.testmodell.identer.LokalIdentIndeks;

public interface TestscenarioBuilderRepository {
    Map<String, Testscenario> getTestscenarios();

    Testscenario getTestscenario(String id);

    Boolean slettScenario(String id);
}
