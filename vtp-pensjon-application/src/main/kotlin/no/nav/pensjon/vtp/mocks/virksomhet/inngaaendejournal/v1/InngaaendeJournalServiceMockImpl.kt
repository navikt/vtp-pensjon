package no.nav.pensjon.vtp.mocks.virksomhet.inngaaendejournal.v1

import no.nav.pensjon.vtp.core.annotations.SoapService
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import no.nav.pensjon.vtp.testmodell.repo.JournalRepository
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.binding.HentJournalpostJournalpostIkkeFunnet
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.binding.InngaaendeJournalV1
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.meldinger.HentJournalpostRequest
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.meldinger.HentJournalpostResponse
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.meldinger.UtledJournalfoeringsbehovRequest
import javax.jws.HandlerChain
import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult
import javax.jws.WebService
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper
import javax.xml.ws.soap.Addressing

@SoapService(path = ["/joark/InngaaendeJournal/v1"])
@Addressing
@WebService(name = "InngaaendeJournal_v1", targetNamespace = "http://nav.no/tjeneste/virksomhet/inngaaendeJournal/v1")
@HandlerChain(file = "/Handler-chain.xml")
class InngaaendeJournalServiceMockImpl(private val journalRepository: JournalRepository) : InngaaendeJournalV1 {
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/inngaaendeJournal/v1/InngaaendeJournal_v1/pingRequest")
    @RequestWrapper(
        localName = "ping",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/inngaaendeJournal/v1",
        className = "no.nav.tjeneste.virksomhet.inngaaendejournal.v1.Ping"
    )
    @ResponseWrapper(
        localName = "pingResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/inngaaendeJournal/v1",
        className = "no.nav.tjeneste.virksomhet.inngaaendejournal.v1.PingResponse"
    )
    override fun ping() {
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/inngaaendeJournal/v1/InngaaendeJournal_v1/hentJournalpostRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "hentJournalpost",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/inngaaendeJournal/v1",
        className = "no.nav.tjeneste.virksomhet.inngaaendejournal.v1.HentJournalpost"
    )
    @ResponseWrapper(
        localName = "hentJournalpostResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/inngaaendeJournal/v1",
        className = "no.nav.tjeneste.virksomhet.inngaaendejournal.v1.HentJournalpostResponse"
    )
    override fun hentJournalpost(@WebParam(name = "request") request: HentJournalpostRequest) =
        HentJournalpostResponse().apply {
            this.inngaaendeJournalpost = buildFromV1(
                journalRepository.finnJournalpostMedJournalpostId(request.journalpostId)
                    ?: throw HentJournalpostJournalpostIkkeFunnet("Kunne ikke finne journalpost")
            )
        }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/inngaaendeJournal/v1/InngaaendeJournal_v1/utledJournalfoeringsbehovRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "utledJournalfoeringsbehov",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/inngaaendeJournal/v1",
        className = "no.nav.tjeneste.virksomhet.inngaaendejournal.v1.UtledJournalfoeringsbehov"
    )
    @ResponseWrapper(
        localName = "utledJournalfoeringsbehovResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/inngaaendeJournal/v1",
        className = "no.nav.tjeneste.virksomhet.inngaaendejournal.v1.UtledJournalfoeringsbehovResponse"
    )
    override fun utledJournalfoeringsbehov(@WebParam(name = "request") request: UtledJournalfoeringsbehovRequest) =
        throw NotImplementedException()
}
