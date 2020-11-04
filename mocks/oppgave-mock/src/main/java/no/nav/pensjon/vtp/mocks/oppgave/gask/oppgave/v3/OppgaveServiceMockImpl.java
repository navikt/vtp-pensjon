package no.nav.pensjon.vtp.mocks.oppgave.gask.oppgave.v3;

import java.util.List;

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
import no.nav.pensjon.vtp.mocks.oppgave.repository.OppgaveRepository;
import no.nav.tjeneste.virksomhet.oppgave.v3.binding.HentOppgaveOppgaveIkkeFunnet;
import no.nav.tjeneste.virksomhet.oppgave.v3.binding.OppgaveV3;
import no.nav.tjeneste.virksomhet.oppgave.v3.feil.OppgaveIkkeFunnet;
import no.nav.tjeneste.virksomhet.oppgave.v3.informasjon.oppgave.Oppgave;
import no.nav.tjeneste.virksomhet.oppgave.v3.meldinger.FinnFeilregistrertOppgaveListeRequest;
import no.nav.tjeneste.virksomhet.oppgave.v3.meldinger.FinnFeilregistrertOppgaveListeResponse;
import no.nav.tjeneste.virksomhet.oppgave.v3.meldinger.FinnFerdigstiltOppgaveListeRequest;
import no.nav.tjeneste.virksomhet.oppgave.v3.meldinger.FinnFerdigstiltOppgaveListeResponse;
import no.nav.tjeneste.virksomhet.oppgave.v3.meldinger.FinnMappeListeRequest;
import no.nav.tjeneste.virksomhet.oppgave.v3.meldinger.FinnMappeListeResponse;
import no.nav.tjeneste.virksomhet.oppgave.v3.meldinger.FinnOppgaveListeRequest;
import no.nav.tjeneste.virksomhet.oppgave.v3.meldinger.FinnOppgaveListeResponse;
import no.nav.tjeneste.virksomhet.oppgave.v3.meldinger.HentOppgaveRequest;
import no.nav.tjeneste.virksomhet.oppgave.v3.meldinger.HentOppgaveResponse;

@SoapService(path = "/nav-gsak-ws/OppgaveV3")
@Addressing
@WebService(name = "Oppgave_v3", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3")
@HandlerChain(file="/Handler-chain.xml")
public class OppgaveServiceMockImpl implements OppgaveV3 {

    private static final Logger LOG = LoggerFactory.getLogger(OppgaveServiceMockImpl.class);

    private final OppgaveRepository oppgaveRepository;

    public OppgaveServiceMockImpl(OppgaveRepository oppgaveRepository) {
        this.oppgaveRepository = oppgaveRepository;
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgave/v3/Oppgave_v3/hentOppgave")
    @WebResult(name = "response")
    @RequestWrapper(localName = "hentOppgave", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.HentOppgave")
    @ResponseWrapper(localName = "hentOppgaveResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.HentOppgaveResponse")
    public HentOppgaveResponse hentOppgave(@WebParam(name = "request") HentOppgaveRequest hentOppgaveRequest) throws HentOppgaveOppgaveIkkeFunnet {
        LOG.info("hentOppgave. OppgaveId: {}", hentOppgaveRequest.getOppgaveId());

        return oppgaveRepository.findById(hentOppgaveRequest.getOppgaveId())
                .map(Mapper::asOppgave3)
                .map(oppgave3 -> {
                    final HentOppgaveResponse hentOppgaveResponse = new HentOppgaveResponse();
                    hentOppgaveResponse.setOppgave(oppgave3);
                    return hentOppgaveResponse;
                })
                .orElseThrow(() -> new HentOppgaveOppgaveIkkeFunnet("Fant ikke oppgave med id=" + hentOppgaveRequest.getOppgaveId(), new OppgaveIkkeFunnet()));
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgave/v3/Oppgave_v3/finnOppgaveListeRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "finnOppgaveListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.FinnOppgaveListe")
    @ResponseWrapper(localName = "finnOppgaveListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.FinnOppgaveListeResponse")
    public FinnOppgaveListeResponse finnOppgaveListe(@WebParam(name = "request") FinnOppgaveListeRequest finnOppgaveListeRequest) {
        FinnOppgaveListeResponse response = new FinnOppgaveListeResponse();

        List<Oppgave> oppgaveListe = response.getOppgaveListe();

        oppgaveRepository.findAll()
                .map(Mapper::asOppgave3)
                .forEach(oppgaveListe::add);


        response.setTotaltAntallTreff(oppgaveListe.size());
        return response;
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgave/v3/Oppgave_v3/finnFerdigstiltOppgaveListeRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "finnFerdigstiltOppgaveListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.FinnFerdigstiltOppgaveListe")
    @ResponseWrapper(localName = "finnFerdigstiltOppgaveListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.FinnFerdigstiltOppgaveListeResponse")
    public FinnFerdigstiltOppgaveListeResponse finnFerdigstiltOppgaveListe(@WebParam(name = "request") FinnFerdigstiltOppgaveListeRequest finnFerdigstiltOppgaveListeRequest) {
        LOG.info("finnFerdigstiltOppgaveListe. Søk: ansvarligEnhetId: {}, brukerId: {}, sakId: {}, søknadsId: {}", finnFerdigstiltOppgaveListeRequest.getSok().getAnsvarligEnhetId(), finnFerdigstiltOppgaveListeRequest.getSok().getBrukerId(),
                finnFerdigstiltOppgaveListeRequest.getSok().getSakId(), finnFerdigstiltOppgaveListeRequest.getSok().getSoknadsId());
        return new FinnFerdigstiltOppgaveListeResponse();
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgave/v3/Oppgave_v3/finnFeilregistrertOppgaveListeRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "finnFeilregistrertOppgaveListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.FinnFeilregistrertOppgaveListe")
    @ResponseWrapper(localName = "finnFeilregistrertOppgaveListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.FinnFeilregistrertOppgaveListeResponse")
    public FinnFeilregistrertOppgaveListeResponse finnFeilregistrertOppgaveListe(@WebParam(name = "request") FinnFeilregistrertOppgaveListeRequest finnFeilregistrertOppgaveListeRequest) {
        LOG.info("finnFeilregistrertOppgaveListe. Søk: ansvarligEnhetId: {}, brukerId: {}, sakId: {}, aøknadsId{}", finnFeilregistrertOppgaveListeRequest.getSok().getAnsvarligEnhetId(), finnFeilregistrertOppgaveListeRequest.getSok().getBrukerId(),
                finnFeilregistrertOppgaveListeRequest.getSok().getSakId(), finnFeilregistrertOppgaveListeRequest.getSok().getSoknadsId());
        return new FinnFeilregistrertOppgaveListeResponse();
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgave/v3/Oppgave_v3/finnMappeListeRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "finnMappeListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.FinnMappeListe")
    @ResponseWrapper(localName = "finnMappeListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.FinnMappeListeResponse")
    public FinnMappeListeResponse finnMappeListe(@WebParam(name = "request") FinnMappeListeRequest finnMappeListeRequest) {
        LOG.info("finnMappeListe. EnhetId: {}", finnMappeListeRequest.getEnhetId());
        return new FinnMappeListeResponse();
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgave/v3/Oppgave_v3/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.PingResponse")
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }
}
