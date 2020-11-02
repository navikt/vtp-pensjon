package no.nav.pensjon.vtp.miscellaneous.api.journalforing;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.pensjon.vtp.testmodell.dokument.JournalpostModellGenerator;
import no.nav.pensjon.vtp.testmodell.dokument.modell.JournalpostModell;
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.Journalstatus;
import no.nav.pensjon.vtp.testmodell.repo.JournalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.NotFoundException;
import java.time.LocalDateTime;

@RestController
@Api(tags = "Journalføringsmock")
@RequestMapping("/api/journalforing")
public class JournalforingRestTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(JournalforingRestTjeneste.class);

    private static final String DOKUMENTTYYPEID_KEY = "dokumenttypeid";
    private static final String AKTORID_KEY = "fnr";
    private static final String JOURNALPOST_ID = "journalpostid";
    private static final String SAKSNUMMER = "saksnummer";
    private static final String JOURNALSTATUS = "journalstatus";

    private final JournalRepository journalRepository;

    public JournalforingRestTjeneste(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    @PostMapping(value = "/foreldrepengesoknadxml/fnr/{fnr}/dokumenttypeid/{dokumenttypeid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "", notes = ("Lager en journalpost av type DokumenttypeId (se kilde for gyldige verdier, e.g. I000003). Innhold i journalpost legges ved som body."), response = JournalforingResultatDto.class)
    public JournalforingResultatDto foreldrepengesoknadErketype(String xml, @PathVariable(AKTORID_KEY) String fnr, @PathVariable(DOKUMENTTYYPEID_KEY) DokumenttypeId dokumenttypeId){



        JournalpostModell journalpostModell = JournalpostModellGenerator.lagJournalpostStrukturertDokument(xml, fnr, dokumenttypeId);
        journalpostModell.setMottattDato(LocalDateTime.now());
        String journalpostId = journalRepository.leggTilJournalpost(journalpostModell);

        LOG.info("Oppretter journalpost for bruke: {}. JournalpostId: {}", fnr, journalpostId);

        JournalforingResultatDto res = new JournalforingResultatDto();
        res.setJournalpostId(journalpostId);
        return res;
    }

    @PostMapping(value = "/ustrukturertjournalpost/fnr/{fnr}/dokumenttypeid/{dokumenttypeid}")
    public JournalforingResultatDto lagUstrukturertJournalpost(@PathVariable(AKTORID_KEY) String fnr, @PathVariable(DOKUMENTTYYPEID_KEY) DokumenttypeId dokumenttypeid, @RequestParam(JOURNALSTATUS) String journalstatus){
        JournalpostModell journalpostModell = JournalpostModellGenerator.lagJournalpostUstrukturertDokument(fnr,dokumenttypeid);
        if(journalstatus != null && journalstatus.length() > 0){
            Journalstatus status = new Journalstatus(journalstatus);
            journalpostModell.setJournalStatus(status);
        }

        String journalpostId = journalRepository.leggTilJournalpost(journalpostModell);

        LOG.info("Oppretter journalpost for bruker: {}. JournalpostId: {}", fnr, journalpostId);

        JournalforingResultatDto response = new JournalforingResultatDto();
        response.setJournalpostId(journalpostId);
        return response;

    }


    @PostMapping(value = "/knyttsaktiljournalpost/journalpostid/{journalpostid}/saksnummer/{saksnummer}")
    public JournalforingResultatDto knyttSakTilJournalpost(@PathVariable(JOURNALPOST_ID) String journalpostId, @PathVariable(SAKSNUMMER) String saksnummer ){

        LOG.info("Knytter sak: {} til journalpost: {}", saksnummer, journalpostId);

        JournalpostModell journalpostModell = journalRepository.finnJournalpostMedJournalpostId(journalpostId).orElseThrow(()-> new NotFoundException("Kunne ikke finne journalpost"));
        journalpostModell.setSakId(saksnummer);

        JournalforingResultatDto res = new JournalforingResultatDto();
        res.setJournalpostId(journalpostId);
        return res;
    }



}
