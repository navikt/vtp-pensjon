package no.nav.pensjon.vtp.mocks.journalpost.dokarkiv

import no.nav.dokarkiv.generated.model.Bruker
import no.nav.dokarkiv.generated.model.Dokument
import no.nav.dokarkiv.generated.model.DokumentVariant
import no.nav.dokarkiv.generated.model.OpprettJournalpostRequest
import no.nav.pensjon.vtp.testmodell.dokument.modell.DokumentModell
import no.nav.pensjon.vtp.testmodell.dokument.modell.DokumentVariantInnhold
import no.nav.pensjon.vtp.testmodell.dokument.modell.JournalpostBruker
import no.nav.pensjon.vtp.testmodell.dokument.modell.JournalpostModell
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.*
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.DokumentTilknyttetJournalpost.HOVEDDOKUMENT
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.DokumentTilknyttetJournalpost.VEDLEGG
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.Journalposttyper.*
import java.time.LocalDate

// TODO: utvid med nødvendig mapping
// TODO: Hvordan håndteres denne (getAvsenderMottaker) sammenlignet med bruker? & Map felter videre
// journalpostRequest.avsenderMottaker
// journalpostRequest.behandlingstema
// journalpostRequest.tilleggsopplysninger
// journalpostRequest.eksternReferanseId
// journalpostRequest.kanal
fun tilModell(journalpostRequest: OpprettJournalpostRequest) =
    JournalpostModell(
        journalposttype = mapJournalposttype(journalpostRequest.journalposttype),
        arkivtema = journalpostRequest.tema?.let { Arkivtema.valueOf(it) },
        bruker = journalpostRequest.bruker?.let { mapAvsenderFraBruker(it) },
        sakId = journalpostRequest.sak?.fagsakId,
        fagsystemId = journalpostRequest.sak?.fagsaksystem?.value,
        mottattDato = journalpostRequest.datoMottatt ?: LocalDate.now(),
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
            },
        journalStatus = Journalstatus.MOTTATT,
        tittel = journalpostRequest.tittel,
    )

fun mapAvsenderFraBruker(bruker: Bruker): JournalpostBruker {
    return when (bruker.idType) {
        Bruker.IdType.fNR -> JournalpostBruker(ident = bruker.id, brukerType = BrukerType.FNR)
        Bruker.IdType.aKTOERID -> JournalpostBruker(ident = bruker.id, brukerType = BrukerType.AKTOERID)
        Bruker.IdType.oRGNR -> JournalpostBruker(ident = bruker.id, brukerType = BrukerType.ORGNR)
    }
}

// TODO: Map videre felter
// dokument.dokumentKategori
// dokument.dokumentvarianter
private fun mapDokument(dokument: Dokument, dokumentTilknyttetJournalpost: DokumentTilknyttetJournalpost) =
    DokumentModell(
        tittel = dokument.tittel,
        dokumentVariantInnholdListe = dokument.dokumentvarianter
            ?.map { mapDokumentVariant(it) },
        dokumentTilknyttetJournalpost = dokumentTilknyttetJournalpost,
        brevkode = dokument.brevkode,
    )

private fun mapDokumentVariant(dokumentVariant: DokumentVariant) = DokumentVariantInnhold(
    filType = Arkivfiltype.valueOf(dokumentVariant.filtype),
    variantFormat = Variantformat.valueOf(dokumentVariant.variantformat),
    dokumentInnhold = dokumentVariant.fysiskDokument
)

private fun mapJournalposttype(type: OpprettJournalpostRequest.Journalposttype): Journalposttyper {
    return when (type) {
        OpprettJournalpostRequest.Journalposttype.iNNGAAENDE -> INNGAAENDE_DOKUMENT
        OpprettJournalpostRequest.Journalposttype.uTGAAENDE -> UTGAAENDE_DOKUMENT
        OpprettJournalpostRequest.Journalposttype.nOTAT -> NOTAT
    }
}
