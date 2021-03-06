package no.nav.pensjon.vtp.mocks.psak

import no.nav.inf.psak.navorgenhet.HentNAVEnhetFaultPenGeneriskMsg
import no.nav.inf.psak.navorgenhet.HentNAVEnhetFaultPenNAVEnhetIkkeFunnetMsg
import no.nav.inf.psak.navorgenhet.PSAKNAVOrgEnhet
import no.nav.lib.pen.psakpselv.asbo.navorgenhet.*
import no.nav.lib.pen.psakpselv.fault.ObjectFactory
import no.nav.pensjon.vtp.annotations.SoapService
import no.nav.pensjon.vtp.mocks.psak.util.asboPenNAVEnhet
import no.nav.pensjon.vtp.testmodell.enheter.EnheterIndeks
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import javax.jws.*
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-cons-pen-psak-navorgenhetWeb/sca/PSAKNAVOrgEnhetWSEXP"])
@WebService(targetNamespace = "http://nav-cons-pen-psak-navorgenhet/no/nav/inf", name = "PSAKNAVOrgEnhet")
@XmlSeeAlso(
    ObjectFactory::class,
    no.nav.lib.pen.psakpselv.asbo.ObjectFactory::class,
    no.nav.lib.pen.psakpselv.fault.navorgenhet.ObjectFactory::class,
    no.nav.lib.pen.psakpselv.asbo.navorgenhet.ObjectFactory::class,
    no.nav.inf.psak.navorgenhet.ObjectFactory::class
)
@HandlerChain(file = "/Handler-chain.xml")
class PsakNavOrgEnhetMock(private val enheterIndeks: EnheterIndeks) : PSAKNAVOrgEnhet {
    override fun hentSpesialenhetTilPerson(hentSpesialenhetTilPersonRequest: ASBOPenHentSpesialEnhetTilPersonRequest) =
        throw NotImplementedException()

    @WebMethod
    @RequestWrapper(
        localName = "hentNAVEnhet",
        targetNamespace = "http://nav-cons-pen-psak-navorgenhet/no/nav/inf",
        className = "no.nav.inf.psak.navorgenhet.HentNAVEnhet"
    )
    @ResponseWrapper(
        localName = "hentNAVEnhetResponse",
        targetNamespace = "http://nav-cons-pen-psak-navorgenhet/no/nav/inf",
        className = "no.nav.inf.psak.navorgenhet.HentNAVEnhetResponse"
    )
    @WebResult(name = "hentNAVEnhetResponse")
    override fun hentNAVEnhet(
        @WebParam(name = "hentNAVEnhetRequest") hentNAVEnhetRequest: ASBOPenNAVEnhet
    ) = enheterIndeks.finnByEnhetId(
        hentNAVEnhetRequest.enhetsId?.toLong()
            ?: throw HentNAVEnhetFaultPenGeneriskMsg("EnhetsId was null")
    )
        ?.let {
            ASBOPenNAVEnhet().apply {
                enhetsId = it.enhetId.toString()
                enhetsNavn = it.navn
            }
        }
        ?: throw HentNAVEnhetFaultPenNAVEnhetIkkeFunnetMsg("Enhet med id ${hentNAVEnhetRequest.enhetsId} ikke funnet")

    override fun hentNAVEnhetGruppeListe(hentNAVEnhetGruppeListeRequest: ASBOPenNAVEnhet) =
        throw NotImplementedException()

    @WebMethod
    @RequestWrapper(
        localName = "finnNAVEnhet",
        targetNamespace = "http://nav-cons-pen-psak-navorgenhet/no/nav/inf",
        className = "no.nav.inf.psak.navorgenhet.FinnNAVEnhet"
    )
    @ResponseWrapper(
        localName = "finnNAVEnhetResponse",
        targetNamespace = "http://nav-cons-pen-psak-navorgenhet/no/nav/inf",
        className = "no.nav.inf.psak.navorgenhet.FinnNAVEnhetResponse"
    )
    @WebResult(name = "finnNAVEnhetResponse", targetNamespace = "")
    override fun finnNAVEnhet(
        @WebParam(name = "finnNAVEnhetRequest", targetNamespace = "") finnNAVEnhetRequest: ASBOPenFinnNAVEnhetRequest
    ) = ASBOPenNAVEnhetListe().apply {
        navEnheter = arrayOf(asboPenNAVEnhet())
    }

    override fun hentNAVEnhetListe(hentNAVEnhetListeRequest: ASBOPenHentNAVEnhetListeRequest) =
        throw NotImplementedException()

    override fun hentForvaltningsenhetTilPersonListe(hentForvaltningsenhetTilPersonListeRequest: ASBOPenHentForvaltningsenhetTilPersonListeRequest) =
        throw NotImplementedException()

    override fun finnArenaNAVEnhetListe(finnArenaNAVEnhetListeRequest: ASBOPenFinnArenaNAVEnhetListeRequest) =
        throw NotImplementedException()
}
