package no.nav.pensjon.vtp.testmodell.personopplysning;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

@Component
public class PersonIndeks {

    private final BrukerModellRepository brukerModellRepository;
    private final Map<String, Personopplysninger> byIdentPersonopplysninger = new HashMap<>();

    public PersonIndeks(BrukerModellRepository brukerModellRepository) {
        this.brukerModellRepository = brukerModellRepository;
    }

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

    public synchronized void leggTil(BrukerModell bruker) {
        if (bruker == null) {
            // quiet escape
            return;
        }
        String ident = bruker.getIdent();
        if (bruker instanceof BrukerIdent && ident == null) {
            // skip - er BrukerIdent, venter på full bruker
            return;
        }

        Optional<BrukerModell> existingBruker = brukerModellRepository.findById(ident);
        if (existingBruker.filter(BrukerIdent.class::isInstance).isPresent()) {
            // overskriv
            brukerModellRepository.save(bruker);
        } else if (existingBruker.isEmpty()) {
            brukerModellRepository.save(bruker);
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized Optional<BrukerModell> finnByIdent(String ident) {
        return brukerModellRepository.findById(ident);
    }

    @SuppressWarnings("unchecked")
    public synchronized Optional<BrukerModell> finnByAktørIdent(String ident) {
        return brukerModellRepository.findByAktørIdent(ident);
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
