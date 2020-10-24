package no.nav.pensjon.vtp.testmodell.repo;

import java.util.Map;

public interface TestscenarioRepository extends TestscenarioBuilderRepository {

    @Override
    Map<String, TestscenarioImpl> getTestscenarios();

    @Override
    TestscenarioImpl getTestscenario(String id);

    TestscenarioImpl opprettTestscenario(TestscenarioTemplate template);

    TestscenarioImpl opprettTestscenario(TestscenarioTemplate template, Map<String, String> userSuppliedVariables);

    TestscenarioImpl opprettTestscenarioFraJsonString(String testscenarioJson, Map<String, String> userSuppliedVariables);

    void indekser(TestscenarioImpl testscenario);
}
