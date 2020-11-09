package no.nav.pensjon.vtp.testmodell.repo;

import java.util.Optional;
import java.util.stream.Stream;

public interface TestscenarioRepository {
    Stream<Testscenario> findAll();

    Optional<Testscenario> findById(String id);

    void delete(String id);

    Testscenario save(Testscenario testscenario);
}
