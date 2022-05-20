package no.nav.pensjon.vtp.mocks.psak

import no.nav.inf.pen.tjenestepensjon.PENTjenestepensjon
import no.nav.inf.pselv.tjenestepensjon.PSELVTjenestePensjon
import no.nav.inf.pselv.tjenestepensjon.SlettTjenestepensjonForhold
import no.nav.inf.pselv.tjenestepensjon.SlettTjenestepensjonForholdResponse
import no.nav.lib.pen.psakpselv.asbo.tjenestepensjon.ASBOPenFinnTjenestepensjonForholdRequest
import no.nav.lib.pen.psakpselv.asbo.tjenestepensjon.ASBOPenTjenestepensjon
import no.nav.lib.pen.psakpselv.asbo.tjenestepensjon.ASBOPenTjenestepensjonForhold
import no.nav.lib.pen.psakpselv.asbo.tjenestepensjon.ASBOPenTjenestepensjonSimulering
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

@SoapService(path = ["/esb/nav-cons-pen-pselv-tjenestepensjonWeb/sca/PSELVTjenestepensjonWSEXP"])
@WebService(targetNamespace = "http://nav-cons-pen-pselv-tjenestepensjon/no/nav/inf", name = "PSELVTjenestepensjon")
@XmlSeeAlso(
    ObjectFactory::class,
    no.nav.lib.pen.psakpselv.asbo.ObjectFactory::class,
    no.nav.lib.pen.psakpselv.fault.tjenestepensjon.ObjectFactory::class,
    no.nav.inf.pen.tjenestepensjon.ObjectFactory::class,
    no.nav.lib.pen.psakpselv.asbo.tjenestepensjon.ObjectFactory::class
)
@HandlerChain(file = "/Handler-chain.xml")
class PSELVTjenestePensjonMock : PSELVTjenestePensjon {
    @WebMethod
    @RequestWrapper(
        localName = "finnTjenestepensjonsForhold",
        targetNamespace = "http://nav-cons-pen-pselv-tjenestepensjon/no/nav/inf",
        className = "no.nav.inf.pselv.tjenestepensjon.FinnTjenestepensjonsForhold"
    )
    @ResponseWrapper(
        localName = "finnTjenestepensjonsForholdResponse",
        targetNamespace = "http://nav-cons-pen-pselv-tjenestepensjon/no/nav/inf",
        className = "no.nav.inf.pselv.tjenestepensjon.FinnTjenestepensjonsForholdResponse"
    )
    @WebResult(name = "finnTjenestepensjonForholdResponse")
    override fun finnTjenestepensjonForhold(
        @WebParam(name = "finnTjenestepensjonForholdRequest") finnTjenestepensjonForholdRequest: ASBOPenFinnTjenestepensjonForholdRequest
    ) = ASBOPenTjenestepensjon().apply {
        fnr = finnTjenestepensjonForholdRequest.fnr
        personId = finnTjenestepensjonForholdRequest.fnr
        tjenestepensjonForholdene = arrayOfNulls(0)
    }

    override fun slettTjenestepensjonForhold(p0: SlettTjenestepensjonForhold?) = SlettTjenestepensjonForholdResponse()
    override fun opprettTjenestepensjonForhold(p0: ASBOPenTjenestepensjon?) = ASBOPenTjenestepensjonForhold()
    override fun lagreTjenestepensjonForhold(p0: ASBOPenTjenestepensjon?) = ASBOPenTjenestepensjonForhold()
    override fun lagreTjenestepensjonSimulering(p0: ASBOPenTjenestepensjonSimulering?) = ASBOPenTjenestepensjonSimulering()
    override fun opprettTjenestepensjonSimulering(p0: ASBOPenTjenestepensjonForhold?) = ASBOPenTjenestepensjonSimulering()
}
