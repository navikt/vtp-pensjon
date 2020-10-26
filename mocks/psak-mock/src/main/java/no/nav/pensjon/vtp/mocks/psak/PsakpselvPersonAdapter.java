package no.nav.pensjon.vtp.mocks.psak;

import no.nav.lib.pen.psakpselv.asbo.person.*;
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseType;
import no.nav.pensjon.vtp.testmodell.personopplysning.FamilierelasjonModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.GateadresseModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.pensjon.vtp.testmodell.personopplysning.VoksenModell;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PsakpselvPersonAdapter {
    private PsakpselvPersonAdapter(){}

    private static final Map<String, ASBOPenPerson> PERSONER = new HashMap<>();

    public static ASBOPenPerson toASBOPerson(Personopplysninger personopplysninger) {
        ASBOPenPerson asboPenPerson = populateAsboPenPerson(personopplysninger.getSøker());
        asboPenPerson.setRelasjoner(fetchRelasjoner(personopplysninger));
        return asboPenPerson;
    }

    private static ASBOPenPerson populateAsboPenPerson(VoksenModell søker) {
        ASBOPenPerson asboPenPerson = new ASBOPenPerson();
        asboPenPerson.setFodselsnummer(søker.getIdent());
        asboPenPerson.setFornavn(søker.getFornavn());
        asboPenPerson.setEtternavn(søker.getEtternavn());
        asboPenPerson.setKortnavn(søker.getFornavn() + " " + søker.getEtternavn());
        asboPenPerson.setStatus(søker.getPersonstatus().getStatus());
        asboPenPerson.setStatusKode(søker.getPersonstatus().getPersonstatusType().toString());
        asboPenPerson.setDiskresjonskode(søker.getDiskresjonskode());
        asboPenPerson.setDodsdato(fetchDate(søker.getDødsdato()).orElse(null));
        asboPenPerson.setSivilstand(søker.getSivilstand().getKode());
        asboPenPerson.setSivilstandDato(fetchDate(søker.getSivilstand().getEndringstidspunkt())
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

        PERSONER.put(asboPenPerson.getFodselsnummer(), asboPenPerson);
        return asboPenPerson;
    }

    private static ASBOPenRelasjonListe fetchRelasjoner(Personopplysninger personopplysninger) {
        List<ASBOPenRelasjon> relasjonList =  personopplysninger.getFamilierelasjoner().stream()
                .map(fr -> populateAsboPenRelasjon(personopplysninger, fr))
                .collect(Collectors.toList());

        ASBOPenRelasjonListe liste = new ASBOPenRelasjonListe();
        liste.setRelasjoner(relasjonList.toArray(ASBOPenRelasjon[]::new));
        return liste;
    }

    private static ASBOPenRelasjon populateAsboPenRelasjon(Personopplysninger personopplysninger, FamilierelasjonModell fr) {
        ASBOPenRelasjon relasjon = new ASBOPenRelasjon();
        relasjon.setRelasjonsType(fr.getRolle().name());
        ASBOPenPerson annen = Stream.ofNullable(personopplysninger.getAnnenPart())
                .filter(p -> p.getIdent().equals(fr.getTil().getIdent()))
                .map(PsakpselvPersonAdapter::populateAsboPenPerson)
                .peek(p -> relasjon.setFom(fetchDate(personopplysninger.getSøker().getSivilstand().getFom()).orElse(null)))
                .peek(p -> relasjon.setTom(fetchDate(personopplysninger.getSøker().getSivilstand().getTom()).orElse(null)))
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

    private static ASBOPenPerson createShallowASBOPerson(FamilierelasjonModell familierelasjon) {
        ASBOPenPerson annen = new ASBOPenPerson();
        annen.setFodselsnummer(familierelasjon.getTil().getIdent());
        return annen;
    }

    public static Optional<ASBOPenPerson> getPreviouslyConverted(String foedselsnummer){
        return Optional.ofNullable(PERSONER.get(foedselsnummer));
    }

    public static String getPreviouslyConverted(){
        return PERSONER.keySet().toString();
    }

    private static Optional<GregorianCalendar> fetchDate(LocalDate date) {
        return Optional.ofNullable(date)
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