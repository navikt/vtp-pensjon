package no.nav.pensjon.vtp.mocks.organisasjonenhet.v2

import no.nav.pensjon.vtp.annotations.SoapService
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import no.nav.tjeneste.virksomhet.organisasjonenhet.v2.ObjectFactory
import no.nav.tjeneste.virksomhet.organisasjonenhet.v2.OrganisasjonEnhetV2
import no.nav.tjeneste.virksomhet.organisasjonenhet.v2.informasjon.WSOrganisasjonsenhet
import no.nav.tjeneste.virksomhet.organisasjonenhet.v2.meldinger.*
import javax.jws.*
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/norg2/ws/OrganisasjonEnhet/v2"])
@WebService(name = "OrganisasjonEnhet_v2", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjonEnhet/v2/")
@HandlerChain(file = "/Handler-chain.xml")
@XmlSeeAlso(
    ObjectFactory::class,
    no.nav.tjeneste.virksomhet.organisasjonenhet.v2.feil.ObjectFactory::class,
    no.nav.tjeneste.virksomhet.organisasjonenhet.v2.informasjon.ObjectFactory::class,
    no.nav.tjeneste.virksomhet.organisasjonenhet.v2.meldinger.ObjectFactory::class
)
class OrganisasjonEnhetMock : OrganisasjonEnhetV2 {
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjonEnhet/v2/OrganisasjonEnhet_v2/finnNAVKontorRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "finnNAVKontor",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjonEnhet/v2/",
        className = "no.nav.tjeneste.virksomhet.organisasjonenhet.v2.FinnNAVKontor"
    )
    @ResponseWrapper(
        localName = "finnNAVKontorResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjonEnhet/v2/",
        className = "no.nav.tjeneste.virksomhet.organisasjonenhet.v2.FinnNAVKontorResponse"
    )
    override fun finnNAVKontor(
        @WebParam(name = "request") request: WSFinnNAVKontorRequest
    ) = WSFinnNAVKontorResponse().apply {
        navKontor = WSOrganisasjonsenhet().apply {
            enhetId = "4407"
            enhetNavn = "NAV Arbeid og ytelser Tønsberg"
        }
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjonEnhet/v2/OrganisasjonEnhet_v2/hentEnhetBolkRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "hentEnhetBolk",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjonEnhet/v2/",
        className = "no.nav.tjeneste.virksomhet.organisasjonenhet.v2.HentEnhetBolk"
    )
    @ResponseWrapper(
        localName = "hentEnhetBolkResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjonEnhet/v2/",
        className = "no.nav.tjeneste.virksomhet.organisasjonenhet.v2.HentEnhetBolkResponse"
    )
    override fun hentEnhetBolk(
        @WebParam(name = "request") request: WSHentEnhetBolkRequest
    ) = throw NotImplementedException()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjonEnhet/v2/OrganisasjonEnhet_v2/hentFullstendigEnhetListeRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "hentFullstendigEnhetListe",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjonEnhet/v2/",
        className = "no.nav.tjeneste.virksomhet.organisasjonenhet.v2.HentFullstendigEnhetListe"
    )
    @ResponseWrapper(
        localName = "hentFullstendigEnhetListeResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjonEnhet/v2/",
        className = "no.nav.tjeneste.virksomhet.organisasjonenhet.v2.HentFullstendigEnhetListeResponse"
    )
    override fun hentFullstendigEnhetListe(
        @WebParam(name = "request") request: WSHentFullstendigEnhetListeRequest
    ) = throw NotImplementedException()

    override fun ping() = Unit

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjonEnhet/v2/OrganisasjonEnhet_v2/hentOverordnetEnhetListeRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "hentOverordnetEnhetListe",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjonEnhet/v2/",
        className = "no.nav.tjeneste.virksomhet.organisasjonenhet.v2.HentOverordnetEnhetListe"
    )
    @ResponseWrapper(
        localName = "hentOverordnetEnhetListeResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjonEnhet/v2/",
        className = "no.nav.tjeneste.virksomhet.organisasjonenhet.v2.HentOverordnetEnhetListeResponse"
    )
    override fun hentOverordnetEnhetListe(
        @WebParam(name = "request") request: WSHentOverordnetEnhetListeRequest
    ) = throw NotImplementedException()
}
