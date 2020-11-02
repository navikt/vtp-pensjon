package no.nav.pensjon.vtp.mocks.journalpost.dokarkiv;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.dokarkiv.generated.model.*;
import no.nav.pensjon.vtp.testmodell.dokument.modell.JournalpostModell;
import no.nav.pensjon.vtp.testmodell.repo.JournalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Api(tags = {"Dokarkiv"})
@RequestMapping("/dokarkiv/rest/journalpostapi/v1")
public class JournalpostMock {
    private static final Logger LOG = LoggerFactory.getLogger(JournalpostMock.class);

    private final JournalRepository journalRepository;

    public JournalpostMock(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    @PostMapping(value = "/journalpost")
    @ApiOperation(value = "lag journalpost")
    public ResponseEntity lagJournalpost(OpprettJournalpostRequest opprettJournalpostRequest, @RequestParam("forsoekFerdigstill") Boolean forsoekFerdigstill) {
        LOG.info("Dokarkiv. Lag journalpost. foersoekFerdigstill: {}", forsoekFerdigstill);

        JournalpostModell modell = new JournalpostMapper().tilModell(opprettJournalpostRequest);
        String journalpostId = journalRepository.leggTilJournalpost(modell);

        Optional<JournalpostModell> journalpostModell = journalRepository.finnJournalpostMedJournalpostId(journalpostId);

        List<DokumentInfo> dokumentInfos = journalpostModell.get().getDokumentModellList().stream()
                .map(it -> {
                    DokumentInfo dokinfo = new DokumentInfo();
                    dokinfo.setDokumentInfoId(it.getDokumentId());
                    return dokinfo;
                }).collect(Collectors.toList());

        OpprettJournalpostResponse response = new OpprettJournalpostResponse();
        response.setDokumenter(dokumentInfos);
        response.setJournalpostId(journalpostId);
        response.setJournalpostferdigstilt(Boolean.TRUE);
        return ResponseEntity.accepted().body(response);
    }


    @PutMapping("/journalpost/{journalpostid}")
    @ApiOperation(value = "Oppdater journalpost")
    public ResponseEntity oppdaterJournalpost(OppdaterJournalpostRequest oppdaterJournalpostRequest, @PathVariable("journalpostid") String journalpostId){

        LOG.info("Kall til oppdater journalpost: {}", journalpostId);
        Optional<JournalpostModell> journalpostModell = journalRepository.finnJournalpostMedJournalpostId(journalpostId);
        if(journalpostModell.isPresent()) {
            journalpostModell.get().setSakId(oppdaterJournalpostRequest.getSak().getFagsakId());
            journalpostModell.get().setBruker(new JournalpostMapper().mapAvsenderFraBruker(oppdaterJournalpostRequest.getBruker()));
        }
        OppdaterJournalpostResponse oppdaterJournalpostResponse = new OppdaterJournalpostResponse();
        oppdaterJournalpostResponse.setJournalpostId(journalpostId);

        return ResponseEntity.accepted().body(oppdaterJournalpostResponse);
    }

    @PATCH
    @Path("/journalpost/{journalpostid}/ferdigstill")
    @ApiOperation(value = "Ferdigstill journalpost")
    public ResponseEntity ferdigstillJournalpost(FerdigstillJournalpostRequest ferdigstillJournalpostRequest){

        String journalfoerendeEnhet = ferdigstillJournalpostRequest.getJournalfoerendeEnhet();
        LOG.info("Kall til ferdigstill journalpost på enhet: {}", journalfoerendeEnhet);

        return ResponseEntity.ok("OK");
    }
}
