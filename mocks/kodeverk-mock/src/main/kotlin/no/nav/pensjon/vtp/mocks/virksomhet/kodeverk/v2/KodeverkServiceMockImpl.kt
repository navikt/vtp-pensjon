package no.nav.pensjon.vtp.mocks.virksomhet.kodeverk.v2

import no.nav.pensjon.vtp.core.annotations.SoapService
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import no.nav.tjeneste.virksomhet.kodeverk.v2.KodeverkPortType
import no.nav.tjeneste.virksomhet.kodeverk.v2.meldinger.FinnKodeverkListeRequest
import no.nav.tjeneste.virksomhet.kodeverk.v2.meldinger.HentKodeverkRequest
import javax.jws.*
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper
import javax.xml.ws.soap.Addressing

@SoapService(path = ["/kodeverk/ws/Kodeverk/v2"])
@Addressing
@WebService(endpointInterface = "no.nav.tjeneste.virksomhet.kodeverk.v2.KodeverkPortType")
@HandlerChain(file = "/Handler-chain.xml")
class KodeverkServiceMockImpl : KodeverkPortType {
    override fun ping() = Unit

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/kodeverk/v2/KodeverkPortType/hentKodeverkRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "hentKodeverk",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/kodeverk/v2/",
        className = "no.nav.tjeneste.virksomhet.kodeverk.v2.HentKodeverk"
    )
    @ResponseWrapper(
        localName = "hentKodeverkResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/kodeverk/v2/",
        className = "no.nav.tjeneste.virksomhet.kodeverk.v2.HentKodeverkResponse"
    )
    override fun hentKodeverk(@WebParam(name = "respons") request: HentKodeverkRequest) =
        throw NotImplementedException()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/kodeverk/v2/KodeverkPortType/finnKodeverkListeRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "finnKodeverkListe",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/kodeverk/v2/",
        className = "no.nav.tjeneste.virksomhet.kodeverk.v2.FinnKodeverkListe"
    )
    @ResponseWrapper(
        localName = "finnKodeverkListeResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/kodeverk/v2/",
        className = "no.nav.tjeneste.virksomhet.kodeverk.v2.FinnKodeverkListeResponse"
    )
    override fun finnKodeverkListe(@WebParam(name = "respons") request: FinnKodeverkListeRequest) =
        throw NotImplementedException()
}
