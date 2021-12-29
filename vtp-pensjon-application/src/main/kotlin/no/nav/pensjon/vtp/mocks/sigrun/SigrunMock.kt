package no.nav.pensjon.vtp.mocks.sigrun

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Sigrun/beregnetskatt")
@RequestMapping("/rest/api/beregnetskatt")
class SigrunMock(
    private val personModellRepository: PersonModellRepository,
    private val inntektYtelseIndeks: InntektYtelseIndeks
) {
    @GetMapping
    @Operation(summary = "beregnetskatt", description = "Returnerer beregnetskatt fra Sigrun")
    fun buildPermitResponse(
        @RequestHeader(value = "x-naturligident", required = false) brukerFnr: String?,
        @RequestHeader(value = "x-inntektsaar", required = false) inntektsAar: String?,
        @RequestHeader(value = "x-aktoerid", required = false) aktørId: String?
    ): ResponseEntity<String> {
        inntektYtelseIndeks.getInntektYtelseModell(
            if (brukerFnr == null && aktørId != null) {
                personModellRepository.findByAktørIdent(aktørId)
                    ?.ident
                    ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kunne ikke finne bruker")
            } else if (brukerFnr == null) {
                return ResponseEntity.badRequest().body("Kan ikke ha tomt felt for både aktoerid og naturligident.")
            } else if (inntektsAar == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Det forespurte inntektsåret er ikke støttet.")
            } else {
                brukerFnr
            }
        )
            ?.let { inntektYtelseModell ->
                return inntektYtelseModell.sigrunModell?.inntektsår?.firstOrNull()
                    ?.let { inntektsår -> ResponseEntity.ok("[\n${inntektsår.oppføring.joinToString(separator = ",\n") { "$it" }}\n]") }
                    ?: ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kunne ikke finne inntektsår")
            }
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kunne ikke finne bruker")
    }
}
