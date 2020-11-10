package no.nav.pensjon.vtp.testmodell.personopplysning;

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
        if (pers.getSøker() != null) {
            putIfAbsent(pers.getSøker().getIdent(), pers);
        }

        if (pers.getAnnenPart() != null) {
            putIfAbsent(pers.getAnnenPart().getIdent(), pers);
        }

        for (FamilierelasjonModell fr : pers.getFamilierelasjoner()) {
            putIfAbsent(fr.getTil().getIdent(), pers);
        }
    }

    private synchronized void putIfAbsent(String ident, Personopplysninger pers) {
        if (personIdentFooRepository.findById(ident) == null) {
            personIdentFooRepository.save(new PersonIdentFoo(ident, pers));
        }
    }

    public synchronized Optional<Personopplysninger> finnPersonopplysningerByIdent(String ident) {
        return Optional.ofNullable(personIdentFooRepository.findById(ident)).map(PersonIdentFoo::getPersonopplysninger);
    }

    public synchronized Stream<Personopplysninger> getAlleSøkere(){
        return personIdentFooRepository
                .findAll()
                .map(PersonIdentFoo::getPersonopplysninger)
                .filter(p -> p.getSøker() != null)
                .distinct();
    }

    public synchronized Stream<Personopplysninger> getAlleAnnenPart(){
        return personIdentFooRepository
                .findAll()
                .map(PersonIdentFoo::getPersonopplysninger)
                .filter(p -> p.getAnnenPart() != null)
                .distinct();
    }

    public Optional<Personopplysninger> findByFødselsnummer(String fodselsnummer) {
        return Optional.ofNullable(personIdentFooRepository.findById(fodselsnummer)).map(PersonIdentFoo::getPersonopplysninger);
    }
}
