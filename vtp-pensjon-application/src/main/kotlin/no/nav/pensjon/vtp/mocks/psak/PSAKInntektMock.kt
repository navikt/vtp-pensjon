package no.nav.pensjon.vtp.mocks.psak

import no.nav.inf.psak.inntekt.*
import no.nav.lib.pen.psakpselv.asbo.inntekt.ASBOPenHentInntektListeRequest
import no.nav.lib.pen.psakpselv.asbo.inntekt.ASBOPenInntekt
import no.nav.lib.pen.psakpselv.asbo.inntekt.ASBOPenInntektListe
import no.nav.lib.pen.psakpselv.fault.ObjectFactory
import no.nav.pensjon.vtp.annotations.SoapService
import javax.jws.*
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-cons-pen-psak-inntektWeb/sca/PSAKInntektWSEXP"])
@WebService(targetNamespace = "http://nav-cons-pen-psak-inntekt/no/nav/inf", name = "PSAKInntekt")
@XmlSeeAlso(ObjectFactory::class, no.nav.lib.pen.psakpselv.asbo.ObjectFactory::class, no.nav.lib.pen.psakpselv.fault.inntekt.ObjectFactory::class, no.nav.lib.pen.psakpselv.asbo.inntekt.ObjectFactory::class, no.nav.inf.psak.inntekt.ObjectFactory::class)
@HandlerChain(file = "/Handler-chain.xml")
class PSAKInntektMock : PSAKInntekt {
    @WebMethod
    @RequestWrapper(localName = "hentDetaljertInntekt", targetNamespace = "http://nav-cons-pen-psak-inntekt/no/nav/inf", className = "no.nav.inf.psak.inntekt.HentDetaljertInntekt")
    @ResponseWrapper(localName = "hentDetaljertInntektResponse", targetNamespace = "http://nav-cons-pen-psak-inntekt/no/nav/inf", className = "no.nav.inf.psak.inntekt.HentDetaljertInntektResponse")
    @WebResult(name = "hentDetaljertInntektResponse", targetNamespace = "")
    @Throws(HentDetaljertInntektFaultPenGeneriskMsg::class, HentDetaljertInntektFaultPenInntektIkkeFunnetMsg::class)
    override fun hentDetaljertInntekt(@WebParam(name = "hentDetaljertInntektRequest", targetNamespace = "") asboPenInntekt: ASBOPenInntekt): ASBOPenInntekt {
        return asboPenInntekt
    }

    @WebMethod
    @RequestWrapper(localName = "slettBrukerOppgittInntekt", targetNamespace = "http://nav-cons-pen-psak-inntekt/no/nav/inf", className = "no.nav.inf.psak.inntekt.SlettBrukerOppgittInntekt")
    @ResponseWrapper(localName = "slettBrukerOppgittInntektResponse", targetNamespace = "http://nav-cons-pen-psak-inntekt/no/nav/inf", className = "no.nav.inf.psak.inntekt.SlettBrukerOppgittInntektResponse")
    @Throws(SlettBrukerOppgittInntektFaultPenGeneriskMsg::class, SlettBrukerOppgittInntektFaultPenInntektIkkeFunnetFMsg::class, SlettBrukerOppgittInntektFaultPenFeilKildeMsg::class)
    override fun slettBrukerOppgittInntekt(@WebParam(name = "slettBrukerOppgittInntektRequest", targetNamespace = "") asboPenInntekt: ASBOPenInntekt) {
    }

    @WebMethod
    @RequestWrapper(localName = "opprettBrukerOppgittInntekt", targetNamespace = "http://nav-cons-pen-psak-inntekt/no/nav/inf", className = "no.nav.inf.psak.inntekt.OpprettBrukerOppgittInntekt")
    @ResponseWrapper(localName = "opprettBrukerOppgittInntektResponse", targetNamespace = "http://nav-cons-pen-psak-inntekt/no/nav/inf", className = "no.nav.inf.psak.inntekt.OpprettBrukerOppgittInntektResponse")
    @WebResult(name = "opprettBrukerOppgittInntektResponse", targetNamespace = "")
    @Throws(OpprettBrukerOppgittInntektFaultPenGeneriskMsg::class, OpprettBrukerOppgittInntektFaultPenInntektAlleredeRegistrertMsg::class)
    override fun opprettBrukerOppgittInntekt(@WebParam(name = "opprettBrukerOppgittInntektRequest", targetNamespace = "") asboPenInntekt: ASBOPenInntekt): ASBOPenInntekt {
        return asboPenInntekt
    }

    @WebMethod
    @RequestWrapper(localName = "hentInntektListe", targetNamespace = "http://nav-cons-pen-psak-inntekt/no/nav/inf", className = "no.nav.inf.psak.inntekt.HentInntektListe")
    @ResponseWrapper(localName = "hentInntektListeResponse", targetNamespace = "http://nav-cons-pen-psak-inntekt/no/nav/inf", className = "no.nav.inf.psak.inntekt.HentInntektListeResponse")
    @WebResult(name = "hentInntektListeResponse", targetNamespace = "")
    @Throws(HentInntektListeFaultPenGeneriskMsg::class)
    override fun hentInntektListe(@WebParam(name = "hentInntektListeRequest", targetNamespace = "") asboPenHentInntektListeRequest: ASBOPenHentInntektListeRequest): ASBOPenInntektListe {
        return ASBOPenInntektListe()
    }
}
