package no.nav.pensjon.vtp.mocks.psak

import no.nav.inf.psak.bidragsak.HentForskuddFaultPenGeneriskMsg
import no.nav.inf.psak.bidragsak.PSAKBidragsak
import no.nav.lib.pen.psakpselv.asbo.bidragsak.ASBOPenForskuddSak
import no.nav.lib.pen.psakpselv.asbo.bidragsak.ASBOPenHentForskuddRequest
import no.nav.lib.pen.psakpselv.asbo.bidragsak.ObjectFactory
import no.nav.pensjon.vtp.annotations.SoapService
import javax.jws.*
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-cons-pen-psak-bidragsakWeb/sca/PSAKBidragsakWSEXP"])
@WebService(targetNamespace = "http://nav-cons-pen-psak-bidragsak/no/nav/inf", name = "PSAKBidragsak")
@XmlSeeAlso(ObjectFactory::class, no.nav.lib.pen.psakpselv.fault.ObjectFactory::class, no.nav.inf.psak.bidragsak.ObjectFactory::class)
@HandlerChain(file = "/Handler-chain.xml")
class PSAKBidragsakMock : PSAKBidragsak {
    @WebMethod
    @RequestWrapper(localName = "hentForskudd", targetNamespace = "http://nav-cons-pen-psak-bidragsak/no/nav/inf", className = "no.nav.inf.psak.bidragsak.HentForskudd")
    @ResponseWrapper(localName = "hentForskuddResponse", targetNamespace = "http://nav-cons-pen-psak-bidragsak/no/nav/inf", className = "no.nav.inf.psak.bidragsak.HentForskuddResponse")
    @WebResult(name = "hentForskuddResponse", targetNamespace = "")
    @Throws(HentForskuddFaultPenGeneriskMsg::class)
    override fun hentForskudd(@WebParam(name = "hentForskuddRequest", targetNamespace = "") request: ASBOPenHentForskuddRequest): ASBOPenForskuddSak {
        return ASBOPenForskuddSak()
    }
}
