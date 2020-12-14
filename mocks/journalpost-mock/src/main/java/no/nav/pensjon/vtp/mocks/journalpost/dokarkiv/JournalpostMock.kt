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
        OpprettJournalpostResponse().apply {
            val journalpostModell = journalRepository.save(tilModell(opprettJournalpostRequest))

            dokumenter = journalpostModell.dokumentModellList
                .map {
                    DokumentInfo().apply {
                        dokumentInfoId = it.dokumentId
                    }
                }
            journalpostId = journalpostModell.journalpostId
            isJournalpostferdigstilt = true
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
                    sakId = oppdaterJournalpostRequest.sak.fagsakId,
                    bruker = mapAvsenderFraBruker(oppdaterJournalpostRequest.bruker)
                )
            )
            notFound().build()
        }
        ?: accepted().body(
            OppdaterJournalpostResponse().apply {
                this.journalpostId = journalpostId
            }
        )

    @PatchMapping("/journalpost/{journalpostid}/ferdigstill")
    @ApiOperation(value = "Ferdigstill journalpost")
    fun ferdigstillJournalpost(@RequestBody ferdigstillJournalpostRequest: FerdigstillJournalpostRequest) =
        ok("OK")
}
