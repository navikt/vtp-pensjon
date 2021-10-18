package no.nav.pensjon.vtp.mocks.pen

import no.nav.pensjon.vtp.annotations.SoapService
import no.nav.tjeneste.virksomhet.pensjonsvedtaksamordning.v1.ObjectFactory
import no.nav.tjeneste.virksomhet.pensjonsvedtaksamordning.v1.binding.PensjonsvedtakSamordningV1
import no.nav.tjeneste.virksomhet.pensjonsvedtaksamordning.v1.meldinger.HentVedtakListeRequest
import no.nav.tjeneste.virksomhet.pensjonsvedtaksamordning.v1.meldinger.HentVedtakListeResponse
import no.nav.tjeneste.virksomhet.pensjonsvedtaksamordning.v1.meldinger.HentVedtakRequest
import no.nav.tjeneste.virksomhet.pensjonsvedtaksamordning.v1.meldinger.HentVedtakResponse
import javax.jws.*
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/pen/services/PensjonsvedtakSamordning_v1"])
@WebService(
    name = "PensjonsvedtakSamordning_v1",
    targetNamespace = "http://nav.no/tjeneste/virksomhet/pensjonsvedtaksamordning/v1"
)
@XmlSeeAlso(
    ObjectFactory::class,
    no.nav.tjeneste.virksomhet.pensjonsvedtaksamordning.v1.informasjon.pensjonsvedtaksamordning.ObjectFactory::class,
    no.nav.tjeneste.virksomhet.pensjonsvedtaksamordning.v1.meldinger.ObjectFactory::class
)
@HandlerChain(file = "/Handler-chain.xml")
class PensjonsvedtakSamordningV1Mock : PensjonsvedtakSamordningV1 {
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/pensjonsvedtaksamordning/v1/PensjonsvedtakSamordning_v1/pingRequest")
    @RequestWrapper(
        localName = "ping",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/pensjonsvedtaksamordning/v1",
        className = "no.nav.tjeneste.virksomhet.pensjonsvedtaksamordning.v1.Ping"
    )
    @ResponseWrapper(
        localName = "pingResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/pensjonsvedtaksamordning/v1",
        className = "no.nav.tjeneste.virksomhet.pensjonsvedtaksamordning.v1.PingResponse"
    )
    override fun ping() = Unit

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/pensjonsvedtaksamordning/v1/PensjonsvedtakSamordning_v1/hentVedtakRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(
        localName = "hentVedtak",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/pensjonsvedtaksamordning/v1",
        className = "no.nav.tjeneste.virksomhet.pensjonsvedtaksamordning.v1.HentVedtak"
    )
    @ResponseWrapper(
        localName = "hentVedtakResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/pensjonsvedtaksamordning/v1",
        className = "no.nav.tjeneste.virksomhet.pensjonsvedtaksamordning.v1.HentVedtakResponse"
    )
    override fun hentVedtak(@WebParam(name = "request", targetNamespace = "") request: HentVedtakRequest?): HentVedtakResponse {
        TODO("Not yet implemented")
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/pensjonsvedtaksamordning/v1/PensjonsvedtakSamordning_v1/hentVedtakListeRequest")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(
        localName = "hentVedtakListe",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/pensjonsvedtaksamordning/v1",
        className = "no.nav.tjeneste.virksomhet.pensjonsvedtaksamordning.v1.HentVedtakListe"
    )
    @ResponseWrapper(
        localName = "hentVedtakListeResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/pensjonsvedtaksamordning/v1",
        className = "no.nav.tjeneste.virksomhet.pensjonsvedtaksamordning.v1.HentVedtakListeResponse"
    )
    override fun hentVedtakListe(@WebParam(name = "request", targetNamespace = "") request: HentVedtakListeRequest?): HentVedtakListeResponse {
        return HentVedtakListeResponse().also { it.vedtakListe }
    }
}
