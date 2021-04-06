package no.nav.pensjon.vtp.mocks.oppgave.gask.sak.v1

import no.nav.tjeneste.virksomhet.sak.v1.binding.FinnSakUgyldigInput
import no.nav.tjeneste.virksomhet.sak.v1.binding.SakV1
import no.nav.tjeneste.virksomhet.sak.v1.feil.UgyldigInput
import no.nav.tjeneste.virksomhet.sak.v1.meldinger.FinnSakRequest
import no.nav.tjeneste.virksomhet.sak.v1.meldinger.FinnSakResponse
import no.nav.tjeneste.virksomhet.sak.v1.meldinger.HentSakRequest
import no.nav.tjeneste.virksomhet.sak.v1.meldinger.HentSakResponse
import javax.jws.*
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper
import javax.xml.ws.soap.Addressing

@Addressing
@WebService(name = "Sak_v1", targetNamespace = "http://nav.no/tjeneste/virksomhet/sak/v1")
@HandlerChain(file = "/Handler-chain.xml")
class SakServiceMockImpl(private val repo: GsakRepo) : SakV1 {
    /**
     * @return returns no.nav.tjeneste.virksomhet.sak.v1.meldinger.FinnSakResponse
     */
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/sak/v1/Sak_v1/finnSakRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "finnSak",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/sak/v1",
        className = "no.nav.tjeneste.virksomhet.sak.v1.FinnSak"
    )
    @ResponseWrapper(
        localName = "finnSakResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/sak/v1",
        className = "no.nav.tjeneste.virksomhet.sak.v1.FinnSakResponse"
    )
    @Throws(
        FinnSakUgyldigInput::class
    )
    override fun finnSak(
        @WebParam(name = "request") request: FinnSakRequest
    ): FinnSakResponse {
        if (request.bruker == null && (request.fagsystem == null || request.fagsystemSakId == null)) {
            throw FinnSakUgyldigInput("BrukerModell; eller FagsystemSakID og Fagsystem må være satt", UgyldigInput())
        }

        return FinnSakResponse().apply {
            sakListe += repo.alleSaker
                .filter { sak -> request.bruker == null || sak.gjelderBrukerListe.any { request.bruker.ident == it.ident } }
                .filter { sak -> request.fagsystem == null || sak.fagsystem.value == request.fagsystem.value }
                .filter { sak ->
                    request.fagomraadeListe == null || request.fagomraadeListe.isEmpty() || request.fagomraadeListe.any { it.value == sak.fagomraade.value }
                }
        }
    }

    /**
     * @return returns no.nav.tjeneste.virksomhet.sak.v1.meldinger.HentSakResponse
     */
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/sak/v1/Sak_v1/hentSakRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "hentSak",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/sak/v1",
        className = "no.nav.tjeneste.virksomhet.sak.v1.HentSak"
    )
    @ResponseWrapper(
        localName = "hentSakResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/sak/v1",
        className = "no.nav.tjeneste.virksomhet.sak.v1.HentSakResponse"
    )
    override fun hentSak(@WebParam(name = "request") request: HentSakRequest) = HentSakResponse().apply {
        sak = repo.hentSak(request.sakId)
    }

    /**
     *
     */
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/sak/v1/Sak_v1/pingRequest")
    @RequestWrapper(
        localName = "ping",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/sak/v1",
        className = "no.nav.tjeneste.virksomhet.sak.v1.Ping"
    )
    @ResponseWrapper(
        localName = "pingResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/sak/v1",
        className = "no.nav.tjeneste.virksomhet.sak.v1.PingResponse"
    )
    override fun ping() = Unit
}
