package no.nav.pensjon.vtp.mocks.journalforing

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.pensjon.vtp.testmodell.dokument.JournalpostModellGenerator.lagJournalpostStrukturertDokument
import no.nav.pensjon.vtp.testmodell.dokument.JournalpostModellGenerator.lagJournalpostUstrukturertDokument
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.DokumenttypeId
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.Journalstatus
import no.nav.pensjon.vtp.testmodell.repo.JournalRepository
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@Tag(name = "Journalføringsmock")
@RequestMapping("/rest/api/journalforing")
class JournalforingRestTjeneste(private val journalRepository: JournalRepository) {
    @PostMapping(
        value = ["/foreldrepengesoknadxml/fnr/{fnr}/dokumenttypeid/{dokumenttypeid}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(
        summary = "",
        description = "Lager en journalpost av type DokumenttypeId (se kilde for gyldige verdier, e.g. I000003). Innhold i journalpost legges ved som body.",
    )
    fun foreldrepengesoknadErketype(
        @RequestBody xml: String?,
        @PathVariable("fnr") fnr: String,
        @PathVariable("dokumenttypeid") dokumenttypeId: DokumenttypeId
    ) = JournalforingResultatDto(
        journalpostId = journalRepository.save(
            lagJournalpostStrukturertDokument(
                xml,
                fnr,
                dokumenttypeId,
                LocalDate.now(),
            )
        ).journalpostId
            ?: throw IllegalStateException("Missing journalpostId for saved journalPost")
    )

    @PostMapping(value = ["/ustrukturertjournalpost/fnr/{fnr}/dokumenttypeid/{dokumenttypeid}"])
    fun lagUstrukturertJournalpost(
        @PathVariable("fnr") fnr: String,
        @PathVariable("dokumenttypeid") dokumenttypeid: DokumenttypeId,
        @RequestParam("journalstatus", required = false) journalstatus: String?
    ) = JournalforingResultatDto(
        journalpostId = journalRepository.save(
            if (journalstatus != null && journalstatus.isNotEmpty()) {
                lagJournalpostUstrukturertDokument(
                    fnr = fnr,
                    dokumenttypeId = dokumenttypeid,
                    journalstatus = Journalstatus.fromCode(journalstatus)
                )
            } else {
                lagJournalpostUstrukturertDokument(
                    fnr = fnr,
                    dokumenttypeId = dokumenttypeid
                )
            }
        ).journalpostId
            ?: throw IllegalStateException("Missing journalpostId for newly saved journalpost")
    )

    @PostMapping(value = ["/knyttsaktiljournalpost/journalpostid/{journalpostid}/saksnummer/{saksnummer}"])
    fun knyttSakTilJournalpost(
        @PathVariable("journalpostid") journalpostId: String,
        @PathVariable("saksnummer") saksnummer: String
    ) = journalRepository.finnJournalpostMedJournalpostId(journalpostId)
        ?.let {
            journalRepository.save(it.copy(sakId = saksnummer))

            ok(
                JournalforingResultatDto(
                    journalpostId = journalpostId
                )
            )
        }
        ?: notFound().build()
}
