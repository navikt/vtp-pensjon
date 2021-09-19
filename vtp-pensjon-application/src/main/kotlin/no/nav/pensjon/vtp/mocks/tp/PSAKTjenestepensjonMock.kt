package no.nav.pensjon.vtp.mocks.tp

import no.nav.inf.psak.tjenestepensjon.*
import no.nav.lib.pen.psakpselv.asbo.tjenestepensjon.ASBOPenTjenestepensjon
import no.nav.pensjon.vtp.annotations.SoapService
import javax.jws.*
import javax.xml.bind.annotation.XmlSeeAlso

@SoapService(path = ["/esb/nav-cons-pen-psak-tjenestepensjonWeb/sca/PSAKTjenestepensjonWSEXP"])
@WebService(targetNamespace = "http://nav-cons-pen-psak-tjenestepensjon/no/nav/inf", name = "PSAKTjenestepensjon")
@XmlSeeAlso(no.nav.lib.pen.psakpselv.fault.ObjectFactory::class, no.nav.lib.pen.psakpselv.asbo.ObjectFactory::class, no.nav.lib.pen.psakpselv.fault.tjenestepensjon.ObjectFactory::class, no.nav.lib.pen.psakpselv.asbo.tjenestepensjon.ObjectFactory::class, ObjectFactory::class)
@HandlerChain(file = "/Handler-chain.xml")
class PSAKTjenestepensjonMock : PSAKTjenestepensjon {

    @WebMethod
    @WebResult(name = "finnTjenestepensjonsForholdResponse", targetNamespace = "http://nav-cons-pen-psak-tjenestepensjon/no/nav/inf", partName = "finnTjenestepensjonsForholdResult")
    override fun finnTjenestepensjonsForhold(@WebParam(partName = "finnTjenestepensjonsForholdParameters", name = "finnTjenestepensjonsForhold", targetNamespace = "http://nav-cons-pen-psak-tjenestepensjon/no/nav/inf") request: FinnTjenestepensjonsForhold): FinnTjenestepensjonsForholdResponse {
        val tjenestepensjon = ASBOPenTjenestepensjon()
        tjenestepensjon.fnr = request.finnTjenestepensjonForholdRequest.fnr

        val response = FinnTjenestepensjonsForholdResponse()
        response.finnTjenestepensjonForholdResponse = tjenestepensjon
        return response
    }

    @WebMethod
    @WebResult(name = "slettTjenestepensjonForholdResponse", targetNamespace = "http://nav-cons-pen-psak-tjenestepensjon/no/nav/inf", partName = "slettTjenestepensjonForholdResult")
    override fun slettTjenestepensjonForhold(@WebParam(partName = "slettTjenestepensjonForholdParameters", name = "slettTjenestepensjonForhold", targetNamespace = "http://nav-cons-pen-psak-tjenestepensjon/no/nav/inf") request: SlettTjenestepensjonForhold): SlettTjenestepensjonForholdResponse {
        TODO("Not yet implemented")
    }

    @WebMethod
    @WebResult(name = "lagreTjenestepensjonForholdResponse", targetNamespace = "http://nav-cons-pen-psak-tjenestepensjon/no/nav/inf", partName = "lagreTjenestepensjonForholdResult")
    override fun lagreTjenestepensjonForhold(@WebParam(partName = "lagreTjenestepensjonForholdParameters", name = "lagreTjenestepensjonForhold", targetNamespace = "http://nav-cons-pen-psak-tjenestepensjon/no/nav/inf") request: LagreTjenestepensjonForhold): LagreTjenestepensjonForholdResponse {
        TODO("Not yet implemented")
    }

    @WebMethod
    @WebResult(name = "opprettTjenestepensjonForholdResponse", targetNamespace = "http://nav-cons-pen-psak-tjenestepensjon/no/nav/inf", partName = "opprettTjenestepensjonForholdResult")
    override fun opprettTjenestepensjonForhold(@WebParam(partName = "opprettTjenestepensjonForholdParameters", name = "opprettTjenestepensjonForhold", targetNamespace = "http://nav-cons-pen-psak-tjenestepensjon/no/nav/inf") request: OpprettTjenestepensjonForhold): OpprettTjenestepensjonForholdResponse {
        TODO("Not yet implemented")
    }
}
