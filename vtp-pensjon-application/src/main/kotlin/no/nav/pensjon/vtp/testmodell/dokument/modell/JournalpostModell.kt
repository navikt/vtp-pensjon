package no.nav.pensjon.vtp.testmodell.dokument.modell

import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.Arkivtema
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.Journalposttyper
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.Journalstatus
import java.time.LocalDateTime
import java.util.*

data class JournalpostModell(
    var journalpostId: String? = null,
    val dokumentModellList: List<DokumentModell> = ArrayList(),
    val avsenderFnr: String? = null,
    val sakId: String? = null,
    val fagsystemId: String? = null,
    val journalStatus: Journalstatus,
    val kommunikasjonsretning: String? = null,
    val mottattDato: LocalDateTime? = null,
    val mottakskanal: String? = null,
    val arkivtema: Arkivtema? = null,
    val journaltilstand: String? = null,
    val journalposttype: Journalposttyper? = null,
    val bruker: JournalpostBruker? = null,
)
