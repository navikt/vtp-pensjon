package no.nav.pensjon.vtp.mocks.virksomhet.behandlejournal.v3

import no.nav.pensjon.vtp.annotations.SoapService
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import no.nav.tjeneste.virksomhet.behandlejournal.v3.binding.BehandleJournalV3
import no.nav.tjeneste.virksomhet.behandlejournal.v3.meldinger.*
import javax.jws.HandlerChain
import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult
import javax.jws.WebService
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper
import javax.xml.ws.soap.Addressing

@SoapService(path = ["/services/behandlejournal/v3"])
@Addressing
@HandlerChain(file = "/Handler-chain.xml")
@WebService(targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3", name = "behandleJournal_v3")
class BehandleJournalV3ServiceMockImpl : BehandleJournalV3 {
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3/behandleJournal_v3/ferdigstillDokumentopplastingRequest")
    @RequestWrapper(
        localName = "ferdigstillDokumentopplasting",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3",
        className = "no.nav.tjeneste.virksomhet.behandlejournal.v3.FerdigstillDokumentopplasting"
    )
    @ResponseWrapper(
        localName = "ferdigstillDokumentopplastingResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3",
        className = "no.nav.tjeneste.virksomhet.behandlejournal.v3.FerdigstillDokumentopplastingResponse"
    )
    override fun ferdigstillDokumentopplasting(@WebParam(name = "ferdigstillDokumentopplastingRequest") request: FerdigstillDokumentopplastingRequest) =
        Unit

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3/behandleJournal_v3/journalfoerNotatRequest")
    @RequestWrapper(
        localName = "journalfoerNotat",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3",
        className = "no.nav.tjeneste.virksomhet.behandlejournal.v3.JournalfoerNotat"
    )
    @ResponseWrapper(
        localName = "journalfoerNotatResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3",
        className = "no.nav.tjeneste.virksomhet.behandlejournal.v3.JournalfoerNotatResponse"
    )
    @WebResult(name = "journalfoerNotatResponse")
    override fun journalfoerNotat(@WebParam(name = "journalfoerNotatRequest") request: JournalfoerNotatRequest) =
        throw NotImplementedException()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3/behandleJournal_v3/arkiverUstrukturertKravRequest")
    @RequestWrapper(
        localName = "arkiverUstrukturertKrav",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3",
        className = "no.nav.tjeneste.virksomhet.behandlejournal.v3.ArkiverUstrukturertKrav"
    )
    @ResponseWrapper(
        localName = "arkiverUstrukturertKravResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3",
        className = "no.nav.tjeneste.virksomhet.behandlejournal.v3.ArkiverUstrukturertKravResponse"
    )
    @WebResult(name = "arkiverUstrukturertKravResponse")
    override fun arkiverUstrukturertKrav(@WebParam(name = "arkiverUstrukturertKravRequest") request: ArkiverUstrukturertKravRequest) =
        throw NotImplementedException()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3/behandleJournal_v3/journalfoerUtgaaendeHenvendelseRequest")
    @RequestWrapper(
        localName = "journalfoerUtgaaendeHenvendelse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3",
        className = "no.nav.tjeneste.virksomhet.behandlejournal.v3.JournalfoerUtgaaendeHenvendelse"
    )
    @ResponseWrapper(
        localName = "journalfoerUtgaaendeHenvendelseResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3",
        className = "no.nav.tjeneste.virksomhet.behandlejournal.v3.JournalfoerUtgaaendeHenvendelseResponse"
    )
    @WebResult(name = "journalfoerUtgaaendeHenvendelseResponse")
    override fun journalfoerUtgaaendeHenvendelse(@WebParam(name = "journalfoerUtgaaendeHenvendelseRequest") request: JournalfoerUtgaaendeHenvendelseRequest) =
        throw NotImplementedException()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3/behandleJournal_v3/lagreVedleggPaaJournalpostRequest")
    @RequestWrapper(
        localName = "lagreVedleggPaaJournalpost",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3",
        className = "no.nav.tjeneste.virksomhet.behandlejournal.v3.LagreVedleggPaaJournalpost"
    )
    @ResponseWrapper(
        localName = "lagreVedleggPaaJournalpostResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3",
        className = "no.nav.tjeneste.virksomhet.behandlejournal.v3.LagreVedleggPaaJournalpostResponse"
    )
    @WebResult(name = "lagreVedleggPaaJournalpostResponse")
    override fun lagreVedleggPaaJournalpost(@WebParam(name = "lagreVedleggPaaJournalpostRequest") request: LagreVedleggPaaJournalpostRequest) =
        throw NotImplementedException()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3/behandleJournal_v3/pingRequest")
    @RequestWrapper(
        localName = "ping",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3",
        className = "no.nav.tjeneste.virksomhet.behandlejournal.v3.Ping"
    )
    @ResponseWrapper(
        localName = "pingResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3",
        className = "no.nav.tjeneste.virksomhet.behandlejournal.v3.PingResponse"
    )
    override fun ping() {
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3/behandleJournal_v3/journalfoerInngaaendeHenvendelseRequest")
    @RequestWrapper(
        localName = "journalfoerInngaaendeHenvendelse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3",
        className = "no.nav.tjeneste.virksomhet.behandlejournal.v3.JournalfoerInngaaendeHenvendelse"
    )
    @ResponseWrapper(
        localName = "journalfoerInngaaendeHenvendelseResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandleJournal/v3",
        className = "no.nav.tjeneste.virksomhet.behandlejournal.v3.JournalfoerInngaaendeHenvendelseResponse"
    )
    @WebResult(name = "journalfoerInngaaendeHenvendelseResponse")
    override fun journalfoerInngaaendeHenvendelse(@WebParam(name = "journalfoerInngaaendeHenvendelseRequest") request: JournalfoerInngaaendeHenvendelseRequest) =
        throw NotImplementedException()
}
