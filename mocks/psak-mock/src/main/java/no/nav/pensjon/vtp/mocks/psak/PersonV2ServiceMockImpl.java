package no.nav.pensjon.vtp.mocks.psak;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson;
import no.nav.pensjon.vtp.core.annotations.SoapService;
import no.nav.virksomhet.part.person.v2.Diskresjonskode;
import no.nav.virksomhet.part.person.v2.Navn;
import no.nav.virksomhet.part.person.v2.Person;
import no.nav.virksomhet.part.person.v2.PersonIdent;
import no.nav.virksomhet.part.person.v2.Personstatus;
import no.nav.virksomhet.tjenester.person.meldinger.v2.HentPersonRequest;
import no.nav.virksomhet.tjenester.person.meldinger.v2.HentPersonResponse;
import no.nav.virksomhet.tjenester.person.meldinger.v2.HentUtenlandskIdentitetListeRequest;
import no.nav.virksomhet.tjenester.person.meldinger.v2.HentUtenlandskIdentitetListeResponse;
import no.nav.virksomhet.tjenester.person.meldinger.v2.RegistrereAdresseForDodsboRequest;
import no.nav.virksomhet.tjenester.person.v2.HentPersonPersonIkkeFunnet;

@SoapService(path = "/esb/nav-tjeneste-person_v2Web/sca/PersonWSEXP")
@WebService(
        targetNamespace = "http://nav.no/virksomhet/tjenester/person/v2",
        name = "Person"
)
@HandlerChain(file = "/Handler-chain.xml")
@XmlSeeAlso({no.nav.virksomhet.tjenester.person.v2.ObjectFactory.class, no.nav.virksomhet.part.person.v2.ObjectFactory.class, no.nav.virksomhet.tjenester.person.meldinger.v2.ObjectFactory.class, no.nav.virksomhet.tjenester.person.feil.v2.ObjectFactory.class})
public class PersonV2ServiceMockImpl implements no.nav.virksomhet.tjenester.person.v2.Person {

    private static final Logger LOG = LoggerFactory.getLogger(PersonV2ServiceMockImpl.class);

    private final PsakpselvPersonAdapter psakpselvPersonAdapter;

    public PersonV2ServiceMockImpl(PsakpselvPersonAdapter psakpselvPersonAdapter) {
        this.psakpselvPersonAdapter = psakpselvPersonAdapter;
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
    public void registrereAdresseForDodsbo(@WebParam(name = "request") RegistrereAdresseForDodsboRequest registrereAdresseForDodsboRequest) {
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
            name = "response"
    )
    public HentPersonResponse hentPerson(@WebParam(name = "request") HentPersonRequest hentPersonRequest) throws HentPersonPersonIkkeFunnet {
        String ident = hentPersonRequest.getIdent();
        LOG.info("Kall mot PersonV2ServiceMockImpl hentPerson med ident " + ident);

        Person person = psakpselvPersonAdapter.getASBOPenPerson(ident)
                .map(this::convertASBOPersonToPersonV2)
                .orElseThrow(HentPersonPersonIkkeFunnet::new);

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
            name = "response"
    )
    public HentUtenlandskIdentitetListeResponse hentUtenlandskIdentitetListe(@WebParam(name = "request") HentUtenlandskIdentitetListeRequest hentUtenlandskIdentitetListeRequest) {
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
