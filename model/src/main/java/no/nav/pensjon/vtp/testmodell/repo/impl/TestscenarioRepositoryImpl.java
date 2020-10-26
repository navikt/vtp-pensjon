package no.nav.pensjon.vtp.testmodell.repo.impl;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import no.nav.pensjon.vtp.testmodell.repo.Testscenario;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplate;

@Component
public class TestscenarioRepositoryImpl implements TestscenarioRepository {

    private final TestscenarioFraTemplateMapper mapper;
    private final TestscenarioBuilderRepository testscenarioBuilderRepository;

    public TestscenarioRepositoryImpl(TestscenarioFraTemplateMapper mapper, TestscenarioBuilderRepository testscenarioBuilderRepository) {
        this.mapper = mapper;
        this.testscenarioBuilderRepository = testscenarioBuilderRepository;
    }


    @Override
    public Testscenario opprettTestscenario(TestscenarioTemplate template) {
        return opprettTestscenario(template, Collections.emptyMap());
    }

    @Override
    public Testscenario opprettTestscenario(TestscenarioTemplate template, Map<String, String> variables) {
        String unikTestscenarioId = UUID.randomUUID().toString();
        Testscenario testscenario = mapper.lagTestscenario(template, unikTestscenarioId, variables);
        testscenarioBuilderRepository.indekser(testscenario);
        return testscenario;
    }

    @Override
    public Testscenario opprettTestscenarioFraJsonString(String testscenarioJson, Map<String, String> variables) {
        String unikTestscenarioId = UUID.randomUUID().toString();
        Testscenario testscenario = mapper.lagTestscenarioFraJsonString(testscenarioJson, unikTestscenarioId, variables);
        testscenarioBuilderRepository.indekser(testscenario);
        return testscenario;
    }

    @Override
    public Map<String, Testscenario> getTestscenarios() {
        return testscenarioBuilderRepository.getTestscenarios();
    }

    @Override
    public Testscenario getTestscenario(String id) {
        return testscenarioBuilderRepository.getTestscenario(id);
    }

    @Override
    public Boolean slettScenario(String id) {
        return testscenarioBuilderRepository.slettScenario(id);
    }
}
