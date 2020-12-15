package no.nav.pensjon.vtp.mocks.virksomhet.arbeidsforhold.v3

import no.nav.pensjon.vtp.core.annotations.SoapService
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.binding.ArbeidsforholdV3
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.binding.FinnArbeidsforholdPrArbeidstakerUgyldigInput
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.binding.HentArbeidsforholdHistorikkArbeidsforholdIkkeFunnet
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.feil.ArbeidsforholdIkkeFunnet
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.feil.UgyldigInput
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.meldinger.*
import org.slf4j.LoggerFactory
import javax.jws.*
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper
import javax.xml.ws.soap.Addressing

@SoapService(path = ["/aareg-core/ArbeidsforholdService/v3", "/aareg-services/ArbeidsforholdService/v3"])
@Addressing
@WebService(name = "Arbeidsforhold_v3", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3")
@HandlerChain(file = "/Handler-chain.xml")
class ArbeidsforholdMockImpl(private val inntektYtelseIndeks: InntektYtelseIndeks) : ArbeidsforholdV3 {
    private val logger = LoggerFactory.getLogger(ArbeidsforholdMockImpl::class.java)

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3/Arbeidsforhold_v3/finnArbeidsforholdPrArbeidsgiverRequest")
    @WebResult(name = "parameters")
    @RequestWrapper(
        localName = "finnArbeidsforholdPrArbeidsgiver",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3",
        className = "no.nav.tjeneste.virksomhet.arbeidsforhold.v3.FinnArbeidsforholdPrArbeidsgiver"
    )
    @ResponseWrapper(
        localName = "FinnArbeidsforholdPrArbeidsgiverResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3",
        className = "no.nav.tjeneste.virksomhet.arbeidsforhold.v3.FinnArbeidsforholdPrArbeidsgiverResponse"
    )
    override fun finnArbeidsforholdPrArbeidsgiver(@WebParam(name = "parameters") finnArbeidsforholdPrArbeidsgiverRequest: FinnArbeidsforholdPrArbeidsgiverRequest) =
        throw NotImplementedException()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3/Arbeidsforhold_v3/pingRequest")
    @RequestWrapper(
        localName = "ping",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3",
        className = "no.nav.tjeneste.virksomhet.arbeidsforhold.v3.Ping"
    )
    @ResponseWrapper(
        localName = "pingResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3",
        className = "no.nav.tjeneste.virksomhet.arbeidsforhold.v3.PingResponse"
    )
    override fun ping() = Unit

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3/Arbeidsforhold_v3/finnArbeidstakerePrArbeidsgiverRequest")
    @WebResult(name = "parameters")
    @RequestWrapper(
        localName = "finnArbeidstakerePrArbeidsgiver",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3",
        className = "no.nav.tjeneste.virksomhet.arbeidsforhold.v3.FinnArbeidstakerePrArbeidsgiverRequest"
    )
    @ResponseWrapper(
        localName = "finnArbeidstakerePrArbeidsgiverResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3",
        className = "no.nav.tjeneste.virksomhet.arbeidsforhold.v3.FinnArbeidstakerePrArbeidsgiverResponse"
    )
    override fun finnArbeidstakerePrArbeidsgiver(@WebParam(name = "parameters") finnArbeidstakerePrArbeidsgiverRequest: FinnArbeidstakerePrArbeidsgiverRequest): FinnArbeidstakerePrArbeidsgiverResponse =
        throw NotImplementedException()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3/Arbeidsforhold_v3/finnArbeidsforholdPrArbeidstakerRequest")
    @WebResult(name = "parameters")
    @RequestWrapper(
        localName = "finnArbeidsforholdPrArbeidstaker",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3",
        className = "no.nav.tjeneste.virksomhet.arbeidsforhold.v3.FinnArbeidsforholdPrArbeidstaker"
    )
    @ResponseWrapper(
        localName = "finnArbeidsforholdPrArbeidstakerResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3",
        className = "no.nav.tjeneste.virksomhet.arbeidsforhold.v3.FinnArbeidsforholdPrArbeidstakerResponse"
    )
    override fun finnArbeidsforholdPrArbeidstaker(@WebParam(name = "parameters") request: FinnArbeidsforholdPrArbeidstakerRequest): FinnArbeidsforholdPrArbeidstakerResponse {
        logger.info("finnArbeidsforholdPrArbeidstaker. Ident: ${request.ident.ident}. Regelverk: ${request.rapportertSomRegelverk.kodeverksRef}")

        if (request.ident?.ident != null && request.rapportertSomRegelverk != null) {
            val fnr = request.ident.ident

            inntektYtelseIndeks.getInntektYtelseModell(fnr)?.arbeidsforholdModell
                ?.let { arbeidsforholdModell ->
                    return FinnArbeidsforholdPrArbeidstakerResponse().apply {
                        arbeidsforhold.addAll(
                            arbeidsforholdModell.arbeidsforhold
                                .map {
                                    ArbeidsforholdAdapter.fra(fnr, it)
                                }
                        )
                    }
                }

            logger.warn("finnArbeidsforholdPrArbeidstaker kunne ikke finne etterspurt bruker")
        }

        logger.warn("finnArbeidsforholdPrArbeidstaker ugyldig forespørsel")
        throw FinnArbeidsforholdPrArbeidstakerUgyldigInput("Ikke gyldig input", UgyldigInput())
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3/Arbeidsforhold_v3/hentArbeidsforholdHistorikkRequest")
    @WebResult(name = "parameters")
    @RequestWrapper(
        localName = "hentArbeidsforholdHistorikk",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3",
        className = "no.nav.tjeneste.virksomhet.arbeidsforhold.v3.HentArbeidsforholdHistorikkRequest"
    )
    @ResponseWrapper(
        localName = "hentArbeidsforholdHistorikkResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsforhold/v3",
        className = "no.nav.tjeneste.virksomhet.arbeidsforhold.v3.HentArbeidsforholdHistorikkResponse"
    )
    override fun hentArbeidsforholdHistorikk(@WebParam(name = "parameters") request: HentArbeidsforholdHistorikkRequest) =
        inntektYtelseIndeks.entries()
            .mapNotNull { (key, value) ->
                value.finnArbeidsforhold(request.arbeidsforholdId)
                    ?.let { ArbeidsforholdAdapter.fra(key, it) }
            }
            .firstOrNull()
            ?.let {
                HentArbeidsforholdHistorikkResponse().apply {
                    arbeidsforhold = it
                }
            }
            ?: throw HentArbeidsforholdHistorikkArbeidsforholdIkkeFunnet(
                "Kunne ikke finne arbeidsforholdHistorikk med Id ${request.arbeidsforholdId}",
                ArbeidsforholdIkkeFunnet()
            )
}
