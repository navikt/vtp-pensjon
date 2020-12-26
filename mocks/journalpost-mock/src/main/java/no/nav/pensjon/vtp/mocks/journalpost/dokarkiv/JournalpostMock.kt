package no.nav.pensjon.vtp.mocks.journalpost.dokarkiv

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import no.nav.dokarkiv.generated.model.*
import no.nav.pensjon.vtp.testmodell.repo.JournalRepository
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*

@RestController
@Api(tags = ["Dokarkiv"])
@RequestMapping("/rest/dokarkiv/rest/journalpostapi/v1")
class JournalpostMock(private val journalRepository: JournalRepository) {
    @PostMapping(value = ["/journalpost"])
    fun lagJournalpost(
        @RequestBody opprettJournalpostRequest: OpprettJournalpostRequest,
        @RequestParam("forsoekFerdigstill") forsoekFerdigstill: Boolean
    ) = accepted().body(
        journalRepository.save(tilModell(opprettJournalpostRequest)).let { journalpostModell ->
            OpprettJournalpostResponse(
                dokumenter = journalpostModell.dokumentModellList
                    .map {
                        DokumentInfo(
                            dokumentInfoId = it.dokumentId ?: throw IllegalStateException("Stored dokument without id")
                        )
                    },
                journalpostId = journalpostModell.journalpostId
                    ?: throw IllegalStateException("Stored journalpost without id"),
                journalpostferdigstilt = true,
                journalstatus = journalpostModell.journalStatus.name
            )
        }
    )

    @PutMapping("/journalpost/{journalpostid}")
    @ApiOperation(value = "Oppdater journalpost")
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
    @ApiOperation(value = "Ferdigstill journalpost")
    fun ferdigstillJournalpost(@RequestBody ferdigstillJournalpostRequest: FerdigstillJournalpostRequest) =
        ok("OK")
}
