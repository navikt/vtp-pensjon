package no.nav.pensjon.vtp.mocks.psak;

import java.util.Optional;

import no.nav.pensjon.vtp.core.annotations.SoapService;
import no.nav.inf.psak.person.*;
import no.nav.lib.pen.psakpselv.asbo.ASBOPenTomRespons;
import no.nav.lib.pen.psakpselv.asbo.person.*;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository;

import javax.jws.*;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;

@SoapService(path = "/esb/nav-cons-pen-psak-personWeb/sca/PSAKPersonWSEXP")
@Addressing
@WebService(targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", name = "PSAKPerson")
@HandlerChain(file = "/Handler-chain.xml")
public class PsakPersonServiceMockImpl implements PSAKPerson {
    private final PersonModellRepository personModellRepository;
    private final PsakpselvPersonAdapter psakpselvPersonAdapter;

    public PsakPersonServiceMockImpl(PersonModellRepository personModellRepository, PsakpselvPersonAdapter psakpselvPersonAdapter) {
        this.personModellRepository = personModellRepository;
        this.psakpselvPersonAdapter = psakpselvPersonAdapter;
    }

    @Override
    @WebMethod
    @RequestWrapper(localName = "finnPerson", targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", className = "no.nav.inf.psak.person.FinnPerson")
    @ResponseWrapper(localName = "finnPersonResponse", targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", className = "no.nav.inf.psak.person.FinnPersonResponse")
    @WebResult(name = "finnPersonResponse")
    public ASBOPenFinnPersonResponse finnPerson(
            @WebParam(name = "finnPersonRequest") no.nav.lib.pen.psakpselv.asbo.person.ASBOPenFinnPersonRequest finnPersonRequest
    ) {
        ASBOPenFinnPersonResponse asboPenFinnPersonResponse = new ASBOPenFinnPersonResponse();
        ASBOPenPersonListe liste = new ASBOPenPersonListe();
        liste.setPersoner(personModellRepository.findAll()
                .stream().map(PersonModell::getIdent)
                .map(psakpselvPersonAdapter::getASBOPenPerson)
                .flatMap(Optional::stream)
                .toArray(ASBOPenPerson[]::new));

        asboPenFinnPersonResponse.setPersoner(liste);
        return asboPenFinnPersonResponse;
    }

    @Override
    public ASBOPenTomRespons lagreSprak(ASBOPenPerson lagreSprakRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenTomRespons opprettSamboerforhold(ASBOPenPerson opprettSamboerforholdRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod
    @RequestWrapper(localName = "hentBrukerprofil", targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", className = "no.nav.inf.psak.person.HentBrukerprofil")
    @ResponseWrapper(localName = "hentBrukerprofilResponse", targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", className = "no.nav.inf.psak.person.HentBrukerprofilResponse")
    @WebResult(name = "hentBrukerprofilResponse")
    public no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson hentBrukerprofil(
            @WebParam(name = "hentBrukerprofilRequest")
                    no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson hentBrukerprofilRequest
    ) throws HentBrukerprofilFaultPenBrukerprofilIkkeFunnetMsg {
        return psakpselvPersonAdapter.getASBOPenPerson(hentBrukerprofilRequest.getFodselsnummer()).orElseThrow(HentBrukerprofilFaultPenBrukerprofilIkkeFunnetMsg::new);
    }

    @Override
    public ASBOPenPerson hentEnhetId(ASBOPenPerson hentEnhetIdRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public String slettAdresse(ASBOPenSlettAdresseRequest slettAdresseRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenTomRespons lagreEpost(ASBOPenPerson lagreEpostRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod
    @RequestWrapper(localName = "hentFamilierelasjoner", targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", className = "no.nav.inf.psak.person.HentFamilierelasjoner")
    @ResponseWrapper(localName = "hentFamilierelasjonerResponse", targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", className = "no.nav.inf.psak.person.HentFamilierelasjonerResponse")
    @WebResult(name = "hentFamilierelasjonerResponse")
    public no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson hentFamilierelasjoner(
            @WebParam(name = "hentFamilierelasjonerRequest")
                    no.nav.lib.pen.psakpselv.asbo.person.ASBOPenHentFamilierelasjonerRequest hentFamilierelasjonerRequest
    ) throws HentFamilierelasjonerFaultPenPersonIkkeFunnetMsg {
        return psakpselvPersonAdapter.getASBOPenPerson(hentFamilierelasjonerRequest.getFodselsnummer()).orElseThrow(HentFamilierelasjonerFaultPenPersonIkkeFunnetMsg::new);
    }

    @Override
    @WebMethod
    @RequestWrapper(localName = "hentSamboerforhold", targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", className = "no.nav.inf.psak.person.HentSamboerforhold")
    @ResponseWrapper(localName = "hentSamboerforholdResponse", targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", className = "no.nav.inf.psak.person.HentSamboerforholdResponse")
    @WebResult(name = "hentSamboerforholdResponse")
    public no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson hentSamboerforhold(
            @WebParam(name = "hentSamboerforholdRequest")
                    no.nav.lib.pen.psakpselv.asbo.person.ASBOPenHentSamboerforholdRequest hentSamboerforholdRequest
    ) throws HentSamboerforholdFaultPenGeneriskMsg {
        return psakpselvPersonAdapter.getASBOPenPerson(hentSamboerforholdRequest.getFodselsnummer()).orElseThrow(HentSamboerforholdFaultPenGeneriskMsg::new);
    }

    @Override
    public String lagreDodsdato(ASBOPenPerson lagreDodsdatoRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenTomRespons lagreAdresse(ASBOPenLagreAdresseRequest lagreAdresseRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public String lagreStatsborgerskap(ASBOPenPerson lagreStatsborgerskapRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenTomRespons lagreTelefonnumre(ASBOPenLagreTelefonnumreRequest lagreTelefonnumreRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public String lagreFamilierelasjon(ASBOPenPerson lagreFamilierelasjonRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public String opprettFamilierelasjon(ASBOPenPerson opprettFamilierelasjonRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public String lagreNavn(ASBOPenPerson lagreNavnRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public String lagreSivilstand(ASBOPenPerson lagreSivilstandRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public String lagreHistoriskSamboerforhold(ASBOPenLagreHistoriskSamboerforholdRequest lagreHistoriskSamboerforholdRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenUtlandHistorikk hentPersonUtlandHistorikkListe(ASBOPenHentPersonUtlandsHistorikkListeRequest hentPersonUtlandHistorikkListeRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public Boolean erEgenansatt(ASBOPenPerson erEgenansattRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenPerson hentPersonUtland(ASBOPenPerson hentPersonUtlandRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenTomRespons lagreBrukerprofil(ASBOPenPerson lagreBrukerprofilRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenTomRespons lagreKontoinformasjon(ASBOPenPerson lagreKontoinformasjonRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenPerson hentHistorikk(ASBOPenHentHistorikkRequest hentHistorikkRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod
    @RequestWrapper(localName = "hentKontoinformasjon", targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", className = "no.nav.inf.psak.person.HentKontoinformasjon")
    @ResponseWrapper(localName = "hentKontoinformasjonResponse", targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", className = "no.nav.inf.psak.person.HentKontoinformasjonResponse")
    @WebResult(name = "hentKontoinformasjonsResponse")
    public no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson hentKontoinformasjon(
            @WebParam(name = "hentKontoinformasjonRequest")
                    no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson hentKontoinformasjonRequest
    ) throws HentKontoinformasjonFaultPenPersonIkkeFunnetMsg {
        return psakpselvPersonAdapter.getASBOPenPerson(hentKontoinformasjonRequest.getFodselsnummer()).orElseThrow(HentKontoinformasjonFaultPenPersonIkkeFunnetMsg::new);
    }

    @Override
    @WebMethod
    @RequestWrapper(localName = "hentPerson", targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", className = "no.nav.inf.psak.person.HentPerson")
    @ResponseWrapper(localName = "hentPersonResponse", targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", className = "no.nav.inf.psak.person.HentPersonResponse")
    @WebResult(name = "hentPersonResponse")
    public ASBOPenPerson hentPerson(
            @WebParam(name = "hentPersonRequest") no.nav.lib.pen.psakpselv.asbo.person.ASBOPenHentPersonRequest hentPersonRequest)
            throws HentPersonFaultPenPersonIkkeFunnetMsg {
        return psakpselvPersonAdapter.getASBOPenPerson(hentPersonRequest.getPerson().getFodselsnummer()).orElseThrow(HentPersonFaultPenPersonIkkeFunnetMsg::new);
    }

    @Override
    public ASBOPenTomRespons lagreSamboerforhold(ASBOPenPerson lagreSamboerforholdRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenTomRespons slettSamboerforhold(ASBOPenPerson slettSamboerforholdRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }
}
