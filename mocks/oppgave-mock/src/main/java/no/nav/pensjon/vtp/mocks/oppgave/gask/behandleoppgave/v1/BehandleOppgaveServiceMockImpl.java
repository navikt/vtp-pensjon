package no.nav.pensjon.vtp.mocks.oppgave.gask.behandleoppgave.v1;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.pensjon.vtp.core.annotations.SoapService;
import no.nav.pensjon.vtp.mocks.oppgave.gask.sak.v1.GsakRepo;
import no.nav.tjeneste.virksomhet.behandleoppgave.v1.BehandleOppgaveV1;
import no.nav.tjeneste.virksomhet.behandleoppgave.v1.meldinger.WSFerdigstillOppgaveResponse;
import no.nav.tjeneste.virksomhet.behandleoppgave.v1.meldinger.WSOpprettOppgaveResponse;

@SoapService(path = "/nav-gsak-ws/BehandleOppgaveV1")
@Addressing
@WebService(targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleoppgave/v1", name = "BehandleOppgaveV1")
@HandlerChain(file = "/Handler-chain.xml")
public class BehandleOppgaveServiceMockImpl implements BehandleOppgaveV1 {

    private static final Logger LOG = LoggerFactory.getLogger(BehandleOppgaveServiceMockImpl.class);

    private final GsakRepo repo;

    public BehandleOppgaveServiceMockImpl(GsakRepo repo) {
        this.repo = repo;
    }

    @Override
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleoppgave/v1", className = "no.nav.tjeneste.virksomhet.behandleoppgave.v1.Ping")
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/behandleoppgave/v1/BehandleOppgave_v1/pingRequest")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleoppgave/v1", className = "no.nav.tjeneste.virksomhet.behandleoppgave.v1.PingResponse")
    public void ping() {
        LOG.info("Ping mottatt og besvart.");
    }

    @Override
    @WebResult(name = "response")
    @RequestWrapper(localName = "ferdigstillOppgave", targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleoppgave/v1", className = "no.nav.tjeneste.virksomhet.behandleoppgave.v1.FerdigstillOppgave")
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/behandleoppgave/v1/BehandleOppgave_v1/ferdigstillOppgaveRequest")
    @ResponseWrapper(localName = "ferdigstillOppgaveResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleoppgave/v1", className = "no.nav.tjeneste.virksomhet.behandleoppgave.v1.FerdigstillOppgaveResponse")
    public no.nav.tjeneste.virksomhet.behandleoppgave.v1.meldinger.WSFerdigstillOppgaveResponse ferdigstillOppgave(
                                                                                                                   @WebParam(name = "request") no.nav.tjeneste.virksomhet.behandleoppgave.v1.meldinger.WSFerdigstillOppgaveRequest request) {
        LOG.info("ferdigstillOppgave. OppgaveId: " + request.getOppgaveId());
        return new WSFerdigstillOppgaveResponse();
    }

    @Override
    @RequestWrapper(localName = "lagreOppgave", targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleoppgave/v1", className = "no.nav.tjeneste.virksomhet.behandleoppgave.v1.LagreOppgave")
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/behandleoppgave/v1/BehandleOppgave_v1/lagreOppgaveRequest")
    @ResponseWrapper(localName = "lagreOppgaveResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleoppgave/v1", className = "no.nav.tjeneste.virksomhet.behandleoppgave.v1.LagreOppgaveResponse")
    public void lagreOppgave(
                             @WebParam(name = "request") no.nav.tjeneste.virksomhet.behandleoppgave.v1.meldinger.WSLagreOppgaveRequest request) {
        throw new UnsupportedOperationException("Ikke implementert");

    }

    @Override
    @WebResult(name = "response")
    @RequestWrapper(localName = "opprettOppgave", targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleoppgave/v1", className = "no.nav.tjeneste.virksomhet.behandleoppgave.v1.OpprettOppgave")
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/behandleoppgave/v1/BehandleOppgave_v1/opprettOppgaveRequest")
    @ResponseWrapper(localName = "opprettOppgaveResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleoppgave/v1", className = "no.nav.tjeneste.virksomhet.behandleoppgave.v1.OpprettOppgaveResponse")
    public no.nav.tjeneste.virksomhet.behandleoppgave.v1.meldinger.WSOpprettOppgaveResponse opprettOppgave(
                                                                                                           @WebParam(name = "request") no.nav.tjeneste.virksomhet.behandleoppgave.v1.meldinger.WSOpprettOppgaveRequest request) {
        WSOpprettOppgaveResponse opprettOppgaveResponse = new WSOpprettOppgaveResponse();
        LOG.info("opprettOppgave. Beskrivelse: " + request.getWsOppgave().getBeskrivelse() + ", saksnummer: " + request.getWsOppgave().getSaksnummer() + ", oppgavetypeKode: " + request.getWsOppgave().getOppgavetypeKode());
        String oppgaveId = repo.opprettOppgave(request.getWsOppgave().getSaksnummer());
        opprettOppgaveResponse.setOppgaveId(oppgaveId );
        return opprettOppgaveResponse;
    }
}
