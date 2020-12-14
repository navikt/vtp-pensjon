package no.nav.pensjon.vtp.mocks.journalpost.dokarkiv

import no.nav.dokarkiv.generated.model.Bruker
import no.nav.dokarkiv.generated.model.Dokument
import no.nav.dokarkiv.generated.model.DokumentVariant
import no.nav.dokarkiv.generated.model.OpprettJournalpostRequest
import no.nav.dokarkiv.generated.model.OpprettJournalpostRequest.JournalpostTypeEnum
import no.nav.pensjon.vtp.testmodell.dokument.modell.DokumentModell
import no.nav.pensjon.vtp.testmodell.dokument.modell.DokumentVariantInnhold
import no.nav.pensjon.vtp.testmodell.dokument.modell.JournalpostBruker
import no.nav.pensjon.vtp.testmodell.dokument.modell.JournalpostModell
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.*
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.Arkivfiltype
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.DokumentTilknyttetJournalpost.HOVEDDOKUMENT
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.DokumentTilknyttetJournalpost.VEDLEGG
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.Journalposttyper.*
import java.time.LocalDateTime.now

// TODO: utvid med nødvendig mapping
// TODO: Hvordan håndteres denne (getAvsenderMottaker) sammenlignet med bruker? & Map felter videre
// journalpostRequest.avsenderMottaker
// journalpostRequest.behandlingstema
// journalpostRequest.tittel
// journalpostRequest.tilleggsopplysninger
// journalpostRequest.eksternReferanseId
// journalpostRequest.kanal
fun tilModell(journalpostRequest: OpprettJournalpostRequest) =
    JournalpostModell(
        journalposttype = mapJournalposttype(journalpostRequest.journalpostType),
        arkivtema = Arkivtema.valueOf(journalpostRequest.tema),
        bruker = mapAvsenderFraBruker(journalpostRequest.bruker),
        sakId = journalpostRequest.sak.arkivsaksnummer,
        mottattDato = journalpostRequest.datoMottatt?.toLocalDateTime() ?: now(),
        dokumentModellList = journalpostRequest.dokumenter
            .mapIndexed { index, dokument ->
                if (index == 0) {
                    mapDokument(
                        dokument,
                        HOVEDDOKUMENT
                    )
                } else {
                    mapDokument(dokument, VEDLEGG)
                }
            }
    )

fun mapAvsenderFraBruker(bruker: Bruker): JournalpostBruker {
    return when (bruker.idType) {
        Bruker.IdTypeEnum.FNR -> JournalpostBruker(ident = bruker.id, brukerType = BrukerType.FNR)
        Bruker.IdTypeEnum.AKTOERID -> JournalpostBruker(ident = bruker.id, brukerType = BrukerType.AKTOERID)
        Bruker.IdTypeEnum.ORGNR -> JournalpostBruker(ident = bruker.id, brukerType = BrukerType.ORGNR)
        else -> {
            throw UnsupportedOperationException("Kan ikke opprette journalpost for brukertype")
        }
    }
}

// TODO: Map videre felter
// dokument.dokumentKategori
// dokument.brevkode
// dokument.dokumentKategori
// dokument.dokumentvarianter
private fun mapDokument(dokument: Dokument, dokumentTilknyttetJournalpost: DokumentTilknyttetJournalpost) =
    DokumentModell(
        dokumentkategori = Dokumentkategori.fromCode(dokument.dokumentKategori),
        tittel = dokument.tittel,
        dokumentVariantInnholdListe = dokument.dokumentvarianter
            .map { mapDokumentVariant(it) },
        dokumentTilknyttetJournalpost = dokumentTilknyttetJournalpost
    )

private fun mapDokumentVariant(dokumentVariant: DokumentVariant) = DokumentVariantInnhold(
    filType = Arkivfiltype.valueOf(dokumentVariant.filtype),
    variantFormat = Variantformat.valueOf(dokumentVariant.variantformat),
    dokumentInnhold = dokumentVariant.fysiskDokument
)

private fun mapJournalposttype(type: JournalpostTypeEnum): Journalposttyper {
    return when {
        type.toString().equals("INNGAAENDE", ignoreCase = true) -> {
            INNGAAENDE_DOKUMENT
        }
        type.toString().equals("UTGAAENDE", ignoreCase = true) -> {
            UTGAAENDE_DOKUMENT
        }
        type.toString().equals("NOTAT", ignoreCase = true) -> {
            NOTAT
        }
        else -> {
            throw IllegalArgumentException("Verdi journalposttype ikke støttet")
        }
    }
}
