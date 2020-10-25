package no.nav.pensjon.vtp.mocks.virksomhet.dokumentproduksjon.v2;

import java.util.Objects;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.soap.Addressing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.pensjon.vtp.core.annotations.SoapService;
import no.nav.pensjon.vtp.mocks.virksomhet.dokumentproduksjon.v2.PdfGenerering.PdfGeneratorUtil;
import no.nav.pensjon.vtp.testmodell.dokument.JournalpostModellGenerator;
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.pensjon.vtp.testmodell.repo.JournalRepository;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.DokumentproduksjonV2;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.informasjon.Aktoer;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.informasjon.Person;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.AvbrytForsendelseRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.AvbrytVedleggRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.EndreDokumentTilRedigerbartRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.FerdigstillForsendelseRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.KnyttVedleggTilForsendelseRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.ProduserDokumentutkastRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.ProduserDokumentutkastResponse;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.ProduserIkkeredigerbartDokumentRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.ProduserIkkeredigerbartDokumentResponse;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.ProduserIkkeredigerbartVedleggRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.ProduserIkkeredigerbartVedleggResponse;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.ProduserRedigerbartDokumentRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.ProduserRedigerbartDokumentResponse;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.RedigerDokumentRequest;
import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.meldinger.RedigerDokumentResponse;

@SoapService(path = "/dokprod/ws/dokumentproduksjon/v2")
@Addressing
@WebService(endpointInterface = "no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.binding.DokumentproduksjonV2")
@HandlerChain(file = "/Handler-chain.xml")
public class DokumentproduksjonV2MockImpl implements DokumentproduksjonV2 {
    private static final Logger LOG = LoggerFactory.getLogger(DokumentproduksjonV2MockImpl.class);

    private final JournalRepository journalRepository;

    public DokumentproduksjonV2MockImpl(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    @Override
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }

    @Override
    public ProduserDokumentutkastResponse produserDokumentutkast(ProduserDokumentutkastRequest request) {
        // String data = xmlToString(((Element) request.getBrevdata()).getOwnerDocument());
        String data = Objects.toString(request.getAny());
        String dokumenttypeId =  request.getDokumenttypeId();

        byte[] bytes = new PdfGeneratorUtil().genererPdfByteArrayFraString(data);

        ProduserDokumentutkastResponse response = new ProduserDokumentutkastResponse();
        response.setDokumentutkast(bytes);

        LOG.info("Brev med dokumentTypeId {} returneres til fpformidling for forhåndsvisning", dokumenttypeId);
        return response;
    }

    @Override
    public void avbrytVedlegg(AvbrytVedleggRequest avbrytVedleggRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ProduserIkkeredigerbartDokumentResponse produserIkkeredigerbartDokument(ProduserIkkeredigerbartDokumentRequest request) {
        Aktoer bruker = request.getDokumentbestillingsinformasjon().getBruker();

        // String data = xmlToString(((Element) request.getBrevdata()).getOwnerDocument());
        String data = Objects.toString(request.getAny());
        LOG.info("Dokument produsert: " + data);

        String dokumenttypeId = request.getDokumentbestillingsinformasjon().getDokumenttypeId();

        LOG.info("produsererIkkeredigerbartDokument med dokumenttypeId {} bestilt for bruker {}({})", dokumenttypeId, ((Person) bruker).getIdent(), ((Person) bruker).getNavn());
        ProduserIkkeredigerbartDokumentResponse response = new ProduserIkkeredigerbartDokumentResponse();
        String journalpostId = journalRepository.leggTilJournalpost(
                JournalpostModellGenerator.lagJournalpostUstrukturertDokument(((Person) bruker).getIdent(), new DokumenttypeId(dokumenttypeId)));
        String dokumentId = journalRepository.finnJournalpostMedJournalpostId(journalpostId).get().getDokumentModellList().get(0).getDokumentId();
        LOG.info("produsererIkkeredigerbartDokument generer journalpost {} med dokument {})", journalpostId, dokumentId);

        response.setDokumentId(dokumentId);
        response.setJournalpostId(journalpostId);
        return response;
    }

    @Override
    public void knyttVedleggTilForsendelse(KnyttVedleggTilForsendelseRequest knyttVedleggTilForsendelseRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ProduserRedigerbartDokumentResponse produserRedigerbartDokument(ProduserRedigerbartDokumentRequest produserRedigerbartDokumentRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public void avbrytForsendelse(AvbrytForsendelseRequest avbrytForsendelseRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public void ferdigstillForsendelse(FerdigstillForsendelseRequest request) {
        LOG.info("ferdigstillForsendelse ferdigstiller journalpost: {}", request.getJournalpostId());
    }

    @Override
    public RedigerDokumentResponse redigerDokument(RedigerDokumentRequest redigerDokumentRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ProduserIkkeredigerbartVedleggResponse produserIkkeredigerbartVedlegg(ProduserIkkeredigerbartVedleggRequest produserIkkeredigerbartVedleggRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public void endreDokumentTilRedigerbart(EndreDokumentTilRedigerbartRequest endreDokumentTilRedigerbartRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }
}
