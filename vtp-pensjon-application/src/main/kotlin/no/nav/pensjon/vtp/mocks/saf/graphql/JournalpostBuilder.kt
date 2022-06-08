package no.nav.pensjon.vtp.mocks.saf.graphql

import no.nav.pensjon.vtp.mocks.saf.*
import no.nav.pensjon.vtp.testmodell.dokument.modell.DokumentModell
import no.nav.pensjon.vtp.testmodell.dokument.modell.JournalpostBruker
import no.nav.pensjon.vtp.testmodell.dokument.modell.JournalpostModell
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.BrukerType
import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.DokumenttypeId
import java.time.Instant
import java.time.ZoneId
import java.util.*

class JournalpostBuilder private constructor() {

    companion object {
        fun buildFrom(modell: JournalpostModell) =
            Journalpost(
                journalpostId = modell.journalpostId!!,
                tittel = modell.tittel,
                tema = Tema.valueOf(modell.arkivtema?.name ?: "UKJ"),
                temanavn = modell.arkivtema?.name,
                journalstatus = tilJournalstatus(modell),
                kanal = Kanal.valueOf(modell.mottakskanal ?: "UKJENT"),
                bruker = modell.bruker?.let { tilBruker(it) },
                datoOpprettet = Date(),
                eksternReferanseId = "ekstern-" + modell.journalpostId,
                avsenderMottaker = AvsenderMottaker(
                    modell.avsenderFnr,
                    AvsenderMottakerIdType.FNR,
                    "Navn",
                    "Norge",
                    true
                ),
                sak = Sak(
                    modell.sakId,
                    Arkivsaksystem.GSAK,
                    Date.from(Instant.now()),
                    modell.sakId,
                    modell.fagsystemId
                ),
                dokumenter = modell.dokumentModellList
                    .sortedBy { it.dokumentTilknyttetJournalpost.rekkefolge }
                    .map { lagDetaljertDokumentinformasjon(it) },
                journalposttype = modell.journalposttype?.code?.let { Journalposttype.valueOf(it) },
                relevanteDatoer = modell.mottattDato?.let {
                    val dato = Date.from(modell.mottattDato.atStartOfDay(ZoneId.systemDefault()).toInstant())
                    listOf(
                        RelevantDato(dato, Datotype.DATO_JOURNALFOERT),
                        RelevantDato(dato, Datotype.DATO_REGISTRERT)
                    )
                },
                antallRetur = "",
                avsenderMottakerId = "deprekert felt",
                avsenderMottakerLand = "deprekert felt",
                avsenderMottakerNavn = "deprekert felt",
                behandlingstema = "ae0216",
                behandlingstemanavn = "PEN",
                journalfoerendeEnhet = "deprekert felt",
                journalforendeEnhet = "001",
                journalfortAvNavn = "Saksbehandler navn",
                kanalnavn = modell.mottakskanal,
                opprettetAvNavn = "Jan Saksbehandler",
                skjerming = "",
                tilleggsopplysninger = listOf()
            )

        private fun lagDetaljertDokumentinformasjon(dokModell: DokumentModell) =
            DokumentInfo(
                tittel = "tittel",
                dokumentInfoId = dokModell.dokumentId!!,
                brevkode = dokModell.brevkode ?: dokumentTypeIdTilBrevkode(dokModell.dokumentType),
                dokumentstatus = Dokumentstatus.FERDIGSTILT,
                datoFerdigstilt = Date.from(Instant.now()),
                dokumentvarianter = dokModell.dokumentVariantInnholdListe!!.map { dokumentVariant ->
                    Dokumentvariant(
                        filtype = dokumentVariant.filType.name,
                        variantformat = dokumentVariant.variantFormat.let { Variantformat.valueOf(it.name) },
                        saksbehandlerHarTilgang = true,
                        filnavn = "filnavn",
                        filuuid = "filuuid",
                        skjerming = null
                    )
                }.toList(),
                logiskeVedlegg = listOf(LogiskVedlegg("id", "Tittel")),
                skjerming = null,
                originalJournalpostId = null
            )


        private fun dokumentTypeIdTilBrevkode(dokumentType: DokumenttypeId?): String? {
            if (DokumenttypeId.INNTEKTSMELDING.equals(dokumentType)) {
                return "4036"
            }
            // Alle andre brevkoder er ukjente for k9-sak -> for denne mockens del kan vi da like gjerne returnere DokumentTypeId sin verdi
            return dokumentType?.code
        }

        private fun tilJournalstatus(modell: JournalpostModell): Journalstatus {
            return when (modell.journalStatus.code) {
                "J" -> Journalstatus.JOURNALFOERT
                "MO" -> Journalstatus.MOTTATT
                "M" -> Journalstatus.MOTTATT
                "A" -> Journalstatus.AVBRUTT
                else -> Journalstatus.UKJENT
            }
        }

        private fun tilBruker(bruker: JournalpostBruker): Bruker {
            if (BrukerType.FNR == bruker.brukerType) {
                return Bruker(bruker.ident, BrukerIdType.FNR)
            } else if (BrukerType.AKTOERID == bruker.brukerType) {
                return Bruker(bruker.ident, BrukerIdType.AKTOERID)
            } else if (BrukerType.ORGNR == bruker.brukerType) {
                return Bruker(bruker.ident, BrukerIdType.ORGNR)
            }
            throw UnsupportedOperationException("Kan ikke opprette journalpost for brukertype")
        }
    }
}