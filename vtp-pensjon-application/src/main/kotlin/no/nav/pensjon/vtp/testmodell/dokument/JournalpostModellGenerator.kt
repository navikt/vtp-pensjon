package no.nav.pensjon.vtp.testmodell.dokument

import no.nav.pensjon.vtp.testmodell.dokument.modell.DokumentModell
import no.nav.pensjon.vtp.testmodell.dokument.modell.DokumentVariantInnhold
import no.nav.pensjon.vtp.testmodell.dokument.modell.JournalpostModell
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.Arkivfiltype.PDF
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.Arkivfiltype.XML
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.DokumentTilknyttetJournalpost.HOVEDDOKUMENT
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.DokumenttypeId
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.Journalposttyper.INNGAAENDE_DOKUMENT
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.Journalstatus
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.Journalstatus.JOURNALFØRT
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.Variantformat.ARKIV
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.Variantformat.FULLVERSJON
import java.time.LocalDate
import java.util.*

object JournalpostModellGenerator {
    fun lagJournalpostStrukturertDokument(
        innhold: String?,
        fnr: String?,
        dokumenttypeId: DokumenttypeId?,
        mottattDato: LocalDate,
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
                        filType = PDF,
                        variantFormat = ARKIV,
                        dokumentInnhold = ByteArray(0)
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
                            uuid = UUID.randomUUID().toString(),
                            filType = PDF,
                            variantFormat = ARKIV,
                            dokumentInnhold = ByteArray(0)
                        )
                    )
                )
            )
        )
}
