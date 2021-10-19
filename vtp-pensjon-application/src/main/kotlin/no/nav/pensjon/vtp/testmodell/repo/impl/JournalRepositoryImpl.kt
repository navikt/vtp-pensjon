package no.nav.pensjon.vtp.testmodell.repo.impl

import no.nav.pensjon.vtp.testmodell.dokument.modell.DokumentModell
import no.nav.pensjon.vtp.testmodell.dokument.modell.JournalpostModell
import no.nav.pensjon.vtp.testmodell.repo.JournalRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter.ofPattern
import java.util.concurrent.atomic.AtomicInteger

@Repository
class JournalRepositoryImpl : JournalRepository {
    private val journalposter = HashMap<String, JournalpostModell>()
    private val dokumenter = HashMap<String, DokumentModell>()

    private val journalpostId = AtomicInteger(now().format(ofPattern("Mdkm")).toInt() * 100)
    private val dokumentId = AtomicInteger(now().format(ofPattern("Mdkm")).toInt() * 100)

    override fun finnDokumentMedDokumentId(dokumentId: String): DokumentModell? {
        return dokumenter[dokumentId]
    }

    override fun finnJournalposterMedFnr(fnr: String): List<JournalpostModell> {
        return journalposter.values
            .filter { e: JournalpostModell -> e.avsenderFnr != null && e.avsenderFnr.equals(fnr, ignoreCase = true) }
    }

    override fun finnJournalposterMedSakId(sakId: String): List<JournalpostModell> {
        return journalposter.values
            .filter { it.sakId != null && it.sakId.equals(sakId, ignoreCase = true) }
    }

    override fun finnJournalpostMedJournalpostId(journalpostId: String): JournalpostModell? {
        return journalposter[journalpostId]
    }

    override fun finnJournalpostMedDokumentId(dokumentId: String): JournalpostModell? =
        journalposter.values
            .firstOrNull { it.dokumentModellList.any { dok -> dok.dokumentId == dokumentId } }

    override fun save(journalpostModell: JournalpostModell): JournalpostModell {
        val journalpostId: String = journalpostModell.journalpostId?.let {
            if (it.isNotEmpty()) {
                it
            } else {
                null
            }
        }
            ?: genererJournalpostId()

        journalpostModell.journalpostId = journalpostId

        for (dokumentModell in journalpostModell.dokumentModellList) {
            val dokumentId =
                dokumentModell.dokumentId?.let {
                    if (it.isNotEmpty()) {
                        it
                    } else {
                        null
                    }
                }
                    ?: genererDokumentId()

            dokumentModell.dokumentId = dokumentId

            check(!dokumenter.containsKey(dokumentId)) { "Forsøker å opprette dokument allerede eksisterende dokumentId" }
            dokumenter[dokumentId] = dokumentModell
        }

        return if (journalposter.containsKey(journalpostId)) {
            throw IllegalStateException("Forsøker å opprette journalpost allerede eksisterende journalpostId")
        } else {
            journalposter[journalpostId] = journalpostModell
            journalpostModell
        }
    }

    fun genererJournalpostId(): String {
        return journalpostId.incrementAndGet().toString()
    }

    fun genererDokumentId(): String {
        return dokumentId.incrementAndGet().toString()
    }
}
