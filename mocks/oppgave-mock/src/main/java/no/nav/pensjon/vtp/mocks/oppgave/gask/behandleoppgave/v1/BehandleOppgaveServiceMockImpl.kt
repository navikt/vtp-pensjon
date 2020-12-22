package no.nav.pensjon.vtp.mocks.oppgave.gask.behandleoppgave.v1

import no.nav.pensjon.vtp.core.annotations.SoapService
import no.nav.pensjon.vtp.mocks.oppgave.gask.sak.v1.GsakRepo
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import no.nav.tjeneste.virksomhet.behandleoppgave.v1.BehandleOppgaveV1
import no.nav.tjeneste.virksomhet.behandleoppgave.v1.meldinger.WSFerdigstillOppgaveRequest
import no.nav.tjeneste.virksomhet.behandleoppgave.v1.meldinger.WSFerdigstillOppgaveResponse
import no.nav.tjeneste.virksomhet.behandleoppgave.v1.meldinger.WSLagreOppgaveRequest
import no.nav.tjeneste.virksomhet.behandleoppgave.v1.meldinger.WSOpprettOppgaveRequest
import no.nav.tjeneste.virksomhet.behandleoppgave.v1.meldinger.WSOpprettOppgaveResponse
import javax.jws.HandlerChain
import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult
import javax.jws.WebService
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper
import javax.xml.ws.soap.Addressing

@SoapService(path = ["/nav-gsak-ws/BehandleOppgaveV1"])
@Addressing
@WebService(targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleoppgave/v1", name = "BehandleOppgaveV1")
@HandlerChain(file = "/Handler-chain.xml")
class BehandleOppgaveServiceMockImpl(private val repo: GsakRepo) : BehandleOppgaveV1 {
    @RequestWrapper(
        localName = "ping",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleoppgave/v1",
        className = "no.nav.tjeneste.virksomhet.behandleoppgave.v1.Ping"
    )
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/behandleoppgave/v1/BehandleOppgave_v1/pingRequest")
    @ResponseWrapper(
        localName = "pingResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleoppgave/v1",
        className = "no.nav.tjeneste.virksomhet.behandleoppgave.v1.PingResponse"
    )
    override fun ping() = Unit

    @WebResult(name = "response")
    @RequestWrapper(
        localName = "ferdigstillOppgave",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleoppgave/v1",
        className = "no.nav.tjeneste.virksomhet.behandleoppgave.v1.FerdigstillOppgave"
    )
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/behandleoppgave/v1/BehandleOppgave_v1/ferdigstillOppgaveRequest")
    @ResponseWrapper(
        localName = "ferdigstillOppgaveResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleoppgave/v1",
        className = "no.nav.tjeneste.virksomhet.behandleoppgave.v1.FerdigstillOppgaveResponse"
    )
    override fun ferdigstillOppgave(
        @WebParam(name = "request") request: WSFerdigstillOppgaveRequest
    ) = WSFerdigstillOppgaveResponse()

    @RequestWrapper(
        localName = "lagreOppgave",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleoppgave/v1",
        className = "no.nav.tjeneste.virksomhet.behandleoppgave.v1.LagreOppgave"
    )
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/behandleoppgave/v1/BehandleOppgave_v1/lagreOppgaveRequest")
    @ResponseWrapper(
        localName = "lagreOppgaveResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleoppgave/v1",
        className = "no.nav.tjeneste.virksomhet.behandleoppgave.v1.LagreOppgaveResponse"
    )
    override fun lagreOppgave(
        @WebParam(name = "request") request: WSLagreOppgaveRequest
    ) = throw NotImplementedException()

    @WebResult(name = "response")
    @RequestWrapper(
        localName = "opprettOppgave",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleoppgave/v1",
        className = "no.nav.tjeneste.virksomhet.behandleoppgave.v1.OpprettOppgave"
    )
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/behandleoppgave/v1/BehandleOppgave_v1/opprettOppgaveRequest")
    @ResponseWrapper(
        localName = "opprettOppgaveResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleoppgave/v1",
        className = "no.nav.tjeneste.virksomhet.behandleoppgave.v1.OpprettOppgaveResponse"
    )
    override fun opprettOppgave(
        @WebParam(name = "request") request: WSOpprettOppgaveRequest
    ) = WSOpprettOppgaveResponse().apply {
        oppgaveId = repo.opprettOppgave(request.wsOppgave.saksnummer)
    }
}
