package no.nav.pensjon.vtp.testmodell.identer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class IdenterIndeks {
    private final Map<String, LokalIdentIndeks> identer = new ConcurrentHashMap<>();
    private final IdentGenerator identGenerator = new FiktiveFnr();

    public LokalIdentIndeks getIdenter(String unikScenarioId) {
        return identer.computeIfAbsent(unikScenarioId, n -> new LokalIdentIndeks(n, identGenerator));
    }
}
