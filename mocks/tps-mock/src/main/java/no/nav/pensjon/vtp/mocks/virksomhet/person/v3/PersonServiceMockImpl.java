package no.nav.pensjon.vtp.mocks.virksomhet.person.v3;

import java.util.List;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.pensjon.vtp.core.annotations.SoapService;
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseType;
import no.nav.pensjon.vtp.testmodell.personopplysning.FamilierelasjonModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.FamilierelasjonModell.Rolle;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.Personopplysninger;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentGeografiskTilknytningPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentPersonPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentPersonhistorikkPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v3.binding.PersonV3;
import no.nav.tjeneste.virksomhet.person.v3.feil.PersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Aktoer;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.BostedsadressePeriode;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Bruker;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Diskresjonskoder;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.Familierelasjon;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.PersonstatusPeriode;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.StatsborgerskapPeriode;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentEkteskapshistorikkRequest;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentEkteskapshistorikkResponse;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentGeografiskTilknytningRequest;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentGeografiskTilknytningResponse;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonRequest;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonResponse;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonerMedSammeAdresseRequest;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonerMedSammeAdresseResponse;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonhistorikkRequest;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonhistorikkResponse;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonnavnBolkRequest;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentPersonnavnBolkResponse;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentSikkerhetstiltakRequest;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentSikkerhetstiltakResponse;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentVergeRequest;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.HentVergeResponse;

