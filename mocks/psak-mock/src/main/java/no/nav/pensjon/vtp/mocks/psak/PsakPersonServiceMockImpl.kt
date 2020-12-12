package no.nav.pensjon.vtp.mocks.psak

import no.nav.inf.psak.person.*
import no.nav.lib.pen.psakpselv.asbo.ASBOPenTomRespons
import no.nav.lib.pen.psakpselv.asbo.person.*
import no.nav.pensjon.vtp.core.annotations.SoapService
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

    override fun lagreSprak(lagreSprakRequest: ASBOPenPerson): ASBOPenTomRespons {
        throw UnsupportedOperationException("Ikke implementert")
    }

    override fun opprettSamboerforhold(opprettSamboerforholdRequest: ASBOPenPerson): ASBOPenTomRespons {
        throw UnsupportedOperationException("Ikke implementert")
    }

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
    @Throws(HentBrukerprofilFaultPenBrukerprofilIkkeFunnetMsg::class)
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

    override fun slettAdresse(slettAdresseRequest: ASBOPenSlettAdresseRequest): String {
        throw UnsupportedOperationException("Ikke implementert")
    }

    override fun lagreEpost(lagreEpostRequest: ASBOPenPerson): ASBOPenTomRespons {
        throw UnsupportedOperationException("Ikke implementert")
    }

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
    @Throws(HentFamilierelasjonerFaultPenPersonIkkeFunnetMsg::class)
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
    @Throws(HentSamboerforholdFaultPenGeneriskMsg::class)
    override fun hentSamboerforhold(
        @WebParam(name = "hentSamboerforholdRequest") hentSamboerforholdRequest: ASBOPenHentSamboerforholdRequest
    ) = psakpselvPersonAdapter.getASBOPenPerson(hentSamboerforholdRequest.fodselsnummer)
        ?: throw HentSamboerforholdFaultPenGeneriskMsg()

    override fun lagreDodsdato(lagreDodsdatoRequest: ASBOPenPerson): String {
        throw UnsupportedOperationException("Ikke implementert")
    }

    override fun lagreAdresse(lagreAdresseRequest: ASBOPenLagreAdresseRequest): ASBOPenTomRespons {
        throw UnsupportedOperationException("Ikke implementert")
    }

    override fun lagreStatsborgerskap(lagreStatsborgerskapRequest: ASBOPenPerson): String {
        throw UnsupportedOperationException("Ikke implementert")
    }

    override fun lagreTelefonnumre(lagreTelefonnumreRequest: ASBOPenLagreTelefonnumreRequest): ASBOPenTomRespons {
        throw UnsupportedOperationException("Ikke implementert")
    }

    override fun lagreFamilierelasjon(lagreFamilierelasjonRequest: ASBOPenPerson): String {
        throw UnsupportedOperationException("Ikke implementert")
    }

    override fun opprettFamilierelasjon(opprettFamilierelasjonRequest: ASBOPenPerson): String {
        throw UnsupportedOperationException("Ikke implementert")
    }

    override fun lagreNavn(lagreNavnRequest: ASBOPenPerson): String {
        throw UnsupportedOperationException("Ikke implementert")
    }

    override fun lagreSivilstand(lagreSivilstandRequest: ASBOPenPerson): String {
        throw UnsupportedOperationException("Ikke implementert")
    }

    override fun lagreHistoriskSamboerforhold(lagreHistoriskSamboerforholdRequest: ASBOPenLagreHistoriskSamboerforholdRequest): String {
        throw UnsupportedOperationException("Ikke implementert")
    }

    override fun hentPersonUtlandHistorikkListe(hentPersonUtlandHistorikkListeRequest: ASBOPenHentPersonUtlandsHistorikkListeRequest): ASBOPenUtlandHistorikk {
        throw UnsupportedOperationException("Ikke implementert")
    }

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
    ) = erEgenansattRequest.erEgenansatt

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
    @Throws(HentPersonUtlandFaultPenPersonIkkeFunnetMsg::class)
    override fun hentPersonUtland(
        @WebParam(
            name = "hentPersonUtlandRequest",
            targetNamespace = ""
        ) hentPersonUtlandRequest: ASBOPenPerson
    ) = psakpselvPersonAdapter.getASBOPenPerson(hentPersonUtlandRequest.fodselsnummer)
        ?: throw HentPersonUtlandFaultPenPersonIkkeFunnetMsg()

    override fun lagreBrukerprofil(lagreBrukerprofilRequest: ASBOPenPerson): ASBOPenTomRespons {
        throw UnsupportedOperationException("Ikke implementert")
    }

    override fun lagreKontoinformasjon(lagreKontoinformasjonRequest: ASBOPenPerson): ASBOPenTomRespons {
        throw UnsupportedOperationException("Ikke implementert")
    }

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
    @Throws(HentHistorikkFaultPenPersonIkkeFunnetMsg::class)
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
    @Throws(HentKontoinformasjonFaultPenPersonIkkeFunnetMsg::class)
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
    @Throws(HentPersonFaultPenPersonIkkeFunnetMsg::class)
    override fun hentPerson(
        @WebParam(name = "hentPersonRequest") hentPersonRequest: ASBOPenHentPersonRequest
    ) = psakpselvPersonAdapter.getASBOPenPerson(hentPersonRequest.person.fodselsnummer)
        ?: throw HentPersonFaultPenPersonIkkeFunnetMsg()

    override fun lagreSamboerforhold(lagreSamboerforholdRequest: ASBOPenPerson): ASBOPenTomRespons {
        throw UnsupportedOperationException("Ikke implementert")
    }

    override fun slettSamboerforhold(slettSamboerforholdRequest: ASBOPenPerson): ASBOPenTomRespons {
        throw UnsupportedOperationException("Ikke implementert")
    }
}
