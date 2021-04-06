package no.nav.pensjon.vtp.mocks.psak

import no.nav.pensjon.vtp.core.annotations.SoapService
import no.nav.virksomhet.tjenester.sak.meldinger.v1.FinnGenerellSakListeRequest
import no.nav.virksomhet.tjenester.sak.meldinger.v1.FinnGenerellSakListeResponse
import no.nav.virksomhet.tjenester.sak.v1.ObjectFactory
import no.nav.virksomhet.tjenester.sak.v1.Sak
import javax.jws.HandlerChain
import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult
import javax.jws.WebService
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-tjeneste-sak_v1Web/sca/SakWSEXP"])
@WebService(targetNamespace = "http://nav.no/virksomhet/tjenester/sak/v1", name = "Sak")
@XmlSeeAlso(
    ObjectFactory::class,
    no.nav.virksomhet.tjenester.sak.meldinger.v1.ObjectFactory::class,
    no.nav.virksomhet.gjennomforing.sak.v1.ObjectFactory::class
)
@HandlerChain(file = "/Handler-chain.xml")
class SakMock : Sak {
    @WebMethod(action = "http://nav.no/virksomhet/tjenester/sak/v1/Sak/finnGenerellSakListeRequest")
    @RequestWrapper(
        localName = "finnGenerellSakListe",
        targetNamespace = "http://nav.no/virksomhet/tjenester/sak/v1",
        className = "no.nav.virksomhet.tjenester.sak.v1.FinnGenerellSakListe"
    )
    @ResponseWrapper(
        localName = "finnGenerellSakListeResponse",
        targetNamespace = "http://nav.no/virksomhet/tjenester/sak/v1",
        className = "no.nav.virksomhet.tjenester.sak.v1.FinnGenerellSakListeResponse"
    )
    @WebResult(name = "response")
    override fun finnGenerellSakListe(
        @WebParam(name = "request") request: FinnGenerellSakListeRequest
    ) = FinnGenerellSakListeResponse()
}
