package no.nav.pensjon.vtp.testmodell

import no.nav.pensjon.vtp.testmodell.dokument.modell.DokumentModell
import no.nav.pensjon.vtp.testmodell.dokument.modell.JournalpostModell
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.DokumentTilknyttetJournalpost.HOVEDDOKUMENT
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.DokumenttypeId.ADOPSJONSSOKNAD_ENGANGSSTONAD
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.Journalstatus.MOTTATT
import no.nav.pensjon.vtp.testmodell.repo.impl.JournalRepositoryImpl
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class JournalRepoTest {
    @Test
    fun skalFinneJournalposterPåSaksid() {
        val sakId = "12381"
        val journalRepository = JournalRepositoryImpl()

        val journalpostModell = JournalpostModell(
            dokumentModellList = listOf(
                DokumentModell(
                    dokumentId = "1235",
                    dokumentTilknyttetJournalpost = HOVEDDOKUMENT,
                    dokumentType = ADOPSJONSSOKNAD_ENGANGSSTONAD,
                    erSensitiv = false,
                    innhold = "<xml/>",
                    tittel = "Tittel på dokumentet"
                )
            ),
            sakId = sakId,
            kommunikasjonsretning = "INN",
            fagsystemId = "FS32",
            journalStatus = MOTTATT,
        )

        journalRepository.save(journalpostModell)

        val journalpostModells = journalRepository.finnJournalposterMedSakId(sakId)
        Assertions.assertThat(journalpostModells).size().isEqualTo(1)
        Assertions.assertThat(journalpostModells[0]).isEqualTo(journalpostModell)
    }

    @Test
    fun skalFinneJournalposterPåAvsenderFnr() {
        val journalRepository = JournalRepositoryImpl()
        val avsenderFnrSøker = "01020304056"
        val avsenderFnrIkkeSøker = "99999999999"

        journalRepository.save(
            JournalpostModell(
                avsenderFnr = avsenderFnrSøker,
                journalStatus = MOTTATT,
            )
        )

        journalRepository.save(
            JournalpostModell(
                avsenderFnr = avsenderFnrSøker,
                journalStatus = MOTTATT,
            )
        )

        journalRepository.save(
            JournalpostModell(
                avsenderFnr = avsenderFnrIkkeSøker,
                journalStatus = MOTTATT,
            )
        )

        val journalpostModells = journalRepository.finnJournalposterMedFnr(avsenderFnrSøker)
        Assertions.assertThat(journalpostModells).size().isEqualTo(2)
    }

    @Test
    fun skalSetteJournalpostIdVedLagring() {
        val journalRepository = JournalRepositoryImpl()

        val resultatModell = journalRepository.save(
            JournalpostModell(
                kommunikasjonsretning = "Inn",
                avsenderFnr = "00115522447",
                journalStatus = MOTTATT,
            )
        )
        assertNotNull(resultatModell.journalpostId)
    }
}
