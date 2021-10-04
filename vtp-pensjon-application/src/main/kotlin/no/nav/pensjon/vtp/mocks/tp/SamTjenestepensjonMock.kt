package no.nav.pensjon.vtp.mocks.tp

import nav_cons_sto_sam_tjenestepensjon.no.nav.inf.FinnTjenestepensjonsforholdFaultStoGeneriskMsg
import nav_cons_sto_sam_tjenestepensjon.no.nav.inf.SAMTjenestepensjon
import nav_lib_cons_sto_sam.no.nav.lib.sto.sam.asbo.tjenestepensjon.ASBOStoFinnTjenestepensjonsforholdRequest
import nav_lib_cons_sto_sam.no.nav.lib.sto.sam.asbo.tjenestepensjon.ASBOStoTjenestepensjon
import nav_lib_cons_sto_sam.no.nav.lib.sto.sam.asbo.tjenestepensjon.ASBOStoTjenestepensjonYtelse
import nav_lib_cons_sto_sam.no.nav.lib.sto.sam.asbo.tjenestepensjon.ASBOStoTjenestepensjonforhold
import no.nav.lib.sto.sam.fault.FaultStoGenerisk
import no.nav.pensjon.vtp.annotations.SoapService
import javax.jws.HandlerChain
import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-cons-sto-sam-tjenestepensjonWeb/sca/SAMTjenestepensjonWSEXP"])
@HandlerChain(file = "/Handler-chain.xml")
class SamTjenestepensjonMock : SAMTjenestepensjon {
    @WebMethod
    @RequestWrapper(
        localName = "opprettTjenestepensjonYtelse",
        targetNamespace = "http://nav-cons-sto-sam-tjenestepensjon/no/nav/inf",
        className = "nav_cons_sto_sam_tjenestepensjon.no.nav.inf.OpprettTjenestepensjonYtelse"
    )
    @ResponseWrapper(
        localName = "opprettTjenestepensjonYtelseResponse",
        targetNamespace = "http://nav-cons-sto-sam-tjenestepensjon/no/nav/inf",
        className = "nav_cons_sto_sam_tjenestepensjon.no.nav.inf.OpprettTjenestepensjonYtelseResponse"
    )
    @WebResult(name = "opprettTjenestepensjonYtelseResponse", targetNamespace = "")
    override fun opprettTjenestepensjonYtelse(
        @WebParam(
            name = "opprettTjenestepensjonYtelseRequest",
            targetNamespace = ""
        ) opprettTjenestepensjonYtelseRequest: ASBOStoTjenestepensjonforhold?
    ): ASBOStoTjenestepensjonYtelse {
        TODO("Not yet implemented")
    }

    @WebMethod
    @RequestWrapper(
        localName = "opprettTjenestepensjonsforhold",
        targetNamespace = "http://nav-cons-sto-sam-tjenestepensjon/no/nav/inf",
        className = "nav_cons_sto_sam_tjenestepensjon.no.nav.inf.OpprettTjenestepensjonsforhold"
    )
    @ResponseWrapper(
        localName = "opprettTjenestepensjonsforholdResponse",
        targetNamespace = "http://nav-cons-sto-sam-tjenestepensjon/no/nav/inf",
        className = "nav_cons_sto_sam_tjenestepensjon.no.nav.inf.OpprettTjenestepensjonsforholdResponse"
    )
    @WebResult(name = "opprettTjenestepensjonsforholdResponse", targetNamespace = "")
    override fun opprettTjenestepensjonsforhold(
        @WebParam(
            name = "opprettTjenestepensjonsforholdRequest",
            targetNamespace = ""
        ) opprettTjenestepensjonsforholdRequest: ASBOStoTjenestepensjon?
    ): ASBOStoTjenestepensjonforhold {
        TODO("Not yet implemented")
    }

    @WebMethod
    @RequestWrapper(
        localName = "slettTjenestepensjonYtelse",
        targetNamespace = "http://nav-cons-sto-sam-tjenestepensjon/no/nav/inf",
        className = "nav_cons_sto_sam_tjenestepensjon.no.nav.inf.SlettTjenestepensjonYtelse"
    )
    @ResponseWrapper(
        localName = "slettTjenestepensjonYtelseResponse",
        targetNamespace = "http://nav-cons-sto-sam-tjenestepensjon/no/nav/inf",
        className = "nav_cons_sto_sam_tjenestepensjon.no.nav.inf.SlettTjenestepensjonYtelseResponse"
    )
    @WebResult(name = "slettTjenestepensjonYtelseResponse", targetNamespace = "")
    override fun slettTjenestepensjonYtelse(
        @WebParam(
            name = "slettTjenestepensjonYtelseRequest",
            targetNamespace = ""
        ) slettTjenestepensjonYtelseRequest: ASBOStoTjenestepensjonYtelse?
    ): String {
        TODO("Not yet implemented")
    }

    @WebMethod
    @RequestWrapper(
        localName = "finnTjenestepensjonsforhold",
        targetNamespace = "http://nav-cons-sto-sam-tjenestepensjon/no/nav/inf",
        className = "nav_cons_sto_sam_tjenestepensjon.no.nav.inf.FinnTjenestepensjonsforhold"
    )
    @ResponseWrapper(
        localName = "finnTjenestepensjonsforholdResponse",
        targetNamespace = "http://nav-cons-sto-sam-tjenestepensjon/no/nav/inf",
        className = "nav_cons_sto_sam_tjenestepensjon.no.nav.inf.FinnTjenestepensjonsforholdResponse"
    )
    @WebResult(name = "finnTjenestepensjonsforholdResponse", targetNamespace = "")
    override fun finnTjenestepensjonsforhold(
        @WebParam(
            name = "finnTjenestepensjonsforholdRequest",
            targetNamespace = ""
        ) request: ASBOStoFinnTjenestepensjonsforholdRequest?
    ): ASBOStoTjenestepensjon {
        if (request != null) {
            TODO("Not yet implemented")
        } else {
            throw FinnTjenestepensjonsforholdFaultStoGeneriskMsg("Request was null", FaultStoGenerisk())
        }
    }

    @WebMethod
    @RequestWrapper(
        localName = "lagreTjenestepensjonYtelse",
        targetNamespace = "http://nav-cons-sto-sam-tjenestepensjon/no/nav/inf",
        className = "nav_cons_sto_sam_tjenestepensjon.no.nav.inf.LagreTjenestepensjonYtelse"
    )
    @ResponseWrapper(
        localName = "lagreTjenestepensjonYtelseResponse",
        targetNamespace = "http://nav-cons-sto-sam-tjenestepensjon/no/nav/inf",
        className = "nav_cons_sto_sam_tjenestepensjon.no.nav.inf.LagreTjenestepensjonYtelseResponse"
    )
    @WebResult(name = "lagreTjenestepensjonYtelseResponse", targetNamespace = "")
    override fun lagreTjenestepensjonYtelse(
        @WebParam(
            name = "lagreTjenestepensjonYtelseRequest",
            targetNamespace = ""
        ) lagreTjenestepensjonYtelseRequest: ASBOStoTjenestepensjonYtelse?
    ): ASBOStoTjenestepensjonYtelse {
        TODO("Not yet implemented")
    }
}
