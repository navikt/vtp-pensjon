package no.nav.pensjon.vtp.mocks.oppgave.gask.oppgave.v3

import no.nav.pensjon.vtp.core.annotations.SoapService
import no.nav.pensjon.vtp.mocks.oppgave.repository.OppgaveFoo
import no.nav.pensjon.vtp.mocks.oppgave.repository.OppgaveFooRepository
import no.nav.tjeneste.virksomhet.oppgave.v3.binding.HentOppgaveOppgaveIkkeFunnet
import no.nav.tjeneste.virksomhet.oppgave.v3.binding.OppgaveV3
import no.nav.tjeneste.virksomhet.oppgave.v3.feil.OppgaveIkkeFunnet
import no.nav.tjeneste.virksomhet.oppgave.v3.informasjon.oppgave.Oppgave
import no.nav.tjeneste.virksomhet.oppgave.v3.meldinger.*
import org.slf4j.LoggerFactory
import javax.jws.*
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper
import javax.xml.ws.soap.Addressing

fun Oppgave.asResponse() : HentOppgaveResponse {
    val hentOppgaveResponse = HentOppgaveResponse()
    hentOppgaveResponse.oppgave = this
    return hentOppgaveResponse
}

fun Collection<OppgaveFoo>.asOppgave3() : List<Oppgave> {
    return this.map { it.asOppgave3() }
}

fun Collection<Oppgave>.asResponse(): FinnOppgaveListeResponse {
    val response = FinnOppgaveListeResponse()
    response.oppgaveListe
            .addAll(this)
    response.totaltAntallTreff = this.size
    return response
}

@SoapService(path = ["/nav-gsak-ws/OppgaveV3"])
@Addressing
@WebService(name = "Oppgave_v3", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3")
@HandlerChain(file = "/Handler-chain.xml")
class OppgaveServiceMockImpl(private val oppgaveRepository: OppgaveFooRepository) : OppgaveV3 {
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgave/v3/Oppgave_v3/hentOppgave")
    @WebResult(name = "response")
    @RequestWrapper(localName = "hentOppgave", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.HentOppgave")
    @ResponseWrapper(localName = "hentOppgaveResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.HentOppgaveResponse")
    @Throws(HentOppgaveOppgaveIkkeFunnet::class)
    override fun hentOppgave(@WebParam(name = "request") request: HentOppgaveRequest): HentOppgaveResponse =
            oppgaveRepository.findById(request.oppgaveId)
                    ?.asOppgave3()
                    ?.asResponse()
                    ?: throw HentOppgaveOppgaveIkkeFunnet("Fant ikke oppgave med id=${request.oppgaveId}", OppgaveIkkeFunnet())

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgave/v3/Oppgave_v3/finnOppgaveListeRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "finnOppgaveListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.FinnOppgaveListe")
    @ResponseWrapper(localName = "finnOppgaveListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.FinnOppgaveListeResponse")
    override fun finnOppgaveListe(@WebParam(name = "request") finnOppgaveListeRequest: FinnOppgaveListeRequest): FinnOppgaveListeResponse =
            oppgaveRepository.findAll()
                    .filter {
                        finnOppgaveListeRequest.sok == null ||
                                filterOppgave(finnOppgaveListeRequest.sok, it)
                    }
                    .asOppgave3()
                    .asResponse()

    private fun filterOppgave(sok: FinnOppgaveListeSok, oppgave: OppgaveFoo): Boolean {
        return with(sok) {
            (sakId == null || sakId == oppgave.saksnummer) &&
                    (brukerId == null || brukerId == oppgave.brukerId)
        }
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgave/v3/Oppgave_v3/finnFerdigstiltOppgaveListeRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "finnFerdigstiltOppgaveListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.FinnFerdigstiltOppgaveListe")
    @ResponseWrapper(localName = "finnFerdigstiltOppgaveListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.FinnFerdigstiltOppgaveListeResponse")
    override fun finnFerdigstiltOppgaveListe(@WebParam(name = "request") finnFerdigstiltOppgaveListeRequest: FinnFerdigstiltOppgaveListeRequest): FinnFerdigstiltOppgaveListeResponse {
        LOG.info("finnFerdigstiltOppgaveListe. Søk: ansvarligEnhetId: {}, brukerId: {}, sakId: {}, søknadsId: {}", finnFerdigstiltOppgaveListeRequest.sok.ansvarligEnhetId, finnFerdigstiltOppgaveListeRequest.sok.brukerId,
                finnFerdigstiltOppgaveListeRequest.sok.sakId, finnFerdigstiltOppgaveListeRequest.sok.soknadsId)
        return FinnFerdigstiltOppgaveListeResponse()
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgave/v3/Oppgave_v3/finnFeilregistrertOppgaveListeRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "finnFeilregistrertOppgaveListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.FinnFeilregistrertOppgaveListe")
    @ResponseWrapper(localName = "finnFeilregistrertOppgaveListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.FinnFeilregistrertOppgaveListeResponse")
    override fun finnFeilregistrertOppgaveListe(@WebParam(name = "request") finnFeilregistrertOppgaveListeRequest: FinnFeilregistrertOppgaveListeRequest): FinnFeilregistrertOppgaveListeResponse {
        LOG.info("finnFeilregistrertOppgaveListe. Søk: ansvarligEnhetId: {}, brukerId: {}, sakId: {}, aøknadsId{}", finnFeilregistrertOppgaveListeRequest.sok.ansvarligEnhetId, finnFeilregistrertOppgaveListeRequest.sok.brukerId,
                finnFeilregistrertOppgaveListeRequest.sok.sakId, finnFeilregistrertOppgaveListeRequest.sok.soknadsId)
        return FinnFeilregistrertOppgaveListeResponse()
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgave/v3/Oppgave_v3/finnMappeListeRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "finnMappeListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.FinnMappeListe")
    @ResponseWrapper(localName = "finnMappeListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.FinnMappeListeResponse")
    override fun finnMappeListe(@WebParam(name = "request") finnMappeListeRequest: FinnMappeListeRequest): FinnMappeListeResponse {
        LOG.info("finnMappeListe. EnhetId: {}", finnMappeListeRequest.enhetId)
        return FinnMappeListeResponse()
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/oppgave/v3/Oppgave_v3/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/oppgave/v3", className = "no.nav.tjeneste.virksomhet.oppgave.v3.PingResponse")
    override fun ping() {
        LOG.info("Ping mottatt og besvart")
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(OppgaveServiceMockImpl::class.java)
    }
}

