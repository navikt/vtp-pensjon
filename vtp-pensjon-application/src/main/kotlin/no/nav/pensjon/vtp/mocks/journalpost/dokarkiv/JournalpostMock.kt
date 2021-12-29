package no.nav.pensjon.vtp.mocks.journalpost.dokarkiv

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.dokarkiv.generated.model.*
import no.nav.pensjon.vtp.testmodell.repo.JournalRepository
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import java.net.InetAddress
import java.net.URI

@RestController
@Tag(name = "Dokarkiv")
@RequestMapping(JournalpostMock.BASE_PATH)
class JournalpostMock(private val journalRepository: JournalRepository) {

    companion object {
        const val BASE_PATH = "/rest/dokarkiv/rest/journalpostapi/v1"

        const val JOURNALPOST_ID_PARAM = "journalpostid"
        const val FIL_UUID_PARAM = "filUuid"
        const val HENT_JOURNALPOST_SUB_PATH = "/journalpost/{$JOURNALPOST_ID_PARAM}/fil/{$FIL_UUID_PARAM}"

        fun buildHentJournalpostFilUri(journalpostId: String, filUuid: String): URI {
            return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(InetAddress.getLocalHost().canonicalHostName)
                .port(8060)
                .path(BASE_PATH)
                .path(HENT_JOURNALPOST_SUB_PATH)
                .build(mapOf(JOURNALPOST_ID_PARAM to journalpostId, FIL_UUID_PARAM to filUuid))
        }
    }

    @PostMapping(value = ["/journalpost"])
    fun lagJournalpost(
        @RequestBody opprettJournalpostRequest: OpprettJournalpostRequest,
        @RequestParam("forsoekFerdigstill") forsoekFerdigstill: Boolean
    ) = accepted().body(
        journalRepository.save(tilModell(opprettJournalpostRequest)).let { journalpostModell ->
            OpprettJournalpostResponse(
                dokumenter = journalpostModell.dokumentModellList
                    .map {
                        DokumentInfoId(
                            dokumentInfoId = it.dokumentId ?: throw IllegalStateException("Stored dokument without id")
                        )
                    },
                journalpostId = journalpostModell.journalpostId
                    ?: throw IllegalStateException("Stored journalpost without id"),
                journalpostferdigstilt = forsoekFerdigstill,
            )
        }
    )

    @PutMapping("/journalpost/{journalpostid}")
    @Operation(summary = "Oppdater journalpost")
    fun oppdaterJournalpost(
        @RequestBody oppdaterJournalpostRequest: OppdaterJournalpostRequest,
        @PathVariable("journalpostid") journalpostId: String
    ) = journalRepository.finnJournalpostMedJournalpostId(journalpostId)
        ?.let {
            journalRepository.save(
                it.copy(
                    sakId = oppdaterJournalpostRequest.sak?.fagsakId,
                    bruker = oppdaterJournalpostRequest.bruker?.let { bruker -> mapAvsenderFraBruker(bruker) }
                )
            )
            notFound().build()
        }
        ?: accepted().body(
            OppdaterJournalpostResponse(journalpostId = journalpostId)
        )

    @PatchMapping("/journalpost/{journalpostid}/ferdigstill")
    @Operation(summary = "Ferdigstill journalpost")
    fun ferdigstillJournalpost(@RequestBody ferdigstillJournalpostRequest: FerdigstillJournalpostRequest) =
        ok("OK")

    @GetMapping(value = [HENT_JOURNALPOST_SUB_PATH], produces = [MediaType.APPLICATION_PDF_VALUE])
    @Operation(summary = "Hent journalpost fil")
    fun hentJournalpostFil(
        @PathVariable(JOURNALPOST_ID_PARAM) journalpostId: String,
        @PathVariable(FIL_UUID_PARAM) filUuid: String,
    ) = journalRepository.finnJournalpostMedJournalpostId(journalpostId)
        ?.dokumentModellList
        ?.flatMap { it.dokumentVariantInnholdListe ?: emptyList() }
        ?.firstOrNull { it.uuid == filUuid }
        ?.let { ok(it.dokumentInnhold) }
        ?: notFound().build()
}
