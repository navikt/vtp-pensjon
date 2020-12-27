package no.nav.pensjon.vtp.mocks.virksomhet.journal.v3

import no.nav.pensjon.vtp.core.annotations.SoapService
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import no.nav.pensjon.vtp.testmodell.repo.JournalRepository
import no.nav.tjeneste.virksomhet.journal.v3.HentDokumentDokumentIkkeFunnet
import no.nav.tjeneste.virksomhet.journal.v3.JournalV3
import no.nav.tjeneste.virksomhet.journal.v3.feil.DokumentIkkeFunnet
import no.nav.tjeneste.virksomhet.journal.v3.meldinger.*
import javax.jws.*
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper
import javax.xml.ws.soap.Addressing

@SoapService(path = ["/joark/Journal/v3"])
@Addressing
@WebService(name = "Journal_v3", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v3")
@HandlerChain(file = "/Handler-chain.xml")
class JournalV3ServiceMockImpl(private val journalRepository: JournalRepository) : JournalV3 {
    override fun ping() {
    }

    override fun hentDokumentURL(hentDokumentURLRequest: HentDokumentURLRequest) =
        throw NotImplementedException()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/journal/v3/Journal_v3/hentKjerneJournalpostListeRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "hentKjerneJournalpostListe",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v3",
        className = "no.nav.tjeneste.virksomhet.journal.v3.HentKjerneJournalpostListe"
    )
    @ResponseWrapper(
        localName = "hentKjerneJournalpostListeResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v3",
        className = "no.nav.tjeneste.virksomhet.journal.v3.HentKjerneJournalpostListeResponse"
    )
    // TODO: sett sisteIntervall?
    override fun hentKjerneJournalpostListe(@WebParam(name = "request") request: HentKjerneJournalpostListeRequest): HentKjerneJournalpostListeResponse {
        return HentKjerneJournalpostListeResponse()
            .apply {
                journalpostListe.addAll(
                    request.arkivSakListe
                        .flatMap { journalRepository.finnJournalposterMedSakId(it.arkivSakId) }
                        .map { buildFromV3(it) }
                )
            }
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/journal/v3/Journal_v3/hentDokumentRequest")
    @RequestWrapper(
        localName = "hentDokument",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v3",
        className = "no.nav.tjeneste.virksomhet.journal.v3.HentDokument"
    )
    @ResponseWrapper(
        localName = "hentDokumentResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v3",
        className = "no.nav.tjeneste.virksomhet.journal.v3.HentDokumentResponse"
    )
    @WebResult(name = "response")
    @Throws(
        HentDokumentDokumentIkkeFunnet::class
    )
    override fun hentDokument(@WebParam(name = "request") request: HentDokumentRequest) =
        journalRepository.finnDokumentMedDokumentId(request.dokumentId)
            ?.let { dokumentModell ->
                HentDokumentResponse().apply {
                    dokument = dokumentModell.innhold?.toByteArray()
                }
            }
            ?: throw HentDokumentDokumentIkkeFunnet(
                "Kunne ikke finne dokument",
                DokumentIkkeFunnet()
            )
}
