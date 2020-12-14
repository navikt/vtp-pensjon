package no.nav.pensjon.vtp.testmodell.dokument.modell

import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.DokumentTilknyttetJournalpost
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.Dokumentkategori
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.DokumenttypeId
import java.util.*

data class DokumentModell(
    var dokumentId: String? = null,
    val dokumentType: DokumenttypeId? = null,
    val erSensitiv: Boolean? = null,
    val tittel: String? = null,
    val innhold: String? = null,
    val dokumentTilknyttetJournalpost: DokumentTilknyttetJournalpost,
    val dokumentVariantInnholdListe: List<DokumentVariantInnhold> = ArrayList(),
    val dokumentkategori: Dokumentkategori? = null,
)
