package no.nav.pensjon.vtp.testmodell.organisasjon;

import static java.util.Optional.ofNullable;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

@Repository
public class OrganisasjonRepositoryInMemory implements OrganisasjonRepository {
    private final Map<String, OrganisasjonModell> organisasjoner = new ConcurrentHashMap<>();

    @Override
    public Optional<OrganisasjonModell> findById(String orgnr) {
        return ofNullable(organisasjoner.get(orgnr));
    }

    @Override
    public synchronized void saveAll(Iterable<OrganisasjonModell> modeller) {
        modeller.forEach(o -> organisasjoner.put(o.getOrgnummer(), o));
    }
}
