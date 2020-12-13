package no.nav.pensjon.vtp.mocks.virksomhet.person.v3;

import no.nav.pensjon.vtp.core.annotations.SoapService;
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException;
import no.nav.pensjon.vtp.testmodell.kodeverk.Rolle;
import no.nav.pensjon.vtp.testmodell.personopplysning.*;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentGeografiskTilknytningPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentPersonPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v3.binding.HentPersonhistorikkPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v3.binding.PersonV3;
import no.nav.tjeneste.virksomhet.person.v3.feil.PersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.person.v3.informasjon.*;
import no.nav.tjeneste.virksomhet.person.v3.meldinger.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.*;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@SoapService(path = "/tpsws/ws/Person/v3")
@Addressing
@WebService(name = "Person_v3", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3")
@HandlerChain(file = "/Handler-chain.xml")
public class PersonServiceMockImpl implements PersonV3 {

    private static final Logger LOG = LoggerFactory.getLogger(PersonServiceMockImpl.class);

    private final PersonModellRepository personModellRepository;
    private final PersonIndeks personIndeks;

    public PersonServiceMockImpl(PersonModellRepository personModellRepository, PersonIndeks personIndeks) {
        this.personModellRepository = personModellRepository;
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


        Personopplysninger pers = Optional.ofNullable(personIndeks.findById(bruker.getIdent()))
                .orElseThrow(() -> {
                    String message = "Person med ident " + bruker.getIdent() + " ikke funnet";
                    return new HentPersonPersonIkkeFunnet(message, new PersonIkkeFunnet().withFeilmelding("Person med ident " + bruker.getIdent() + " ikke funnet"));
                });

        boolean erBarnet = false;
        for (FamilierelasjonModell relasjon : pers.getFamilierelasjoner()) {
            if(relasjon.getRolle().equals(Rolle.BARN) && ofNullable(personModellRepository
                    .findById(relasjon.getTil())).orElseThrow(() -> new RuntimeException("Unable to locate child with ident " + relasjon.getTil())).getAktørIdent().equals(bruker.getAktørIdent())) {
                erBarnet = true;
            }
        }

        List<Familierelasjon> familierelasjoner;
        if(pers.getAnnenPart() != null && pers.getAnnenPart().getAktørIdent().equals(bruker.getAktørIdent())) { //TODO HACK for annenpart (annenpart burde ha en egen personopplysning fil eller liknende)
            familierelasjoner = new FamilierelasjonAdapter(personModellRepository).tilFamilerelasjon(pers.getFamilierelasjonerAnnenPart());
            familierelasjoner.forEach(fr -> person.getHarFraRolleI().add(fr));
        }
        else if(erBarnet) { //TODO HACK Familierelasjon for barnet
            familierelasjoner = new FamilierelasjonAdapter(personModellRepository).tilFamilerelasjon(pers.getFamilierelasjonerBarn());
            familierelasjoner.forEach(fr -> person.getHarFraRolleI().add(fr));
        }
        else {
            familierelasjoner = new FamilierelasjonAdapter(personModellRepository).tilFamilerelasjon(pers.getFamilierelasjoner());
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
        Optional<PersonModell> optionalBrukerModell;
        String ident;
        if (aktoer instanceof PersonIdent) {
            PersonIdent personIdent = (PersonIdent) aktoer;
            ident = personIdent.getIdent().getIdent();
            optionalBrukerModell = ofNullable(personModellRepository.findById(ident));
        } else {
            AktoerId aktoerId = (AktoerId) aktoer;
            ident = aktoerId.getAktoerId();
            optionalBrukerModell = ofNullable(personModellRepository.findByAktørIdent(ident));
        }

        return optionalBrukerModell
                .orElseThrow(() -> new HentPersonPersonIkkeFunnet("BrukerModell ikke funnet:" + ident, new PersonIkkeFunnet()));
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
        Diskresjonskoder diskresjonskoder = personAdapter.tilDiskresjonskode(bruker.getDiskresjonskode());
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
        throw new NotImplementedException();
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentEkteskapshistorikkRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "hentEkteskapshistorikk", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentEkteskapshistorikk")
    @ResponseWrapper(localName = "hentEkteskapshistorikkResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentEkteskapshistorikkResponse")
    public HentEkteskapshistorikkResponse hentEkteskapshistorikk(@WebParam(name = "request") HentEkteskapshistorikkRequest hentEkteskapshistorikkRequest) {
        throw new NotImplementedException();
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentPersonerMedSammeAdresseRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "hentPersonerMedSammeAdresse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonerMedSammeAdresse")
    @ResponseWrapper(localName = "hentPersonerMedSammeAdresseResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentPersonerMedSammeAdresseResponse")
    @Override
    public HentPersonerMedSammeAdresseResponse hentPersonerMedSammeAdresse(@WebParam(name = "request") HentPersonerMedSammeAdresseRequest var1) {
        throw new NotImplementedException();
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
        return new StatsborgerskapAdapter().fra(ofNullable(bruker.getStatsborgerskap()).orElseGet(Collections::emptyList));
    }

    private List<PersonstatusPeriode> hentPersonstatusPerioder(PersonModell bruker) {
        return new PersonstatusAdapter().fra(ofNullable(bruker.getPersonstatus()).orElseGet(Collections::emptyList));
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
        throw new NotImplementedException();
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/person/v3/Person_v3/hentSikkerhetstiltakRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "hentSikkerhetstiltak", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentSikkerhetstiltak")
    @ResponseWrapper(localName = "hentSikkerhetstiltakResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/person/v3", className = "no.nav.tjeneste.virksomhet.person.v3.HentSikkerhetstiltakResponse")
    @Override
    public HentSikkerhetstiltakResponse hentSikkerhetstiltak(@WebParam(name = "request") HentSikkerhetstiltakRequest var1) {
        throw new NotImplementedException();
    }
}


