package no.nav.pensjon.vtp.mocks.virksomhet.behandleinngaaendejournal.v1;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.pensjon.vtp.core.annotations.SoapService;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.binding.BehandleInngaaendeJournalV1;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.meldinger.FerdigstillJournalfoeringRequest;
import no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.meldinger.OppdaterJournalpostRequest;

@SoapService(path = "/services/behandleinngaaendejournal/v1")
@Addressing
@HandlerChain(file="/Handler-chain.xml")
@WebService(
        name = "BehandleInngaaendeJournal_v1",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1"
)
public class BehandleInngaaendeJournalV1ServiceMock implements BehandleInngaaendeJournalV1 {
    private static final Logger LOG = LoggerFactory.getLogger(BehandleInngaaendeJournalV1ServiceMock.class);

    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1/BehandleInngaaendeJournal_v1/pingRequest"
    )
    @RequestWrapper(
            localName = "ping",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1",
            className = "no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.Ping"
    )
    @ResponseWrapper(
            localName = "pingResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1",
            className = "no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.PingResponse"
    )
    @Override
    public void ping() {
        LOG.info("Ping mottatt og besvart");

    }


    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1/BehandleInngaaendeJournal_v1/ferdigstillJournalfoeringRequest"
    )
    @RequestWrapper(
            localName = "ferdigstillJournalfoering",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1",
            className = "no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.FerdigstillJournalfoering"
    )
    @ResponseWrapper(
            localName = "ferdigstillJournalfoeringResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1",
            className = "no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.FerdigstillJournalfoeringResponse"
    )
    @Override
    public void ferdigstillJournalfoering(@WebParam(name = "request") FerdigstillJournalfoeringRequest ferdigstillJournalfoeringRequest) {
        LOG.info("invoke: ferdigstillJournalfoering");

    }

    @WebMethod(
            action = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1/BehandleInngaaendeJournal_v1/oppdaterJournalpostRequest"
    )
    @RequestWrapper(
            localName = "oppdaterJournalpost",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1",
            className = "no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.OppdaterJournalpost"
    )
    @ResponseWrapper(
            localName = "oppdaterJournalpostResponse",
            targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleInngaaendeJournal/v1",
            className = "no.nav.tjeneste.virksomhet.behandleinngaaendejournal.v1.OppdaterJournalpostResponse"
    )
    @Override
    public void oppdaterJournalpost(@WebParam(name = "request") OppdaterJournalpostRequest oppdaterJournalpostRequest) {
        LOG.info("invoke: oppdaterJournalpostRequest");
    }
}

