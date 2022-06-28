package no.nav.pensjon.vtp.mocks.psak

import no.nav.inf.pen.person.*
import no.nav.lib.pen.psakpselv.asbo.person.*
import no.nav.lib.pen.psakpselv.fault.ObjectFactory
import no.nav.pensjon.vtp.annotations.SoapService
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import javax.jws.*
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-cons-pen-pen-personWeb/sca/PENPersonWSEXP"])
@WebService(targetNamespace = "http://nav-cons-pen-pen-person/no/nav/inf/PENPerson", name = "PENPerson")
@XmlSeeAlso(
    ObjectFactory::class,
    no.nav.lib.pen.psakpselv.asbo.person.ObjectFactory::class,
    no.nav.inf.pen.person.ObjectFactory::class
)
@HandlerChain(file = "/Handler-chain.xml")
class PenPersonServiceMockImpl(private val psakpselvPersonAdapter: PsakpselvPersonAdapter) : PENPerson {
    @WebMethod
    @RequestWrapper(
        localName = "hentPerson",
        targetNamespace = "http://nav-cons-pen-pen-person/no/nav/inf/PENPerson",
        className = "no.nav.inf.pen.person.HentPerson"
    )
    @ResponseWrapper(
        localName = "hentPersonResponse",
        targetNamespace = "http://nav-cons-pen-pen-person/no/nav/inf/PENPerson",
        className = "no.nav.inf.pen.person.HentPersonResponse"
    )
    @WebResult(name = "hentPersonResponse")
    @Throws(
        HentPersonFaultPenPersonIkkeFunnetMsg::class
    )
    override fun hentPerson(
        @WebParam(name = "hentPersonRequest") hentPersonRequest: ASBOPenHentPersonRequest
    ) = psakpselvPersonAdapter.getASBOPenPerson(hentPersonRequest.person.fodselsnummer)
        ?: throw HentPersonFaultPenPersonIkkeFunnetMsg()

    @WebMethod
    @RequestWrapper(
        localName = "hentFamilierelasjonsHistorikk",
        targetNamespace = "http://nav-cons-pen-pen-person/no/nav/inf/PENPerson",
        className = "no.nav.inf.pen.person.HentFamilierelasjonsHistorikk"
    )
    @ResponseWrapper(
        localName = "hentFamilierelasjonsHistorikkResponse",
        targetNamespace = "http://nav-cons-pen-pen-person/no/nav/inf/PENPerson",
        className = "no.nav.inf.pen.person.HentFamilierelasjonsHistorikkResponse"
    )
    @WebResult(name = "hentFamilierelasjonsHistorikkResponse")
    @Throws(
        HentFamilierelasjonsHistorikkFaultPenPersonIkkeFunnetMsg::class
    )
    override fun hentFamilierelasjonsHistorikk(
        @WebParam(name = "hentFamilierelasjonsHistorikkRequest") hentFamilierelasjonsHistorikkRequest: ASBOPenHentFamilierelasjonsHistorikkRequest
    ) = psakpselvPersonAdapter.getASBOPenPerson(hentFamilierelasjonsHistorikkRequest.fnr)
        ?: throw HentFamilierelasjonsHistorikkFaultPenPersonIkkeFunnetMsg()

    override fun finnAdresseListe(finnAdresseListeRequest: ASBOPenFinnAdresseListeRequest) =
        throw NotImplementedException()

    @WebMethod
    @RequestWrapper(
        localName = "hentPersonUtland",
        targetNamespace = "http://nav-cons-pen-pen-person/no/nav/inf/PENPerson",
        className = "no.nav.inf.pen.person.HentPersonUtland"
    )
    @ResponseWrapper(
        localName = "hentPersonUtlandResponse",
        targetNamespace = "http://nav-cons-pen-pen-person/no/nav/inf/PENPerson",
        className = "no.nav.inf.pen.person.HentPersonUtlandResponse"
    )
    @WebResult(name = "hentPersonUtlandResponse")
    @Throws(
        HentPersonUtlandFaultPenPersonIkkeFunnetMsg::class
    )
    override fun hentPersonUtland(
        @WebParam(name = "hentPersonUtlandRequest") hentPersonUtlandRequest: ASBOPenPerson
    ) = psakpselvPersonAdapter.getASBOPenPerson(hentPersonUtlandRequest.fodselsnummer)
        ?: throw HentPersonUtlandFaultPenPersonIkkeFunnetMsg()

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
    @WebResult(name = "hentFamilierelasjonerResponse")
    @Throws(
        HentFamilierelasjonerFaultPenPersonIkkeFunnetMsg::class
    )
    override fun hentFamilierelasjoner(@WebParam(name = "hentFamilierelasjonerRequest") hentFamilierelasjonerRequest: ASBOPenHentFamilierelasjonerRequest): ASBOPenPerson? {
        val asboPenPerson = psakpselvPersonAdapter.getASBOPenPerson(hentFamilierelasjonerRequest.fodselsnummer)
        if ( asboPenPerson!= null && !hentFamilierelasjonerRequest.hentSamboerforhold) {
            asboPenPerson.apply { samboer = null }
            asboPenPerson.relasjoner.relasjoner.filter { it.relasjonsType != "SAMB" }
        }
        return asboPenPerson ?: throw HentFamilierelasjonerFaultPenPersonIkkeFunnetMsg()
    }

    @WebMethod
    @RequestWrapper(
        localName = "hentHistorikk",
        targetNamespace = "http://nav-cons-pen-pen-person/no/nav/inf/PENPerson",
        className = "no.nav.inf.pen.person.HentHistorikk"
    )
    @ResponseWrapper(
        localName = "hentHistorikkResponse",
        targetNamespace = "http://nav-cons-pen-pen-person/no/nav/inf/PENPerson",
        className = "no.nav.inf.pen.person.HentHistorikkResponse"
    )
    @WebResult(name = "hentHistorikkResponse")
    @Throws(
        HentHistorikkFaultPenPersonIkkeFunnetMsg::class
    )
    override fun hentHistorikk(
        @WebParam(name = "hentHistorikkRequest") hentHistorikkRequest: ASBOPenHentHistorikkRequest
    ) = psakpselvPersonAdapter.getASBOPenPerson(hentHistorikkRequest.fodselsnummer)
        ?: throw HentHistorikkFaultPenPersonIkkeFunnetMsg()
}
