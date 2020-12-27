package no.nav.pensjon.vtp.mocks.psak

import no.nav.inf.psak.ppen015.ObjectFactory
import no.nav.inf.psak.ppen015.PSAKPPEN015
import no.nav.lib.pen.psakpselv.asbo.ppen015.ASBOPSAKPPEN015Request
import no.nav.lib.pen.psakpselv.asbo.ppen015.ASBOPSAKPPEN015Response
import no.nav.pensjon.vtp.core.annotations.SoapService
import javax.jws.HandlerChain
import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult
import javax.jws.WebService
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-cons-pen-psak-ppen015Web/sca/PSAKPPEN015WSEXP"])
@WebService(targetNamespace = "http://nav-cons-pen-psak-ppen015/no/nav/inf/PSAKPPEN015", name = "PSAKPPEN015")
@XmlSeeAlso(
    ObjectFactory::class, no.nav.lib.pen.psakpselv.asbo.ppen015.ObjectFactory::class
)
@HandlerChain(file = "/Handler-chain.xml")
class PSAKPPEN015Mock : PSAKPPEN015 {
    @WebMethod
    @RequestWrapper(
        localName = "iverksettVedtak",
        targetNamespace = "http://nav-cons-pen-psak-ppen015/no/nav/inf/PSAKPPEN015",
        className = "no.nav.inf.psak.ppen015.IverksettVedtak"
    )
    @ResponseWrapper(
        localName = "iverksettVedtakResponse",
        targetNamespace = "http://nav-cons-pen-psak-ppen015/no/nav/inf/PSAKPPEN015",
        className = "no.nav.inf.psak.ppen015.IverksettVedtakResponse"
    )
    @WebResult(name = "iverksettVedtakResponse")
    override fun iverksettVedtak(
        @WebParam(name = "iverksettVedtakRequest") iverksettVedtakRequest: ASBOPSAKPPEN015Request
    ) = ASBOPSAKPPEN015Response().apply {
        startetOk = true
    }
}
