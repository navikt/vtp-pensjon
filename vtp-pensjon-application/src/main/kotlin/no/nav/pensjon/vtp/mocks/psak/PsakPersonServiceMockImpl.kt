package no.nav.pensjon.vtp.mocks.psak

import no.nav.inf.psak.person.*
import no.nav.lib.pen.psakpselv.asbo.ASBOPenTomRespons
import no.nav.lib.pen.psakpselv.asbo.person.*
import no.nav.pensjon.vtp.annotations.SoapService
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModell
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository
import javax.jws.*
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper
import javax.xml.ws.soap.Addressing

@SoapService(path = ["/esb/nav-cons-pen-psak-personWeb/sca/PSAKPersonWSEXP"])
@Addressing
@WebService(targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf", name = "PSAKPerson")
@HandlerChain(file = "/Handler-chain.xml")
class PsakPersonServiceMockImpl(
    private val personModellRepository: PersonModellRepository,
    private val psakpselvPersonAdapter: PsakpselvPersonAdapter
) : PSAKPerson {
    @WebMethod
    @RequestWrapper(
        localName = "finnPerson",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.FinnPerson"
    )
    @ResponseWrapper(
        localName = "finnPersonResponse",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.FinnPersonResponse"
    )
    @WebResult(name = "finnPersonResponse")
    override fun finnPerson(
        @WebParam(name = "finnPersonRequest") finnPersonRequest: ASBOPenFinnPersonRequest
    ) = ASBOPenFinnPersonResponse().apply {
        personer = ASBOPenPersonListe().apply {
            personer = personModellRepository.findAll()
                .map(PersonModell::ident).mapNotNull(psakpselvPersonAdapter::getASBOPenPerson)
                .toTypedArray()
        }
    }

    @WebMethod
    @RequestWrapper(
        localName = "lagreSprak",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.LagreSprak"
    )
    @ResponseWrapper(
        localName = "lagreSprakResponse",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.LagreSprakResponse"
    )
    @WebResult(name = "lagreSprakResponse", targetNamespace = "")
    override fun lagreSprak(lagreSprakRequest: ASBOPenPerson) = ASBOPenTomRespons()

    @WebMethod
    @RequestWrapper(
        localName = "opprettSamboerforhold",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.OpprettSamboerforhold"
    )
    @ResponseWrapper(
        localName = "opprettSamboerforholdResponse",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.OpprettSamboerforholdResponse"
    )
    @WebResult(name = "opprettSamboerforholdResponse", targetNamespace = "")
    override fun opprettSamboerforhold(opprettSamboerforholdRequest: ASBOPenPerson) = ASBOPenTomRespons()

    @WebMethod
    @RequestWrapper(
        localName = "hentBrukerprofil",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.HentBrukerprofil"
    )
    @ResponseWrapper(
        localName = "hentBrukerprofilResponse",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.HentBrukerprofilResponse"
    )
    @WebResult(name = "hentBrukerprofilResponse")
    override fun hentBrukerprofil(
        @WebParam(name = "hentBrukerprofilRequest") hentBrukerprofilRequest: ASBOPenPerson
    ) = psakpselvPersonAdapter.getASBOPenPerson(hentBrukerprofilRequest.fodselsnummer)
        ?: throw HentBrukerprofilFaultPenBrukerprofilIkkeFunnetMsg()

    @WebMethod
    @RequestWrapper(
        localName = "hentEnhetId",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.HentEnhetId"
    )
    @ResponseWrapper(
        localName = "hentEnhetIdResponse",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.HentEnhetIdResponse"
    )
    @WebResult(name = "hentEnhetIdResponse", targetNamespace = "")
    override fun hentEnhetId(
        @WebParam(
            name = "hentEnhetIdRequest",
            targetNamespace = ""
        ) hentEnhetIdRequest: ASBOPenPerson
    ) = psakpselvPersonAdapter.getASBOPenPerson(hentEnhetIdRequest.fodselsnummer)
        ?: throw HentEnhetIdFaultPenPersonIkkeFunnetMsg()

    override fun slettAdresse(slettAdresseRequest: ASBOPenSlettAdresseRequest) =
        throw NotImplementedException()

    @WebMethod
    @RequestWrapper(
        localName = "lagreEpost",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.LagreEpost"
    )
    @ResponseWrapper(
        localName = "lagreEpostResponse",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.LagreEpostResponse"
    )
    @WebResult(name = "lagreEpostResponse", targetNamespace = "")
    override fun lagreEpost(lagreEpostRequest: ASBOPenPerson) = ASBOPenTomRespons()

    @WebMethod
    @RequestWrapper(
        localName = "hentFamilierelasjoner",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.HentFamilierelasjoner"
    )
    @ResponseWrapper(
        localName = "hentFamilierelasjonerResponse",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.HentFamilierelasjonerResponse"
    )
    @WebResult(name = "hentFamilierelasjonerResponse")
    override fun hentFamilierelasjoner(
        @WebParam(name = "hentFamilierelasjonerRequest") hentFamilierelasjonerRequest: ASBOPenHentFamilierelasjonerRequest
    ) = psakpselvPersonAdapter.getASBOPenPerson(hentFamilierelasjonerRequest.fodselsnummer)
        ?: throw HentFamilierelasjonerFaultPenPersonIkkeFunnetMsg()

    @WebMethod
    @RequestWrapper(
        localName = "hentSamboerforhold",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.HentSamboerforhold"
    )
    @ResponseWrapper(
        localName = "hentSamboerforholdResponse",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.HentSamboerforholdResponse"
    )
    @WebResult(name = "hentSamboerforholdResponse")
    override fun hentSamboerforhold(
        @WebParam(name = "hentSamboerforholdRequest") hentSamboerforholdRequest: ASBOPenHentSamboerforholdRequest
    ) = psakpselvPersonAdapter.getASBOPenPerson(hentSamboerforholdRequest.fodselsnummer)
        ?: throw HentSamboerforholdFaultPenGeneriskMsg()

    override fun lagreDodsdato(lagreDodsdatoRequest: ASBOPenPerson) = throw NotImplementedException()

    @WebMethod
    @RequestWrapper(
        localName = "lagreAdresse",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.LagreAdresse"
    )
    @ResponseWrapper(
        localName = "lagreAdresseResponse",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.LagreAdresseResponse"
    )
    @WebResult(name = "lagreAdresseResponse", targetNamespace = "")
    override fun lagreAdresse(lagreAdresseRequest: ASBOPenLagreAdresseRequest) = ASBOPenTomRespons()

    override fun lagreStatsborgerskap(lagreStatsborgerskapRequest: ASBOPenPerson) =
        throw NotImplementedException()

    @WebMethod
    @RequestWrapper(
        localName = "lagreTelefonnumre",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.LagreTelefonnumre"
    )
    @ResponseWrapper(
        localName = "lagreTelefonnumreResponse",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.LagreTelefonnumreResponse"
    )
    @WebResult(name = "lagreTelefonnumreResponse", targetNamespace = "")
    override fun lagreTelefonnumre(lagreTelefonnumreRequest: ASBOPenLagreTelefonnumreRequest) = ASBOPenTomRespons()

    override fun lagreFamilierelasjon(lagreFamilierelasjonRequest: ASBOPenPerson) =
        throw NotImplementedException()

    override fun opprettFamilierelasjon(opprettFamilierelasjonRequest: ASBOPenPerson) =
        throw NotImplementedException()

    override fun lagreNavn(lagreNavnRequest: ASBOPenPerson) = throw NotImplementedException()

    override fun lagreSivilstand(lagreSivilstandRequest: ASBOPenPerson) = throw NotImplementedException()

    override fun lagreHistoriskSamboerforhold(lagreHistoriskSamboerforholdRequest: ASBOPenLagreHistoriskSamboerforholdRequest) =
        throw NotImplementedException()

    override fun hentPersonUtlandHistorikkListe(hentPersonUtlandHistorikkListeRequest: ASBOPenHentPersonUtlandsHistorikkListeRequest) =
        throw NotImplementedException()

    @WebMethod
    @RequestWrapper(
        localName = "erEgenansatt",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.ErEgenansatt"
    )
    @ResponseWrapper(
        localName = "erEgenansattResponse",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.ErEgenansattResponse"
    )
    @WebResult(name = "erEgenansattResponse", targetNamespace = "")
    override fun erEgenansatt(
        @WebParam(
            name = "erEgenansattRequest",
            targetNamespace = ""
        ) erEgenansattRequest: ASBOPenPerson
    ): Boolean = erEgenansattRequest.erEgenansatt

    @WebMethod
    @RequestWrapper(
        localName = "hentPersonUtland",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.HentPersonUtland"
    )
    @ResponseWrapper(
        localName = "hentPersonUtlandResponse",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.HentPersonUtlandResponse"
    )
    @WebResult(name = "hentPersonUtlandResponse", targetNamespace = "")
    override fun hentPersonUtland(
        @WebParam(
            name = "hentPersonUtlandRequest",
            targetNamespace = ""
        ) hentPersonUtlandRequest: ASBOPenPerson
    ) = psakpselvPersonAdapter.getASBOPenPerson(hentPersonUtlandRequest.fodselsnummer)
        ?: throw HentPersonUtlandFaultPenPersonIkkeFunnetMsg()

    @WebMethod
    @RequestWrapper(
        localName = "lagreBrukerprofil",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.LagreBrukerprofil"
    )
    @ResponseWrapper(
        localName = "lagreBrukerprofilResponse",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.LagreBrukerprofilResponse"
    )
    @WebResult(name = "lagreBrukerprofilResponse", targetNamespace = "")
    override fun lagreBrukerprofil(lagreBrukerprofilRequest: ASBOPenPerson) = ASBOPenTomRespons()

    @WebMethod
    @RequestWrapper(
        localName = "lagreKontoinformasjon",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.LagreKontoinformasjon"
    )
    @ResponseWrapper(
        localName = "lagreKontoinformasjonResponse",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.LagreKontoinformasjonResponse"
    )
    @WebResult(name = "lagrekontoinformasjonResponse", targetNamespace = "")
    override fun lagreKontoinformasjon(lagreKontoinformasjonRequest: ASBOPenPerson) = ASBOPenTomRespons()

    @WebMethod
    @RequestWrapper(
        localName = "hentHistorikk",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.HentHistorikk"
    )
    @ResponseWrapper(
        localName = "hentHistorikkResponse",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.HentHistorikkResponse"
    )
    @WebResult(name = "hentHistorikkResponse", targetNamespace = "")
    override fun hentHistorikk(
        @WebParam(
            name = "hentHistorikkRequest",
            targetNamespace = ""
        ) hentHistorikkRequest: ASBOPenHentHistorikkRequest
    ) = psakpselvPersonAdapter.getASBOPenPerson(hentHistorikkRequest.fodselsnummer)
        ?: throw HentHistorikkFaultPenPersonIkkeFunnetMsg()

    @WebMethod
    @RequestWrapper(
        localName = "hentKontoinformasjon",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.HentKontoinformasjon"
    )
    @ResponseWrapper(
        localName = "hentKontoinformasjonResponse",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.HentKontoinformasjonResponse"
    )
    @WebResult(name = "hentKontoinformasjonsResponse")
    override fun hentKontoinformasjon(
        @WebParam(name = "hentKontoinformasjonRequest") hentKontoinformasjonRequest: ASBOPenPerson
    ) = psakpselvPersonAdapter.getASBOPenPerson(hentKontoinformasjonRequest.fodselsnummer)
        ?: throw HentKontoinformasjonFaultPenPersonIkkeFunnetMsg()

    @WebMethod
    @RequestWrapper(
        localName = "hentPerson",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.HentPerson"
    )
    @ResponseWrapper(
        localName = "hentPersonResponse",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.HentPersonResponse"
    )
    @WebResult(name = "hentPersonResponse")
    override fun hentPerson(
        @WebParam(name = "hentPersonRequest") hentPersonRequest: ASBOPenHentPersonRequest
    ) = psakpselvPersonAdapter.getASBOPenPerson(hentPersonRequest.person.fodselsnummer)
        ?: throw HentPersonFaultPenPersonIkkeFunnetMsg()

    @WebMethod
    @RequestWrapper(
        localName = "lagreSamboerforhold",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.LagreSamboerforhold"
    )
    @ResponseWrapper(
        localName = "lagreSamboerforholdResponse",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.LagreSamboerforholdResponse"
    )
    @WebResult(name = "lagreSamboerforholdResponse", targetNamespace = "")
    override fun lagreSamboerforhold(lagreSamboerforholdRequest: ASBOPenPerson) = ASBOPenTomRespons()

    @WebMethod
    @RequestWrapper(
        localName = "slettSamboerforhold",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.SlettSamboerforhold"
    )
    @ResponseWrapper(
        localName = "slettSamboerforholdResponse",
        targetNamespace = "http://nav-cons-pen-psak-person/no/nav/inf",
        className = "no.nav.inf.psak.person.SlettSamboerforholdResponse"
    )
    @WebResult(name = "slettSamboerforholdResponse", targetNamespace = "")
    override fun slettSamboerforhold(slettSamboerforholdRequest: ASBOPenPerson) = ASBOPenTomRespons()
}
