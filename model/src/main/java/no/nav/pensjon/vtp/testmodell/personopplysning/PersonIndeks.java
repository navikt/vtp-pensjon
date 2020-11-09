package no.nav.pensjon.vtp.testmodell.personopplysning;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

@Component
public class PersonIndeks {

    private final Map<String, Personopplysninger> byIdentPersonopplysninger = new HashMap<>();

    public synchronized void indekserPersonopplysningerByIdent(Personopplysninger pers) {
        if (pers.getSøker() != null) {
            byIdentPersonopplysninger.putIfAbsent(pers.getSøker().getIdent(), pers);
        }

        if (pers.getAnnenPart() != null) {
            byIdentPersonopplysninger.putIfAbsent(pers.getAnnenPart().getIdent(), pers);
        }

        for (FamilierelasjonModell fr : pers.getFamilierelasjoner()) {
            byIdentPersonopplysninger.putIfAbsent(fr.getTil().getIdent(), pers);
        }
    }

    public synchronized Personopplysninger finnPersonopplysningerByIdent(String ident) {
        return byIdentPersonopplysninger.get(ident);
    }

    public synchronized Stream<Personopplysninger> getAlleSøkere(){
        return byIdentPersonopplysninger.values().stream().filter(p -> p.getSøker()!=null).distinct();
    }

    public synchronized Stream<Personopplysninger> getAlleAnnenPart(){
        return byIdentPersonopplysninger.values().stream().filter(p -> p.getAnnenPart()!=null).distinct();
    }

    public Optional<Personopplysninger> findByFødselsnummer(String fodselsnummer) {
        return Optional.ofNullable(byIdentPersonopplysninger.get(fodselsnummer));
    }
}
