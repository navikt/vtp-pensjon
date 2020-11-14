package no.nav.pensjon.vtp.testmodell.personopplysning;

import java.util.Optional;

import org.springframework.data.repository.Repository;

public interface PersonModellRepository extends Repository<PersonModell, String> {
    Optional<PersonModell> findById(String ident);

    Optional<PersonModell> findByAktørIdent(String ident);

    PersonModell save(PersonModell bruker);
}
