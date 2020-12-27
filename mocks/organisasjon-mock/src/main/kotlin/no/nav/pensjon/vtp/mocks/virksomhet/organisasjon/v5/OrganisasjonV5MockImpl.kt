package no.nav.pensjon.vtp.mocks.virksomhet.organisasjon.v5

import no.nav.pensjon.vtp.core.annotations.SoapService
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonRepository
import no.nav.tjeneste.virksomhet.organisasjon.v5.binding.HentOrganisasjonUgyldigInput
import no.nav.tjeneste.virksomhet.organisasjon.v5.binding.OrganisasjonV5
import no.nav.tjeneste.virksomhet.organisasjon.v5.feil.UgyldigInput
import no.nav.tjeneste.virksomhet.organisasjon.v5.meldinger.*
import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult
import javax.jws.WebService
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/ereg/ws/OrganisasjonService/v5"])
@WebService(name = "Organisasjon_v5", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v5")
class OrganisasjonV5MockImpl(private val organisasjonRepository: OrganisasjonRepository) : OrganisasjonV5 {
    override fun ping() = Unit

    override fun finnOrganisasjon(finnOrganisasjonRequest: FinnOrganisasjonRequest) =
        throw NotImplementedException()

    override fun hentOrganisasjonsnavnBolk(hentOrganisasjonsnavnBolkRequest: HentOrganisasjonsnavnBolkRequest) =
        throw NotImplementedException()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjon/v5/BindinghentOrganisasjon/")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "hentOrganisasjon",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v5",
        className = "no.nav.tjeneste.virksomhet.organisasjon.v5.HentOrganisasjon"
    )
    @ResponseWrapper(
        localName = "hentOrganisasjonResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v5",
        className = "no.nav.tjeneste.virksomhet.organisasjon.v5.HentOrganisasjonResponse"
    )
    override fun hentOrganisasjon(@WebParam(name = "request") request: HentOrganisasjonRequest): HentOrganisasjonResponse {
        return request.orgnummer
            ?.let {
                HentOrganisasjonResponse().apply {
                    organisasjon = organisasjonRepository.findById(request.orgnummer)
                        ?.let {
                            mapOrganisasjonFraModell(it)
                        }
                }
            }
            ?: throw HentOrganisasjonUgyldigInput(
                "Orgnummer ikke angitt",
                UgyldigInput()
            )
    }

    override fun hentNoekkelinfoOrganisasjon(hentNoekkelinfoOrganisasjonRequest: HentNoekkelinfoOrganisasjonRequest) =
        throw NotImplementedException()

    override fun validerOrganisasjon(validerOrganisasjonRequest: ValiderOrganisasjonRequest) =
        throw NotImplementedException()

    override fun hentVirksomhetsOrgnrForJuridiskOrgnrBolk(hentVirksomhetsOrgnrForJuridiskOrgnrBolkRequest: HentVirksomhetsOrgnrForJuridiskOrgnrBolkRequest) =
        throw NotImplementedException()

    override fun finnOrganisasjonsendringerListe(finnOrganisasjonsendringerListeRequest: FinnOrganisasjonsendringerListeRequest) =
        throw NotImplementedException()
}
