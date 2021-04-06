package no.nav.pensjon.vtp.mocks.virksomhet.organisasjon.v4

import no.nav.pensjon.vtp.annotations.SoapService
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonRepository
import no.nav.tjeneste.virksomhet.organisasjon.v4.binding.HentNoekkelinfoOrganisasjonUgyldigInput
import no.nav.tjeneste.virksomhet.organisasjon.v4.binding.HentOrganisasjonUgyldigInput
import no.nav.tjeneste.virksomhet.organisasjon.v4.binding.OrganisasjonV4
import no.nav.tjeneste.virksomhet.organisasjon.v4.feil.UgyldigInput
import no.nav.tjeneste.virksomhet.organisasjon.v4.meldinger.*
import javax.jws.*
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper
import javax.xml.ws.soap.Addressing

@SoapService(path = ["/ereg/ws/OrganisasjonService/v4"])
@Addressing
@WebService(name = "Organisasjon_v4", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4")
@HandlerChain(file = "/Handler-chain.xml")
class OrganisasjonV4MockImpl(private val organisasjonRepository: OrganisasjonRepository) : OrganisasjonV4 {
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjon/v4/Organisasjon_v4/finnOrganisasjonRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "finnOrganisasjon",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
        className = "no.nav.tjeneste.virksomhet.organisasjon.v4.FinnOrganisasjon"
    )
    @ResponseWrapper(
        localName = "finnOrganisasjonResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
        className = "no.nav.tjeneste.virksomhet.organisasjon.v4.FinnOrganisasjonResponse"
    )
    override fun finnOrganisasjon(@WebParam(name = "request") finnOrganisasjonRequest: FinnOrganisasjonRequest) =
        throw NotImplementedException()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjon/v4/Organisasjon_v4/pingRequest")
    @RequestWrapper(
        localName = "ping",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
        className = "no.nav.tjeneste.virksomhet.organisasjon.v4.Ping"
    )
    @ResponseWrapper(
        localName = "pingResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
        className = "no.nav.tjeneste.virksomhet.organisasjon.v4.PingResponse"
    )
    override fun ping() = Unit

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjon/v4/Organisasjon_v4/hentOrganisasjonsnavnBolkRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "hentOrganisasjonsnavnBolk",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
        className = "no.nav.tjeneste.virksomhet.organisasjon.v4.HentOrganisasjonsnavnBolk"
    )
    @ResponseWrapper(
        localName = "hentOrganisasjonsnavnBolkResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
        className = "no.nav.tjeneste.virksomhet.organisasjon.v4.HentOrganisasjonsnavnBolkResponse"
    )
    override fun hentOrganisasjonsnavnBolk(@WebParam(name = "request") hentOrganisasjonsnavnBolkRequest: HentOrganisasjonsnavnBolkRequest) =
        throw NotImplementedException()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjon/v4/Organisasjon_v4/hentOrganisasjonRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "hentOrganisasjon",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
        className = "no.nav.tjeneste.virksomhet.organisasjon.v4.HentOrganisasjon"
    )
    @ResponseWrapper(
        localName = "hentOrganisasjonResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
        className = "no.nav.tjeneste.virksomhet.organisasjon.v4.HentOrganisasjonResponse"
    )
    @Throws(
        HentOrganisasjonUgyldigInput::class
    )
    override fun hentOrganisasjon(@WebParam(name = "request") request: HentOrganisasjonRequest) =
        request.orgnummer
            ?.let { orgnummer ->
                HentOrganisasjonResponse().apply {
                    organisasjon = organisasjonRepository.findById(orgnummer)
                        ?.let { mapOrganisasjonFraModell(it) }
                }
            }
            ?: throw HentOrganisasjonUgyldigInput(
                "Orgnummer ikke angitt",
                UgyldigInput()
            )

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjon/v4/Organisasjon_v4/hentNoekkelinfoOrganisasjonRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "hentNoekkelinfoOrganisasjon",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
        className = "no.nav.tjeneste.virksomhet.organisasjon.v4.HentNoekkelinfoOrganisasjon"
    )
    @ResponseWrapper(
        localName = "hentNoekkelinfoOrganisasjonResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
        className = "no.nav.tjeneste.virksomhet.organisasjon.v4.HentNoekkelinfoOrganisasjonResponse"
    )
    @Throws(
        HentNoekkelinfoOrganisasjonUgyldigInput::class
    )
    override fun hentNoekkelinfoOrganisasjon(@WebParam(name = "request") request: HentNoekkelinfoOrganisasjonRequest) =
        request.orgnummer
            ?.let {
                lagHentNoekkelinfoOrganisasjonResponse(it)
            }
            ?: throw HentNoekkelinfoOrganisasjonUgyldigInput(
                "Orgnummer ikke angitt", UgyldigInput()
            )

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjon/v4/Organisasjon_v4/validerOrganisasjonRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "validerOrganisasjon",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
        className = "no.nav.tjeneste.virksomhet.organisasjon.v4.ValiderOrganisasjon"
    )
    @ResponseWrapper(
        localName = "validerOrganisasjonResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
        className = "no.nav.tjeneste.virksomhet.organisasjon.v4.ValiderOrganisasjonResponse"
    )
    override fun validerOrganisasjon(@WebParam(name = "request") request: ValiderOrganisasjonRequest) =
        throw NotImplementedException()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjon/v4/Organisasjon_v4/hentVirksomhetsOrgnrForJuridiskOrgnrBolkRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "hentVirksomhetsOrgnrForJuridiskOrgnrBolk",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
        className = "no.nav.tjeneste.virksomhet.organisasjon.v4.HentVirksomhetsOrgnrForJuridiskOrgnrBolk"
    )
    @ResponseWrapper(
        localName = "hentVirksomhetsOrgnrForJuridiskOrgnrBolkResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
        className = "no.nav.tjeneste.virksomhet.organisasjon.v4.HentVirksomhetsOrgnrForJuridiskOrgnrBolkResponse"
    )
    override fun hentVirksomhetsOrgnrForJuridiskOrgnrBolk(@WebParam(name = "request") hentVirksomhetsOrgnrForJuridiskOrgnrBolkRequest: HentVirksomhetsOrgnrForJuridiskOrgnrBolkRequest) =
        throw NotImplementedException()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjon/v4/Organisasjon_v4/finnOrganisasjonsendringerListeRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "finnOrganisasjonsendringerListe",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
        className = "no.nav.tjeneste.virksomhet.organisasjon.v4.FinnOrganisasjonsendringerListe"
    )
    @ResponseWrapper(
        localName = "finnOrganisasjonsendringerListeResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
        className = "no.nav.tjeneste.virksomhet.organisasjon.v4.FinnOrganisasjonsendringerListeResponse"
    )
    override fun finnOrganisasjonsendringerListe(@WebParam(name = "request") finnOrganisasjonsendringerListeRequest: FinnOrganisasjonsendringerListeRequest) =
        throw NotImplementedException()
}
