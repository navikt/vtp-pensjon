package no.nav.pensjon.vtp.mocks.psak

import no.nav.inf.psak.utbetaling.HentPeriodisertUtbetalingListeFaultPenGeneriskMsg
import no.nav.inf.psak.utbetaling.HentUtbetalingListeFaultPenGeneriskSMsg
import no.nav.inf.psak.utbetaling.PSAKUtbetaling
import no.nav.lib.pen.psakpselv.asbo.utbetaling.ASBOPenUtbetalingRequest
import no.nav.lib.pen.psakpselv.asbo.utbetaling.ASBOPenUtbetalingResponse
import no.nav.lib.pen.psakpselv.fault.ObjectFactory
import no.nav.pensjon.vtp.core.annotations.SoapService
import javax.jws.*
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-cons-pen-psak-utbetalingWeb/sca/PSAKUtbetalingWSEXP"])
@WebService(targetNamespace = "http://nav-cons-pen-psak-utbetaling/no/nav/inf", name = "PSAKUtbetaling")
@XmlSeeAlso(ObjectFactory::class, no.nav.lib.pen.psakpselv.asbo.utbetaling.ObjectFactory::class, no.nav.inf.psak.utbetaling.ObjectFactory::class)
@HandlerChain(file = "/Handler-chain.xml")
class PSAKUtbetalingMock : PSAKUtbetaling {
    @WebMethod
    @RequestWrapper(localName = "hentUtbetalingListe", targetNamespace = "http://nav-cons-pen-psak-utbetaling/no/nav/inf", className = "no.nav.inf.psak.utbetaling.HentUtbetalingListe")
    @ResponseWrapper(localName = "hentUtbetalingListeResponse", targetNamespace = "http://nav-cons-pen-psak-utbetaling/no/nav/inf", className = "no.nav.inf.psak.utbetaling.HentUtbetalingListeResponse")
    @WebResult(name = "hentUtbetalingListeResponse", targetNamespace = "")
    @Throws(HentUtbetalingListeFaultPenGeneriskSMsg::class)
    override fun hentUtbetalingListe(@WebParam(name = "hentUtbetalingListeRequest", targetNamespace = "") asboPenUtbetalingRequest: ASBOPenUtbetalingRequest): ASBOPenUtbetalingResponse {
        val response = ASBOPenUtbetalingResponse()
        response.utbetalinger = emptyArray()
        return response
    }

    @WebMethod
    @RequestWrapper(localName = "hentPeriodisertUtbetalingListe", targetNamespace = "http://nav-cons-pen-psak-utbetaling/no/nav/inf", className = "no.nav.inf.psak.utbetaling.HentPeriodisertUtbetalingListe")
    @ResponseWrapper(localName = "hentPeriodisertUtbetalingListeResponse", targetNamespace = "http://nav-cons-pen-psak-utbetaling/no/nav/inf", className = "no.nav.inf.psak.utbetaling.HentPeriodisertUtbetalingListeResponse")
    @WebResult(name = "hentPeriodisertUtbetalingListeResponse", targetNamespace = "")
    @Throws(HentPeriodisertUtbetalingListeFaultPenGeneriskMsg::class)
    override fun hentPeriodisertUtbetalingListe(@WebParam(name = "hentPeriodisertUtbetalingListeRequest", targetNamespace = "") asboPenUtbetalingRequest: ASBOPenUtbetalingRequest): ASBOPenUtbetalingResponse {
        val response = ASBOPenUtbetalingResponse()
        response.utbetalinger = emptyArray()
        return response
    }
}
