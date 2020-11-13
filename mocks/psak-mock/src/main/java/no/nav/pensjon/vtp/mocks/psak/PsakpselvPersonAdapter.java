package no.nav.pensjon.vtp.mocks.psak;

import static java.util.Optional.ofNullable;

import no.nav.lib.pen.psakpselv.asbo.person.*;
import no.nav.pensjon.vtp.testmodell.kodeverk.Diskresjonskoder;
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseType;
import no.nav.pensjon.vtp.testmodell.personopplysning.BrukerModellRepository;
import no.nav.pensjon.vtp.testmodell.personopplysning.FamilierelasjonModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.GateadresseModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.Personopplysninger;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

@Component
public class PsakpselvPersonAdapter {
    private final BrukerModellRepository brukerModellRepository;

    public PsakpselvPersonAdapter(BrukerModellRepository brukerModellRepository) {
        this.brukerModellRepository = brukerModellRepository;
    }

    public ASBOPenPerson toASBOPerson(Personopplysninger personopplysninger) {
        ASBOPenPerson asboPenPerson = populateAsboPenPerson(personopplysninger.getSøker());
        asboPenPerson.setRelasjoner(fetchRelasjoner(personopplysninger));
        return asboPenPerson;
    }

    private static ASBOPenPerson populateAsboPenPerson(PersonModell søker) {
        ASBOPenPerson asboPenPerson = new ASBOPenPerson();
        asboPenPerson.setFodselsnummer(søker.getIdent());
        asboPenPerson.setFornavn(søker.getFornavn());
        asboPenPerson.setEtternavn(søker.getEtternavn());
        asboPenPerson.setKortnavn(søker.getFornavn() + " " + søker.getEtternavn());
        asboPenPerson.setStatus(søker.getPersonstatusFoo().getKode().name());
        asboPenPerson.setStatusKode(søker.getPersonstatusFoo().getKode().name());
        asboPenPerson.setDiskresjonskode(ofNullable(søker.getDiskresjonskode()).map(Diskresjonskoder::toString).orElse(null));
        asboPenPerson.setDodsdato(fetchDate(søker.getDødsdato()).orElse(null));
        asboPenPerson.setSivilstand(søker.getSivilstandFoo().getKode().name());
        asboPenPerson.setSivilstandDato(fetchDate(søker.getSivilstandFoo().getEndringstidspunkt())
                .orElse(fetchDate(søker.getFødselsdato())
                        .orElse(null)));
        asboPenPerson.setSprakKode(søker.getSpråk2Bokstaver());
        asboPenPerson.setSprakBeskrivelse(søker.getSpråk());
        asboPenPerson.setBostedsAdresse(søker.getAdresse(AdresseType.BOSTEDSADRESSE)
                .map(a -> (GateadresseModell)a)
                .map(PsakpselvPersonAdapter::convert)
                .orElse(null));
        asboPenPerson.setErEgenansatt(false);
        asboPenPerson.setBrukerprofil(new ASBOPenBrukerprofil());
        ASBOPenPersonUtland personUtland = new ASBOPenPersonUtland();
        personUtland.setStatsborgerKode("NOR");
        personUtland.setStatsborgerskap("NOR");
        asboPenPerson.setPersonUtland(personUtland);
        ASBOPenHistorikk historikk = new ASBOPenHistorikk();
        historikk.setBostedsadresser(null);
        asboPenPerson.setHistorikk(historikk);

        ASBOPenUtbetalingsinformasjon utbetalingsinformasjon = new ASBOPenUtbetalingsinformasjon();
        ASBOPenNorskKonto norskKonto = new ASBOPenNorskKonto();
        norskKonto.setKontonummer("1234.45678.0232.7777");

        utbetalingsinformasjon.setNorskKonto(norskKonto);
        utbetalingsinformasjon.setUtbetalingsType("NORKNT");
        asboPenPerson.setUtbetalingsinformasjon(utbetalingsinformasjon);

        return asboPenPerson;
    }

    private ASBOPenRelasjonListe fetchRelasjoner(Personopplysninger personopplysninger) {
        List<ASBOPenRelasjon> relasjonList =  personopplysninger.getFamilierelasjoner().stream()
                .map(fr -> populateAsboPenRelasjon(personopplysninger, fr))
                .collect(Collectors.toList());

        ASBOPenRelasjonListe liste = new ASBOPenRelasjonListe();
        liste.setRelasjoner(relasjonList.toArray(ASBOPenRelasjon[]::new));
        return liste;
    }

    private ASBOPenRelasjon populateAsboPenRelasjon(Personopplysninger personopplysninger, FamilierelasjonModell fr) {
        ASBOPenRelasjon relasjon = new ASBOPenRelasjon();
        relasjon.setRelasjonsType(fr.getRolle().name());
        ASBOPenPerson annen = Stream.ofNullable(personopplysninger.getAnnenPart())
                .filter(p -> p.getIdent().equals(brukerModellRepository.findById(fr.getTil()).orElseThrow(() -> new RuntimeException("No person with ident " + fr.getTil())).getIdent()))
                .map(PsakpselvPersonAdapter::populateAsboPenPerson)
                .peek(p -> relasjon.setFom(fetchDate(personopplysninger.getSøker().getSivilstandFoo().getFom()).orElse(null)))
                .peek(p -> relasjon.setTom(fetchDate(personopplysninger.getSøker().getSivilstandFoo().getTom()).orElse(null)))
                .findFirst().orElseGet(() -> createShallowASBOPerson(fr));
        relasjon.setPerson(annen);

        //annen part må ha en lik relasjon også
        ASBOPenPerson shallowSoker = new ASBOPenPerson();
        shallowSoker.setFodselsnummer(personopplysninger.getSøker().getIdent());

        ASBOPenRelasjon annenPartRelasjon = new ASBOPenRelasjon();
        annenPartRelasjon.setRelasjonsType(relasjon.getRelasjonsType());
        annenPartRelasjon.setFom(relasjon.getFom());
        annenPartRelasjon.setFom(relasjon.getTom());
        annenPartRelasjon.setAdresseStatus(relasjon.getAdresseStatus());
        annenPartRelasjon.setPerson(shallowSoker);

        ASBOPenRelasjonListe liste = new ASBOPenRelasjonListe();
        liste.setRelasjoner(new ASBOPenRelasjon[]{annenPartRelasjon});
        annen.setRelasjoner(liste);

        return relasjon;
    }

    private ASBOPenPerson createShallowASBOPerson(FamilierelasjonModell familierelasjon) {
        ASBOPenPerson annen = new ASBOPenPerson();
        annen.setFodselsnummer(brukerModellRepository.findById(familierelasjon.getTil()).orElseThrow(() -> new RuntimeException("No person with ident " + familierelasjon.getTil())).getIdent());
        return annen;
    }

    private static Optional<GregorianCalendar> fetchDate(LocalDate date) {
        return ofNullable(date)
                .map(d -> GregorianCalendar.from(d.atStartOfDay(ZoneId.systemDefault())));
    }

    private static ASBOPenBostedsAdresse convert(GateadresseModell adresse) {
        ASBOPenBostedsAdresse asboPenBostedsAdresse = new ASBOPenBostedsAdresse();
        asboPenBostedsAdresse.setAdresseType(AdresseType.BOSTEDSADRESSE.name());
        asboPenBostedsAdresse.setBolignr(adresse.getHusnummer().toString());
        asboPenBostedsAdresse.setBoadresse1(adresse.getGatenavn() + " " + adresse.getHusnummer().toString());
        asboPenBostedsAdresse.setPostnummer(adresse.getPostnummer());
        return asboPenBostedsAdresse;
    }
}
