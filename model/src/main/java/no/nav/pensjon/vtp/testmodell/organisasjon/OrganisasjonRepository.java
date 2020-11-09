package no.nav.pensjon.vtp.testmodell.organisasjon;

import java.util.Optional;

public interface OrganisasjonRepository {
    Optional<OrganisasjonModell> findById(String orgnr);

    void saveAll(Iterable<OrganisasjonModell> modeller);
}
