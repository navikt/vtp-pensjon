package no.nav.pensjon.vtp.testmodell.personopplysning;

import java.util.Optional;

public interface BrukerModellRepository {
    Optional<BrukerModell> findById(String ident);

    Optional<BrukerModell> findByAktørIdent(String ident);

    BrukerModell save(BrukerModell bruker);
}
