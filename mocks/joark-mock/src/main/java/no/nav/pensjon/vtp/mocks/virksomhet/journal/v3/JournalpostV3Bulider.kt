package no.nav.pensjon.vtp.mocks.virksomhet.journal.modell

import no.nav.pensjon.vtp.core.util.asXMLGregorianCalendar
import no.nav.pensjon.vtp.testmodell.dokument.modell.DokumentModell
import no.nav.pensjon.vtp.testmodell.dokument.modell.JournalpostModell
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
                .first { it.dokumentTilknyttetJournalpost.kode == "HOVEDDOKUMENT" }
        )
        vedleggListe.addAll(
            modell.dokumentModellList
                .filter { it.dokumentTilknyttetJournalpost.kode == "VEDLEGG" }
                .map { lagDetaljertDokumentinformasjon(it) }
        )
        journaltilstand = modell.journaltilstand?.let {
            fromValue(it)
        }
        forsendelseMottatt = modell.mottattDato?.asXMLGregorianCalendar()
        forsendelseJournalfoert = modell.mottattDato?.asXMLGregorianCalendar()
        journalposttype = modell.journalposttype?.let {
            Journalposttyper().apply {
                kodeverksRef = it.kode
            }
        }
    }

private fun lagDetaljertDokumentinformasjon(dokumentModell: DokumentModell) =
    DetaljertDokumentinformasjon()
        .apply {
            dokumentId = dokumentModell.dokumentId
            dokumentkategori = dokumentModell.dokumentkategori?.let {
                Dokumentkategorier().apply {
                    kodeverksRef = it.kode
                }
            }
                ?: Dokumentkategorier()

            dokumentTypeId = dokumentModell.dokumentType
                ?.let {
                    DokumenttypeIder().apply {
                        kodeverksRef = it.kode
                        value = it.kode
                    }
                }
                ?: DokumenttypeIder()

            dokumentInnholdListe.addAll(
                dokumentModell.dokumentVariantInnholdListe
                    .map { (filType, variantFormat) ->
                        DokumentInnhold().apply {
                            arkivfiltype = Arkivfiltyper().apply {
                                kodeverksRef = filType.kode
                                value = filType.kode
                            }
                            variantformat = Variantformater().apply {
                                kodeverksRef = variantFormat.kode
                                value = variantFormat.kode
                            }
                        }
                    }
            )
        }
