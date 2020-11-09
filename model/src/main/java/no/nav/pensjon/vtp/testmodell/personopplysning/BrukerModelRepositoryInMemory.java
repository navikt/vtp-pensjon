package no.nav.pensjon.vtp.testmodell.personopplysning;

import static java.util.Optional.ofNullable;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

@Repository
public class BrukerModelRepositoryInMemory implements BrukerModellRepository {
    private final Map<String, BrukerModell> byIdent = new ConcurrentHashMap<>();

    @Override
    public Optional<BrukerModell> findById(final String ident) {
        return ofNullable(byIdent.get(ident));
    }

    @Override
    public Optional<BrukerModell> findByAktørIdent(final String aktørIdent) {
        return byIdent.values()
                .stream()
                .filter(b -> aktørIdent.equals(b.getAktørIdent()))
                .findAny();
    }

    @Override
    public BrukerModell save(final BrukerModell bruker) {
        byIdent.put(bruker.getIdent(), bruker);
        return bruker;
    }
}
