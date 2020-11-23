package no.nav.pensjon.vtp.mocks.psak;

import no.nav.lib.pen.psakpselv.asbo.person.*;
import no.nav.pensjon.vtp.testmodell.kodeverk.Diskresjonskoder;
import no.nav.pensjon.vtp.testmodell.personopplysning.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

@Component
public class PsakpselvPersonAdapter {
    private final PersonIndeks personIndeks;
    private final PersonModellRepository personModellRepository;

    public PsakpselvPersonAdapter(PersonIndeks personIndeks, PersonModellRepository personModellRepository) {
        this.personIndeks = personIndeks;
        this.personModellRepository = personModellRepository;
    }

    public Optional<ASBOPenPerson> getASBOPenPerson(String fodselsnummer) {
        return ofNullable(personModellRepository.findById(fodselsnummer))
                .flatMap(person -> personIndeks.findById(fodselsnummer)
                        .map(personopplysninger -> toASBOPerson(person, personopplysninger)));
    }

    public ASBOPenPerson toASBOPerson(final PersonModell person, final Personopplysninger personopplysninger) {
        ASBOPenPerson asboPenPerson = populateAsboPenPerson(person);
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
        ASBOPenHistorikk historikk = createHistorikk();
        asboPenPerson.setHistorikk(historikk);

        ASBOPenUtbetalingsinformasjon utbetalingsinformasjon = new ASBOPenUtbetalingsinformasjon();
        ASBOPenNorskKonto norskKonto = new ASBOPenNorskKonto();
        norskKonto.setKontonummer("1234.45678.0232.7777");

        utbetalingsinformasjon.setNorskKonto(norskKonto);
        utbetalingsinformasjon.setUtbetalingsType("NORKNT");
        asboPenPerson.setUtbetalingsinformasjon(utbetalingsinformasjon);

        return asboPenPerson;
    }

    @NotNull
    private static ASBOPenHistorikk createHistorikk() {
        ASBOPenHistorikk historikk = new ASBOPenHistorikk();
        historikk.setBostedsadresser(null);//må være enten være populert eller null

        ASBOPenAnnenAdresseListe adresseLinjer = new ASBOPenAnnenAdresseListe();
        adresseLinjer.setAdresseLinjer(new ASBOPenAnnenAdresse[0]);
        historikk.setAdresseLinjer(adresseLinjer);

        ASBOPenHistoriskFnrListe fnrListe = new ASBOPenHistoriskFnrListe();
        fnrListe.setHistoriskeFnr(new ASBOPenHistoriskFnr[0]);
        historikk.setHistoriskeFnr(fnrListe);

        ASBOPenNavnEndringListe navnListe = new ASBOPenNavnEndringListe();
        navnListe.setNavnEndringer(new ASBOPenNavnEndring[0]);
        historikk.setNavnEndringer(navnListe);

        return historikk;
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
                .filter(p -> p.getIdent().equals(ofNullable(personModellRepository.findById(fr.getTil())).orElseThrow(() -> new RuntimeException("No person with ident " + fr.getTil())).getIdent()))
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
        annen.setFodselsnummer(
                ofNullable(personModellRepository.findById(familierelasjon.getTil())).orElseThrow(() -> new RuntimeException("No person with ident " + familierelasjon.getTil())).getIdent());
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
