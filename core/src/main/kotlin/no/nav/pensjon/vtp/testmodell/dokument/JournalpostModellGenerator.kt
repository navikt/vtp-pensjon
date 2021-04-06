package no.nav.pensjon.vtp.testmodell.dokument

import no.nav.pensjon.vtp.testmodell.dokument.modell.DokumentModell
import no.nav.pensjon.vtp.testmodell.dokument.modell.DokumentVariantInnhold
import no.nav.pensjon.vtp.testmodell.dokument.modell.JournalpostModell
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.*
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.Arkivfiltype.*
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.DokumentTilknyttetJournalpost.*
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.Journalposttyper.*
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.Journalstatus.*
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.Variantformat.*
import java.time.LocalDateTime

object JournalpostModellGenerator {
    fun lagJournalpostStrukturertDokument(
        innhold: String?,
        fnr: String?,
        dokumenttypeId: DokumenttypeId?,
        mottattDato: LocalDateTime,
    ) = JournalpostModell(
        journalStatus = JOURNALFØRT,
        avsenderFnr = fnr,
        journalposttype = INNGAAENDE_DOKUMENT,
        mottattDato = mottattDato,
        dokumentModellList = listOf(
            DokumentModell(
                innhold = innhold,
                dokumentType = dokumenttypeId,
                dokumentTilknyttetJournalpost = HOVEDDOKUMENT,
                dokumentVariantInnholdListe = listOf(
                    DokumentVariantInnhold(
                        filType = XML,
                        variantFormat = FULLVERSJON,
                        dokumentInnhold = innhold?.toByteArray() ?: ByteArray(0)
                    ),
                    DokumentVariantInnhold(
                        filType = PDF, variantFormat = ARKIV, dokumentInnhold = ByteArray(0)
                    )

                )
            )
        )
    )

    fun lagJournalpostUstrukturertDokument(fnr: String?, dokumenttypeId: DokumenttypeId, journalstatus: Journalstatus = JOURNALFØRT) =
        JournalpostModell(
            journalStatus = journalstatus,
            avsenderFnr = fnr,
            dokumentModellList = listOf(
                DokumentModell(
                    dokumentType = dokumenttypeId,
                    innhold = "innhold: $dokumenttypeId",
                    dokumentTilknyttetJournalpost = HOVEDDOKUMENT,
                    dokumentVariantInnholdListe = listOf(
                        DokumentVariantInnhold(
                            filType = PDF, variantFormat = ARKIV, dokumentInnhold = ByteArray(0)
                        )
                    )
                )
            )
        )
}
