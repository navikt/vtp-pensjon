package no.nav.pensjon.vtp.mocks.virksomhet.journal.v2;

import no.nav.pensjon.vtp.core.annotations.SoapService;
import no.nav.pensjon.vtp.mocks.virksomhet.journal.modell.JournalpostV2Builder;
import no.nav.pensjon.vtp.testmodell.dokument.modell.DokumentModell;
import no.nav.pensjon.vtp.testmodell.dokument.modell.JournalpostModell;
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException;
import no.nav.pensjon.vtp.testmodell.repo.JournalRepository;
import no.nav.tjeneste.virksomhet.journal.v2.binding.HentDokumentDokumentIkkeFunnet;
import no.nav.tjeneste.virksomhet.journal.v2.binding.JournalV2;
import no.nav.tjeneste.virksomhet.journal.v2.feil.DokumentIkkeFunnet;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Sak;
import no.nav.tjeneste.virksomhet.journal.v2.meldinger.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.*;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SoapService(path = "/joark/Journal/v2")
@Addressing
@WebService(name = "Journal_v2", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2")
@HandlerChain(file="/Handler-chain.xml")
public class JournalV2ServiceMockImpl implements JournalV2 {
    private static final Logger LOG = LoggerFactory.getLogger(JournalV2ServiceMockImpl.class);

    private final JournalRepository journalRepository;

    public JournalV2ServiceMockImpl(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/journal/v2/Journal_v2/hentJournalpostListeRequest")
    @WebResult(name = "Response")
    @RequestWrapper(localName = "hentJournalpostListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2", className = "no.nav.tjeneste.virksomhet.journal.v2.HentJournalpostListe")
    @ResponseWrapper(localName = "hentJournalpostListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2", className = "no.nav.tjeneste.virksomhet.journal.v2.HentJournalpostListeResponse")
    @Override
    public HentJournalpostListeResponse hentJournalpostListe(@WebParam(name = "Request") HentJournalpostListeRequest request) {

        HentJournalpostListeResponse response = new HentJournalpostListeResponse();

        List<String> saker = request.getSakListe().stream().map(Sak::getSakId).collect(Collectors.toList());

        LOG.info("Henter journalpost for følgende saker: "+ String.join(",", saker));

        for(String sak : saker){
            for(JournalpostModell modell : journalRepository.finnJournalposterMedSakId(sak)){
                response.getJournalpostListe().add(JournalpostV2Builder.buildFrom(modell));
            }
        }

        return response;

    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/journal/v2/Journal_v2/hentDokumentRequest")
    @WebResult(name = "Response")
    @RequestWrapper(localName = "hentDokument", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2", className = "no.nav.tjeneste.virksomhet.journal.v2.HentDokument")
    @ResponseWrapper(localName = "hentDokumentResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2", className = "no.nav.tjeneste.virksomhet.journal.v2.HentDokumentResponse")
    @Override
    public HentDokumentResponse hentDokument(@WebParam(name = "Request") HentDokumentRequest request)
            throws HentDokumentDokumentIkkeFunnet {

        HentDokumentResponse dokumentResponse = new HentDokumentResponse();
        Optional<DokumentModell> dokumentModell = journalRepository.finnDokumentMedDokumentId(request.getDokumentId());
        LOG.info("Henter dokument på følgende dokumentId: " + request.getDokumentId());

        if(dokumentModell.isPresent()){
            //TODO OL: Fix format her.
            String innhold = dokumentModell.get().getInnhold();
            dokumentResponse.setDokument(innhold.getBytes());
            return  dokumentResponse;
        } else {
            throw new HentDokumentDokumentIkkeFunnet("Kunne ikke finne dokument",new DokumentIkkeFunnet());
        }

    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/journal/v2/Journal_v2/hentDokumentURLRequest")
    @WebResult(name = "Response")
    @RequestWrapper(localName = "hentDokumentURL", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2", className = "no.nav.tjeneste.virksomhet.journal.v2.HentDokumentURL")
    @ResponseWrapper(localName = "hentDokumentURLResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2", className = "no.nav.tjeneste.virksomhet.journal.v2.HentDokumentURLResponse")
    @Override
    public HentDokumentURLResponse hentDokumentURL(@WebParam(name = "Request") HentDokumentURLRequest request) {
        throw new NotImplementedException();
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/journal/v2/Journal_v2/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2", className = "no.nav.tjeneste.virksomhet.journal.v2.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2", className = "no.nav.tjeneste.virksomhet.journal.v2.PingResponse")
    @Override
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }


}
