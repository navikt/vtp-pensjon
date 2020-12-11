package no.nav.pensjon.vtp.mocks.psak

import no.nav.inf.psak.trekk.*
import no.nav.lib.pen.psakpselv.asbo.trekk.*
import no.nav.lib.pen.psakpselv.fault.ObjectFactory
import no.nav.pensjon.vtp.core.annotations.SoapService
import javax.jws.*
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-cons-pen-psak-trekkWeb/sca/PSAKTrekkWSEXP"])
@WebService(targetNamespace = "http://nav-cons-pen-psak-trekk/no/nav/inf", name = "PSAKTrekk")
@XmlSeeAlso(ObjectFactory::class, no.nav.lib.pen.psakpselv.asbo.ObjectFactory::class, no.nav.lib.pen.psakpselv.asbo.trekk.ObjectFactory::class, no.nav.inf.psak.trekk.ObjectFactory::class, no.nav.lib.pen.psakpselv.fault.trekk.ObjectFactory::class)
@HandlerChain(file = "/Handler-chain.xml")
class PSAKTrekkWSEXPMock : PSAKTrekk {
    @WebMethod
    @RequestWrapper(localName = "hentSkattOgTrekk", targetNamespace = "http://nav-cons-pen-psak-trekk/no/nav/inf", className = "no.nav.inf.psak.trekk.HentSkattOgTrekk")
    @ResponseWrapper(localName = "hentSkattOgTrekkResponse", targetNamespace = "http://nav-cons-pen-psak-trekk/no/nav/inf", className = "no.nav.inf.psak.trekk.HentSkattOgTrekkResponse")
    @WebResult(name = "hentSkattOgTrekkResponse", targetNamespace = "")
    @Throws(HentSkattOgTrekkFaultPenGeneriskMsg::class, HentSkattOgTrekkFaultPenIngenVedtaksopplysningerFunnetMsg::class)
    override fun hentSkattOgTrekk(@WebParam(name = "hentSkattOgTrekkRequest", targetNamespace = "") asboPenHentSkattOgTrekkRequest: ASBOPenHentSkattOgTrekkRequest): ASBOPenSkattOgTrekk {
        val trekk = ASBOPenSkattOgTrekk()
        trekk.skattetrekk = ASBOPenSkattetrekk()
        return trekk
    }

    @WebMethod
    @RequestWrapper(localName = "hentTrekkListe", targetNamespace = "http://nav-cons-pen-psak-trekk/no/nav/inf", className = "no.nav.inf.psak.trekk.HentTrekkListe")
    @ResponseWrapper(localName = "hentTrekkListeResponse", targetNamespace = "http://nav-cons-pen-psak-trekk/no/nav/inf", className = "no.nav.inf.psak.trekk.HentTrekkListeResponse")
    @WebResult(name = "hentTrekkListeResponse", targetNamespace = "")
    @Throws(HentTrekkListeFaultPenGeneriskMsg::class, HentTrekkListeFaultPenKreditorAvdMaaOppgisMsg::class, HentTrekkListeFaultPenIngenTrekkFunnetMsg::class, HentTrekkListeFaultPenKreditorIkkeRegMsg::class)
    override fun hentTrekkListe(@WebParam(name = "hentTrekkListeRequest", targetNamespace = "") asboPenHentTrekkListeRequest: ASBOPenHentTrekkListeRequest): ASBOPenAndreTrekkListe {
        val liste = ASBOPenAndreTrekkListe()
        liste.setTrekkListe(emptyArray())
        return liste
    }

    @WebMethod
    @RequestWrapper(localName = "hentTrekktransaksjonListe", targetNamespace = "http://nav-cons-pen-psak-trekk/no/nav/inf", className = "no.nav.inf.psak.trekk.HentTrekktransaksjonListe")
    @ResponseWrapper(localName = "hentTrekktransaksjonListeResponse", targetNamespace = "http://nav-cons-pen-psak-trekk/no/nav/inf", className = "no.nav.inf.psak.trekk.HentTrekktransaksjonListeResponse")
    @WebResult(name = "hentTrekktransaksjonListeResponse", targetNamespace = "")
    @Throws(HentTrekktransaksjonListeFaultPenGeneriskMsg::class, HentTrekktransaksjonListeFaultPenIngenTransaksjonerFunnetMsg::class, HentTrekktransaksjonListeFaultPenTrekkvedtakIkkeFunnetMsg::class)
    override fun hentTrekktransaksjonListe(@WebParam(name = "hentTrekktransaksjonListeRequest", targetNamespace = "") asboPenHentTrekktransaksjonListeRequest: ASBOPenHentTrekktransaksjonListeRequest): ASBOPenTrekktransaksjonListe {
        val liste = ASBOPenTrekktransaksjonListe()
        liste.trekktransaksjonListe = emptyArray()
        return liste
    }
}
