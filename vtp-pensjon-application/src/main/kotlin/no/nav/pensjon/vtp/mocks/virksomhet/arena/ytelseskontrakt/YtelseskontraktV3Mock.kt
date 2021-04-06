package no.nav.pensjon.vtp.mocks.virksomhet.arena.ytelseskontrakt

import no.nav.pensjon.vtp.annotations.SoapService
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.YtelseskontraktV3
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.informasjon.ytelseskontrakt.WSBruker
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.informasjon.ytelseskontrakt.WSRettighetsgruppe
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.informasjon.ytelseskontrakt.WSYtelseskontrakt
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.meldinger.ObjectFactory
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.meldinger.WSHentYtelseskontraktListeRequest
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.meldinger.WSHentYtelseskontraktListeResponse
import javax.jws.*
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/ail_ws/Ytelseskontrakt_v3"])
@WebService(targetNamespace = "http://nav.no/tjeneste/virksomhet/ytelseskontrakt/v3", name = "Ytelseskontrakt_v3")
@XmlSeeAlso(
    ObjectFactory::class,
    no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.metadata.ObjectFactory::class,
    no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.feil.ObjectFactory::class,
    no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.ObjectFactory::class,
    no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.informasjon.ytelseskontrakt.ObjectFactory::class
)
@HandlerChain(file = "/Handler-chain.xml")
class YtelseskontraktV3Mock : YtelseskontraktV3 {
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/ytelseskontrakt/v3/Ytelseskontrakt_v3/pingRequest")
    @RequestWrapper(
        localName = "ping",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/ytelseskontrakt/v3",
        className = "no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.Ping"
    )
    @ResponseWrapper(
        localName = "pingResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/ytelseskontrakt/v3",
        className = "no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.PingResponse"
    )
    override fun ping() = Unit

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/ytelseskontrakt/v3/Ytelseskontrakt_v3/hentYtelseskontraktListeRequest")
    @RequestWrapper(
        localName = "hentYtelseskontraktListe",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/ytelseskontrakt/v3",
        className = "no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.HentYtelseskontraktListe"
    )
    @ResponseWrapper(
        localName = "hentYtelseskontraktListeResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/ytelseskontrakt/v3",
        className = "no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.HentYtelseskontraktListeResponse"
    )
    @WebResult(name = "response")
    override fun hentYtelseskontraktListe(
        @WebParam(name = "request") request: WSHentYtelseskontraktListeRequest
    ) = WSHentYtelseskontraktListeResponse().apply {
        ytelseskontraktListe += WSYtelseskontrakt()

        bruker = WSBruker().apply {
            rettighetsgruppe = WSRettighetsgruppe().apply {
                rettighetsGruppe = "Gruppe"
            }
        }
    }
}
