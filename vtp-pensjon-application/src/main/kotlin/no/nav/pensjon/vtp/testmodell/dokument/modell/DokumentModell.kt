package no.nav.pensjon.vtp.testmodell.dokument.modell

import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.DokumentTilknyttetJournalpost
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.Dokumentkategori
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.DokumenttypeId

data class DokumentModell(
    var dokumentId: String? = null,
    val dokumentType: DokumenttypeId? = null,
    val erSensitiv: Boolean? = null,
    val tittel: String? = null,
    val innhold: String? = null,
    val dokumentTilknyttetJournalpost: DokumentTilknyttetJournalpost,
    val dokumentVariantInnholdListe: List<DokumentVariantInnhold>? = null,
    val dokumentkategori: Dokumentkategori? = null,
    val brevkode: String? = null,
)
