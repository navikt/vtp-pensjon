package no.nav.pensjon.vtp.mocks.virksomhet.aktoer.v2

import no.nav.pensjon.vtp.annotations.SoapService
import no.nav.pensjon.vtp.util.asXMLGregorianCalendar
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository
import no.nav.tjeneste.virksomhet.aktoer.v2.binding.AktoerV2
import no.nav.tjeneste.virksomhet.aktoer.v2.binding.HentAktoerIdForIdentPersonIkkeFunnet
import no.nav.tjeneste.virksomhet.aktoer.v2.binding.HentIdentForAktoerIdPersonIkkeFunnet
import no.nav.tjeneste.virksomhet.aktoer.v2.feil.PersonIkkeFunnet
import no.nav.tjeneste.virksomhet.aktoer.v2.meldinger.*
import java.time.LocalDate
import javax.jws.HandlerChain
import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult
import javax.jws.WebService
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper
import javax.xml.ws.soap.Addressing
import kotlin.Throws

@SoapService(path = ["erregister/ws/Aktoer/v2"])
@Addressing
@WebService(name = "Aktoer_v2", targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2")
@HandlerChain(file = "/Handler-chain.xml")
class AktoerServiceMockImpl(private val personModellRepository: PersonModellRepository) : AktoerV2 {
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/aktoer/v2/Aktoer_v2/hentIdentForAktoerIdRequest")
    @WebResult(name = "hentIdentForAktoerIdResponse")
    @RequestWrapper(
        localName = "hentIdentForAktoerId",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2",
        className = "no.nav.tjeneste.virksomhet.aktoer.v2.HentIdentForAktoerId"
    )
    @ResponseWrapper(
        localName = "hentIdentForAktoerIdResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2",
        className = "no.nav.tjeneste.virksomhet.aktoer.v2.HentIdentForAktoerIdResponse"
    )
    @Throws(
        HentIdentForAktoerIdPersonIkkeFunnet::class
    )
    override fun hentIdentForAktoerId(
        @WebParam(name = "hentIdentForAktoerIdRequest") request: HentIdentForAktoerIdRequest
    ) = HentIdentForAktoerIdResponse().apply {
        ident = personModellRepository.findByAktørIdent(request.aktoerId)
            ?.ident
            ?: throw HentIdentForAktoerIdPersonIkkeFunnet(
                "Fant ingen ident for aktoerid: " + request.aktoerId,
                PersonIkkeFunnet()
            )
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/aktoer/v2/Aktoer_v2/hentAktoerIdForIdentRequest")
    @WebResult(name = "hentAktoerIdForIdentResponse")
    @RequestWrapper(
        localName = "hentAktoerIdForIdent",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2",
        className = "no.nav.tjeneste.virksomhet.aktoer.v2.HentAktoerIdForIdent"
    )
    @ResponseWrapper(
        localName = "hentAktoerIdForIdentResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2",
        className = "no.nav.tjeneste.virksomhet.aktoer.v2.HentAktoerIdForIdentResponse"
    )
    @Throws(
        HentAktoerIdForIdentPersonIkkeFunnet::class
    )
    override fun hentAktoerIdForIdent(
        @WebParam(name = "hentAktoerIdForIdentRequest") request: HentAktoerIdForIdentRequest
    ) = HentAktoerIdForIdentResponse().apply {
        aktoerId = personModellRepository.findById(request.ident)
            ?.aktørIdent
            ?: throw HentAktoerIdForIdentPersonIkkeFunnet(
                "Fant ingen aktoerid for ident: " + request.ident,
                PersonIkkeFunnet()
            )
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/aktoer/v2/Aktoer_v2/hentAktoerIdForIdentListeRequest")
    @WebResult(name = "hentAktoerIdForIdentListeResponse")
    @RequestWrapper(
        localName = "hentAktoerIdForIdentListe",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2",
        className = "no.nav.tjeneste.virksomhet.aktoer.v2.HentAktoerIdForIdentListe"
    )
    @ResponseWrapper(
        localName = "hentAktoerIdForIdentListeResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2",
        className = "no.nav.tjeneste.virksomhet.aktoer.v2.HentAktoerIdForIdentListeResponse"
    )
    override fun hentAktoerIdForIdentListe(
        @WebParam(name = "hentAktoerIdForIdentListeRequest") hentAktoerIdForIdentListeRequest: HentAktoerIdForIdentListeRequest
    ): HentAktoerIdForIdentListeResponse {
        val identTilAktoerId = hentAktoerIdForIdentListeRequest.identListe
            .map { it to personModellRepository.findById(it)?.aktørIdent }
            .toMap()

        val response = HentAktoerIdForIdentListeResponse()

        identTilAktoerId.forEach { (ident: String, aktoerId: String?) ->
            if (aktoerId == null) {
                response.feilListe += Feil().apply {
                    feilBeskrivelse = "Fant ikke aktørId for ident=$ident"
                    feilKode = "<dummy kode>"
                }
            } else {
                val aktoerIder = AktoerIder().apply {
                    gjeldendeIdent = IdentDetaljer().apply {
                        datoFom = LocalDate.now().minusYears(1).asXMLGregorianCalendar()
                        tpsId = "Paakrevd-tulle-id"
                    }
                }
                aktoerIder.aktoerId = aktoerId
                response.aktoerListe += aktoerIder
            }
        }

        return response
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/aktoer/v2/Aktoer_v2/hentIdentForAktoerIdListeRequest")
    @WebResult(name = "hentIdentForAktoerIdListeResponse")
    @RequestWrapper(
        localName = "hentIdentForAktoerIdListe",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2",
        className = "no.nav.tjeneste.virksomhet.aktoer.v2.HentIdentForAktoerIdListe"
    )
    @ResponseWrapper(
        localName = "hentIdentForAktoerIdListeResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2",
        className = "no.nav.tjeneste.virksomhet.aktoer.v2.HentIdentForAktoerIdListeResponse"
    )
    override fun hentIdentForAktoerIdListe(
        @WebParam(name = "hentIdentForAktoerIdListeRequest") hentIdentForAktoerIdListeRequest: HentIdentForAktoerIdListeRequest
    ): HentIdentForAktoerIdListeResponse = throw NotImplementedException()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/aktoer/v2/Aktoer_v2/pingRequest")
    @RequestWrapper(
        localName = "ping",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2",
        className = "no.nav.tjeneste.virksomhet.aktoer.v2.Ping"
    )
    @ResponseWrapper(
        localName = "pingResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/aktoer/v2",
        className = "no.nav.tjeneste.virksomhet.aktoer.v2.PingResponse"
    )
    override fun ping() = Unit
}
