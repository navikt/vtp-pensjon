package no.nav.pensjon.vtp.mocks.dialog

import no.nav.pensjon.vtp.core.annotations.SoapService
import no.nav.tjeneste.virksomhet.dialog.v1.DialogV1
import no.nav.tjeneste.virksomhet.dialog.v1.meldinger.ObjectFactory
import no.nav.tjeneste.virksomhet.dialog.v1.meldinger.WSHentDialogerRequest
import no.nav.tjeneste.virksomhet.dialog.v1.meldinger.WSHentDialogerResponse
import javax.jws.HandlerChain
import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult
import javax.jws.WebService
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/henvendelse/services/domene.Virksomhet/Dialog_v1"])
@WebService(targetNamespace = "http://nav.no/tjeneste/virksomhet/dialog/v1", name = "Dialog_v1")
@XmlSeeAlso(
    ObjectFactory::class,
    no.nav.tjeneste.virksomhet.dialog.v1.feil.ObjectFactory::class,
    no.nav.tjeneste.virksomhet.dialog.v1.ObjectFactory::class,
    no.nav.tjeneste.virksomhet.dialog.v1.informasjon.ObjectFactory::class
)
@HandlerChain(file = "/Handler-chain.xml")
class DialogMock : DialogV1 {
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/dialog/v1/Dialog_v1/pingRequest")
    @RequestWrapper(
        localName = "ping",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/dialog/v1",
        className = "no.nav.tjeneste.virksomhet.dialog.v1.Ping"
    )
    @ResponseWrapper(
        localName = "pingResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/dialog/v1",
        className = "no.nav.tjeneste.virksomhet.dialog.v1.PingResponse"
    )
    override fun ping() = Unit

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/dialog/v1/Dialog_v1/hentDialogerRequest")
    @RequestWrapper(
        localName = "hentDialoger",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/dialog/v1",
        className = "no.nav.tjeneste.virksomhet.dialog.v1.HentDialoger"
    )
    @ResponseWrapper(
        localName = "hentDialogerResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/dialog/v1",
        className = "no.nav.tjeneste.virksomhet.dialog.v1.HentDialogerResponse"
    )
    @WebResult(name = "response")
    override fun hentDialoger(
        @WebParam(name = "request") request: WSHentDialogerRequest
    ) = WSHentDialogerResponse()
}
