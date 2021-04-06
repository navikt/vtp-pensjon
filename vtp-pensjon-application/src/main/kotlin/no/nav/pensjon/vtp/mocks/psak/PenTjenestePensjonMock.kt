package no.nav.pensjon.vtp.mocks.psak

import no.nav.inf.pen.tjenestepensjon.PENTjenestepensjon
import no.nav.lib.pen.psakpselv.asbo.tjenestepensjon.ASBOPenFinnTjenestepensjonForholdRequest
import no.nav.lib.pen.psakpselv.asbo.tjenestepensjon.ASBOPenTjenestepensjon
import no.nav.lib.pen.psakpselv.fault.ObjectFactory
import no.nav.pensjon.vtp.annotations.SoapService
import javax.jws.HandlerChain
import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult
import javax.jws.WebService
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-cons-pen-pen-tjenestepensjonWeb/sca/PENTjenestepensjonWSEXP"])
@WebService(targetNamespace = "http://nav-cons-pen-pen-tjenestepensjon/no/nav/inf", name = "PENTjenestepensjon")
@XmlSeeAlso(
    ObjectFactory::class,
    no.nav.lib.pen.psakpselv.asbo.ObjectFactory::class,
    no.nav.lib.pen.psakpselv.fault.tjenestepensjon.ObjectFactory::class,
    no.nav.inf.pen.tjenestepensjon.ObjectFactory::class,
    no.nav.lib.pen.psakpselv.asbo.tjenestepensjon.ObjectFactory::class
)
@HandlerChain(file = "/Handler-chain.xml")
class PenTjenestePensjonMock : PENTjenestepensjon {
    @WebMethod
    @RequestWrapper(
        localName = "finnTjenestepensjonsForhold",
        targetNamespace = "http://nav-cons-pen-pen-tjenestepensjon/no/nav/inf",
        className = "no.nav.inf.pen.tjenestepensjon.FinnTjenestepensjonsForhold"
    )
    @ResponseWrapper(
        localName = "finnTjenestepensjonsForholdResponse",
        targetNamespace = "http://nav-cons-pen-pen-tjenestepensjon/no/nav/inf",
        className = "no.nav.inf.pen.tjenestepensjon.FinnTjenestepensjonsForholdResponse"
    )
    @WebResult(name = "finnTjenestepensjonForholdResponse")
    override fun finnTjenestepensjonsForhold(
        @WebParam(name = "finnTjenestepensjonForholdRequest") finnTjenestepensjonForholdRequest: ASBOPenFinnTjenestepensjonForholdRequest
    ) = ASBOPenTjenestepensjon().apply {
        fnr = finnTjenestepensjonForholdRequest.fnr
        personId = finnTjenestepensjonForholdRequest.fnr
        tjenestepensjonForholdene = arrayOfNulls(0)
    }
}
