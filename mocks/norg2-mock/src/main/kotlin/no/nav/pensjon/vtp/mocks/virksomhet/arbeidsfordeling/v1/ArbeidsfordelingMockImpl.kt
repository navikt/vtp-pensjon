package no.nav.pensjon.vtp.mocks.virksomhet.arbeidsfordeling.v1

import no.nav.pensjon.vtp.core.annotations.SoapService
import no.nav.pensjon.vtp.testmodell.enheter.EnheterIndeks
import no.nav.pensjon.vtp.testmodell.enheter.Norg2Modell
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.binding.ArbeidsfordelingV1
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.informasjon.Enhetsstatus
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.informasjon.Enhetstyper
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.informasjon.Organisasjonsenhet
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.meldinger.FinnAlleBehandlendeEnheterListeRequest
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.meldinger.FinnAlleBehandlendeEnheterListeResponse
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.meldinger.FinnBehandlendeEnhetListeRequest
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.meldinger.FinnBehandlendeEnhetListeResponse
import javax.jws.*
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper
import javax.xml.ws.soap.Addressing

@SoapService(path = ["/norg2/ws/Arbeidsfordeling/v1"])
@Addressing
@WebService(name = "Arbeidsfordeling_v1", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/")
@HandlerChain(file = "/Handler-chain.xml")
class ArbeidsfordelingMockImpl(private val enheterIndeks: EnheterIndeks) : ArbeidsfordelingV1 {
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/Arbeidsfordeling_v1/finnBehandlendeEnhetListeRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "finnBehandlendeEnhetListe",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/",
        className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.FinnBehandlendeEnhetListe"
    )
    @ResponseWrapper(
        localName = "finnBehandlendeEnhetListeResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/",
        className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.FinnBehandlendeEnhetListeResponse"
    )
    override fun finnBehandlendeEnhetListe(
        @WebParam(name = "request") request: FinnBehandlendeEnhetListeRequest
    ) = FinnBehandlendeEnhetListeResponse().apply {
        findOrganisasjonsenhet(
            request.arbeidsfordelingKriterier.diskresjonskode?.value,
            request.arbeidsfordelingKriterier.tema?.value
        )?.let { behandlendeEnhetListe.add(it) }
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/Arbeidsfordeling_v1/pingRequest")
    @RequestWrapper(
        localName = "ping",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/",
        className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.Ping"
    )
    @ResponseWrapper(
        localName = "pingResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/",
        className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.PingResponse"
    )
    override fun ping() = Unit

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/Arbeidsfordeling_v1/finnAlleBehandlendeEnheterListeRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "finnAlleBehandlendeEnheterListe",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/",
        className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.FinnAlleBehandlendeEnheterListe"
    )
    @ResponseWrapper(
        localName = "finnAlleBehandlendeEnheterListeResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/",
        className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.FinnAlleBehandlendeEnheterListeResponse"
    )
    override fun finnAlleBehandlendeEnheterListe(
        @WebParam(name = "request") request: FinnAlleBehandlendeEnheterListeRequest
    ) = FinnAlleBehandlendeEnheterListeResponse().apply {
        behandlendeEnhetListe.addAll(
            enheterIndeks.alleEnheter
                .filter {
                    val tema = request.arbeidsfordelingKriterier.tema?.value
                    when {
                        tema == null -> true
                        "FOR".equals(tema, ignoreCase = true) && "YTA".equals(it.type, ignoreCase = true) -> false
                        "OMS".equals(tema, ignoreCase = true) && "FPY".equals(it.type, ignoreCase = true) -> false
                        else -> true
                    }
                }
                .map { lagEnhet(it) }
        )
    }

    private fun findOrganisasjonsenhet(diskrKode: String?, tema: String?): Organisasjonsenhet? {
        return (
            if (diskrKode != null && listOf("UFB", "SPSF", "SPFO").contains(diskrKode)) {
                enheterIndeks.finnByDiskresjonskode(diskrKode)
            } else if (tema != null) {
                enheterIndeks.finnByDiskresjonskode(tema)
                    ?: enheterIndeks.finnByDiskresjonskode("NORMAL-$tema")
            } else {
                null
            }
            )?.let { lagEnhet(it) }
    }

    private fun lagEnhet(modell: Norg2Modell): Organisasjonsenhet {
        return Organisasjonsenhet()
            .apply {
                enhetId = modell.enhetId.toString()
                enhetNavn = modell.navn
                status = modell.status?.let { Enhetsstatus.fromValue(it) }
                type = modell.type?.let {
                    Enhetstyper().apply {
                        value = it
                    }
                }
            }
    }
}
