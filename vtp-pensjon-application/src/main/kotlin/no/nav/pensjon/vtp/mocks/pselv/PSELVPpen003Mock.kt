package no.nav.pensjon.vtp.mocks.pselv

import no.nav.inf.pselv.ppen003.ObjectFactory
import no.nav.inf.pselv.ppen003.PSELVPPEN003
import no.nav.lib.pen.psakpselv.asbo.ppen003.ASBOPenBehandleFleksibelAPSakRequest
import no.nav.lib.pen.psakpselv.asbo.ppen003.ASBOPenBehandleFleksibelAPSakResponse
import no.nav.pensjon.vtp.annotations.SoapService
import javax.jws.*
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-cons-pen-pselv-ppen003Web/sca/PSELVPPEN003WSEXP"])
@WebService(targetNamespace = "http://nav-cons-pen-pselv-ppen003/no/nav/inf", name = "PSELVPPEN003")
@XmlSeeAlso(ObjectFactory::class, no.nav.lib.pen.psakpselv.asbo.ppen003.ObjectFactory::class)
@HandlerChain(file = "/Handler-chain.xml")
class PSELVPpen003Mock : PSELVPPEN003 {

    @WebMethod
    @RequestWrapper(
        localName = "behandleFleksibelAPSak",
        targetNamespace = "http://nav-cons-pen-pselv-ppen003/no/nav/inf",
        className = "no.nav.inf.pselv.ppen003.BehandleFleksibelAPSak"
    )
    @ResponseWrapper(
        localName = "behandleFleksibelAPSakResponse",
        targetNamespace = "http://nav-cons-pen-pselv-ppen003/no/nav/inf",
        className = "no.nav.inf.pselv.ppen003.BehandleFleksibelAPSakResponse"
    )
    @WebResult(name = "behandleFleksibelAPSakResponse", targetNamespace = "")
    override fun behandleFleksibelAPSak(
        @WebParam(name = "behandleFleksibelAPSakRequest", targetNamespace = "")
        behandleFleksibelAPSakRequest: ASBOPenBehandleFleksibelAPSakRequest
    ): ASBOPenBehandleFleksibelAPSakResponse {
        return ASBOPenBehandleFleksibelAPSakResponse().apply { startetOk = true }
    }
}