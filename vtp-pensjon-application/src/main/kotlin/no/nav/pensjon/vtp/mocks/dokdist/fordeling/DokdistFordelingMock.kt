package no.nav.pensjon.vtp.mocks.dokdist.fordeling

import io.swagger.annotations.Api
import no.nav.dokdist.fordeling.generated.model.DistribuerJournalpostRequestToModel
import no.nav.dokdist.fordeling.generated.model.DistribuerJournalpostResponseToModel
import no.nav.pensjon.vtp.testmodell.dokdist.Adresse
import no.nav.pensjon.vtp.testmodell.dokdist.DistribuerJournalpostBestilling
import no.nav.pensjon.vtp.testmodell.dokdist.DokdistRepository
import no.nav.pensjon.vtp.testmodell.repo.JournalRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Api(tags = ["Dokdist"])
@RequestMapping("/rest/dokdist/rest/v1")
class DokdistFordelingMock(
    private val dokdistRepository: DokdistRepository,
    private val journalRepository: JournalRepository,
) {

    @PostMapping(value = ["/distribuerjournalpost"])
    fun distribuerJournalpost(
        @RequestBody distribuerJournalpost: DistribuerJournalpostRequestToModel,
    ): ResponseEntity<DistribuerJournalpostResponseToModel> =
        if (distribuerJournalpost.journalpostId != null && journalRepository.finnJournalpostMedJournalpostId(distribuerJournalpost.journalpostId) == null)
            ResponseEntity.notFound().build()
        else
            distribuerJournalpost.tilModell()
                .let { dokdistRepository.save(it) }
                .let {
                    DistribuerJournalpostResponseToModel(
                        bestillingsId = it.bestillingsId ?: throw IllegalStateException("Stored distribuer journalpost bestilling without id")
                    )
                }.let { ResponseEntity.accepted().body(it) }

    @GetMapping(value = ["/test/bestilling/{bestillingsId}"])
    fun getBestilling(@PathVariable("bestillingsId") bestillingsId: String) =
        ResponseEntity.ok(
            dokdistRepository.finnDistribuerJournalpostBestillingMedBestillingsId(bestillingsId)
        )

    @GetMapping(value = ["test/bestilling"])
    fun getAlleBestillinger() = ResponseEntity.ok(dokdistRepository.finnAlleBestillingsId())
}

private fun DistribuerJournalpostRequestToModel.tilModell() =
    DistribuerJournalpostBestilling(
        journalPostId = journalpostId,
        dokumentProdApp = dokumentProdApp,
        bestillendeFagsystem = bestillendeFagsystem,
        batchId = batchId,
        adresse = adresse?.let {
            Adresse(
                adresselinje1 = it.adresselinje1,
                adresselinje2 = it.adresselinje2,
                adresselinje3 = it.adresselinje3,
                adressetype = it.adressetype,
                land = it.land,
                postnummer = it.postnummer,
                poststed = it.poststed,
            )
        },
    )
