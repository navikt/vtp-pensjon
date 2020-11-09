package no.nav.pensjon.vtp.testmodell.repo.impl;

import static java.util.Optional.ofNullable;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.springframework.stereotype.Repository;

import no.nav.pensjon.vtp.testmodell.repo.Testscenario;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioRepository;

@Repository
public class TestscenarioRepositoryImpl implements TestscenarioRepository {
    private final Map<String, Testscenario> scenarios = new ConcurrentHashMap<>();

    @Override
    public Stream<Testscenario> findAll() {
        return scenarios.values().stream();
    }

    @Override
    public Optional<Testscenario> findById(String id) {
        return ofNullable(scenarios.get(id));
    }

    @Override
    public Testscenario save(Testscenario testScenario) {
        scenarios.put(testScenario.getId(), testScenario);
        return testScenario;
    }

    @Override
    public void delete(String id) {
        scenarios.remove(id);
    }
}
