package no.nav;

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

import no.nav.foreldrepenger.vtp.testmodell.repo.TestscenarioBuilderRepository;
import no.nav.inf.pselv.person.ErEgenansattFaultPenGeneriskMsg;
import no.nav.inf.pselv.person.ErEgenansattFaultPenPersonIkkeFunnetMsg;
import no.nav.inf.pselv.person.HentBrukerprofilFaultPenBrukerprofilIkkeFunnetMsg;
import no.nav.inf.pselv.person.HentBrukerprofilFaultPenGeneriskMsg;
import no.nav.inf.pselv.person.HentEnhetIdFaultPenGeneriskMsg;
import no.nav.inf.pselv.person.HentEnhetIdFaultPenPersonIkkeFunnetMsg;
import no.nav.inf.pselv.person.HentFamilierelasjonerFaultPenGeneriskMsg;
import no.nav.inf.pselv.person.HentFamilierelasjonerFaultPenPersonIkkeFunnetMsg;
import no.nav.inf.pselv.person.HentFamilierelasjonsHistorikkFaultPenGeneriskMsg;
import no.nav.inf.pselv.person.HentFamilierelasjonsHistorikkFaultPenPersonIkkeFunnetMsg;
import no.nav.inf.pselv.person.HentKontoinformasjonFaultPenGeneriskMsg;
import no.nav.inf.pselv.person.HentKontoinformasjonFaultPenPersonIkkeFunnetMsg;
import no.nav.inf.pselv.person.HentPersonFaultPenGeneriskMsg;
import no.nav.inf.pselv.person.HentPersonFaultPenPersonIkkeFunnetMsg;
import no.nav.inf.pselv.person.HentPersonListeFaultPenGeneriskMsg;
import no.nav.inf.pselv.person.HentPersonListeFaultPenPersonIkkeFunnetMsg;
import no.nav.inf.pselv.person.HentSamboerforholdFaultPenGeneriskMsg;
import no.nav.inf.pselv.person.HentSamboerforholdFaultPenPersonIkkeFunnetMsg;
import no.nav.inf.pselv.person.LagreAdresseFaultPenGeneriskMsg;
import no.nav.inf.pselv.person.LagreAdresseFaultPenPersonIkkeFunnetMsg;
import no.nav.inf.pselv.person.LagreBrukerprofilFaultPenGeneriskMsg;
import no.nav.inf.pselv.person.LagreBrukerprofilFaultPenPersonIkkeFunnetMsg;
import no.nav.inf.pselv.person.LagreEpostFaultPenGeneriskMsg;
import no.nav.inf.pselv.person.LagreEpostFaultPenPersonIkkeFunnetMsg;
import no.nav.inf.pselv.person.LagreKontoinformasjonFaultPenGeneriskMsg;
import no.nav.inf.pselv.person.LagreKontoinformasjonFaultPenPersonIkkeFunnetMsg;
import no.nav.inf.pselv.person.LagreSprakFaultPenGeneriskMsg;
import no.nav.inf.pselv.person.LagreSprakFaultPenPersonIkkeFunnetMsg;
import no.nav.inf.pselv.person.LagreTelefonnumreFaultPenGeneriskMsg;
import no.nav.inf.pselv.person.LagreTelefonnumreFaultPenPersonIkkeFunnetMsg;
import no.nav.inf.pselv.person.OpprettSamboerforholdFaultPenAlleredeRegistrertSamboerforholdMsg;
import no.nav.inf.pselv.person.OpprettSamboerforholdFaultPenGeneriskMsg;
import no.nav.inf.pselv.person.OpprettSamboerforholdFaultPenPersonIkkeFunnetMsg;
import no.nav.inf.pselv.person.OpprettSamboerforholdFaultPenSamboerDodMsg;
import no.nav.inf.pselv.person.OpprettSamboerforholdFaultPenSamboerIFamilieMsg;
import no.nav.inf.pselv.person.OpprettSamboerforholdFaultPenSamboerIkkeFunnetMsg;
import no.nav.inf.pselv.person.OpprettSamboerforholdFaultPenSamboerValideringFeiletMsg;
import no.nav.inf.pselv.person.PSELVPerson;
import no.nav.inf.pselv.person.SlettAdresseFaultPenGeneriskAfMsg;
import no.nav.inf.pselv.person.SlettAdresseFaultPenPersonIkkeFunnetMsg;
import no.nav.inf.pselv.person.SlettSamboerforholdFaultPenGeneriskMsg;
import no.nav.inf.pselv.person.SlettSamboerforholdFaultPenPersonIkkeFunnetMsg;
import no.nav.inf.pselv.person.SlettSamboerforholdFaultPenSamboerIkkeFunnetMsg;
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

