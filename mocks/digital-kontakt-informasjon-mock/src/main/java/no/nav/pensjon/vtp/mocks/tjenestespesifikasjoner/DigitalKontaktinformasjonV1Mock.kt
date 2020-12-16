package no.nav.pensjon.vtp.mocks.tjenestespesifikasjoner

import no.nav.pensjon.vtp.core.annotations.SoapService
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.DigitalKontaktinformasjonV1
import no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.ObjectFactory
import no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.informasjon.WSEpostadresse
import no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.informasjon.WSKontaktinformasjon
import no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.informasjon.WSMobiltelefonnummer
import no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.meldinger.WSHentDigitalKontaktinformasjonBolkRequest
import no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.meldinger.WSHentDigitalKontaktinformasjonBolkResponse
import no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.meldinger.WSHentDigitalKontaktinformasjonRequest
import no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.meldinger.WSHentDigitalKontaktinformasjonResponse
import no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.meldinger.WSHentPrintsertifikatRequest
import no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.meldinger.WSHentSikkerDigitalPostadresseBolkRequest
import no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.meldinger.WSHentSikkerDigitalPostadresseRequest
import no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.meldinger.WSHentSikkerDigitalPostadresseResponse
import javax.jws.HandlerChain
import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult
import javax.jws.WebService
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/ws/DigitalKontaktinformasjon/v1"])
@WebService(
    targetNamespace = "http://nav.no/tjeneste/virksomhet/digitalKontaktinformasjon/v1",
    name = "DigitalKontaktinformasjon_v1"
)
@XmlSeeAlso(
    ObjectFactory::class,
    no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.informasjon.ObjectFactory::class,
    no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.feil.ObjectFactory::class,
    no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.meldinger.ObjectFactory::class
)
@HandlerChain(file = "/Handler-chain.xml")
class DigitalKontaktinformasjonV1Mock : DigitalKontaktinformasjonV1 {
    override fun hentSikkerDigitalPostadresseBolk(wsHentSikkerDigitalPostadresseBolkRequest: WSHentSikkerDigitalPostadresseBolkRequest) =
        throw NotImplementedException()

    override fun hentPrintsertifikat(wsHentPrintsertifikatRequest: WSHentPrintsertifikatRequest) =
        throw NotImplementedException()

    @WebMethod(
        operationName = "HentDigitalKontaktinformasjon",
        action = "http://nav.no/tjeneste/virksomhet/digitalKontaktinformasjon/v1/DigitalKontaktinformasjon_v1/HentDigitalKontaktinformasjonRequest"
    )
    @RequestWrapper(
        localName = "HentDigitalKontaktinformasjon",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/digitalKontaktinformasjon/v1",
        className = "no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.HentDigitalKontaktinformasjon"
    )
    @ResponseWrapper(
        localName = "HentDigitalKontaktinformasjonResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/digitalKontaktinformasjon/v1",
        className = "no.nav.tjeneste.virksomhet.digitalkontaktinformasjon.v1.HentDigitalKontaktinformasjonResponse"
    )
    @WebResult(name = "response")
    override fun hentDigitalKontaktinformasjon(
        @WebParam(name = "request") request: WSHentDigitalKontaktinformasjonRequest
    ) = WSHentDigitalKontaktinformasjonResponse().apply {
        digitalKontaktinformasjon = WSKontaktinformasjon().apply {
            epostadresse = WSEpostadresse().withValue("example@com")
            mobiltelefonnummer = WSMobiltelefonnummer().withValue("11223211")
        }
    }

    override fun ping() = Unit

    override fun hentDigitalKontaktinformasjonBolk(wsHentDigitalKontaktinformasjonBolkRequest: WSHentDigitalKontaktinformasjonBolkRequest): WSHentDigitalKontaktinformasjonBolkResponse {
        throw NotImplementedException()
    }

    override fun hentSikkerDigitalPostadresse(wsHentSikkerDigitalPostadresseRequest: WSHentSikkerDigitalPostadresseRequest): WSHentSikkerDigitalPostadresseResponse {
        throw NotImplementedException()
    }
}
