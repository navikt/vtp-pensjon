package no.nav.pensjon.vtp.mocks.sigrun;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks;
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.pensjon.vtp.testmodell.inntektytelse.sigrun.Inntektsår;
import no.nav.pensjon.vtp.testmodell.inntektytelse.sigrun.Oppføring;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonIndeks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Api(tags = {"Sigrun/beregnetskatt"})
@RequestMapping("/api/beregnetskatt")
public class SigrunMock {
    private static final Logger LOG = LoggerFactory.getLogger(SigrunMock.class);

    private final InntektYtelseIndeks inntektYtelseIndeks;
    private final PersonIndeks personIndeks;

    public SigrunMock(InntektYtelseIndeks inntektYtelseIndeks, PersonIndeks personIndeks) {
        this.inntektYtelseIndeks = inntektYtelseIndeks;
        this.personIndeks = personIndeks;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "beregnetskatt", notes = ("Returnerer beregnetskatt fra Sigrun"))
    public ResponseEntity buildPermitResponse(@RequestHeader(value = "x-naturligident", required = false) String brukerFnr,
                                              @RequestHeader(value = "x-inntektsaar", required = false) String inntektsAar,
                                              @RequestHeader(value = "x-aktoerid", required = false) String aktørId) {
        LOG.info("Sigrun for aktørId: {} ", aktørId);

        if (brukerFnr == null && aktørId != null) {
            brukerFnr = personIndeks.finnByAktørIdent(aktørId).getIdent();
        } else if (brukerFnr == null) {
            LOG.info("sigrun. fnr eller aktoerid må være oppgitt.");
            return ResponseEntity.badRequest().body("Kan ikke ha tomt felt for både aktoerid og naturligident.");
        } else if (inntektsAar == null) {
            LOG.info("sigrun. Inntektsår må være oppgitt.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Det forespurte inntektsåret er ikke støttet.");
        }

        Optional<InntektYtelseModell> inntektYtelseModell = inntektYtelseIndeks.getInntektYtelseModell(brukerFnr);
        String response;

        if (inntektYtelseModell.isPresent() && !inntektYtelseModell.get().getSigrunModell().getInntektsår().isEmpty()) {
            List<Inntektsår> inntektsår = inntektYtelseModell.get().getSigrunModell().getInntektsår();
            String test = inntektsår.get(0).getOppføring().stream().map(Oppføring::toString).collect(Collectors.joining(",\n"));

            response = String.format("[\n%s\n]", test);
        } else if (inntektYtelseModell.isEmpty()){
            LOG.info("Kunne ikke finne bruker.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kunne ikke finne bruker");
        } else {
            LOG.info("Kunne ikke finne inntektsår");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kunne ikke finne inntektsår");
        }
        return ResponseEntity.ok(response);
    }

}
