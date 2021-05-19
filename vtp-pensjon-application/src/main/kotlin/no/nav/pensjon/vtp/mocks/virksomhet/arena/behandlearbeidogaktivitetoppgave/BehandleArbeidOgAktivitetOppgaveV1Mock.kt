package no.nav.pensjon.vtp.mocks.virksomhet.arena.behandlearbeidogaktivitetoppgave

import no.nav.pensjon.vtp.annotations.SoapService
import no.nav.tjeneste.virksomhet.behandlearbeidogaktivitetoppgave.v1.BehandleArbeidOgAktivitetOppgaveV1
import no.nav.tjeneste.virksomhet.behandlearbeidogaktivitetoppgave.v1.meldinger.WSBestillOppgaveRequest
import no.nav.tjeneste.virksomhet.behandlearbeidogaktivitetoppgave.v1.meldinger.WSBestillOppgaveResponse
import javax.jws.HandlerChain
import javax.jws.WebMethod
import javax.jws.WebService
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/ail_ws/BehandleArbeidOgAktivitetOppgave_v1"])
@WebService(
    targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleArbeidOgAktivitetOppgave/v1",
    name = "BehandleArbeidOgAktivitetOppgave_v1"
)
@XmlSeeAlso(
    no.nav.tjeneste.virksomhet.behandlearbeidogaktivitetoppgave.v1.ObjectFactory::class,
    no.nav.tjeneste.virksomhet.behandlearbeidogaktivitetoppgave.v1.meldinger.ObjectFactory::class,
    no.nav.tjeneste.virksomhet.behandlearbeidogaktivitetoppgave.v1.feil.ObjectFactory::class,
    no.nav.tjeneste.virksomhet.behandlearbeidogaktivitetoppgave.v1.informasjon.ObjectFactory::class
)
@HandlerChain(file = "/Handler-chain.xml")
class BehandleArbeidOgAktivitetOppgaveV1Mock : BehandleArbeidOgAktivitetOppgaveV1 {

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/behandleArbeidOgAktivitetOppgave/v1/BehandleArbeidOgAktivitetOppgave_v1/bestillOppgaveRequest")
    @RequestWrapper(
        localName = "bestillOppgave",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleArbeidOgAktivitetOppgave/v1",
        className = "no.nav.tjeneste.virksomhet.behandlearbeidogaktivitetoppgave.v1.BestillOppgave"
    )
    @ResponseWrapper(
        localName = "bestillOppgaveResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleArbeidOgAktivitetOppgave/v1",
        className = "no.nav.tjeneste.virksomhet.behandlearbeidogaktivitetoppgave.v1.BestillOppgaveResponse"
    )
    override fun bestillOppgave(request: WSBestillOppgaveRequest): WSBestillOppgaveResponse {
        return WSBestillOppgaveResponse()
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/behandleArbeidOgAktivitetOppgave/v1/BehandleArbeidOgAktivitetOppgave_v1/pingRequest")
    @RequestWrapper(
        localName = "ping",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleArbeidOgAktivitetOppgave/v1",
        className = "no.nav.tjeneste.virksomhet.behandlearbeidogaktivitetoppgave.v1.Ping"
    )
    @ResponseWrapper(
        localName = "pingResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleArbeidOgAktivitetOppgave/v1",
        className = "no.nav.tjeneste.virksomhet.behandlearbeidogaktivitetoppgave.v1.PingResponse"
    )
    override fun ping() = Unit
}
