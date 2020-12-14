package no.nav.pensjon.vtp.mocks.psak

import no.nav.inf.pen.navorgenhet.HentNAVEnhetFaultPenGeneriskMsg
import no.nav.inf.pen.navorgenhet.HentNAVEnhetFaultPenNAVEnhetIkkeFunnetMsg
import no.nav.inf.pen.navorgenhet.PENNAVOrgEnhet
import no.nav.lib.pen.psakpselv.asbo.navorgenhet.ASBOPenHentForvaltningsenhetTilPersonListeRequest
import no.nav.lib.pen.psakpselv.asbo.navorgenhet.ASBOPenHentNAVEnhetListeRequest
import no.nav.lib.pen.psakpselv.asbo.navorgenhet.ASBOPenHentSpesialEnhetTilPersonRequest
import no.nav.lib.pen.psakpselv.asbo.navorgenhet.ASBOPenNAVEnhet
import no.nav.lib.pen.psakpselv.fault.ObjectFactory
import no.nav.pensjon.vtp.core.annotations.SoapService
import no.nav.pensjon.vtp.testmodell.enheter.EnheterIndeks
import javax.jws.*
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-cons-pen-pen-navorgenhetWeb/sca/PENNAVOrgEnhetWSEXP"])
@WebService(targetNamespace = "http://nav-cons-pen-pen-navorgenhet/no/nav/inf/PENNAVOrgEnhet", name = "PENNAVOrgEnhet")
@HandlerChain(file = "/Handler-chain.xml")
@XmlSeeAlso(
    ObjectFactory::class,
    no.nav.lib.pen.psakpselv.asbo.ObjectFactory::class,
    no.nav.lib.pen.psakpselv.fault.navorgenhet.ObjectFactory::class,
    no.nav.lib.pen.psakpselv.asbo.navorgenhet.ObjectFactory::class,
    no.nav.inf.pen.navorgenhet.ObjectFactory::class
)
class PenNavOrgEnhetMock(private val enheterIndeks: EnheterIndeks) : PENNAVOrgEnhet {
    @WebMethod
    @RequestWrapper(
        localName = "hentNAVEnhet",
        targetNamespace = "http://nav-cons-pen-pen-navorgenhet/no/nav/inf/PENNAVOrgEnhet",
        className = "no.nav.inf.pen.navorgenhet.HentNAVEnhet"
    )
    @ResponseWrapper(
        localName = "hentNAVEnhetResponse",
        targetNamespace = "http://nav-cons-pen-pen-navorgenhet/no/nav/inf/PENNAVOrgEnhet",
        className = "no.nav.inf.pen.navorgenhet.HentNAVEnhetResponse"
    )
    @WebResult(name = "hentNAVEnhetResponse")
    override fun hentNAVEnhet(@WebParam(name = "hentNAVEnhetRequest") asboPenNAVEnhet: ASBOPenNAVEnhet) =
        enheterIndeks.finnByEnhetId(
            asboPenNAVEnhet.enhetsId?.toLong()
                ?: throw HentNAVEnhetFaultPenGeneriskMsg("EnhetsId was null")
        )?.let {
            ASBOPenNAVEnhet().apply {
                enhetsId = it.enhetId.toString()
                enhetsNavn = it.navn
            }
        }
            ?: throw HentNAVEnhetFaultPenNAVEnhetIkkeFunnetMsg()

    @WebMethod
    @RequestWrapper(
        localName = "hentNAVEnhetListe",
        targetNamespace = "http://nav-cons-pen-pen-navorgenhet/no/nav/inf/PENNAVOrgEnhet",
        className = "no.nav.inf.pen.navorgenhet.HentNAVEnhetListe"
    )
    @ResponseWrapper(
        localName = "hentNAVEnhetListeResponse",
        targetNamespace = "http://nav-cons-pen-pen-navorgenhet/no/nav/inf/PENNAVOrgEnhet",
        className = "no.nav.inf.pen.navorgenhet.HentNAVEnhetListeResponse"
    )
    @WebResult(name = "hentNAVEnhetListeResponse", targetNamespace = "")
    override fun hentNAVEnhetListe(
        @WebParam(
            name = "hentNAVEnhetListeRequest",
            targetNamespace = ""
        ) asboPenHentNAVEnhetListeRequest: ASBOPenHentNAVEnhetListeRequest
    ) = throw UnsupportedOperationException("Ikke implementert")

    @WebMethod
    @RequestWrapper(
        localName = "hentForvaltningsenhetTilPersonListe",
        targetNamespace = "http://nav-cons-pen-pen-navorgenhet/no/nav/inf/PENNAVOrgEnhet",
        className = "no.nav.inf.pen.navorgenhet.HentForvaltningsenhetTilPersonListe"
    )
    @ResponseWrapper(
        localName = "hentForvaltningsenhetTilPersonListeResponse",
        targetNamespace = "http://nav-cons-pen-pen-navorgenhet/no/nav/inf/PENNAVOrgEnhet",
        className = "no.nav.inf.pen.navorgenhet.HentForvaltningsenhetTilPersonListeResponse"
    )
    @WebResult(name = "hentForvaltningsenhetTilPersonListeResponse", targetNamespace = "")
    override fun hentForvaltningsenhetTilPersonListe(
        @WebParam(
            name = "hentForvaltningsenhetTilPersonListeRequest",
            targetNamespace = ""
        ) asboPenHentForvaltningsenhetTilPersonListeRequest: ASBOPenHentForvaltningsenhetTilPersonListeRequest
    ) = throw UnsupportedOperationException("Ikke implementert")

    @WebMethod
    @RequestWrapper(
        localName = "hentSpesialenhetTilPerson",
        targetNamespace = "http://nav-cons-pen-pen-navorgenhet/no/nav/inf/PENNAVOrgEnhet",
        className = "no.nav.inf.pen.navorgenhet.HentSpesialenhetTilPerson"
    )
    @ResponseWrapper(
        localName = "hentSpesialenhetTilPersonResponse",
        targetNamespace = "http://nav-cons-pen-pen-navorgenhet/no/nav/inf/PENNAVOrgEnhet",
        className = "no.nav.inf.pen.navorgenhet.HentSpesialenhetTilPersonResponse"
    )
    @WebResult(name = "hentSpesialenhetTilPersonResponse", targetNamespace = "")
    override fun hentSpesialenhetTilPerson(
        @WebParam(
            name = "hentSpesialenhetTilPersonRequest",
            targetNamespace = ""
        ) asboPenHentSpesialEnhetTilPersonRequest: ASBOPenHentSpesialEnhetTilPersonRequest
    ) = throw UnsupportedOperationException("Ikke implementert")
}