@WebService(targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf", name = "PSELVPerson")
@XmlSeeAlso({ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.ObjectFactory.class, no.nav.inf.pselv.person.ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.tjenestepensjon.ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.person.ObjectFactory.class, no.nav.lib.pen.psakpselv.fault.person.ObjectFactory.class})
@HandlerChain(file = "Handler-chain.xml")
public class PselvPersonServiceMockImpl implements PSELVPerson {
    private static final Logger logger = LoggerFactory.getLogger(PsakPersonServiceMockImpl.class);

    private final TestscenarioBuilderRepository repo;

    public PselvPersonServiceMockImpl(TestscenarioBuilderRepository repo) {
        this.repo = repo;
    }

    private Optional<ASBOPenPerson> getASBOPerson(String fodselsnummer) {
        return PsakpselvPersonAdapter.getPreviouslyConverted(fodselsnummer)
                .or(() -> hentFraRepo(fodselsnummer))
                .or(() -> logIkkeFunnet(fodselsnummer));
    }

    private Optional<ASBOPenPerson> hentFraRepo(String fodselsnummer) {
        return repo.getPersonIndeks().getAlleSøkere().parallelStream()
                .filter(p -> p.getSøker().getIdent().equals(fodselsnummer))
                .map(PsakpselvPersonAdapter::toASBOPerson)
                .findFirst();
    }

    private Optional<ASBOPenPerson> logIkkeFunnet(String fodselsnummer) {
        logger.warn("Klarte ikke å finne person med fnr: " + fodselsnummer + " verken i repo eller blant konverterte: " + PsakpselvPersonAdapter.getPreviouslyConverted());
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
            name = "hentEnhetIdResponse",
            targetNamespace = ""
    )
    public ASBOPenPerson hentEnhetId(@WebParam(name = "hentEnhetIdRequest",targetNamespace = "") ASBOPenPerson var1) throws HentEnhetIdFaultPenGeneriskMsg,
            HentEnhetIdFaultPenPersonIkkeFunnetMsg {
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
            name = "slettAdresseResponse",
            targetNamespace = ""
    )
    public ASBOPenTomRespons slettAdresse(@WebParam(name = "slettAdresseRequest",targetNamespace = "") ASBOPenSlettAdresseRequest var1) throws SlettAdresseFaultPenGeneriskAfMsg,
            SlettAdresseFaultPenPersonIkkeFunnetMsg {
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
            name = "lagreTelefonnumreResponse",
            targetNamespace = ""
    )
    @Override
    public ASBOPenTomRespons lagreTelefonnumre(@WebParam(name = "lagreTelefonnumreRequest",targetNamespace = "") ASBOPenLagreTelefonnumreRequest var1) throws
            LagreTelefonnumreFaultPenGeneriskMsg, LagreTelefonnumreFaultPenPersonIkkeFunnetMsg {
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
            name = "lagreSprakResponse",
            targetNamespace = ""
    )
    @Override
    public ASBOPenTomRespons lagreSprak(@WebParam(name = "lagreSprakRequest",targetNamespace = "") ASBOPenPerson var1) throws LagreSprakFaultPenGeneriskMsg,
            LagreSprakFaultPenPersonIkkeFunnetMsg {
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
            name = "opprettSamboerforholdResponse",
            targetNamespace = ""
    )
    @Override
    public ASBOPenTomRespons opprettSamboerforhold(@WebParam(name = "opprettSamboerforholdRequest",targetNamespace = "") ASBOPenPerson var1) throws
            OpprettSamboerforholdFaultPenGeneriskMsg, OpprettSamboerforholdFaultPenSamboerIFamilieMsg, OpprettSamboerforholdFaultPenSamboerIkkeFunnetMsg,
            OpprettSamboerforholdFaultPenSamboerValideringFeiletMsg, OpprettSamboerforholdFaultPenAlleredeRegistrertSamboerforholdMsg,
            OpprettSamboerforholdFaultPenPersonIkkeFunnetMsg, OpprettSamboerforholdFaultPenSamboerDodMsg {
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
            name = "hentBrukerprofilResponse",
            targetNamespace = ""
    )
    @Override
    public ASBOPenPerson hentBrukerprofil(@WebParam(name = "hentBrukerprofilRequest",targetNamespace = "") ASBOPenPerson var1) throws HentBrukerprofilFaultPenGeneriskMsg,
            HentBrukerprofilFaultPenBrukerprofilIkkeFunnetMsg {
        throw new UnsupportedOperationException("Ikke implementert");
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
            name = "hentPersonResponse",
            targetNamespace = ""
    )
    @Override
    public ASBOPenPerson hentPerson(@WebParam(name = "hentPersonRequest",targetNamespace = "") ASBOPenHentPersonRequest hentPersonRequest) throws HentPersonFaultPenGeneriskMsg,
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
            name = "hentKontoinformasjonResponse",
            targetNamespace = ""
    )
    @Override
    public ASBOPenPerson hentKontoinformasjon(@WebParam(name = "hentKontoinformasjonRequest",targetNamespace = "") ASBOPenPerson var1) throws HentKontoinformasjonFaultPenGeneriskMsg,
            HentKontoinformasjonFaultPenPersonIkkeFunnetMsg {
        throw new UnsupportedOperationException("Ikke implementert");
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
            name = "hentSamboerforholdResponse",
            targetNamespace = ""
    )
    @Override
    public ASBOPenPerson hentSamboerforhold(@WebParam(name = "hentSamboerforholdRequest",targetNamespace = "") ASBOPenHentSamboerforholdRequest var1) throws
            HentSamboerforholdFaultPenGeneriskMsg, HentSamboerforholdFaultPenPersonIkkeFunnetMsg {
        throw new UnsupportedOperationException("Ikke implementert");
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
            name = "lagreAdresseResponse",
            targetNamespace = ""
    )
    @Override
    public ASBOPenTomRespons lagreAdresse(@WebParam(name = "lagreAdresseRequest",targetNamespace = "") ASBOPenLagreAdresseRequest var1) throws LagreAdresseFaultPenGeneriskMsg,
            LagreAdresseFaultPenPersonIkkeFunnetMsg {
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
            name = "slettSamboerResponse",
            targetNamespace = ""
    )
    @Override
    public ASBOPenTomRespons slettSamboerforhold(@WebParam(name = "slettSamboerRequest",targetNamespace = "") ASBOPenPerson var1) throws SlettSamboerforholdFaultPenGeneriskMsg,
            SlettSamboerforholdFaultPenPersonIkkeFunnetMsg, SlettSamboerforholdFaultPenSamboerIkkeFunnetMsg {
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
            name = "lagreEpostResponse",
            targetNamespace = ""
    )
    @Override
    public ASBOPenTomRespons lagreEpost(@WebParam(name = "lagreEpostRequest",targetNamespace = "") ASBOPenPerson var1) throws LagreEpostFaultPenGeneriskMsg,
            LagreEpostFaultPenPersonIkkeFunnetMsg {
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
            name = "erEgenansattResponse",
            targetNamespace = ""
    )
    @Override
    public Boolean erEgenansatt(@WebParam(name = "erEgenansattRequest",targetNamespace = "") ASBOPenPerson var1) throws ErEgenansattFaultPenGeneriskMsg,
            ErEgenansattFaultPenPersonIkkeFunnetMsg {
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
            name = "hentFamilierelasjonsHistorikkResponse",
            targetNamespace = ""
    )
    @Override
    public ASBOPenPerson hentFamilierelasjonsHistorikk(@WebParam(name = "hentFamilierelasjonsHistorikkRequest",targetNamespace = "") ASBOPenHentFamilierelasjonsHistorikkRequest var1) throws
            HentFamilierelasjonsHistorikkFaultPenGeneriskMsg, HentFamilierelasjonsHistorikkFaultPenPersonIkkeFunnetMsg {
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
            name = "hentFamilierelasjonerResponse",
            targetNamespace = ""
    )
    @Override
    public ASBOPenPerson hentFamilierelasjoner(@WebParam(name = "hentFamilierelasjonerRequest",targetNamespace = "") ASBOPenHentFamilierelasjonerRequest var1) throws
            HentFamilierelasjonerFaultPenGeneriskMsg, HentFamilierelasjonerFaultPenPersonIkkeFunnetMsg {
        throw new UnsupportedOperationException("Ikke implementert");
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
            name = "lagreKontoinformasjonResponse",
            targetNamespace = ""
    )
    @Override
    public ASBOPenTomRespons lagreKontoinformasjon(@WebParam(name = "lagreKontoinformasjonRequest",targetNamespace = "") ASBOPenPerson var1) throws
            LagreKontoinformasjonFaultPenGeneriskMsg, LagreKontoinformasjonFaultPenPersonIkkeFunnetMsg {
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
            name = "lagreBrukerprofilResponse",
            targetNamespace = ""
    )
    @Override
    public ASBOPenTomRespons lagreBrukerprofil(@WebParam(name = "lagreBrukerprofilRequest",targetNamespace = "") ASBOPenPerson var1) throws LagreBrukerprofilFaultPenGeneriskMsg,
            LagreBrukerprofilFaultPenPersonIkkeFunnetMsg {
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
            name = "hentPersonListeResponse",
            targetNamespace = ""
    )
    @Override
    public ASBOPenPersonListe hentPersonListe(@WebParam(name = "hentPersonListeRequest",targetNamespace = "") ASBOPenHentPersonListeRequest var1) throws
            HentPersonListeFaultPenGeneriskMsg, HentPersonListeFaultPenPersonIkkeFunnetMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }
}
