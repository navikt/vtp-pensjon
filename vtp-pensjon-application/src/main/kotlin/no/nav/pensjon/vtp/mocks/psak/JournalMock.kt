package no.nav.pensjon.vtp.mocks.psak

import no.nav.pensjon.vtp.annotations.SoapService
import no.nav.pensjon.vtp.mocks.journalpost.dokarkiv.JournalpostMock
import no.nav.pensjon.vtp.testmodell.dokument.modell.DokumentModell
import no.nav.pensjon.vtp.testmodell.dokument.modell.DokumentVariantInnhold
import no.nav.pensjon.vtp.testmodell.dokument.modell.JournalpostModell
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import no.nav.pensjon.vtp.testmodell.repo.JournalRepository
import no.nav.pensjon.vtp.util.asGregorianCalendar
import no.nav.virksomhet.gjennomforing.arkiv.journal.v2.*
import no.nav.virksomhet.tjenester.arkiv.journal.meldinger.v2.*
import no.nav.virksomhet.tjenester.arkiv.journal.v2.Journal
import no.nav.virksomhet.tjenester.felles.v1.ObjectFactory
import javax.jws.*
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-tjeneste-journal_v2Web/sca/JournalWSEXP"])
@WebService(targetNamespace = "http://nav.no/virksomhet/tjenester/arkiv/journal/v2", name = "Journal")
@XmlSeeAlso(
    no.nav.virksomhet.tjenester.arkiv.journal.meldinger.v2.ObjectFactory::class,
    ObjectFactory::class,
    no.nav.virksomhet.tjenester.arkiv.journal.v2.ObjectFactory::class,
    no.nav.virksomhet.gjennomforing.arkiv.journal.v2.ObjectFactory::class,
    no.nav.virksomhet.tjenester.arkiv.journal.feil.v2.ObjectFactory::class
)
@HandlerChain(file = "/Handler-chain.xml")
class JournalMock(private val journalRepository: JournalRepository) : Journal {
    override fun hentJournalpost(request: HentJournalpostRequest) =
        throw NotImplementedException()

    /**
     * Operasjon for å identifisere og hente brevgruppeKode for en gitt brevkodeId.
     */
    override fun identifiserBrevgruppe(request: IdentifiserBrevgruppeRequest) =
        throw NotImplementedException()

    @WebMethod(action = "http://nav.no/virksomhet/tjenester/arkiv/journal/v2/Journal/finnJournalpostRequest")
    @RequestWrapper(
        localName = "finnJournalpost",
        targetNamespace = "http://nav.no/virksomhet/tjenester/arkiv/journal/v2",
        className = "no.nav.virksomhet.tjenester.arkiv.journal.v2.FinnJournalpost"
    )
    @ResponseWrapper(
        localName = "finnJournalpostResponse",
        targetNamespace = "http://nav.no/virksomhet/tjenester/arkiv/journal/v2",
        className = "no.nav.virksomhet.tjenester.arkiv.journal.v2.FinnJournalpostResponse"
    )
    @WebResult(name = "response")
    override fun finnJournalpost(
        @WebParam(name = "request") request: FinnJournalpostRequest
    ): FinnJournalpostResponse = with(request.sokescenario) {
        when {
            sokPaSak != null -> FinnJournalpostResponse().apply {
                journalpostListe = sokPaSak.sakIdListe.flatMap { journalRepository.finnJournalposterMedSakId(it) }
                    .map { tilJournalpost(it) }
                    .toTypedArray()
            }

            sokPaDokument != null -> FinnJournalpostResponse().apply {
                journalpostListe = sokPaDokument.dokumentInfoIdListe
                    .map { journalRepository.finnJournalpostMedDokumentId(it.toString()) }
                    .filterNotNull()
                    .map { tilJournalpost(it) }
                    .toTypedArray()
            }

            else -> FinnJournalpostResponse()
        }
    }

    @WebMethod(action = "http://nav.no/virksomhet/tjenester/arkiv/journal/v2/Journal/hentDokumentURLRequest")
    @RequestWrapper(
        localName = "hentDokumentURL",
        targetNamespace = "http://nav.no/virksomhet/tjenester/arkiv/journal/v2",
        className = "no.nav.virksomhet.tjenester.arkiv.journal.v2.HentDokumentURL"
    )
    @ResponseWrapper(
        localName = "hentDokumentURLResponse",
        targetNamespace = "http://nav.no/virksomhet/tjenester/arkiv/journal/v2",
        className = "no.nav.virksomhet.tjenester.arkiv.journal.v2.HentDokumentURLResponse"
    )
    @WebResult(name = "response")
    override fun hentDokumentURL(
        @WebParam(name = "request") request: HentDokumentURLRequest
    ): HentDokumentURLResponse {
        val journalpostId = request.journalpostId.toString()
        val dok = journalRepository.finnJournalpostMedJournalpostId(journalpostId)
            ?.dokumentModellList
            ?.flatMap { it.dokumentVariantInnholdListe ?: emptyList() }
            ?.firstOrNull { it.uuid == request.filUuId }

        return HentDokumentURLResponse().apply {
            dokumentURL = dok?.let { JournalpostMock.buildHentJournalpostFilUri(journalpostId, it.uuid).toString() }
        }
    }

    override fun hentDokument(request: HentDokumentRequest) = throw NotImplementedException()
}

private fun tilJournalpost(modell: JournalpostModell): Journalpost =
    Journalpost().apply {
        journalpostId = modell.journalpostId?.toLong()
        journalstatus = Journalstatus().apply { kode = modell.journalStatus.code }
        journalposttype = modell.journalposttype?.let { Journalposttype().apply { kode = it.code } }
        mottattDato = modell.mottattDato?.asGregorianCalendar()
        journalpostDokumentInfoRelasjonListe = modell.dokumentModellList.map { tilDokumentInfoRelasjon(it) }.toTypedArray()
        fagomrade = Fagomrade().apply {
            kode = modell.arkivtema?.name
        }
        innhold = modell.tittel
        saksrelasjon = Saksrelasjon().apply {
            sakId = modell.sakId
            fagsystem = Fagsystem().apply { kode = modell.fagsystemId }
        }
    }

private fun tilDokumentInfoRelasjon(modell: DokumentModell) =
    JournalpostDokumentInfoRelasjon().apply {
        dokumentInfo = tilDokumentInfo(modell)
        tilknyttetJournalpostSom = TilknyttetJournalpostSom().apply { kode = modell.dokumentTilknyttetJournalpost.name }
    }

private fun tilDokumentInfo(modell: DokumentModell) =
    DokumentInfo().apply {
        dokumentInfoId = modell.dokumentId?.toLong()
        kategori = modell.dokumentkategori?.let {
            Dokumentkategori().apply { kode = it.code }
        }
        tittel = modell.tittel
        brevkode = modell.brevkode
        fildetaljerListe = modell.dokumentVariantInnholdListe
            ?.map { tilFildetaljer(it) }
            ?.toTypedArray()
    }

private fun tilFildetaljer(dok: DokumentVariantInnhold) =
    Fildetaljer().apply {
        filUuid = dok.uuid
        filtype = Filtype().apply { kode = dok.filType.name }
        variantFormat = VariantFormat().apply { kode = dok.variantFormat.name }
    }
