package no.nav.pensjon.vtp.testmodell.personopplysning;

import static java.util.Optional.ofNullable;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

@Component
public class PersonIndeks {
    private final PersonIdentFooRepository personIdentFooRepository;

    public PersonIndeks(PersonIdentFooRepository personIdentFooRepository) {
        this.personIdentFooRepository = personIdentFooRepository;
    }

    public synchronized void indekserPersonopplysningerByIdent(Personopplysninger pers) {
        personIdentFooRepository.save(new PersonIdentFoo(pers.getSøker().getIdent(), pers));

        ofNullable(pers.getAnnenPart())
                .map(PersonModell::getIdent)
                .ifPresent(ident -> personIdentFooRepository.save(new PersonIdentFoo(ident, pers)));
    }

    public synchronized Stream<Personopplysninger> getAlleSøkere(){
        return personIdentFooRepository
                .findAll()
                .map(PersonIdentFoo::getPersonopplysninger)
                .distinct();
    }

    public Optional<Personopplysninger> findById(String id) {
        return ofNullable(personIdentFooRepository.findById(id))
                .map(PersonIdentFoo::getPersonopplysninger);
    }
}
