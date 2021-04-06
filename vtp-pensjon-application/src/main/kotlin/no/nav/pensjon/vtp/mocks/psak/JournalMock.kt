package no.nav.pensjon.vtp.mocks.psak

import no.nav.pensjon.vtp.annotations.SoapService
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import no.nav.virksomhet.tjenester.arkiv.journal.meldinger.v2.*
import no.nav.virksomhet.tjenester.arkiv.journal.v2.Journal
import no.nav.virksomhet.tjenester.felles.v1.ObjectFactory
import javax.jws.HandlerChain
import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult
import javax.jws.WebService
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
class JournalMock : Journal {
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
    ) = FinnJournalpostResponse()

    override fun hentDokumentURL(request: HentDokumentURLRequest) =
        throw NotImplementedException()

    override fun hentDokument(request: HentDokumentRequest) = throw NotImplementedException()
}
