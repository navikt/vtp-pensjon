package no.nav.pensjon.vtp.mocks.virksomhet.medlemskap.v2

import no.nav.pensjon.vtp.core.annotations.SoapService
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository
import no.nav.tjeneste.virksomhet.medlemskap.v2.MedlemskapV2
import no.nav.tjeneste.virksomhet.medlemskap.v2.PersonIkkeFunnet
import no.nav.tjeneste.virksomhet.medlemskap.v2.Sikkerhetsbegrensning
import no.nav.tjeneste.virksomhet.medlemskap.v2.meldinger.HentPeriodeListeRequest
import no.nav.tjeneste.virksomhet.medlemskap.v2.meldinger.HentPeriodeListeResponse
import no.nav.tjeneste.virksomhet.medlemskap.v2.meldinger.HentPeriodeRequest
import javax.jws.*
import javax.xml.ws.Action
import javax.xml.ws.FaultAction
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper
import javax.xml.ws.soap.Addressing

@SoapService(path = ["/medl2/ws/Medlemskap/v2"])
@Addressing
@WebService(name = "Medlemskap_v2", targetNamespace = "http://nav.no/tjeneste/virksomhet/medlemskap/v2")
@HandlerChain(file = "/Handler-chain.xml")
class MedlemServiceMockImpl(private val personModellRepository: PersonModellRepository) : MedlemskapV2 {
    @WebMethod
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(
        localName = "hentPeriode",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/medlemskap/v2",
        className = "no.nav.tjeneste.virksomhet.medlemskap.v2.HentPeriode"
    )
    @ResponseWrapper(
        localName = "hentPeriodeResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/medlemskap/v2",
        className = "no.nav.tjeneste.virksomhet.medlemskap.v2.HentPeriodeResponse"
    )
    @Action(
        input = "http://nav.no/tjeneste/virksomhet/medlemskap/v2/Medlemskap_v2/hentPeriodeRequest",
        output = "http://nav.no/tjeneste/virksomhet/medlemskap/v2/Medlemskap_v2/hentPeriodeResponse",
        fault = [
            FaultAction(
                className = Sikkerhetsbegrensning::class,
                value = "http://nav.no/tjeneste/virksomhet/medlemskap/v2/Medlemskap_v2/hentPeriode/Fault/Sikkerhetsbegrensning"
            )
        ]
    )
    override fun hentPeriode(
        @WebParam(
            name = "request",
            targetNamespace = ""
        ) request: HentPeriodeRequest
    ) = throw NotImplementedException()

    @WebMethod
    @RequestWrapper(
        localName = "ping",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/medlemskap/v2",
        className = "no.nav.tjeneste.virksomhet.medlemskap.v2.Ping"
    )
    @ResponseWrapper(
        localName = "pingResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/medlemskap/v2",
        className = "no.nav.tjeneste.virksomhet.medlemskap.v2.PingResponse"
    )
    @Action(
        input = "http://nav.no/tjeneste/virksomhet/medlemskap/v2/Medlemskap_v2/pingRequest",
        output = "http://nav.no/tjeneste/virksomhet/medlemskap/v2/Medlemskap_v2/pingResponse"
    )
    override fun ping() = Unit

    @WebMethod
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(
        localName = "hentPeriodeListe",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/medlemskap/v2",
        className = "no.nav.tjeneste.virksomhet.medlemskap.v2.HentPeriodeListe"
    )
    @ResponseWrapper(
        localName = "hentPeriodeListeResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/medlemskap/v2",
        className = "no.nav.tjeneste.virksomhet.medlemskap.v2.HentPeriodeListeResponse"
    )
    @Action(
        input = "http://nav.no/tjeneste/virksomhet/medlemskap/v2/Medlemskap_v2/hentPeriodeListeRequest",
        output = "http://nav.no/tjeneste/virksomhet/medlemskap/v2/Medlemskap_v2/hentPeriodeListeResponse",
        fault = [
            FaultAction(
                className = PersonIkkeFunnet::class,
                value = "http://nav.no/tjeneste/virksomhet/medlemskap/v2/Medlemskap_v2/hentPeriodeListe/Fault/PersonIkkeFunnet"
            ), FaultAction(
                className = Sikkerhetsbegrensning::class,
                value = "http://nav.no/tjeneste/virksomhet/medlemskap/v2/Medlemskap_v2/hentPeriodeListe/Fault/Sikkerhetsbegrensning"
            )
        ]
    )
    override fun hentPeriodeListe(
        @WebParam(
            name = "request",
            targetNamespace = ""
        ) request: HentPeriodeListeRequest
    ): HentPeriodeListeResponse = request.ident
        ?.let {
            return HentPeriodeListeResponse()
                .withPeriodeListe(
                    finnMedlemsperioder(it.value)
                )
        }
        ?: throw IllegalArgumentException("Ident i request mangler")

    private fun finnMedlemsperioder(personIdent: String) = personModellRepository.findById(personIdent)
        ?.medlemskap?.perioder
        ?.map { tilMedlemsperiode(it) }
        ?: emptyList()
}
