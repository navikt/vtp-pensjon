package no.nav.pensjon.vtp.mocks.virksomhet.journal.modell

import no.nav.pensjon.vtp.core.util.asXMLGregorianCalendar
import no.nav.pensjon.vtp.testmodell.dokument.modell.JournalpostModell
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.*
import java.time.LocalDate.now

fun buildFromV2(modell: JournalpostModell) = Journalpost().apply {
    journalpostId = modell.journalpostId
    journalstatus = modell.journalStatus?.let {
        Journalstatuser().apply {
            value = it.kode
        }
    }
    // TODO: OL Fjern hardkoding om nødvendig og xmlGregorianConvert
    kommunikasjonsretning = Kommunikasjonsretninger().apply {
        value = "INN"
    }
    mottatt = now().asXMLGregorianCalendar()

    dokumentinfoRelasjonListe.addAll(
        modell.dokumentModellList
            .map {
                DokumentinfoRelasjon().apply {
                    journalfoertDokument = JournalfoertDokumentInfo().apply {
                        dokumentType = it.dokumentType?.let {
                            Dokumenttyper().apply {
                                value = it.kode
                            }
                        }
                        beskriverInnholdListe.addAll(
                            it.dokumentVariantInnholdListe.map {
                                DokumentInnhold().apply {
                                    filtype = Arkivfiltyper().apply {
                                        value = it.filType.kode
                                    }
                                    variantformat = Variantformater().apply {
                                        value = it.variantFormat.kode
                                    }
                                }
                            }
                        )
                    }
                    dokumentTilknyttetJournalpost = TilknyttetJournalpostSom().apply {
                        value = it.dokumentTilknyttetJournalpost.kode
                    }
                }
            }
    )

    // arkivtema = Arkivtemaer().apply {
    //     value = modell.getArkivtema().kode
    // }
    // kommunikasjonsretning = Kommunikasjonsretninger().apply {
    //     value = modell.getKommunikasjonsretning()
    // }
}
