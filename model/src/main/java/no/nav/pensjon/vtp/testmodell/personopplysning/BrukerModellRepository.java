package no.nav.pensjon.vtp.testmodell.personopplysning;

import java.util.Optional;

public interface BrukerModellRepository {
    Optional<PersonModell> findById(String ident);

    Optional<PersonModell> findByAktørIdent(String ident);

    PersonModell save(PersonModell bruker);
}
