package no.nav.pensjon.vtp.mocks.trekk

import nav_cons_sto_sam_trekk.no.nav.inf.OpprettAndreTrekkFaultStoGeneriskMsg
import nav_cons_sto_sam_trekk.no.nav.inf.SAMTrekk
import nav_lib_cons_sto_sam.no.nav.lib.sto.sam.asbo.trekk.ASBOStoAndreTrekkListe
import nav_lib_cons_sto_sam.no.nav.lib.sto.sam.fault.trekk.ObjectFactory
import no.nav.lib.sto.sam.fault.FaultStoGenerisk
import no.nav.pensjon.vtp.annotations.SoapService
import javax.jws.*
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-cons-sto-sam-trekkWeb/sca/SAMTrekkWSEXP"])
@WebService(targetNamespace = "http://nav-cons-sto-sam-trekk/no/nav/inf", name = "SAMTrekk")
@XmlSeeAlso(
    ObjectFactory::class,
    no.nav.lib.sto.sam.asbo.ObjectFactory::class,
    nav_lib_cons_sto_sam.no.nav.lib.sto.sam.asbo.trekk.ObjectFactory::class,
    no.nav.lib.sto.sam.fault.ObjectFactory::class,
    nav_cons_sto_sam_trekk.no.nav.inf.ObjectFactory::class
)
@HandlerChain(file = "/Handler-chain.xml")
class SamTrekkMock : SAMTrekk {
    @WebMethod
    @RequestWrapper(
        localName = "opprettAndreTrekk",
        targetNamespace = "http://nav-cons-sto-sam-trekk/no/nav/inf",
        className = "nav_cons_sto_sam_trekk.no.nav.inf.OpprettAndreTrekk"
    )
    @ResponseWrapper(
        localName = "opprettAndreTrekkResponse",
        targetNamespace = "http://nav-cons-sto-sam-trekk/no/nav/inf",
        className = "nav_cons_sto_sam_trekk.no.nav.inf.OpprettAndreTrekkResponse"
    )
    @WebResult(name = "opprettAndreTrekkResponse", targetNamespace = "")
    override fun opprettAndreTrekk(@WebParam(name = "opprettAndreTrekkRequest", targetNamespace = "") request: ASBOStoAndreTrekkListe?): ASBOStoAndreTrekkListe {
        if (request != null) {
            TODO("Not yet implemented")
        } else {
            throw OpprettAndreTrekkFaultStoGeneriskMsg("Request was null", FaultStoGenerisk())
        }
    }
}
