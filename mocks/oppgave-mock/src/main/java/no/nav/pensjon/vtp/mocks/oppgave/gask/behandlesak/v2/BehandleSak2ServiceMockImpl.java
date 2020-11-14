package no.nav.pensjon.vtp.mocks.oppgave.gask.behandlesak.v2;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModell;
import no.nav.tjeneste.virksomhet.behandlesak.v2.BehandleSakV2;
import no.nav.tjeneste.virksomhet.behandlesak.v2.WSAktor;
import no.nav.tjeneste.virksomhet.behandlesak.v2.WSOpprettSakResponse;
import no.nav.tjeneste.virksomhet.behandlesak.v2.WSSak;

@SoapService(path = "/nav-gsak-ws/BehandleSakV2")
@Addressing
@WebService(targetNamespace = "http://nav.no/tjeneste/virksomhet/behandlesak/v2", name = "BehandleSakV2")
@HandlerChain(file = "/Handler-chain.xml")
public class BehandleSak2ServiceMockImpl implements BehandleSakV2 {

    private static final Logger LOG = LoggerFactory.getLogger(BehandleSak2ServiceMockImpl.class);

    private final PersonModellRepository personModellRepository;
    private final GsakRepo gsakRepo;
    private final PersonIndeks personIndeks;

    public BehandleSak2ServiceMockImpl(PersonModellRepository personModellRepository, GsakRepo gsakRepo, PersonIndeks personIndeks) {
        this.personModellRepository = personModellRepository;
        this.gsakRepo = gsakRepo;
        this.personIndeks = personIndeks;
    }

    @Override
    @WebResult(name = "response")
    @RequestWrapper(localName = "opprettSak", targetNamespace = "http://nav.no/tjeneste/virksomhet/behandlesak/v2", className = "no.nav.tjeneste.virksomhet.behandlesak.v2.OpprettSak")
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/behandlesak/v2/BehandleSak_v2/opprettSakRequest")
    @ResponseWrapper(localName = "opprettSakResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/behandlesak/v2", className = "no.nav.tjeneste.virksomhet.behandlesak.v2.OpprettSakResponse")
    public no.nav.tjeneste.virksomhet.behandlesak.v2.WSOpprettSakResponse opprettSak(@WebParam(name = "opprettSakRequest") no.nav.tjeneste.virksomhet.behandlesak.v2.WSOpprettSakRequest request) {
        LOG.info("opprettSak. Saktype: {}. Fagområde: {}. Fagsystem: {}", request.getSak().getSaktype(), request.getSak().getFagomrade(), request.getSak().getFagsystem());

        Set<String> identer = request.getSak().getGjelderBrukerListe().stream().map(WSAktor::getIdent).collect(Collectors.toSet());

        List<PersonModell> personer = identer.stream().map(i -> (PersonModell) personModellRepository.findById(i).orElseThrow(() -> new RuntimeException("Person ikke funnet")))
            .collect(Collectors.toList());

        WSSak wsSak = request.getSak();
        String fagomrade = wsSak.getFagomrade();
        String fagsystem = wsSak.getFagsystem();
        String saktype = wsSak.getSaktype();

        no.nav.tjeneste.virksomhet.sak.v1.informasjon.Sak sak = gsakRepo.leggTilSak(personer, fagomrade, fagsystem, saktype);

        LOG.info("Sak opprettet med saksnummer: {}", sak.getSakId());
        WSOpprettSakResponse response = new WSOpprettSakResponse();
        response.setSakId(sak.getSakId());
        return response;
    }

    @Override
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/behandlesak/v2", className = "no.nav.tjeneste.virksomhet.behandlesak.v2.Ping")
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/behandlesak/v2/BehandleSak_v2/pingRequest")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/behandlesak/v2", className = "no.nav.tjeneste.virksomhet.behandlesak.v2.PingResponse")
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }

}
