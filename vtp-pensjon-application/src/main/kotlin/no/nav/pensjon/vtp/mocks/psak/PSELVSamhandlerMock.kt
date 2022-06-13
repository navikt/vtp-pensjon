package no.nav.pensjon.vtp.mocks.psak

import no.nav.inf.pselv.samhandler.PSELVSamhandler
import no.nav.lib.pen.psakpselv.asbo.samhandler.ASBOPenFinnSamhandlerRequest
import no.nav.lib.pen.psakpselv.asbo.samhandler.ASBOPenHentSamhandlerRequest
import no.nav.lib.pen.psakpselv.asbo.samhandler.ASBOPenSamhandler
import no.nav.lib.pen.psakpselv.asbo.samhandler.ASBOPenSamhandlerListe
import no.nav.lib.pen.psakpselv.fault.ObjectFactory
import no.nav.pensjon.vtp.annotations.SoapService
import javax.jws.HandlerChain
import javax.jws.WebMethod
import javax.jws.WebResult
import javax.jws.WebService
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-cons-pen-pselv-samhandlerWeb/sca/PSELVSamhandlerWSEXP"])
@WebService(targetNamespace = "http://nav-cons-pen-pselv-samhandler/no/nav/inf/", name = "PSELVSamhandlerWSEXP")
@XmlSeeAlso(
    ObjectFactory::class,
    no.nav.lib.pen.psakpselv.asbo.samhandler.ObjectFactory::class,
    no.nav.inf.pselv.samhandler.ObjectFactory::class
)
@HandlerChain(file = "/Handler-chain.xml")
class PSELVSamhandlerMock: PSELVSamhandler {

    @WebMethod
    @RequestWrapper(
        localName = "hentSamhandler",
        targetNamespace = "http://nav-cons-pen-pselv-samhandler/no/nav/inf",
        className = "no.nav.inf.pselv.samhandler.HentSamhandler"
    )
    @ResponseWrapper(
        localName = "hentSamhandlerResponse",
        targetNamespace = "http://nav-cons-pen-pselv-samhandler/no/nav/inf",
        className = "no.nav.inf.pselv.samhandler.HentSamhandlerResponse"
    )
    @WebResult(name = "hentSamhandlerResponse", targetNamespace = "")
    override fun hentSamhandler(p0: ASBOPenHentSamhandlerRequest?): ASBOPenSamhandler = ASBOPenSamhandler()

    @WebMethod
    @RequestWrapper(
        localName = "finnSamhandler",
        targetNamespace = "http://nav-cons-pen-pselv-samhandler/no/nav/inf",
        className = "no.nav.inf.pselv.samhandler.FinnSamhandler"
    )
    @ResponseWrapper(
        localName = "finnSamhandlerResponse",
        targetNamespace = "http://nav-cons-pen-pselv-samhandler/no/nav/inf",
        className = "no.nav.inf.pselv.samhandler.FinnSamhandlerResponse"
    )
    @WebResult(name = "finnSamhandlerResponse", targetNamespace = "")
    override fun finnSamhandler(p0: ASBOPenFinnSamhandlerRequest?): ASBOPenSamhandlerListe = ASBOPenSamhandlerListe()
}
