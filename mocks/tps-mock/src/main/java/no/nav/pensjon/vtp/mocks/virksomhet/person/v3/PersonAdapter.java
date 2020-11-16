package no.nav.pensjon.vtp.mocks.virksomhet.person.v3;

import static java.util.Optional.ofNullable;

import javax.xml.datatype.XMLGregorianCalendar;

import no.nav.pensjon.vtp.felles.ConversionUtils;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.GeografiskTilknytningModell;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Bruker;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Bydel;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Diskresjonskoder;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Foedselsdato;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.GeografiskTilknytning;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Kjoenn;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Kjoennstyper;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Kommune;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Land;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.NorskIdent;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Person;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.PersonIdent;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Personidenter;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Personnavn;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Personstatus;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Personstatuser;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Sivilstand;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Sivilstander;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Spraak;

public class PersonAdapter {
    public Bruker fra(PersonModell person) {
        Bruker bruker = mapFraBruker(person);

        // Kjønn
        Kjoenn kjonn = new Kjoenn();
        Kjoennstyper kjonnstype = new Kjoennstyper();
        kjonnstype.setValue(person.getKjønnKode());
        kjonn.setKjoenn(kjonnstype);
        bruker.setKjoenn(kjonn);

        // Fødselsdato
        Foedselsdato fodselsdato = new Foedselsdato();
        XMLGregorianCalendar xcal = ConversionUtils.convertToXMLGregorianCalendar(person.getFødselsdato());
        fodselsdato.setFoedselsdato(xcal);
        bruker.setFoedselsdato(fodselsdato);

        // Navn
        Personnavn personnavn = new Personnavn();
        personnavn.setEtternavn(person.getEtternavn().toUpperCase());
        personnavn.setFornavn(person.getFornavn().toUpperCase());
        personnavn.setSammensattNavn(person.getEtternavn().toUpperCase() + " " + person.getFornavn().toUpperCase());
        bruker.setPersonnavn(personnavn);

        // PersonstatusModell
        Personstatus personstatus = new Personstatus();
        Personstatuser personstatuser = new Personstatuser();
        personstatuser.setValue(person.getPersonstatusFoo().getKode().name());
        personstatus.setPersonstatus(personstatuser);
        bruker.setPersonstatus(personstatus);

        // Målform
        Spraak spraak = new Spraak();
        spraak.setValue(person.getSpråk2Bokstaver());
        bruker.setMaalform(spraak);

        // Geografisk tilknytning
        bruker.setGeografiskTilknytning(tilGeografiskTilknytning(person));

        // SivilstandModell
        Sivilstand sivilstand = new Sivilstand();
        Sivilstander sivilstander = new Sivilstander();
        sivilstander.setValue(person.getSivilstandFoo().getKode().name());
        sivilstand.setSivilstand(sivilstander);
        bruker.setSivilstand(sivilstand);

        // Diskresjonskode
        bruker.setDiskresjonskode(tilDiskresjonskode(person.getDiskresjonskode()));

        // statsborgerskap

        bruker.setStatsborgerskap(new StatsborgerskapAdapter().fra(person.getStatsborgerskapFoo()));

        new AdresseAdapter().setAdresser(bruker, person);

        return bruker;
    }

    public Person mapTilPerson(PersonModell modell) {
        Person person = new Person();
        NorskIdent norskIdent = new NorskIdent();
        norskIdent.setIdent(modell.getIdent());
        PersonIdent personIdent = new PersonIdent();
        personIdent.setIdent(norskIdent);
        person.setAktoer(personIdent);

        return person;
    }

    private Bruker mapFraBruker(PersonModell person) {
        Bruker bruker = new Bruker();

        // Ident
        NorskIdent norskIdent = new NorskIdent();
        norskIdent.setIdent(person.getIdent());
        Personidenter personidenter = new Personidenter();
        personidenter.setValue("fnr");
        norskIdent.setType(personidenter);

        PersonIdent personIdent = new PersonIdent();
        personIdent.setIdent(norskIdent);
        bruker.setAktoer(personIdent);
        return bruker;
    }

    public Diskresjonskoder tilDiskresjonskode(no.nav.pensjon.vtp.testmodell.kodeverk.Diskresjonskoder diskresjonskoder) {
        return ofNullable(diskresjonskoder)
                .map(Enum::name)
                .map(kode -> {
                    Diskresjonskoder d = new Diskresjonskoder();
                    d.withKodeverksRef("Diskresjonskoder");
                    d.withKodeRef(kode);
                    d.withValue(kode);
                    return d;
                })
                .orElse(null);
    }

    public GeografiskTilknytning tilGeografiskTilknytning(PersonModell bruker) {
        GeografiskTilknytningModell tilknytning = bruker.getGeografiskTilknytning();
        if (tilknytning == null) {
            return null;
        } else {
            GeografiskTilknytning geo;
            switch (tilknytning.getType()) {
                case Land:
                    geo = new Land();
                    break;
                case Kommune:
                    geo = new Kommune();
                    break;
                case Bydel:
                    geo = new Bydel();
                    break;
                default:
                    return null;
            }
            geo.setGeografiskTilknytning(tilknytning.getKode());

            return geo;
        }
    }

}
