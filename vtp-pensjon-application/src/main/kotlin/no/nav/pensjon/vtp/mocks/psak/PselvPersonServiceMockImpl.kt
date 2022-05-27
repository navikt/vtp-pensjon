package no.nav.pensjon.vtp.mocks.psak

import no.nav.inf.pselv.person.*
import no.nav.lib.pen.psakpselv.asbo.person.*
import no.nav.lib.pen.psakpselv.fault.ObjectFactory
import no.nav.pensjon.vtp.annotations.SoapService
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import javax.jws.*
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-cons-pen-pselv-personWeb/sca/PSELVPersonWSEXP"])
@WebService(targetNamespace = "http://nav-cons-pen-pselv-person/no/nav/inf", name = "PSELVPerson")
@XmlSeeAlso(
    ObjectFactory::class,
    no.nav.lib.pen.psakpselv.asbo.ObjectFactory::class,
    no.nav.inf.pselv.person.ObjectFactory::class,
    no.nav.lib.pen.psakpselv.asbo.tjenestepensjon.ObjectFactory::class,
    no.nav.lib.pen.psakpselv.asbo.person.ObjectFactory::class,
    no.nav.lib.pen.psakpselv.fault.person.ObjectFactory::class
)
@HandlerChain(file = "/Handler-chain.xml")
class PselvPersonServiceMockImpl(private val psakpselvPersonAdapter: PsakpselvPersonAdapter) : PSELVPerson {

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
    @WebResult(name = "hentEnhetIdResponse")
    override fun hentEnhetId(@WebParam(name = "hentEnhetIdRequest") var1: ASBOPenPerson) =
        throw NotImplementedException()

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
    @WebResult(name = "slettAdresseResponse")
    override fun slettAdresse(@WebParam(name = "slettAdresseRequest") var1: ASBOPenSlettAdresseRequest) =
        throw NotImplementedException()

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
    @WebResult(name = "lagreTelefonnumreResponse")
    override fun lagreTelefonnumre(@WebParam(name = "lagreTelefonnumreRequest") var1: ASBOPenLagreTelefonnumreRequest) =
        throw NotImplementedException()

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
    @WebResult(name = "lagreSprakResponse")
    override fun lagreSprak(@WebParam(name = "lagreSprakRequest") var1: ASBOPenPerson) =
        throw NotImplementedException()

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
    @WebResult(name = "opprettSamboerforholdResponse")
    override fun opprettSamboerforhold(@WebParam(name = "opprettSamboerforholdRequest") var1: ASBOPenPerson) =
        throw NotImplementedException()

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
    @WebResult(name = "hentBrukerprofilResponse")
    @Throws(
        HentBrukerprofilFaultPenBrukerprofilIkkeFunnetMsg::class
    )
    override fun hentBrukerprofil(@WebParam(name = "hentBrukerprofilRequest") hentBrukerprofilRequest: ASBOPenPerson) =
        psakpselvPersonAdapter.getASBOPenPerson(hentBrukerprofilRequest.fodselsnummer)
            ?: throw HentBrukerprofilFaultPenBrukerprofilIkkeFunnetMsg()

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
    @WebResult(name = "hentPersonResponse")
    @Throws(
        HentPersonFaultPenPersonIkkeFunnetMsg::class
    )
    override fun hentPerson(@WebParam(name = "hentPersonRequest") hentPersonRequest: ASBOPenHentPersonRequest) =
        psakpselvPersonAdapter.getASBOPenPerson(hentPersonRequest.person.fodselsnummer)
            ?: throw HentPersonFaultPenPersonIkkeFunnetMsg()

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
    @WebResult(name = "hentKontoinformasjonResponse")
    @Throws(
        HentKontoinformasjonFaultPenPersonIkkeFunnetMsg::class
    )
    override fun hentKontoinformasjon(@WebParam(name = "hentKontoinformasjonRequest") hentKontoinformasjonRequest: ASBOPenPerson) =
        psakpselvPersonAdapter.getASBOPenPerson(hentKontoinformasjonRequest.fodselsnummer)
            ?: throw HentKontoinformasjonFaultPenPersonIkkeFunnetMsg()

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
    @WebResult(name = "hentSamboerforholdResponse")
    @Throws(
        HentSamboerforholdFaultPenGeneriskMsg::class
    )
    override fun hentSamboerforhold(@WebParam(name = "hentSamboerforholdRequest") hentSamboerforholdRequest: ASBOPenHentSamboerforholdRequest) =
        psakpselvPersonAdapter.getASBOPenPerson(hentSamboerforholdRequest.fodselsnummer)
            ?: throw HentSamboerforholdFaultPenGeneriskMsg()

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
    @WebResult(name = "lagreAdresseResponse")
    override fun lagreAdresse(@WebParam(name = "lagreAdresseRequest") var1: ASBOPenLagreAdresseRequest) =
        throw NotImplementedException()

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
    @WebResult(name = "slettSamboerResponse")
    override fun slettSamboerforhold(@WebParam(name = "slettSamboerRequest") var1: ASBOPenPerson) =
        throw NotImplementedException()

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
    @WebResult(name = "lagreEpostResponse")
    override fun lagreEpost(@WebParam(name = "lagreEpostRequest") var1: ASBOPenPerson) =
        throw NotImplementedException()

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
    @WebResult(name = "erEgenansattResponse")
    override fun erEgenansatt(@WebParam(name = "erEgenansattRequest") var1: ASBOPenPerson) =
        throw NotImplementedException()

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
    @WebResult(name = "hentFamilierelasjonsHistorikkResponse")
    override fun hentFamilierelasjonsHistorikk(@WebParam(name = "hentFamilierelasjonsHistorikkRequest") var1: ASBOPenHentFamilierelasjonsHistorikkRequest) =
        psakpselvPersonAdapter.getASBOPenPerson(var1.fnr)
            ?: throw HentSamboerforholdFaultPenGeneriskMsg()

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
    @WebResult(name = "hentFamilierelasjonerResponse")
    @Throws(
        HentFamilierelasjonerFaultPenPersonIkkeFunnetMsg::class
    )
    override fun hentFamilierelasjoner(@WebParam(name = "hentFamilierelasjonerRequest") hentFamilierelasjonerRequest: ASBOPenHentFamilierelasjonerRequest) =
        psakpselvPersonAdapter.getASBOPenPerson(hentFamilierelasjonerRequest.fodselsnummer)
            ?: throw HentFamilierelasjonerFaultPenPersonIkkeFunnetMsg()

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
    @WebResult(name = "lagreKontoinformasjonResponse")
    override fun lagreKontoinformasjon(@WebParam(name = "lagreKontoinformasjonRequest") var1: ASBOPenPerson) =
        throw NotImplementedException()

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
    @WebResult(name = "lagreBrukerprofilResponse")
    override fun lagreBrukerprofil(@WebParam(name = "lagreBrukerprofilRequest") var1: ASBOPenPerson) =
        throw NotImplementedException()

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
    @WebResult(name = "hentPersonListeResponse")
    override fun hentPersonListe(@WebParam(name = "hentPersonListeRequest") var1: ASBOPenHentPersonListeRequest) =
        throw NotImplementedException()
}
