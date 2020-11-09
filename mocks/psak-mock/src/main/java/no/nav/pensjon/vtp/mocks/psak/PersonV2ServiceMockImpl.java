package no.nav.pensjon.vtp.mocks.psak;

import no.nav.pensjon.vtp.core.annotations.SoapService;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonIndeks;
import no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson;
import no.nav.virksomhet.part.person.v2.*;
import no.nav.virksomhet.tjenester.person.meldinger.v2.*;
import no.nav.virksomhet.tjenester.person.v2.HentPersonPersonIkkeFunnet;
import no.nav.virksomhet.tjenester.person.v2.HentUtenlandskIdentitetListePersonIkkeFunnet;
import no.nav.virksomhet.tjenester.person.v2.RegistrereAdresseForDodsboKunneIkkeRegistrereAdresseForDodsbo;
import no.nav.virksomhet.tjenester.person.v2.RegistrereAdresseForDodsboPersonHarUtlandsadresse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.*;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@SoapService(path = "/esb/nav-tjeneste-person_v2Web/sca/PersonWSEXP")
@WebService(
        targetNamespace = "http://nav.no/virksomhet/tjenester/person/v2",
        name = "Person"
)
@HandlerChain(file = "/Handler-chain.xml")
@XmlSeeAlso({no.nav.virksomhet.tjenester.person.v2.ObjectFactory.class, no.nav.virksomhet.part.person.v2.ObjectFactory.class, no.nav.virksomhet.tjenester.person.meldinger.v2.ObjectFactory.class, no.nav.virksomhet.tjenester.person.feil.v2.ObjectFactory.class})
public class PersonV2ServiceMockImpl implements no.nav.virksomhet.tjenester.person.v2.Person {

    private static final Logger LOG = LoggerFactory.getLogger(PersonV2ServiceMockImpl.class);

    private final PersonIndeks personIndeks;

    public PersonV2ServiceMockImpl(PersonIndeks personIndeks) {
        this.personIndeks = personIndeks;
    }

    @Override
    @WebMethod
    @RequestWrapper(
            localName = "registrereAdresseForDodsbo",
            targetNamespace = "http://nav.no/virksomhet/tjenester/person/v2",
            className = "no.nav.virksomhet.tjenester.person.v2.RegistrereAdresseForDodsbo"
    )
    @ResponseWrapper(
            localName = "registrereAdresseForDodsboResponse",
            targetNamespace = "http://nav.no/virksomhet/tjenester/person/v2",
            className = "no.nav.virksomhet.tjenester.person.v2.RegistrereAdresseForDodsboResponse"
    )
    public void registrereAdresseForDodsbo(@WebParam(name = "request",targetNamespace = "") RegistrereAdresseForDodsboRequest registrereAdresseForDodsboRequest) throws RegistrereAdresseForDodsboKunneIkkeRegistrereAdresseForDodsbo, RegistrereAdresseForDodsboPersonHarUtlandsadresse {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod
    @RequestWrapper(
            localName = "hentPerson",
            targetNamespace = "http://nav.no/virksomhet/tjenester/person/v2",
            className = "no.nav.virksomhet.tjenester.person.v2.HentPerson"
    )
    @ResponseWrapper(
            localName = "hentPersonResponse",
            targetNamespace = "http://nav.no/virksomhet/tjenester/person/v2",
            className = "no.nav.virksomhet.tjenester.person.v2.HentPersonResponse"
    )
    @WebResult(
            name = "response",
            targetNamespace = ""
    )
    public HentPersonResponse hentPerson(@WebParam(name = "request",targetNamespace = "") HentPersonRequest hentPersonRequest) throws HentPersonPersonIkkeFunnet {
        String ident = hentPersonRequest.getIdent();
        LOG.info("Kall mot PersonV2ServiceMockImpl hentPerson med ident " + ident);

        Person person = personIndeks.getAlleSøkere().filter(s -> ident.equalsIgnoreCase(s.getSøker().getIdent()))
                .map(PsakpselvPersonAdapter::toASBOPerson).map(this::convertASBOPersonToPersonV2).findFirst().orElseThrow(HentPersonPersonIkkeFunnet::new);

        HentPersonResponse hentPersonResponse = new HentPersonResponse();
        hentPersonResponse.setPerson(person);
        return hentPersonResponse;
    }

    @Override@WebMethod
    @RequestWrapper(
            localName = "hentUtenlandskIdentitetListe",
            targetNamespace = "http://nav.no/virksomhet/tjenester/person/v2",
            className = "no.nav.virksomhet.tjenester.person.v2.HentUtenlandskIdentitetListe"
    )
    @ResponseWrapper(
            localName = "hentUtenlandskIdentitetListeResponse",
            targetNamespace = "http://nav.no/virksomhet/tjenester/person/v2",
            className = "no.nav.virksomhet.tjenester.person.v2.HentUtenlandskIdentitetListeResponse"
    )
    @WebResult(
            name = "response",
            targetNamespace = ""
    )
    public HentUtenlandskIdentitetListeResponse hentUtenlandskIdentitetListe(@WebParam(name = "request", targetNamespace = "") HentUtenlandskIdentitetListeRequest hentUtenlandskIdentitetListeRequest) throws HentUtenlandskIdentitetListePersonIkkeFunnet {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    private Person convertASBOPersonToPersonV2(ASBOPenPerson asboPenPerson){
        Person person = new Person();

        PersonIdent ident = new PersonIdent();
        ident.setIdent(asboPenPerson.getFodselsnummer());
        person.setPersonIdent(ident);

        Personstatus status = new Personstatus();
        status.setKode(asboPenPerson.getStatusKode());
        status.setUtvidelse(null);
        person.setPersonstatus(status);

        Navn navn = new Navn();
        navn.setFornavn(asboPenPerson.getFornavn());
        navn.setEtternavn(asboPenPerson.getEtternavn());
        navn.setKortnavn(asboPenPerson.getKortnavn());
        navn.setMellomnavn(asboPenPerson.getMellomnavn());
        person.setNavn(navn);

        Diskresjonskode diskresjonskode = new Diskresjonskode();
        diskresjonskode.setKode(asboPenPerson.getDiskresjonskode());
        person.setDiskresjonskode(diskresjonskode);

        return person;
    }

}
