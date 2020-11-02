package no.nav.pensjon.vtp.mocks.fpformidling;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.pensjon.vtp.mocks.fpformidling.dto.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Produces;
import java.util.*;

@RestController
@Api("/fpformidling")
@RequestMapping("/fpformidling")
public class FpFormidlingMock {
    private final Map<UUID, List<String>> dokumentProduksjon = new HashMap<>();
    private final Map<UUID, TekstFraSaksbehandlerDto> saksbehandlerTekst = new HashMap<>();

    @PostMapping(value = "/hent-dokumentmaler", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "HentDokumentmalListe", notes = ("Returnerer tilgjengelige dokumentmaler"))
    public ResponseEntity hentDokumentmalListe(BehandlingUuidDto request) {
        return ResponseEntity.ok(new HentBrevmalerDto(Collections.emptyList()));
    }

    @PostMapping(value = "brev/maler")
    @Produces("application/json")
    public HentBrevmalerDto hentBrevmaler(BehandlingUuidDto uuidDto) {
        return new HentBrevmalerDto(Collections.emptyList());
    }

    @PostMapping(value = "brev/dokument-sendt")
    @Produces("application/json")
    public Boolean erDokumentSendt(DokumentProdusertDto request) {
        return dokumentProduksjon.getOrDefault(request.getBehandlingUuid(), List.of()).contains(request.getDokumentMal());
    }

    @PostMapping(value = "brev/bestill")
    @Produces("application/json")
    public void bestillDokument(DokumentbestillingDto request) {
        dokumentProduksjon.putIfAbsent(request.getBehandlingUuid(), new ArrayList<>());
        dokumentProduksjon.get(request.getBehandlingUuid()).add(request.getDokumentMal());
    }

    @PostMapping(value = "saksbehandlertekst/hent")
    @Produces("application/json")
    public TekstFraSaksbehandlerDto hentSaksbehandlersTekst(BehandlingUuidDto uuidDto) {
        return saksbehandlerTekst.getOrDefault(uuidDto.getBehandlingUuid(), null);
    }

    @PostMapping(value = "saksbehandlertekst/lagre")
    @Produces("application/json")
    public void lagreSaksbehandlersTekst(TekstFraSaksbehandlerDto request) {
        saksbehandlerTekst.put(request.getBehandlingUuid(), request);
    }

}
