package no.nav.pensjon.vtp.mocks.saf.dokument

import no.nav.pensjon.vtp.testmodell.dokument.modell.DokumentVariantInnhold
import no.nav.pensjon.vtp.testmodell.repo.JournalRepository
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class DokumentService(val journalRepository: JournalRepository) {

    fun hentDokument(journalpostId: String?, dokumentInformasjonId: String, variantFormat: String): Any =
        journalRepository.finnDokumentMedDokumentId(dokumentInformasjonId)?.let {
            it.dokumentVariantInnholdListe
                ?.filter { innhold -> innhold.variantFormat.name == variantFormat }
                ?.map(DokumentVariantInnhold::dokumentInnhold)
        } ?: if (journalpostId != null) { // TODO: Fjern når SafMock ikke er WIP
            try {
                javaClass.getResourceAsStream("/dokumenter/dummy_soknad.pdf").use {
                    return it!!.readAllBytes()
                }
            } catch (e: IOException) {
                throw RuntimeException(
                    String.format(
                        "Kunne ikke lese dummyrespons for " +
                                "journalpostId=%s, dokumentInfoId=%s, variantFormat=%s",
                        journalpostId,
                        dokumentInformasjonId,
                        variantFormat
                    )
                )
            }
        } else {
            throw RuntimeException(
                String.format(
                    "Kunne ikke finne dokument for " +
                            "dokumentInfoId=%s, variantFormat=%s",
                    dokumentInformasjonId,
                    variantFormat
                )
            )
        }
}