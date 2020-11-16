package no.nav.pensjon.vtp.mocks.psak;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import no.nav.inf.pen.person.HentFamilierelasjonerFaultPenPersonIkkeFunnetMsg;
import no.nav.inf.pen.person.HentFamilierelasjonsHistorikkFaultPenPersonIkkeFunnetMsg;
import no.nav.inf.pen.person.HentHistorikkFaultPenPersonIkkeFunnetMsg;
import no.nav.inf.pen.person.HentPersonFaultPenPersonIkkeFunnetMsg;
import no.nav.inf.pen.person.HentPersonUtlandFaultPenPersonIkkeFunnetMsg;
import no.nav.inf.pen.person.ObjectFactory;
import no.nav.inf.pen.person.PENPerson;
import no.nav.lib.pen.psakpselv.asbo.person.ASBOPenFinnAdresseListeRequest;
import no.nav.lib.pen.psakpselv.asbo.person.ASBOPenFinnAdresseListeResponse;
import no.nav.lib.pen.psakpselv.asbo.person.ASBOPenHentFamilierelasjonerRequest;
import no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson;
import no.nav.pensjon.vtp.core.annotations.SoapService;

@SoapService(path = "/esb/nav-cons-pen-pen-personWeb/sca/PENPersonWSEXP")
@WebService(targetNamespace = "http://nav-cons-pen-pen-person/no/nav/inf/PENPerson", name = "PENPerson")
@XmlSeeAlso({no.nav.lib.pen.psakpselv.fault.ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.person.ObjectFactory.class, ObjectFactory.class})
@HandlerChain(file = "/Handler-chain.xml")
public class PenPersonServiceMockImpl implements PENPerson {
    private final PsakpselvPersonAdapter psakpselvPersonAdapter;

    public PenPersonServiceMockImpl(PsakpselvPersonAdapter psakpselvPersonAdapter) {
        this.psakpselvPersonAdapter = psakpselvPersonAdapter;
    }

    @Override
    @WebMethod
    @RequestWrapper(localName = "hentPerson", targetNamespace = "http://nav-cons-pen-pen-person/no/nav/inf/PENPerson", className = "no.nav.inf.pen.person.HentPerson")
    @ResponseWrapper(localName = "hentPersonResponse", targetNamespace = "http://nav-cons-pen-pen-person/no/nav/inf/PENPerson", className = "no.nav.inf.pen.person.HentPersonResponse")
    @WebResult(name = "hentPersonResponse")
    public no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson hentPerson(
            @WebParam(name = "hentPersonRequest")
                    no.nav.lib.pen.psakpselv.asbo.person.ASBOPenHentPersonRequest hentPersonRequest
    ) throws HentPersonFaultPenPersonIkkeFunnetMsg {
        return psakpselvPersonAdapter.getASBOPenPerson(hentPersonRequest.getPerson().getFodselsnummer())
                .orElseThrow(HentPersonFaultPenPersonIkkeFunnetMsg::new);
    }

    @Override
    @WebMethod
    @RequestWrapper(localName = "hentFamilierelasjonsHistorikk", targetNamespace = "http://nav-cons-pen-pen-person/no/nav/inf/PENPerson", className = "no.nav.inf.pen.person.HentFamilierelasjonsHistorikk")
    @ResponseWrapper(localName = "hentFamilierelasjonsHistorikkResponse", targetNamespace = "http://nav-cons-pen-pen-person/no/nav/inf/PENPerson", className = "no.nav.inf.pen.person.HentFamilierelasjonsHistorikkResponse")
    @WebResult(name = "hentFamilierelasjonsHistorikkResponse")
    public no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson hentFamilierelasjonsHistorikk(
            @WebParam(name = "hentFamilierelasjonsHistorikkRequest")
                    no.nav.lib.pen.psakpselv.asbo.person.ASBOPenHentFamilierelasjonsHistorikkRequest hentFamilierelasjonsHistorikkRequest
    ) throws HentFamilierelasjonsHistorikkFaultPenPersonIkkeFunnetMsg {
        return psakpselvPersonAdapter.getASBOPenPerson(hentFamilierelasjonsHistorikkRequest.getFnr()).orElseThrow(HentFamilierelasjonsHistorikkFaultPenPersonIkkeFunnetMsg::new);
    }

    @Override
    public ASBOPenFinnAdresseListeResponse finnAdresseListe(ASBOPenFinnAdresseListeRequest finnAdresseListeRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod
    @RequestWrapper(localName = "hentPersonUtland", targetNamespace = "http://nav-cons-pen-pen-person/no/nav/inf/PENPerson", className = "no.nav.inf.pen.person.HentPersonUtland")
    @ResponseWrapper(localName = "hentPersonUtlandResponse", targetNamespace = "http://nav-cons-pen-pen-person/no/nav/inf/PENPerson", className = "no.nav.inf.pen.person.HentPersonUtlandResponse")
    @WebResult(name = "hentPersonUtlandResponse")
    public no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson hentPersonUtland(
            @WebParam(name = "hentPersonUtlandRequest")
                    no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson hentPersonUtlandRequest
    ) throws HentPersonUtlandFaultPenPersonIkkeFunnetMsg {
        return psakpselvPersonAdapter.getASBOPenPerson(hentPersonUtlandRequest.getFodselsnummer()).orElseThrow(HentPersonUtlandFaultPenPersonIkkeFunnetMsg::new);
    }

    @Override
    @WebMethod
    @RequestWrapper(
            localName = "hentFamilierelasjoner",
            targetNamespace = "http://nav-cons-pen-pen-person/no/nav/inf/PENPerson",
            className = "no.nav.inf.pen.person.HentFamilierelasjoner"
    )
    @ResponseWrapper(
            localName = "hentFamilierelasjonerResponse",
            targetNamespace = "http://nav-cons-pen-pen-person/no/nav/inf/PENPerson",
            className = "no.nav.inf.pen.person.HentFamilierelasjonerResponse"
    )
    @WebResult(
            name = "hentFamilierelasjonerResponse"
    )
    public ASBOPenPerson hentFamilierelasjoner(@WebParam(name = "hentFamilierelasjonerRequest") ASBOPenHentFamilierelasjonerRequest hentFamilierelasjonerRequest) throws HentFamilierelasjonerFaultPenPersonIkkeFunnetMsg {
        return psakpselvPersonAdapter.getASBOPenPerson(hentFamilierelasjonerRequest.getFodselsnummer())
            .orElseThrow(HentFamilierelasjonerFaultPenPersonIkkeFunnetMsg::new);
    }

    @Override
    @WebMethod
    @RequestWrapper(localName = "hentHistorikk", targetNamespace = "http://nav-cons-pen-pen-person/no/nav/inf/PENPerson", className = "no.nav.inf.pen.person.HentHistorikk")
    @ResponseWrapper(localName = "hentHistorikkResponse", targetNamespace = "http://nav-cons-pen-pen-person/no/nav/inf/PENPerson", className = "no.nav.inf.pen.person.HentHistorikkResponse")
    @WebResult(name = "hentHistorikkResponse")
    public no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson hentHistorikk(
            @WebParam(name = "hentHistorikkRequest")
                    no.nav.lib.pen.psakpselv.asbo.person.ASBOPenHentHistorikkRequest hentHistorikkRequest
    ) throws HentHistorikkFaultPenPersonIkkeFunnetMsg {
        return psakpselvPersonAdapter.getASBOPenPerson(hentHistorikkRequest.getFodselsnummer())
                .orElseThrow(HentHistorikkFaultPenPersonIkkeFunnetMsg::new);
    }
}
