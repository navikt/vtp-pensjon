package no.nav.pensjon.vtp.mocks.psak;

import java.util.Optional;

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

import no.nav.pensjon.vtp.core.annotations.SoapService;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonIndeks;
import no.nav.inf.pselv.person.HentBrukerprofilFaultPenBrukerprofilIkkeFunnetMsg;
import no.nav.inf.pselv.person.HentFamilierelasjonerFaultPenPersonIkkeFunnetMsg;
import no.nav.inf.pselv.person.HentKontoinformasjonFaultPenPersonIkkeFunnetMsg;
import no.nav.inf.pselv.person.HentPersonFaultPenPersonIkkeFunnetMsg;
import no.nav.inf.pselv.person.HentSamboerforholdFaultPenGeneriskMsg;
import no.nav.inf.pselv.person.PSELVPerson;
import no.nav.lib.pen.psakpselv.asbo.ASBOPenTomRespons;
import no.nav.lib.pen.psakpselv.asbo.person.ASBOPenHentFamilierelasjonerRequest;
import no.nav.lib.pen.psakpselv.asbo.person.ASBOPenHentFamilierelasjonsHistorikkRequest;
import no.nav.lib.pen.psakpselv.asbo.person.ASBOPenHentPersonListeRequest;
import no.nav.lib.pen.psakpselv.asbo.person.ASBOPenHentPersonRequest;
import no.nav.lib.pen.psakpselv.asbo.person.ASBOPenHentSamboerforholdRequest;
import no.nav.lib.pen.psakpselv.asbo.person.ASBOPenLagreAdresseRequest;
import no.nav.lib.pen.psakpselv.asbo.person.ASBOPenLagreTelefonnumreRequest;
import no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson;
import no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPersonListe;
import no.nav.lib.pen.psakpselv.asbo.person.ASBOPenSlettAdresseRequest;
import no.nav.lib.pen.psakpselv.fault.ObjectFactory;

