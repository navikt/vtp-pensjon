package no.nav.pensjon.vtp.mocks.virksomhet.person.v3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Component;

import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository;
import no.nav.pensjon.vtp.testmodell.personopplysning.FamilierelasjonModell;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Familierelasjon;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Familierelasjoner;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Person;

@Component
public class FamilierelasjonAdapter {
    private final PersonModellRepository personModellRepository;

    public FamilierelasjonAdapter(PersonModellRepository personModellRepository) {
        this.personModellRepository = personModellRepository;
    }

    public List<Familierelasjon> tilFamilerelasjon(Collection<FamilierelasjonModell> relasjoner){

        List<Familierelasjon> resultat = new ArrayList<>();
        for(FamilierelasjonModell rel: relasjoner) {
            Familierelasjon familierelasjon = new Familierelasjon();
            familierelasjon.setHarSammeBosted(true);
            Familierelasjoner familierelasjoner = new Familierelasjoner();
            familierelasjoner.setValue(rel.getRolleKode());
            familierelasjon.setTilRolle(familierelasjoner);

            Person tilBruker = new PersonAdapter().mapTilPerson(
                    personModellRepository.findById(rel.getTil()).orElseThrow(() -> new RuntimeException("Unable to locate persion with ident " + rel.getTil())));
            familierelasjon.setTilPerson(tilBruker);
            resultat.add(familierelasjon);

        }
        return resultat;
    }
}
