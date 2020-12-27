package no.nav.pensjon.vtp.mocks.virksomhet.journal.v3

import no.nav.pensjon.vtp.core.util.asXMLGregorianCalendar
import no.nav.pensjon.vtp.testmodell.dokument.modell.DokumentModell
import no.nav.pensjon.vtp.testmodell.dokument.modell.JournalpostModell
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.DokumentTilknyttetJournalpost.HOVEDDOKUMENT
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.DokumentTilknyttetJournalpost.VEDLEGG
import no.nav.tjeneste.virksomhet.journal.v3.informasjon.*
import no.nav.tjeneste.virksomhet.journal.v3.informasjon.Journaltilstand.fromValue
import no.nav.tjeneste.virksomhet.journal.v3.informasjon.hentkjernejournalpostliste.ArkivSak
import no.nav.tjeneste.virksomhet.journal.v3.informasjon.hentkjernejournalpostliste.DetaljertDokumentinformasjon
import no.nav.tjeneste.virksomhet.journal.v3.informasjon.hentkjernejournalpostliste.DokumentInnhold
import no.nav.tjeneste.virksomhet.journal.v3.informasjon.hentkjernejournalpostliste.Journalpost

fun buildFromV3(modell: JournalpostModell) =
    Journalpost().apply {
        journalpostId = modell.journalpostId
        gjelderArkivSak = ArkivSak().apply {
            arkivSakId = modell.sakId
        }
        hoveddokument = lagDetaljertDokumentinformasjon(
            modell.dokumentModellList
                .first { it.dokumentTilknyttetJournalpost == HOVEDDOKUMENT }
        )
        vedleggListe.addAll(
            modell.dokumentModellList
                .filter { it.dokumentTilknyttetJournalpost == VEDLEGG }
                .map { lagDetaljertDokumentinformasjon(it) }
        )
        journaltilstand = modell.journaltilstand?.let {
            fromValue(it)
        }
        forsendelseMottatt = modell.mottattDato?.asXMLGregorianCalendar()
        forsendelseJournalfoert = modell.mottattDato?.asXMLGregorianCalendar()
        journalposttype = modell.journalposttype?.let {
            Journalposttyper().apply {
                kodeverksRef = it.name
            }
        }
    }

private fun lagDetaljertDokumentinformasjon(dokumentModell: DokumentModell) =
    DetaljertDokumentinformasjon()
        .apply {
            dokumentId = dokumentModell.dokumentId
            dokumentkategori = dokumentModell.dokumentkategori?.let {
                Dokumentkategorier().apply {
                    kodeverksRef = it.name
                }
            }
                ?: Dokumentkategorier()

            dokumentTypeId = dokumentModell.dokumentType
                ?.let {
                    DokumenttypeIder().apply {
                        kodeverksRef = it.name
                        value = it.name
                    }
                }
                ?: DokumenttypeIder()

            dokumentInnholdListe += dokumentModell.dokumentVariantInnholdListe
                ?.map { (filType, variantFormat) ->
                    DokumentInnhold().apply {
                        arkivfiltype = Arkivfiltyper().apply {
                            kodeverksRef = filType.name
                            value = filType.name
                        }
                        variantformat = Variantformater().apply {
                            kodeverksRef = variantFormat.name
                            value = variantFormat.name
                        }
                    }
                }
                ?: emptyList()
        }
