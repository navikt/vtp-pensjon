package no.nav.pensjon.vtp.mocks.psak

import no.nav.pensjon.vtp.core.annotations.SoapService
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import no.nav.virksomhet.tjenester.medlemskap.meldinger.v1.HentPeriodeListeRequest
import no.nav.virksomhet.tjenester.medlemskap.meldinger.v1.HentPeriodeListeResponse
import no.nav.virksomhet.tjenester.medlemskap.meldinger.v1.HentPeriodeRequest
import no.nav.virksomhet.tjenester.medlemskap.v1.Medlemskap
import no.nav.virksomhet.tjenester.medlemskap.v1.ObjectFactory
import javax.jws.*
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-tjeneste-medlemskap_v1Web/sca/MedlemskapWSEXP"])
@WebService(targetNamespace = "http://nav.no/virksomhet/tjenester/medlemskap/v1", name = "Medlemskap")
@XmlSeeAlso(
    ObjectFactory::class,
    no.nav.virksomhet.tjenester.medlemskap.feil.v1.ObjectFactory::class,
    no.nav.virksomhet.tjenester.medlemskap.meldinger.v1.ObjectFactory::class,
    no.nav.virksomhet.tjenester.felles.v1.ObjectFactory::class,
    no.nav.virksomhet.grunnlag.medlemskap.v1.ObjectFactory::class
)
@HandlerChain(file = "/Handler-chain.xml")
class MedlemskapMock : Medlemskap {
    /**
     *
     * Operasjonen skal tilby å hente en liste med medlemskapsperioder til en person.
     */
    @WebMethod
    @RequestWrapper(
        localName = "hentPeriodeListe",
        targetNamespace = "http://nav.no/virksomhet/tjenester/medlemskap/v1",
        className = "no.nav.virksomhet.tjenester.medlemskap.v1.HentPeriodeListe"
    )
    @ResponseWrapper(
        localName = "hentPeriodeListeResponse",
        targetNamespace = "http://nav.no/virksomhet/tjenester/medlemskap/v1",
        className = "no.nav.virksomhet.tjenester.medlemskap.v1.HentPeriodeListeResponse"
    )
    @WebResult(name = "response")
    override fun hentPeriodeListe(
        @WebParam(name = "request") request: HentPeriodeListeRequest
    ) = HentPeriodeListeResponse()

    /**
     *
     * Operasjonen skal tilby å hente en medlemskapsperiode.
     */
    override fun hentPeriode(request: HentPeriodeRequest) = throw NotImplementedException()
}
