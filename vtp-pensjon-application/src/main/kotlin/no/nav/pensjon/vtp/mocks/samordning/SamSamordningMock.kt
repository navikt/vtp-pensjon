package no.nav.pensjon.vtp.mocks.samordning

import nav_cons_sto_sam_samordning.no.nav.inf.SAMSamordning
import nav_cons_sto_sam_samordning.no.nav.inf.VarsleEndringPersonaliaFaultStoGeneriskMsg
import nav_lib_cons_sto_sam.no.nav.lib.sto.sam.asbo.samordning.ASBOStoVarsleEndringPersonaliaRequest
import nav_lib_cons_sto_sam.no.nav.lib.sto.sam.asbo.samordning.ASBOStoVarsleManglendeRefusjonskravRequest
import nav_lib_cons_sto_sam.no.nav.lib.sto.sam.asbo.samordning.ASBOStoVarsleTPSamordningRequest
import nav_lib_cons_sto_sam.no.nav.lib.sto.sam.asbo.samordning.ASBOStoVarsleVedtakSamordningRequest
import no.nav.lib.sto.sam.asbo.person.ObjectFactory
import no.nav.lib.sto.sam.fault.FaultStoGenerisk
import no.nav.pensjon.vtp.annotations.SoapService
import javax.jws.*
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-cons-sto-sam-samordningWeb/sca/SAMSamordningWSEXP"])
@WebService(targetNamespace = "http://nav-cons-sto-sam-samordning/no/nav/inf", name = "SAMSamordning")
@XmlSeeAlso(
    ObjectFactory::class,
    nav_lib_cons_sto_sam.no.nav.lib.sto.sam.asbo.vedtak.ObjectFactory::class,
    no.nav.lib.sto.sam.asbo.ObjectFactory::class,
    nav_cons_sto_sam_samordning.no.nav.inf.ObjectFactory::class,
    nav_lib_cons_sto_sam.no.nav.lib.sto.sam.asbo.samordning.ObjectFactory::class,
    no.nav.lib.sto.sam.fault.ObjectFactory::class,
    nav_lib_cons_sto_sam.no.nav.lib.sto.sam.fault.samordning.ObjectFactory::class
)
@HandlerChain(file = "/Handler-chain.xml")
class SamSamordningMock : SAMSamordning {
    @WebMethod
    @RequestWrapper(
        localName = "varsleVedtakSamordning",
        targetNamespace = "http://nav-cons-sto-sam-samordning/no/nav/inf",
        className = "nav_cons_sto_sam_samordning.no.nav.inf.VarsleVedtakSamordning"
    )
    @ResponseWrapper(
        localName = "varsleVedtakSamordningResponse",
        targetNamespace = "http://nav-cons-sto-sam-samordning/no/nav/inf",
        className = "nav_cons_sto_sam_samordning.no.nav.inf.VarsleVedtakSamordningResponse"
    )
    @WebResult(name = "varsleVedtakSamordningResponse", targetNamespace = "")
    override fun varsleVedtakSamordning(
        @WebParam(
            name = "varsleVedtakSamordningRequest",
            targetNamespace = ""
        ) varsleVedtakSamordningRequest: ASBOStoVarsleVedtakSamordningRequest?
    ): String {
        TODO("Not yet implemented")
    }

    @WebMethod
    @RequestWrapper(
        localName = "varsleTPSamordning",
        targetNamespace = "http://nav-cons-sto-sam-samordning/no/nav/inf",
        className = "nav_cons_sto_sam_samordning.no.nav.inf.VarsleTPSamordning"
    )
    @ResponseWrapper(
        localName = "varsleTPSamordningResponse",
        targetNamespace = "http://nav-cons-sto-sam-samordning/no/nav/inf",
        className = "nav_cons_sto_sam_samordning.no.nav.inf.VarsleTPSamordningResponse"
    )
    @WebResult(name = "varsleTPSamordningResponse", targetNamespace = "")
    override fun varsleTPSamordning(
        @WebParam(name = "varsleTPSamordningRequest", targetNamespace = "")
        varsleTPSamordningRequest: ASBOStoVarsleTPSamordningRequest?
    ): String {
        TODO("Not yet implemented")
    }

    @WebMethod
    @RequestWrapper(
        localName = "varsleEndringPersonalia",
        targetNamespace = "http://nav-cons-sto-sam-samordning/no/nav/inf",
        className = "nav_cons_sto_sam_samordning.no.nav.inf.VarsleEndringPersonalia"
    )
    @ResponseWrapper(
        localName = "varsleEndringPersonaliaResponse",
        targetNamespace = "http://nav-cons-sto-sam-samordning/no/nav/inf",
        className = "nav_cons_sto_sam_samordning.no.nav.inf.VarsleEndringPersonaliaResponse"
    )
    @WebResult(name = "varsleEndringPersonaliaResponse", targetNamespace = "")
    override fun varsleEndringPersonalia(
        @WebParam(
            name = "varsleEndringPersonaliaRequest",
            targetNamespace = ""
        ) request: ASBOStoVarsleEndringPersonaliaRequest?
    ): String {
        if (request != null) {
            TODO("Not yet implemented")
        } else {
            throw VarsleEndringPersonaliaFaultStoGeneriskMsg("Request was null", FaultStoGenerisk())
        }
    }

    @WebMethod
    @RequestWrapper(
        localName = "varsleManglendeRefusjonskrav",
        targetNamespace = "http://nav-cons-sto-sam-samordning/no/nav/inf",
        className = "nav_cons_sto_sam_samordning.no.nav.inf.VarsleManglendeRefusjonskrav"
    )
    @ResponseWrapper(
        localName = "varsleManglendeRefusjonskravResponse",
        targetNamespace = "http://nav-cons-sto-sam-samordning/no/nav/inf",
        className = "nav_cons_sto_sam_samordning.no.nav.inf.VarsleManglendeRefusjonskravResponse"
    )
    @WebResult(name = "varsleManglendeRefusjonskravResponse", targetNamespace = "")
    override fun varsleManglendeRefusjonskrav(
        @WebParam(
            name = "varsleManglendeRefusjonskravRequest",
            targetNamespace = ""
        ) varsleManglendeRefusjonskravRequest: ASBOStoVarsleManglendeRefusjonskravRequest?
    ): String {
        TODO("Not yet implemented")
    }
}
