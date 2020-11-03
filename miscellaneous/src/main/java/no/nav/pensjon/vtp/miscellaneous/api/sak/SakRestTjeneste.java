package no.nav.pensjon.vtp.miscellaneous.api.sak;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.pensjon.vtp.mocks.oppgave.gask.sak.v1.GsakRepo;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.SøkerModell;
import no.nav.tjeneste.virksomhet.sak.v1.informasjon.Sak;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(tags = "Gsak repository")
@RequestMapping("/api/sak")
public class SakRestTjeneste {
    private static final Logger LOG = LoggerFactory.getLogger(SakRestTjeneste.class);

    private final GsakRepo gsakRepo;

    public SakRestTjeneste(GsakRepo gsakRepo) {
        this.gsakRepo = gsakRepo;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "", notes = ("Lager nytt saksnummer fra sekvens"), response = OpprettSakResponseDTO.class)
    public ResponseEntity foreldrepengesoknadErketype(OpprettSakRequestDTO requestDTO){

        if(requestDTO.getLokalIdent() == null || requestDTO.getLokalIdent().size() < 1 || requestDTO.getFagområde() == null ||
                requestDTO.getFagsystem() == null || requestDTO.getSakstype() == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Request mangler påkrevde verdier");
        }

        List<PersonModell> brukere = requestDTO.getLokalIdent().stream().map(p ->
                new SøkerModell(p, "place holder", null, null)).collect(Collectors.toList());

        Sak sak = gsakRepo.leggTilSak(brukere, requestDTO.getFagområde(), requestDTO.getFagsystem(), requestDTO.getSakstype());

        LOG.info("Opprettet sak med saksnummer: {}", sak.getSakId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new OpprettSakResponseDTO(sak.getSakId()));

    }

}