@SoapService(path = "/tpsws/ws/Person/v3")
@Addressing
@WebService(name = "Person_v3", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3")
@HandlerChain(file = "/Handler-chain.xml")
public class PersonServiceMockImpl implements PersonV3 {

    private static final Logger LOG = LoggerFactory.getLogger(PersonServiceMockImpl.class);

    private final PersonIndeks personIndeks;

    public PersonServiceMockImpl(PersonIndeks personIndeks) {
        this.personIndeks = personIndeks;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentPersonRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "hentPerson", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentPerson")
    @ResponseWrapper(localName = "hentPersonResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonResponse")
    @Override
    public HentPersonResponse hentPerson(@WebParam(name = "request") HentPersonRequest hentPersonRequest)
            throws HentPersonPersonIkkeFunnet {

        LOG.info("hentPerson. Aktoer: {}", hentPersonRequest.getAktoer().toString());
        Aktoer aktoer = hentPersonRequest.getAktoer();
        PersonModell bruker = finnPerson(aktoer);

        HentPersonResponse response = new HentPersonResponse();
        Bruker person = new PersonAdapter().fra(bruker);


        Personopplysninger pers = personIndeks.finnPersonopplysningerByIdent(bruker.getIdent());

        boolean erBarnet = false;
        for (FamilierelasjonModell relasjon : pers.getFamilierelasjoner()) {
            if(relasjon.getRolle().equals(Rolle.BARN) && relasjon.getTil().getAktørIdent().equals(bruker.getAktørIdent())) {
                erBarnet = true;
            }
        }

        List<Familierelasjon> familierelasjoner;
        if(pers.getAnnenPart() != null && pers.getAnnenPart().getAktørIdent().equals(bruker.getAktørIdent())) { //TODO HACK for annenpart (annenpart burde ha en egen personopplysning fil eller liknende)
            familierelasjoner = new FamilierelasjonAdapter().tilFamilerelasjon(pers.getFamilierelasjonerForAnnenPart());
            familierelasjoner.forEach(fr -> person.getHarFraRolleI().add(fr));
        }
        else if(erBarnet) { //TODO HACK Familierelasjon for barnet
            familierelasjoner = new FamilierelasjonAdapter().tilFamilerelasjon(pers.getFamilierelasjonerForBarnet());
            familierelasjoner.forEach(fr -> person.getHarFraRolleI().add(fr));
        }
        else {
            familierelasjoner = new FamilierelasjonAdapter().tilFamilerelasjon(pers.getFamilierelasjoner());
            familierelasjoner.forEach(fr -> person.getHarFraRolleI().add(fr));
        }

        System.out.println("Relasjoner for person: " + hentPersonRequest.getAktoer().toString());
        for (Familierelasjon familierelasjon : familierelasjoner) {
            System.out.println("    " + familierelasjon.getTilPerson().getAktoer().toString());
        }

        response.setPerson(person);
        return response;
    }

    public PersonModell finnPerson(Aktoer aktoer) throws HentPersonPersonIkkeFunnet {
        return new FinnPerson(personIndeks).finnPerson(aktoer);
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentGeografiskTilknytningRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "hentGeografiskTilknytning", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentGeografiskTilknytning")
    @ResponseWrapper(localName = "hentGeografiskTilknytningResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentGeografiskTilknytningResponse")
    @Override
    public HentGeografiskTilknytningResponse hentGeografiskTilknytning(@WebParam(name = "request") HentGeografiskTilknytningRequest hentGeografiskTilknytningRequest)
            throws HentGeografiskTilknytningPersonIkkeFunnet {
        LOG.info("hentGeografiskTilknytning. {}", hentGeografiskTilknytningRequest.toString());
        PersonModell bruker;
        try {
            bruker = finnPerson(hentGeografiskTilknytningRequest.getAktoer());
        } catch (HentPersonPersonIkkeFunnet e) {
            throw new HentGeografiskTilknytningPersonIkkeFunnet(e.getMessage(), new PersonIkkeFunnet());
        }

        HentGeografiskTilknytningResponse response = new HentGeografiskTilknytningResponse();
        PersonAdapter personAdapter = new PersonAdapter();
        Diskresjonskoder diskresjonskoder = personAdapter.tilDiskresjonskode(bruker);
        response.setDiskresjonskode(diskresjonskoder);
        response.setGeografiskTilknytning(personAdapter.tilGeografiskTilknytning(bruker));
        return response;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentVergeRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "hentVerge", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentVerge")
    @ResponseWrapper(localName = "hentVergeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentVergeResponse")
    @Override
    public HentVergeResponse hentVerge(@WebParam(name = "request") HentVergeRequest var1) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentEkteskapshistorikkRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "hentEkteskapshistorikk", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentEkteskapshistorikk")
    @ResponseWrapper(localName = "hentEkteskapshistorikkResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentEkteskapshistorikkResponse")
    public HentEkteskapshistorikkResponse hentEkteskapshistorikk(@WebParam(name = "request") HentEkteskapshistorikkRequest hentEkteskapshistorikkRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentPersonerMedSammeAdresseRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "hentPersonerMedSammeAdresse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonerMedSammeAdresse")
    @ResponseWrapper(localName = "hentPersonerMedSammeAdresseResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonerMedSammeAdresseResponse")
    @Override
    public HentPersonerMedSammeAdresseResponse hentPersonerMedSammeAdresse(@WebParam(name = "request") HentPersonerMedSammeAdresseRequest var1) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentPersonhistorikkRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "hentPersonhistorikk", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonhistorikk")
    @ResponseWrapper(localName = "hentPersonhistorikkResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonhistorikkResponse")
    @Override
    public HentPersonhistorikkResponse hentPersonhistorikk(@WebParam(name = "request") HentPersonhistorikkRequest hentPersonhistorikkRequest)
            throws HentPersonhistorikkPersonIkkeFunnet {

        LOG.info("hentPersonhistorikk. AktoerId: {}", hentPersonhistorikkRequest.getAktoer().toString());
        PersonModell bruker;
        try {
            bruker = finnPerson(hentPersonhistorikkRequest.getAktoer());
        } catch (HentPersonPersonIkkeFunnet e) {
            throw new HentPersonhistorikkPersonIkkeFunnet(e.getMessage(), new PersonIkkeFunnet());
        }

        HentPersonhistorikkResponse response = new HentPersonhistorikkResponse();
        response.withAktoer(hentPersonhistorikkRequest.getAktoer());

        if (bruker != null) {
            response.withPersonstatusListe(hentPersonstatusPerioder(bruker));
            response.withBostedsadressePeriodeListe(hentBostedadressePerioder(bruker));
            response.withStatsborgerskapListe(hentStatsborgerskapPerioder(bruker));
        }

        return response;
    }

    private List<BostedsadressePeriode> hentBostedadressePerioder(PersonModell bruker) {
        return new BostedsadresseAdapter().fra(bruker.getAdresser(AdresseType.BOSTEDSADRESSE));
    }

    private List<StatsborgerskapPeriode> hentStatsborgerskapPerioder(PersonModell bruker) {
        return new StatsborgerskapAdapter().fra(bruker.getAlleStatsborgerskap());
    }

    private List<PersonstatusPeriode> hentPersonstatusPerioder(PersonModell bruker) {
        return new PersonstatusAdapter().fra(bruker.getAllePersonstatus());
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.PingResponse")
    @Override
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentPersonnavnBolkRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "hentPersonnavnBolk", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonnavnBolk")
    @ResponseWrapper(localName = "hentPersonnavnBolkResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonnavnBolkResponse")
    @Override
    public HentPersonnavnBolkResponse hentPersonnavnBolk(@WebParam(name = "request") HentPersonnavnBolkRequest var1) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentSikkerhetstiltakRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "hentSikkerhetstiltak", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentSikkerhetstiltak")
    @ResponseWrapper(localName = "hentSikkerhetstiltakResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentSikkerhetstiltakResponse")
    @Override
    public HentSikkerhetstiltakResponse hentSikkerhetstiltak(@WebParam(name = "request") HentSikkerhetstiltakRequest var1) {
        throw new UnsupportedOperationException("Ikke implementert");
    }
}


