package no.nav.pensjon.vtp.mocks.oppgave.gask.oppgavebehandling.v3

import no.nav.pensjon.vtp.mocks.oppgave.gask.sak.v1.GsakRepo
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import no.nav.tjeneste.virksomhet.oppgavebehandling.v3.binding.OppgavebehandlingV3
import no.nav.tjeneste.virksomhet.oppgavebehandling.v3.meldinger.*
import javax.jws.*
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper
import javax.xml.ws.soap.Addressing

@Addressing
@WebService(name = "Oppgavebehandling_v3", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3")
@HandlerChain(file = "/Handler-chain.xml")
class OppgavebehandlingServiceMockImpl(private val gsakRepo: GsakRepo) : OppgavebehandlingV3 {
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3/Oppgavebehandling_v3/pingRequest")
    @RequestWrapper(
        localName = "ping",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3",
        className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.Ping"
    )
    @ResponseWrapper(
        localName = "pingResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3",
        className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.PingResponse"
    )
    override fun ping() = Unit

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3/Oppgavebehandling_v3/opprettOppgaveRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "opprettOppgave",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3",
        className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.OpprettOppgave"
    )
    @ResponseWrapper(
        localName = "opprettOppgaveResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3",
        className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.OpprettOppgaveResponse"
    )
    override fun opprettOppgave(@WebParam(name = "request") opprettOppgaveRequest: OpprettOppgaveRequest) =
        OpprettOppgaveResponse().apply {
            oppgaveId = gsakRepo.opprettOppgave(opprettOppgaveRequest.opprettOppgave.saksnummer)
        }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3/Oppgavebehandling_v3/feilregistrerOppgaveRequest")
    @RequestWrapper(
        localName = "feilregistrerOppgave",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3",
        className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.FeilregistrerOppgave"
    )
    @ResponseWrapper(
        localName = "feilregistrerOppgaveResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3",
        className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.FeilregistrerOppgaveResponse"
    )
    override fun feilregistrerOppgave(@WebParam(name = "request") feilregistrerOppgaveRequest: FeilregistrerOppgaveRequest) =
        throw NotImplementedException()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3/Oppgavebehandling_v3/lagreOppgaveRequest")
    @RequestWrapper(
        localName = "lagreOppgave",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3",
        className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.LagreOppgave"
    )
    @ResponseWrapper(
        localName = "lagreOppgaveResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3",
        className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.LagreOppgaveResponse"
    )
    override fun lagreOppgave(@WebParam(name = "request") lagreOppgaveRequest: LagreOppgaveRequest) =
        throw NotImplementedException()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3/Oppgavebehandling_v3/opprettOppgaveBolkRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "opprettOppgaveBolk",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3",
        className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.OpprettOppgaveBolk"
    )
    @ResponseWrapper(
        localName = "opprettOppgaveBolkResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3",
        className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.OpprettOppgaveBolkResponse"
    )
    override fun opprettOppgaveBolk(@WebParam(name = "request") opprettOppgaveBolkRequest: OpprettOppgaveBolkRequest) =
        throw NotImplementedException()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3/Oppgavebehandling_v3/ferdigstillOppgaveBolkRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "ferdigstillOppgaveBolk",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3",
        className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.FerdigstillOppgaveBolk"
    )
    @ResponseWrapper(
        localName = "ferdigstillOppgaveBolkResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3",
        className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.FerdigstillOppgaveBolkResponse"
    )
    override fun ferdigstillOppgaveBolk(@WebParam(name = "request") ferdigstillOppgaveBolkRequest: FerdigstillOppgaveBolkRequest) =
        FerdigstillOppgaveBolkResponse().apply {
            isTransaksjonOk = true
        }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3/Oppgavebehandling_v3/opprettMappeRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "opprettMappe",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3",
        className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.OpprettMappe"
    )
    @ResponseWrapper(
        localName = "opprettMappeResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3",
        className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.OpprettMappeResponse"
    )
    override fun opprettMappe(@WebParam(name = "request") opprettMappeRequest: OpprettMappeRequest) =
        throw NotImplementedException()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3/Oppgavebehandling_v3/lagreMappeRequest")
    @RequestWrapper(
        localName = "lagreMappe",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3",
        className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.LagreMappe"
    )
    @ResponseWrapper(
        localName = "lagreMappeResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3",
        className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.LagreMappeResponse"
    )
    override fun lagreMappe(@WebParam(name = "request") lagreMappeRequest: LagreMappeRequest) =
        throw NotImplementedException()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3/Oppgavebehandling_v3/slettMappeRequest")
    @RequestWrapper(
        localName = "slettMappe",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3",
        className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.SlettMappe"
    )
    @ResponseWrapper(
        localName = "slettMappeResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3",
        className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.SlettMappeResponse"
    )
    override fun slettMappe(@WebParam(name = "request") slettMappeRequest: SlettMappeRequest) =
        throw NotImplementedException()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3/Oppgavebehandling_v3/lagreOppgaveBolkRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "lagreOppgaveBolk",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3",
        className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.LagreOppgaveBolk"
    )
    @ResponseWrapper(
        localName = "lagreOppgaveBolkResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3",
        className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.LagreOppgaveBolkResponse"
    )
    override fun lagreOppgaveBolk(@WebParam(name = "request") lagreOppgaveBolkRequest: LagreOppgaveBolkRequest) =
        throw NotImplementedException()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3/Oppgavebehandling_v3/tildelOppgaveRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "tildelOppgave",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3",
        className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.TildelOppgave"
    )
    @ResponseWrapper(
        localName = "tildelOppgaveResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3",
        className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.TildelOppgaveResponse"
    )
    override fun tildelOppgave(@WebParam(name = "request") tildelOppgaveRequest: TildelOppgaveRequest) =
        throw NotImplementedException()
}
