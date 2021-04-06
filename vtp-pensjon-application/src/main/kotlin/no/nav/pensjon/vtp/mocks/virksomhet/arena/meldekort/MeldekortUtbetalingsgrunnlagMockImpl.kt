package no.nav.pensjon.vtp.mocks.virksomhet.arena.meldekort

import no.nav.pensjon.vtp.core.annotations.SoapService
import no.nav.pensjon.vtp.core.util.asXMLGregorianCalendar
import no.nav.pensjon.vtp.mocks.virksomhet.arena.meldekort.modell.ArenaMUMapper
import no.nav.pensjon.vtp.testmodell.FeilKode
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.binding.FinnMeldekortUtbetalingsgrunnlagListeAktoerIkkeFunnet
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.binding.FinnMeldekortUtbetalingsgrunnlagListeSikkerhetsbegrensning
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.binding.FinnMeldekortUtbetalingsgrunnlagListeUgyldigInput
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.binding.MeldekortUtbetalingsgrunnlagV1
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.informasjon.AktoerId
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.meldinger.FinnMeldekortUtbetalingsgrunnlagListeRequest
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.meldinger.FinnMeldekortUtbetalingsgrunnlagListeResponse
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.meldinger.ObjectFactory
import java.time.LocalDateTime
import javax.jws.*
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper
import javax.xml.ws.soap.Addressing
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.feil.ObjectFactory as FeilObjectFactory

@SoapService(path = ["/ail_ws/MeldekortUtbetalingsgrunnlag_v1"])
@Addressing
@WebService(endpointInterface = "no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.binding.MeldekortUtbetalingsgrunnlagV1")
@HandlerChain(file = "/Handler-chain.xml")
class MeldekortUtbetalingsgrunnlagMockImpl(private val inntektYtelseIndeks: InntektYtelseIndeks) :
    MeldekortUtbetalingsgrunnlagV1 {
    private val objectFactory = ObjectFactory()
    private val feilObjectFactory = FeilObjectFactory()
    private val arenaMapper = ArenaMUMapper()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/meldekortutbetalingsgrunnlag/v1/meldekortutbetalingsgrunnlag_v1/FinnMeldekortUtbetalingsgrunnlagListeRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "finnMeldekortUtbetalingsgrunnlagListe",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/meldekortutbetalingsgrunnlag/v1",
        className = "no.nav.tjeneste.virksomhet.arena.meldekort.finnMeldekortUtbetalingsgrunnlagListe"
    )
    @ResponseWrapper(
        localName = "finnMeldekortUtbetalingsgrunnlagListeResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/ytelseskontrakt/v1",
        className = "no.nav.tjeneste.virksomhet.arena.meldekort.finnMeldekortUtbetalingsgrunnlagListeResponse"
    )
    override fun finnMeldekortUtbetalingsgrunnlagListe(@WebParam(name = "request") finnMeldekortUtbetalingsgrunnlagListeRequest: FinnMeldekortUtbetalingsgrunnlagListeRequest): FinnMeldekortUtbetalingsgrunnlagListeResponse {
        val ident = finnMeldekortUtbetalingsgrunnlagListeRequest.ident
        val aktoerId = if (ident is AktoerId) {
            ident.aktoerId
        } else {
            null
        }

        if (aktoerId == null) {
            val faultInfo = lagUgyldigInput(null)
            throw FinnMeldekortUtbetalingsgrunnlagListeUgyldigInput(faultInfo.feilmelding, faultInfo)
        }

        inntektYtelseIndeks.getInntektYtelseModellFraAktørId(aktoerId)
            ?.let { inntektYtelseModell ->
                val arenaModell = inntektYtelseModell.arenaModell

                arenaModell?.feilkode
                    ?.let { haandterExceptions(it, aktoerId) }

                return arenaMapper.mapArenaSaker(
                    finnMeldekortUtbetalingsgrunnlagListeRequest,
                    arenaModell?.saker ?: emptyList()
                )
            }
            ?: return objectFactory.createFinnMeldekortUtbetalingsgrunnlagListeResponse()
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/meldekortutbetalingsgrunnlag/v1/meldekortutbetalingsgrunnlag_v1/pingRequest")
    @RequestWrapper(
        localName = "ping",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/meldekortutbetalingsgrunnlag/v1",
        className = "no.nav.tjeneste.virksomhet.arena.meldekort.Ping"
    )
    @ResponseWrapper(
        localName = "pingResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/meldekortutbetalingsgrunnlag/v1",
        className = "no.nav.tjeneste.virksomhet.arena.meldekort.PingResponse"
    )
    override fun ping() = Unit

    private fun haandterExceptions(kode: FeilKode, ident: String): Nothing = when (kode) {
        FeilKode.UgyldigInput -> {
            val faultInfo = lagUgyldigInput(ident)
            throw FinnMeldekortUtbetalingsgrunnlagListeUgyldigInput(faultInfo.feilmelding, faultInfo)
        }
        FeilKode.PersonIkkeFunnet -> {
            val faultInfo = lagAktoerIkkeFunnet()
            throw FinnMeldekortUtbetalingsgrunnlagListeAktoerIkkeFunnet(faultInfo.feilmelding, faultInfo)
        }
        FeilKode.Sikkerhetsbegrensning -> {
            val faultInfo = lagSikkerhetsbegrensning(ident)
            throw FinnMeldekortUtbetalingsgrunnlagListeSikkerhetsbegrensning(faultInfo.feilmelding, faultInfo)
        }
    }

    private fun lagUgyldigInput(ident: String?) = feilObjectFactory.createUgyldigInput().apply {
        feilmelding = "Aktør \"$ident\" er ugyldig input"
        feilaarsak = FAULTINFO_FEILAARSAK
        feilkilde = FAULTINFO_FEILKILDE
        tidspunkt = now()
    }

    private fun lagSikkerhetsbegrensning(ident: String) =
        feilObjectFactory.createSikkerhetsbegrensning().apply {
            feilmelding = "PersonModell med aktørId \"$ident\" har sikkerhetsbegrensning"
            feilaarsak = FAULTINFO_FEILAARSAK
            feilkilde = FAULTINFO_FEILKILDE
            tidspunkt = now()
        }

    private fun lagAktoerIkkeFunnet() = feilObjectFactory.createAktoerIkkeFunnet().apply {
        feilmelding = "Aktør er ikke funnet"
        feilaarsak = FAULTINFO_FEILAARSAK
        feilkilde = FAULTINFO_FEILKILDE
        tidspunkt = now()
    }

    private fun now() = LocalDateTime.now().asXMLGregorianCalendar()

    companion object {
        private const val FAULTINFO_FEILAARSAK = "Feilaarsak"
        private const val FAULTINFO_FEILKILDE = "Mock MeldekortUtbetalingsgrunnlag"
    }
}
