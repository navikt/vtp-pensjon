package no.nav.pensjon.vtp.mocks.virksomhet.journal.v2

import no.nav.pensjon.vtp.core.annotations.SoapService
import no.nav.pensjon.vtp.mocks.virksomhet.journal.modell.buildFromV2
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import no.nav.pensjon.vtp.testmodell.repo.JournalRepository
import no.nav.tjeneste.virksomhet.journal.v2.binding.HentDokumentDokumentIkkeFunnet
import no.nav.tjeneste.virksomhet.journal.v2.binding.JournalV2
import no.nav.tjeneste.virksomhet.journal.v2.feil.DokumentIkkeFunnet
import no.nav.tjeneste.virksomhet.journal.v2.meldinger.*
import javax.jws.*
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper
import javax.xml.ws.soap.Addressing

@SoapService(path = ["/joark/Journal/v2"])
@Addressing
@WebService(name = "Journal_v2", targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2")
@HandlerChain(file = "/Handler-chain.xml")
class JournalV2ServiceMockImpl(private val journalRepository: JournalRepository) : JournalV2 {
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/journal/v2/Journal_v2/hentJournalpostListeRequest")
    @WebResult(name = "Response")
    @RequestWrapper(
        localName = "hentJournalpostListe",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2",
        className = "no.nav.tjeneste.virksomhet.journal.v2.HentJournalpostListe"
    )
    @ResponseWrapper(
        localName = "hentJournalpostListeResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2",
        className = "no.nav.tjeneste.virksomhet.journal.v2.HentJournalpostListeResponse"
    )
    override fun hentJournalpostListe(@WebParam(name = "Request") request: HentJournalpostListeRequest) =
        HentJournalpostListeResponse()
            .apply {
                journalpostListe.addAll(
                    request.sakListe
                        .flatMap { journalRepository.finnJournalposterMedSakId(it.sakId) }
                        .map { buildFromV2(it) }
                )
            }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/journal/v2/Journal_v2/hentDokumentRequest")
    @WebResult(name = "Response")
    @RequestWrapper(
        localName = "hentDokument",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2",
        className = "no.nav.tjeneste.virksomhet.journal.v2.HentDokument"
    )
    @ResponseWrapper(
        localName = "hentDokumentResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2",
        className = "no.nav.tjeneste.virksomhet.journal.v2.HentDokumentResponse"
    )
    @Throws(
        HentDokumentDokumentIkkeFunnet::class
    )
    override fun hentDokument(@WebParam(name = "Request") request: HentDokumentRequest) =
        journalRepository.finnDokumentMedDokumentId(request.dokumentId)
            ?.let {
                HentDokumentResponse().apply {
                    dokument = it.innhold?.toByteArray()
                }
            }
            ?: throw HentDokumentDokumentIkkeFunnet(
                "Kunne ikke finne dokument",
                DokumentIkkeFunnet()
            )

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/journal/v2/Journal_v2/hentDokumentURLRequest")
    @WebResult(name = "Response")
    @RequestWrapper(
        localName = "hentDokumentURL",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2",
        className = "no.nav.tjeneste.virksomhet.journal.v2.HentDokumentURL"
    )
    @ResponseWrapper(
        localName = "hentDokumentURLResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2",
        className = "no.nav.tjeneste.virksomhet.journal.v2.HentDokumentURLResponse"
    )
    override fun hentDokumentURL(@WebParam(name = "Request") request: HentDokumentURLRequest) =
        throw NotImplementedException()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/journal/v2/Journal_v2/pingRequest")
    @RequestWrapper(
        localName = "ping",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2",
        className = "no.nav.tjeneste.virksomhet.journal.v2.Ping"
    )
    @ResponseWrapper(
        localName = "pingResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/journal/v2",
        className = "no.nav.tjeneste.virksomhet.journal.v2.PingResponse"
    )
    override fun ping() = Unit
}