@SoapService(path = "/esb/nav-cons-pen-pselv-personWeb/sca/PSELVPersonWSEXP")
@SuppressWarnings("ValidExternallyBoundObject")
@WebService(targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf", name = "PSELVPerson")
@XmlSeeAlso({ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.ObjectFactory.class, no.nav.inf.pselv.person.ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.tjenestepensjon.ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.person.ObjectFactory.class, no.nav.lib.pen.psakpselv.fault.person.ObjectFactory.class})
@HandlerChain(file = "/Handler-chain.xml")
public class PselvPersonServiceMockImpl implements PSELVPerson {
    private static final Logger logger = LoggerFactory.getLogger(PsakPersonServiceMockImpl.class);

    private final PersonIndeks personIndeks;
    private final PsakpselvPersonAdapter psakpselvPersonAdapter;

    public PselvPersonServiceMockImpl(PersonIndeks personIndeks, PsakpselvPersonAdapter psakpselvPersonAdapter) {
        this.personIndeks = personIndeks;
        this.psakpselvPersonAdapter = psakpselvPersonAdapter;
    }

    private Optional<ASBOPenPerson> getASBOPerson(String fodselsnummer) {
        return personIndeks.findById(fodselsnummer)
                .map(psakpselvPersonAdapter::toASBOPerson)
                .or(() -> logIkkeFunnet(fodselsnummer));
    }

    private Optional<ASBOPenPerson> logIkkeFunnet(String fodselsnummer) {
        logger.warn("Klarte ikke å finne person med fnr: " + fodselsnummer);
        return Optional.empty();
    }

    @WebMethod
    @RequestWrapper(
            localName = "hentEnhetId",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.HentEnhetId"
    )
    @ResponseWrapper(
            localName = "hentEnhetIdResponse",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.HentEnhetIdResponse"
    )
    @WebResult(
            name = "hentEnhetIdResponse"
    )
    public ASBOPenPerson hentEnhetId(@WebParam(name = "hentEnhetIdRequest") ASBOPenPerson var1) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @WebMethod
    @RequestWrapper(
            localName = "slettAdresse",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.SlettAdresse"
    )
    @ResponseWrapper(
            localName = "slettAdresseResponse",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.SlettAdresseResponse"
    )
    @WebResult(
            name = "slettAdresseResponse"
    )
    public ASBOPenTomRespons slettAdresse(@WebParam(name = "slettAdresseRequest") ASBOPenSlettAdresseRequest var1) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @WebMethod
    @RequestWrapper(
            localName = "lagreTelefonnumre",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.LagreTelefonnumre"
    )
    @ResponseWrapper(
            localName = "lagreTelefonnumreResponse",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.LagreTelefonnumreResponse"
    )
    @WebResult(
            name = "lagreTelefonnumreResponse"
    )
    @Override
    public ASBOPenTomRespons lagreTelefonnumre(@WebParam(name = "lagreTelefonnumreRequest") ASBOPenLagreTelefonnumreRequest var1) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @WebMethod
    @RequestWrapper(
            localName = "lagreSprak",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.LagreSprak"
    )
    @ResponseWrapper(
            localName = "lagreSprakResponse",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.LagreSprakResponse"
    )
    @WebResult(
            name = "lagreSprakResponse"
    )
    @Override
    public ASBOPenTomRespons lagreSprak(@WebParam(name = "lagreSprakRequest") ASBOPenPerson var1) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @WebMethod
    @RequestWrapper(
            localName = "opprettSamboerforhold",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.OpprettSamboerforhold"
    )
    @ResponseWrapper(
            localName = "opprettSamboerforholdResponse",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.OpprettSamboerforholdResponse"
    )
    @WebResult(
            name = "opprettSamboerforholdResponse"
    )
    @Override
    public ASBOPenTomRespons opprettSamboerforhold(@WebParam(name = "opprettSamboerforholdRequest") ASBOPenPerson var1) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @WebMethod
    @RequestWrapper(
            localName = "hentBrukerprofil",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.HentBrukerprofil"
    )
    @ResponseWrapper(
            localName = "hentBrukerprofilResponse",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.HentBrukerprofilResponse"
    )
    @WebResult(
            name = "hentBrukerprofilResponse"
    )
    @Override
    public ASBOPenPerson hentBrukerprofil(@WebParam(name = "hentBrukerprofilRequest") ASBOPenPerson hentBrukerprofilRequest) throws
            HentBrukerprofilFaultPenBrukerprofilIkkeFunnetMsg {
        return getASBOPerson(hentBrukerprofilRequest.getFodselsnummer()).orElseThrow(HentBrukerprofilFaultPenBrukerprofilIkkeFunnetMsg::new);
    }

    @WebMethod
    @RequestWrapper(
            localName = "hentPerson",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.HentPerson"
    )
    @ResponseWrapper(
            localName = "hentPersonResponse",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.HentPersonResponse"
    )
    @WebResult(
            name = "hentPersonResponse"
    )
    @Override
    public ASBOPenPerson hentPerson(@WebParam(name = "hentPersonRequest") ASBOPenHentPersonRequest hentPersonRequest) throws
            HentPersonFaultPenPersonIkkeFunnetMsg {
        return getASBOPerson(hentPersonRequest.getPerson().getFodselsnummer()).orElseThrow(HentPersonFaultPenPersonIkkeFunnetMsg::new);
    }

    @WebMethod
    @RequestWrapper(
            localName = "hentKontoinformasjon",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.HentKontoinformasjon"
    )
    @ResponseWrapper(
            localName = "hentKontoinformasjonResponse",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.HentKontoinformasjonResponse"
    )
    @WebResult(
            name = "hentKontoinformasjonResponse"
    )
    @Override
    public ASBOPenPerson hentKontoinformasjon(@WebParam(name = "hentKontoinformasjonRequest") ASBOPenPerson hentKontoinformasjonRequest) throws
            HentKontoinformasjonFaultPenPersonIkkeFunnetMsg {
        return getASBOPerson(hentKontoinformasjonRequest.getFodselsnummer()).orElseThrow(HentKontoinformasjonFaultPenPersonIkkeFunnetMsg::new);
    }

    @WebMethod
    @RequestWrapper(
            localName = "hentSamboerforhold",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.HentSamboerforhold"
    )
    @ResponseWrapper(
            localName = "hentSamboerforholdResponse",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.HentSamboerforholdResponse"
    )
    @WebResult(
            name = "hentSamboerforholdResponse"
    )
    @Override
    public ASBOPenPerson hentSamboerforhold(@WebParam(name = "hentSamboerforholdRequest") ASBOPenHentSamboerforholdRequest hentSamboerforholdRequest) throws
            HentSamboerforholdFaultPenGeneriskMsg {
        return getASBOPerson(hentSamboerforholdRequest.getFodselsnummer()).orElseThrow(HentSamboerforholdFaultPenGeneriskMsg::new);
    }

    @WebMethod
    @RequestWrapper(
            localName = "lagreAdresse",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.LagreAdresse"
    )
    @ResponseWrapper(
            localName = "lagreAdresseResponse",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.LagreAdresseResponse"
    )
    @WebResult(
            name = "lagreAdresseResponse"
    )
    @Override
    public ASBOPenTomRespons lagreAdresse(@WebParam(name = "lagreAdresseRequest") ASBOPenLagreAdresseRequest var1) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @WebMethod
    @RequestWrapper(
            localName = "slettSamboerforhold",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.SlettSamboerforhold"
    )
    @ResponseWrapper(
            localName = "slettSamboerforholdResponse",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.SlettSamboerforholdResponse"
    )
    @WebResult(
            name = "slettSamboerResponse"
    )
    @Override
    public ASBOPenTomRespons slettSamboerforhold(@WebParam(name = "slettSamboerRequest") ASBOPenPerson var1) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @WebMethod
    @RequestWrapper(
            localName = "lagreEpost",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.LagreEpost"
    )
    @ResponseWrapper(
            localName = "lagreEpostResponse",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.LagreEpostResponse"
    )
    @WebResult(
            name = "lagreEpostResponse"
    )
    @Override
    public ASBOPenTomRespons lagreEpost(@WebParam(name = "lagreEpostRequest") ASBOPenPerson var1) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @WebMethod
    @RequestWrapper(
            localName = "erEgenansatt",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.ErEgenansatt"
    )
    @ResponseWrapper(
            localName = "erEgenansattResponse",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.ErEgenansattResponse"
    )
    @WebResult(
            name = "erEgenansattResponse"
    )
    @Override
    public Boolean erEgenansatt(@WebParam(name = "erEgenansattRequest") ASBOPenPerson var1) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @WebMethod
    @RequestWrapper(
            localName = "hentFamilierelasjonsHistorikk",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.HentFamilierelasjonsHistorikk"
    )
    @ResponseWrapper(
            localName = "hentFamilierelasjonsHistorikkResponse",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.HentFamilierelasjonsHistorikkResponse"
    )
    @WebResult(
            name = "hentFamilierelasjonsHistorikkResponse"
    )
    @Override
    public ASBOPenPerson hentFamilierelasjonsHistorikk(@WebParam(name = "hentFamilierelasjonsHistorikkRequest") ASBOPenHentFamilierelasjonsHistorikkRequest var1) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @WebMethod
    @RequestWrapper(
            localName = "hentFamilierelasjoner",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.HentFamilierelasjoner"
    )
    @ResponseWrapper(
            localName = "hentFamilierelasjonerResponse",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.HentFamilierelasjonerResponse"
    )
    @WebResult(
            name = "hentFamilierelasjonerResponse"
    )
    @Override
    public ASBOPenPerson hentFamilierelasjoner(@WebParam(name = "hentFamilierelasjonerRequest") ASBOPenHentFamilierelasjonerRequest hentFamilierelasjonerRequest) throws
            HentFamilierelasjonerFaultPenPersonIkkeFunnetMsg {
        return getASBOPerson(hentFamilierelasjonerRequest.getFodselsnummer()).orElseThrow(HentFamilierelasjonerFaultPenPersonIkkeFunnetMsg::new);
    }

    @WebMethod
    @RequestWrapper(
            localName = "lagreKontoinformasjon",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.LagreKontoinformasjon"
    )
    @ResponseWrapper(
            localName = "lagreKontoinformasjonResponse",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.LagreKontoinformasjonResponse"
    )
    @WebResult(
            name = "lagreKontoinformasjonResponse"
    )
    @Override
    public ASBOPenTomRespons lagreKontoinformasjon(@WebParam(name = "lagreKontoinformasjonRequest") ASBOPenPerson var1) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @WebMethod
    @RequestWrapper(
            localName = "lagreBrukerprofil",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.LagreBrukerprofil"
    )
    @ResponseWrapper(
            localName = "lagreBrukerprofilResponse",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.LagreBrukerprofilResponse"
    )
    @WebResult(
            name = "lagreBrukerprofilResponse"
    )
    @Override
    public ASBOPenTomRespons lagreBrukerprofil(@WebParam(name = "lagreBrukerprofilRequest") ASBOPenPerson var1) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @WebMethod
    @RequestWrapper(
            localName = "hentPersonListe",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.HentPersonListe"
    )
    @ResponseWrapper(
            localName = "hentPersonListeResponse",
            targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf",
            className = "no.nav.inf.pselv.person.HentPersonListeResponse"
    )
    @WebResult(
            name = "hentPersonListeResponse"
    )
    @Override
    public ASBOPenPersonListe hentPersonListe(@WebParam(name = "hentPersonListeRequest") ASBOPenHentPersonListeRequest var1) {
        throw new UnsupportedOperationException("Ikke implementert");
    }
}
