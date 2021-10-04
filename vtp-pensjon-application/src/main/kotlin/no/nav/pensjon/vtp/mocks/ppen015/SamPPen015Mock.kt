package no.nav.pensjon.vtp.mocks.ppen015

import nav_cons_sto_sam_ppen015.no.nav.inf.ppen015.SAMPPEN015
import nav_lib_cons_sto_sam.no.nav.lib.sto.sam.asbo.ppen015.ASBOStoMottaSamhandlerSvarRequest
import nav_lib_cons_sto_sam.no.nav.lib.sto.sam.asbo.ppen015.ObjectFactory
import no.nav.pensjon.vtp.annotations.SoapService
import javax.jws.HandlerChain
import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebService
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-cons-sto-sam-ppen015Web/sca/SAMPPEN015WSEXP"])
@WebService(targetNamespace = "http://nav-cons-sto-sam-ppen015/no/nav/inf/PPEN015", name = "SAMPPEN015")
@XmlSeeAlso(
    ObjectFactory::class,
    nav_cons_sto_sam_ppen015.no.nav.inf.ppen015.ObjectFactory::class
)
@HandlerChain(file = "/Handler-chain.xml")
class SamPPen015Mock : SAMPPEN015 {
    @WebMethod
    @RequestWrapper(
        localName = "mottaSamhandlerSvar",
        targetNamespace = "http://nav-cons-sto-sam-ppen015/no/nav/inf/PPEN015",
        className = "nav_cons_sto_sam_ppen015.no.nav.inf.ppen015.MottaSamhandlerSvar"
    )
    @ResponseWrapper(
        localName = "mottaSamhandlerSvarResponse",
        targetNamespace = "http://nav-cons-sto-sam-ppen015/no/nav/inf/PPEN015",
        className = "nav_cons_sto_sam_ppen015.no.nav.inf.ppen015.MottaSamhandlerSvarResponse"
    )
    override fun mottaSamhandlerSvar(@WebParam(name = "samhandlerSvarRequest", targetNamespace = "") request: ASBOStoMottaSamhandlerSvarRequest?) = Unit
}
