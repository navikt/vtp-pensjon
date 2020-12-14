package no.nav.pensjon.vtp.mocks.virksomhet.inngaaendejournal.v1

import no.nav.pensjon.vtp.core.util.asXMLGregorianCalendar
import no.nav.pensjon.vtp.testmodell.dokument.modell.DokumentModell
import no.nav.pensjon.vtp.testmodell.dokument.modell.JournalpostModell
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.DokumentTilknyttetJournalpost.*
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.*

fun buildFromV1(journalpostModell: JournalpostModell): InngaaendeJournalpost {
    return InngaaendeJournalpost().apply {
        avsenderId = journalpostModell.avsenderFnr
        forsendelseMottatt = journalpostModell.mottattDato?.asXMLGregorianCalendar()

        mottakskanal = journalpostModell.mottakskanal?.let {
            Mottakskanaler().apply {
                value = it
            }
        }

        tema = journalpostModell.arkivtema?.let {
            Tema().apply {
                value = it.kode
            }
        }

        journaltilstand = journalpostModell.journaltilstand?.let {
            Journaltilstand.fromValue(journalpostModell.journaltilstand)
        }

        arkivSak = lagArkivSak(journalpostModell)

        brukerListe.addAll(

            journalpostModell.avsenderFnr?.let {
                listOf(
                    Person().apply {
                        ident = it
                    }
                )
            }
                ?: emptyList()
        )

        hoveddokument = lagDokumentinformasjon(
            journalpostModell
                .dokumentModellList.firstOrNull { HOVEDDOKUMENT == it.dokumentTilknyttetJournalpost }
                ?: throw IllegalStateException("Journalpost mangler hoveddokument")
        )

        vedleggListe.addAll(
            journalpostModell.dokumentModellList
                .filter { VEDLEGG == it.dokumentTilknyttetJournalpost }
                .map { lagDokumentinformasjon(it) }
        )
    }
}

private fun lagDokumentinformasjon(dokumentModell: DokumentModell) = Dokumentinformasjon().apply {
    dokumenttypeId = dokumentModell.dokumentType?.let {
        DokumenttypeIder().apply {
            value = dokumentModell.dokumentType!!.kode
        }
    }

    dokumentId = dokumentModell.dokumentId

    dokumentInnholdListe.addAll(
        dokumentModell.dokumentVariantInnholdListe
            .map {
                Dokumentinnhold().apply {

                    arkivfiltype = Arkivfiltyper().withValue(it.filType.kode)
                    variantformat = Variantformater().withValue(it.variantFormat.kode)
                }
            }
    )
}

private fun lagArkivSak(journalpostmodell: JournalpostModell): ArkivSak? =
    if (journalpostmodell.sakId != null || journalpostmodell.fagsystemId != null) {
        ArkivSak().apply {
            arkivSakId = journalpostmodell.sakId
            arkivSakSystem = journalpostmodell.fagsystemId
        }
    } else {
        null
    }
