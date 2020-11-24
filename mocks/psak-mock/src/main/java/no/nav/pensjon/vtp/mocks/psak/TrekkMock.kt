package no.nav.pensjon.vtp.mocks.psak

import no.nav.pensjon.vtp.core.annotations.SoapService
import no.nav.virksomhet.tjenester.trekk.feil.v1.ObjectFactory
import no.nav.virksomhet.tjenester.trekk.meldinger.v1.*
import no.nav.virksomhet.tjenester.trekk.v1.FinnTrekkListeKreditorFinnesIkke
import no.nav.virksomhet.tjenester.trekk.v1.FinnTrekkListeKreditorHarFlereAvdelinger
import no.nav.virksomhet.tjenester.trekk.v1.Trekk
import javax.jws.*
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-tjeneste-trekk_v1Web/sca/TrekkWSEXP"])
@WebService(targetNamespace = "http://nav.no/virksomhet/tjenester/trekk/v1", name = "Trekk")
@XmlSeeAlso(ObjectFactory::class, no.nav.virksomhet.okonomi.trekk.v1.ObjectFactory::class, no.nav.virksomhet.tjenester.trekk.v1.ObjectFactory::class, no.nav.virksomhet.tjenester.trekk.meldinger.v1.ObjectFactory::class)
@HandlerChain(file = "/Handler-chain.xml")
class TrekkMock : Trekk {
    @WebMethod
    @RequestWrapper(localName = "hentTrekktransaksjonListe", targetNamespace = "http://nav.no/virksomhet/tjenester/trekk/v1", className = "no.nav.virksomhet.tjenester.trekk.v1.HentTrekktransaksjonListe")
    @ResponseWrapper(localName = "hentTrekktransaksjonListeResponse", targetNamespace = "http://nav.no/virksomhet/tjenester/trekk/v1", className = "no.nav.virksomhet.tjenester.trekk.v1.HentTrekktransaksjonListeResponse")
    @WebResult(name = "response", targetNamespace = "")
    override fun hentTrekktransaksjonListe(@WebParam(name = "request", targetNamespace = "") hentTrekktransaksjonListeRequest: HentTrekktransaksjonListeRequest): HentTrekktransaksjonListeResponse {
        val response = HentTrekktransaksjonListeResponse()
        response.trekktransaksjonListe = emptyArray()
        return response
    }

    @WebMethod
    @RequestWrapper(localName = "finnTrekkListe", targetNamespace = "http://nav.no/virksomhet/tjenester/trekk/v1", className = "no.nav.virksomhet.tjenester.trekk.v1.FinnTrekkListe")
    @ResponseWrapper(localName = "finnTrekkListeResponse", targetNamespace = "http://nav.no/virksomhet/tjenester/trekk/v1", className = "no.nav.virksomhet.tjenester.trekk.v1.FinnTrekkListeResponse")
    @WebResult(name = "response", targetNamespace = "")
    @Throws(FinnTrekkListeKreditorFinnesIkke::class, FinnTrekkListeKreditorHarFlereAvdelinger::class)
    override fun finnTrekkListe(@WebParam(name = "request", targetNamespace = "") finnTrekkListeRequest: FinnTrekkListeRequest): FinnTrekkListeResponse {
        val response = FinnTrekkListeResponse()
        response.trekkInfoListe = emptyArray()
        return response
    }

    @WebMethod
    @RequestWrapper(localName = "hentSkattOgTrekk", targetNamespace = "http://nav.no/virksomhet/tjenester/trekk/v1", className = "no.nav.virksomhet.tjenester.trekk.v1.HentSkattOgTrekk")
    @ResponseWrapper(localName = "hentSkattOgTrekkResponse", targetNamespace = "http://nav.no/virksomhet/tjenester/trekk/v1", className = "no.nav.virksomhet.tjenester.trekk.v1.HentSkattOgTrekkResponse")
    @WebResult(name = "response", targetNamespace = "")
    override fun hentSkattOgTrekk(@WebParam(name = "request", targetNamespace = "") hentSkattOgTrekkRequest: HentSkattOgTrekkRequest): HentSkattOgTrekkResponse {
        return HentSkattOgTrekkResponse()
    }
}