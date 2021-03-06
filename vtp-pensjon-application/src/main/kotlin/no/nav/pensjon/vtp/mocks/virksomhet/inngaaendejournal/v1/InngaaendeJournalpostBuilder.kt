package no.nav.pensjon.vtp.mocks.virksomhet.inngaaendejournal.v1

import no.nav.pensjon.vtp.testmodell.dokument.modell.DokumentModell
import no.nav.pensjon.vtp.testmodell.dokument.modell.JournalpostModell
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.DokumentTilknyttetJournalpost.*
import no.nav.pensjon.vtp.util.asXMLGregorianCalendar
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
                value = it.name
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
            value = it.name
        }
    }

    dokumentId = dokumentModell.dokumentId

    dokumentInnholdListe += dokumentModell.dokumentVariantInnholdListe
        ?.map {
            Dokumentinnhold().apply {
                arkivfiltype = Arkivfiltyper().withValue(it.filType.name)
                variantformat = Variantformater().withValue(it.variantFormat.name)
            }
        }
        ?: emptyList()
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
