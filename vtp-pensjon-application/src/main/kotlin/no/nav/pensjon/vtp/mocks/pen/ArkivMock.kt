package no.nav.pensjon.vtp.mocks.pen

import no.nav.pensjon.vtp.annotations.SoapService
import no.nav.virksomhet.tjenester.arkiv.feil.v1.ObjectFactory
import no.nav.virksomhet.tjenester.arkiv.meldinger.v1.BestillBrevRequest
import no.nav.virksomhet.tjenester.arkiv.meldinger.v1.BestillBrevResponse
import no.nav.virksomhet.tjenester.arkiv.v1.Arkiv
import javax.jws.*
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-tjeneste-arkiv_v1Web/sca/ArkivWSEXP"])
@WebService(targetNamespace = "http://nav.no/virksomhet/tjenester/arkiv/v1", name = "Arkiv")
@XmlSeeAlso(
    ObjectFactory::class,
    no.nav.virksomhet.tjenester.arkiv.meldinger.v1.ObjectFactory::class,
    no.nav.virksomhet.tjenester.felles.v1.ObjectFactory::class,
    no.nav.virksomhet.tjenester.arkiv.v1.ObjectFactory::class
)
@HandlerChain(file = "/Handler-chain.xml")
class ArkivMock : Arkiv {
    @WebMethod
    @RequestWrapper(
        localName = "bestillBrev",
        targetNamespace = "http://nav.no/virksomhet/tjenester/arkiv/v1",
        className = "no.nav.virksomhet.tjenester.arkiv.v1.BestillBrev"
    )
    @ResponseWrapper(
        localName = "bestillBrevResponse",
        targetNamespace = "http://nav.no/virksomhet/tjenester/arkiv/v1",
        className = "no.nav.virksomhet.tjenester.arkiv.v1.BestillBrevResponse"
    )
    @WebResult(name = "response")
    override fun bestillBrev(@WebParam(name = "request") request: BestillBrevRequest): BestillBrevResponse =
        BestillBrevResponse().apply {
            journalpostId = "0001"
        }
}
