package no.nav.pensjon.vtp.mocks.virksomhet.journal.v2

import no.nav.pensjon.vtp.testmodell.dokument.modell.JournalpostModell
import no.nav.pensjon.vtp.util.asXMLGregorianCalendar
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.*
import java.time.LocalDate.now

fun buildFromV2(modell: JournalpostModell) = Journalpost().apply {
    journalpostId = modell.journalpostId
    journalstatus = modell.journalStatus.let {
        Journalstatuser().apply {
            value = it.name
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
                                value = it.name
                            }
                        }
                        beskriverInnholdListe += it.dokumentVariantInnholdListe
                            ?.map {
                                DokumentInnhold().apply {
                                    filtype = Arkivfiltyper().apply {
                                        value = it.filType.name
                                    }
                                    variantformat = Variantformater().apply {
                                        value = it.variantFormat.name
                                    }
                                }
                            }
                            ?: emptyList()
                    }
                    dokumentTilknyttetJournalpost = TilknyttetJournalpostSom().apply {
                        value = it.dokumentTilknyttetJournalpost.name
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
