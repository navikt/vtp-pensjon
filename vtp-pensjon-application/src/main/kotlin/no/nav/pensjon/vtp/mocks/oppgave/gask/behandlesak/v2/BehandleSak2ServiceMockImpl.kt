package no.nav.pensjon.vtp.mocks.oppgave.gask.behandlesak.v2

import no.nav.pensjon.vtp.annotations.SoapService
import no.nav.pensjon.vtp.mocks.oppgave.gask.sak.v1.GsakRepo
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository
import no.nav.tjeneste.virksomhet.behandlesak.v2.*
import java.lang.RuntimeException
import javax.jws.HandlerChain
import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult
import javax.jws.WebService
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper
import javax.xml.ws.soap.Addressing

@SoapService(path = ["/nav-gsak-ws/BehandleSakV2"])
@Addressing
@WebService(targetNamespace = "http://nav.no/tjeneste/virksomhet/behandlesak/v2", name = "BehandleSakV2")
@HandlerChain(file = "/Handler-chain.xml")
class BehandleSak2ServiceMockImpl(
    private val personModellRepository: PersonModellRepository,
    private val gsakRepo: GsakRepo
) : BehandleSakV2 {
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "opprettSak",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandlesak/v2",
        className = "no.nav.tjeneste.virksomhet.behandlesak.v2.OpprettSak"
    )
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/behandlesak/v2/BehandleSak_v2/opprettSakRequest")
    @ResponseWrapper(
        localName = "opprettSakResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandlesak/v2",
        className = "no.nav.tjeneste.virksomhet.behandlesak.v2.OpprettSakResponse"
    )
    override fun opprettSak(@WebParam(name = "opprettSakRequest") request: WSOpprettSakRequest): WSOpprettSakResponse {
        val sak = gsakRepo.leggTilSak(
            request.sak.gjelderBrukerListe.map {
                personModellRepository.findById(it.ident)
                    ?: throw RuntimeException("Person ikke funnet")
            },
            request.sak.fagomrade,
            request.sak.fagsystem,
            request.sak.saktype
        )

        return WSOpprettSakResponse().apply {
            sakId = sak.sakId
        }
    }

    @RequestWrapper(
        localName = "ping",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandlesak/v2",
        className = "no.nav.tjeneste.virksomhet.behandlesak.v2.Ping"
    )
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/behandlesak/v2/BehandleSak_v2/pingRequest")
    @ResponseWrapper(
        localName = "pingResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/behandlesak/v2",
        className = "no.nav.tjeneste.virksomhet.behandlesak.v2.PingResponse"
    )
    override fun ping() = Unit
}
