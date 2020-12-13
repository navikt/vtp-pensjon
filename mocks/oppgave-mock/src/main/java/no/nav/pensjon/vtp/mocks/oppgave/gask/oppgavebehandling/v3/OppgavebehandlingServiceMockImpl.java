package no.nav.pensjon.vtp.mocks.oppgave.gask.oppgavebehandling.v3;

import no.nav.pensjon.vtp.mocks.oppgave.gask.sak.v1.GsakRepo;
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException;
import no.nav.tjeneste.virksomhet.oppgavebehandling.v3.binding.OppgavebehandlingV3;
import no.nav.tjeneste.virksomhet.oppgavebehandling.v3.binding.SlettMappeMappeIkkeTom;
import no.nav.tjeneste.virksomhet.oppgavebehandling.v3.meldinger.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.*;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;
import java.util.stream.Collectors;

@Addressing
@WebService(name = "Oppgavebehandling_v3", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3")
@HandlerChain(file = "/Handler-chain.xml")
public class OppgavebehandlingServiceMockImpl implements OppgavebehandlingV3 {

    private static final Logger LOG = LoggerFactory.getLogger(OppgavebehandlingServiceMockImpl.class);
    private final GsakRepo gsakRepo;

    public OppgavebehandlingServiceMockImpl(GsakRepo gsakRepo) {
        this.gsakRepo = gsakRepo;
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3/Oppgavebehandling_v3/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3", className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3", className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.PingResponse")
    public void ping() {
        LOG.info("Ping mottatt og besvart.");
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3/Oppgavebehandling_v3/opprettOppgaveRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "opprettOppgave", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3", className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.OpprettOppgave")
    @ResponseWrapper(localName = "opprettOppgaveResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3", className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.OpprettOppgaveResponse")
    public OpprettOppgaveResponse opprettOppgave(@WebParam(name = "request") OpprettOppgaveRequest opprettOppgaveRequest) {
        LOG.info("Oppgavebehandling_opprettOppgave. OpprettetAvEnhetId: {}, oppgavetypeKode: {}, brukerId {}", opprettOppgaveRequest.getOpprettetAvEnhetId(), opprettOppgaveRequest.getOpprettOppgave().getOppgavetypeKode(),
                opprettOppgaveRequest.getOpprettOppgave().getBrukerId());
        OpprettOppgaveResponse opprettOppgaveResponse = new OpprettOppgaveResponse();
        String oppgaveId = gsakRepo.opprettOppgave(opprettOppgaveRequest.getOpprettOppgave().getSaksnummer());
        opprettOppgaveResponse.setOppgaveId(oppgaveId);
        return opprettOppgaveResponse;
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3/Oppgavebehandling_v3/feilregistrerOppgaveRequest")
    @RequestWrapper(localName = "feilregistrerOppgave", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3", className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.FeilregistrerOppgave")
    @ResponseWrapper(localName = "feilregistrerOppgaveResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3", className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.FeilregistrerOppgaveResponse")
    public void feilregistrerOppgave(@WebParam(name = "request") FeilregistrerOppgaveRequest feilregistrerOppgaveRequest) {
        throw new NotImplementedException();
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3/Oppgavebehandling_v3/lagreOppgaveRequest")
    @RequestWrapper(localName = "lagreOppgave", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3", className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.LagreOppgave")
    @ResponseWrapper(localName = "lagreOppgaveResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3", className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.LagreOppgaveResponse")
    public void lagreOppgave(@WebParam(name = "request") LagreOppgaveRequest lagreOppgaveRequest) {
        throw new NotImplementedException();
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3/Oppgavebehandling_v3/opprettOppgaveBolkRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "opprettOppgaveBolk", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3", className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.OpprettOppgaveBolk")
    @ResponseWrapper(localName = "opprettOppgaveBolkResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3", className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.OpprettOppgaveBolkResponse")
    public OpprettOppgaveBolkResponse opprettOppgaveBolk(@WebParam(name = "request") OpprettOppgaveBolkRequest opprettOppgaveBolkRequest) {
        throw new NotImplementedException();
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3/Oppgavebehandling_v3/ferdigstillOppgaveBolkRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "ferdigstillOppgaveBolk", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3", className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.FerdigstillOppgaveBolk")
    @ResponseWrapper(localName = "ferdigstillOppgaveBolkResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3", className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.FerdigstillOppgaveBolkResponse")
    public FerdigstillOppgaveBolkResponse ferdigstillOppgaveBolk(@WebParam(name = "request") FerdigstillOppgaveBolkRequest ferdigstillOppgaveBolkRequest) {
// throw new NotImplementedException();
        LOG.info("Oppgavebehandling_ferdigstillOppgaveBolk. oppgaveIdListe: {}", ferdigstillOppgaveBolkRequest.getOppgaveIdListe().stream().collect(Collectors.joining(",")));
        FerdigstillOppgaveBolkResponse response = new FerdigstillOppgaveBolkResponse();
        response.setTransaksjonOk(true);
        return response;
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3/Oppgavebehandling_v3/opprettMappeRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "opprettMappe", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3", className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.OpprettMappe")
    @ResponseWrapper(localName = "opprettMappeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3", className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.OpprettMappeResponse")
    public OpprettMappeResponse opprettMappe(@WebParam(name = "request") OpprettMappeRequest opprettMappeRequest) {
        throw new NotImplementedException();
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3/Oppgavebehandling_v3/lagreMappeRequest")
    @RequestWrapper(localName = "lagreMappe", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3", className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.LagreMappe")
    @ResponseWrapper(localName = "lagreMappeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3", className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.LagreMappeResponse")
    public void lagreMappe(@WebParam(name = "request") LagreMappeRequest lagreMappeRequest) {
        throw new NotImplementedException();

    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3/Oppgavebehandling_v3/slettMappeRequest")
    @RequestWrapper(localName = "slettMappe", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3", className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.SlettMappe")
    @ResponseWrapper(localName = "slettMappeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3", className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.SlettMappeResponse")
    public void slettMappe(@WebParam(name = "request") SlettMappeRequest slettMappeRequest)
            throws SlettMappeMappeIkkeTom {
        throw new NotImplementedException();

    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3/Oppgavebehandling_v3/lagreOppgaveBolkRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "lagreOppgaveBolk", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3", className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.LagreOppgaveBolk")
    @ResponseWrapper(localName = "lagreOppgaveBolkResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3", className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.LagreOppgaveBolkResponse")
    public LagreOppgaveBolkResponse lagreOppgaveBolk(@WebParam(name = "request") LagreOppgaveBolkRequest lagreOppgaveBolkRequest) {
        throw new NotImplementedException();
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3/Oppgavebehandling_v3/tildelOppgaveRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "tildelOppgave", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3", className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.TildelOppgave")
    @ResponseWrapper(localName = "tildelOppgaveResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgavebehandling/v3", className = "no.nav.tjeneste.virksomhet.oppgavebehandling.v3.TildelOppgaveResponse")
    public TildelOppgaveResponse tildelOppgave(@WebParam(name = "request") TildelOppgaveRequest tildelOppgaveRequest) {
        throw new NotImplementedException();
    }
}
