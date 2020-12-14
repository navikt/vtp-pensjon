package no.nav.pensjon.vtp.testmodell.repo

import no.nav.pensjon.vtp.testmodell.dokument.modell.DokumentModell
import no.nav.pensjon.vtp.testmodell.dokument.modell.JournalpostModell

interface JournalRepository {
    fun finnDokumentMedDokumentId(dokumentId: String): DokumentModell?
    fun finnJournalposterMedFnr(fnr: String): List<JournalpostModell>
    fun finnJournalposterMedSakId(sakId: String): List<JournalpostModell>
    fun finnJournalpostMedJournalpostId(journalpostId: String): JournalpostModell?
    fun save(journalpostModell: JournalpostModell): JournalpostModell
}
