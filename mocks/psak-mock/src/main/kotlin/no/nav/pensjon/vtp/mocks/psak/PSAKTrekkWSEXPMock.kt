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
    @RequestWrapper(
        localName = "hentSkattOgTrekk",
        targetNamespace = "http://nav-cons-pen-psak-trekk/no/nav/inf",
        className = "no.nav.inf.psak.trekk.HentSkattOgTrekk"
    )
    @ResponseWrapper(
        localName = "hentSkattOgTrekkResponse",
        targetNamespace = "http://nav-cons-pen-psak-trekk/no/nav/inf",
        className = "no.nav.inf.psak.trekk.HentSkattOgTrekkResponse"
    )
    @WebResult(name = "hentSkattOgTrekkResponse", targetNamespace = "")
    override fun hentSkattOgTrekk(
        @WebParam(
            name = "hentSkattOgTrekkRequest",
            targetNamespace = ""
        ) asboPenHentSkattOgTrekkRequest: ASBOPenHentSkattOgTrekkRequest
    ) = ASBOPenSkattOgTrekk().apply {
        skattetrekk = ASBOPenSkattetrekk()
    }

    @WebMethod
    @RequestWrapper(
        localName = "hentTrekkListe",
        targetNamespace = "http://nav-cons-pen-psak-trekk/no/nav/inf",
        className = "no.nav.inf.psak.trekk.HentTrekkListe"
    )
    @ResponseWrapper(
        localName = "hentTrekkListeResponse",
        targetNamespace = "http://nav-cons-pen-psak-trekk/no/nav/inf",
        className = "no.nav.inf.psak.trekk.HentTrekkListeResponse"
    )
    @WebResult(name = "hentTrekkListeResponse", targetNamespace = "")
    override fun hentTrekkListe(
        @WebParam(
            name = "hentTrekkListeRequest",
            targetNamespace = ""
        ) asboPenHentTrekkListeRequest: ASBOPenHentTrekkListeRequest
    ) = ASBOPenAndreTrekkListe().apply {
        trekkListe = emptyArray()
    }

    @WebMethod
    @RequestWrapper(
        localName = "hentTrekktransaksjonListe",
        targetNamespace = "http://nav-cons-pen-psak-trekk/no/nav/inf",
        className = "no.nav.inf.psak.trekk.HentTrekktransaksjonListe"
    )
    @ResponseWrapper(
        localName = "hentTrekktransaksjonListeResponse",
        targetNamespace = "http://nav-cons-pen-psak-trekk/no/nav/inf",
        className = "no.nav.inf.psak.trekk.HentTrekktransaksjonListeResponse"
    )
    @WebResult(name = "hentTrekktransaksjonListeResponse", targetNamespace = "")
    override fun hentTrekktransaksjonListe(
        @WebParam(
            name = "hentTrekktransaksjonListeRequest",
            targetNamespace = ""
        ) asboPenHentTrekktransaksjonListeRequest: ASBOPenHentTrekktransaksjonListeRequest
    ) = ASBOPenTrekktransaksjonListe().apply {
        trekktransaksjonListe = emptyArray()
    }
}
