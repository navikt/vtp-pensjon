package no.nav.pensjon.vtp.mocks.oppgave.repository;

import static java.lang.String.format;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.springframework.stereotype.Repository;

@Repository
public class OppgaveRepository {
    private final Map<String, OppgaveFoo> oppgaver = new ConcurrentHashMap<>();

    public Optional<OppgaveFoo> findById(String oppgaveId) {
        return Optional.ofNullable(oppgaver.get(oppgaveId));
    }

    public Stream<OppgaveFoo> findAll() {
        return oppgaver.values().stream();
    }

    public String saveOppgave(OppgaveFoo oppgaveFoo) {
        final OppgaveFoo existingOppgaveFoo = oppgaver.get(oppgaveFoo.getOppgaveId());

        if (existingOppgaveFoo != null && existingOppgaveFoo.getVersion() != oppgaveFoo.getVersion()) {
            throw new IllegalArgumentException(
                    format("Oppgaven kan ikke oppdateres når gjeldende versjon og innsendt versjon ikke er like. Gjeldende versjon: %d, angitt versjon: %d uuid: %s",
                            existingOppgaveFoo.getVersion(), oppgaveFoo.getVersion(), oppgaveFoo.getOppgaveId())
            );
        }

        this.oppgaver.put(oppgaveFoo.getOppgaveId(), oppgaveFoo.withIncrementedVersion());
        return oppgaveFoo.getOppgaveId();
    }

}
