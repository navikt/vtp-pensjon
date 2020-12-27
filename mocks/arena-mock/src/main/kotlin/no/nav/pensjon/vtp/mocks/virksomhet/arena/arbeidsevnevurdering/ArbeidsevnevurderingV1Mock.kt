package no.nav.pensjon.vtp.mocks.virksomhet.arena.arbeidsevnevurdering

import no.nav.pensjon.vtp.core.annotations.SoapService
import no.nav.pensjon.vtp.core.util.asXMLGregorianCalendar
import no.nav.tjeneste.virksomhet.arbeidsevnevurdering.v1.binding.ArbeidsevnevurderingV1
import no.nav.tjeneste.virksomhet.arbeidsevnevurdering.v1.informasjon.arbeidsevnevurdering.Arbeidsevnevurdering
import no.nav.tjeneste.virksomhet.arbeidsevnevurdering.v1.meldinger.FinnArbeidsevnevurderingRequest
import no.nav.tjeneste.virksomhet.arbeidsevnevurdering.v1.meldinger.FinnArbeidsevnevurderingResponse
import java.time.LocalDate
import javax.jws.*
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/ail_ws/Arbeidsevnevurdering_v1"])
@WebService(
    name = "Arbeidsevnevurdering_v1",
    targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsevnevurdering/v1"
)
@HandlerChain(file = "/Handler-chain.xml")
class ArbeidsevnevurderingV1Mock : ArbeidsevnevurderingV1 {
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/arbeidsevnevurdering/v1/BindingfinnArbeidsevnevurdering/")
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(
        localName = "finnArbeidsevnevurdering",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsevnevurdering/v1",
        className = "no.nav.tjeneste.virksomhet.arbeidsevnevurdering.v1.FinnArbeidsevnevurdering"
    )
    @ResponseWrapper(
        localName = "finnArbeidsevnevurderingResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsevnevurdering/v1",
        className = "no.nav.tjeneste.virksomhet.arbeidsevnevurdering.v1.FinnArbeidsevnevurderingResponse"
    )
    override fun finnArbeidsevnevurdering(
        @WebParam(name = "request", targetNamespace = "") request: FinnArbeidsevnevurderingRequest
    ) = FinnArbeidsevnevurderingResponse().apply {
        isForeliggerArbeidsevnevurdering = true
        arbeidsevnevurdering = Arbeidsevnevurdering().apply {
            isErVarigTilpassetInnsats = true
            fomVarigTilrettelagtArbeid = LocalDate.now().minusMonths(10).asXMLGregorianCalendar()
            isGodkjentArbeidsevnevurdering = true
            utfallArbeidsevnevurdering = "Veldig god vurdering YES"
        }
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/arbeidsevnevurdering/v1/Bindingping/")
    @RequestWrapper(
        localName = "ping",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsevnevurdering/v1",
        className = "no.nav.tjeneste.virksomhet.arbeidsevnevurdering.v1.Ping"
    )
    @ResponseWrapper(
        localName = "pingResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsevnevurdering/v1",
        className = "no.nav.tjeneste.virksomhet.arbeidsevnevurdering.v1.PingResponse"
    )
    override fun ping() = Unit
}
