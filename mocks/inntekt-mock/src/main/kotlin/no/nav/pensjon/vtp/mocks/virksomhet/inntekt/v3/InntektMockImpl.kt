package no.nav.pensjon.vtp.mocks.virksomhet.inntekt.v3

import no.nav.pensjon.vtp.core.annotations.SoapService
import no.nav.pensjon.vtp.core.util.asLocalDate
import no.nav.pensjon.vtp.mocks.virksomhet.inntekt.v3.modell.HentInntektlistBolkMapper
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseModell
import no.nav.tjeneste.virksomhet.inntekt.v3.binding.HentInntektListeBolkUgyldigInput
import no.nav.tjeneste.virksomhet.inntekt.v3.binding.InntektV3
import no.nav.tjeneste.virksomhet.inntekt.v3.feil.UgyldigInput
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.Aktoer
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.AktoerId
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.ArbeidsInntektMaaned
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.PersonIdent
import no.nav.tjeneste.virksomhet.inntekt.v3.meldinger.*
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.util.*
import java.util.stream.Collectors
import javax.jws.*
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper
import javax.xml.ws.soap.Addressing

@SoapService(path = ["/inntektskomponenten-ws/inntekt/v3/Inntekt"])
@Addressing
@WebService(name = "Inntekt_v3", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3")
@HandlerChain(file = "/Handler-chain.xml")
class InntektMockImpl(private val inntektYtelseIndeks: InntektYtelseIndeks) : InntektV3 {
    private val logger = LoggerFactory.getLogger(InntektMockImpl::class.java)

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/inntekt/v3/Inntekt_v3/hentInntektListeBolkRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "hentInntektListeBolk", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3", className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentInntektListeBolk")
    @ResponseWrapper(localName = "hentInntektListeBolkResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3", className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentInntektListeBolkResponse")
    override fun hentInntektListeBolk(@WebParam(name = "request") request: HentInntektListeBolkRequest): HentInntektListeBolkResponse {
        logger.info("hentInntektListeBolk. AktoerIdentListe: {}", request.identListe.stream().map { aktoer: Aktoer -> getIdentFromAktoer(aktoer) }.collect(Collectors.joining(",")))
        if (request.formaal == null || request.ainntektsfilter == null) {
            logger.warn("Request ugyldig. Mangler Formål eller A-inntektsfilter. ")
            throw HentInntektListeBolkUgyldigInput("Formål eller A-inntektsfilter mangler", UgyldigInput())
        }
        val response = HentInntektListeBolkResponse()
        if (request.identListe != null && !request.identListe.isEmpty() &&
            request.uttrekksperiode != null
        ) {
            val fom = request.uttrekksperiode.maanedFom?.asLocalDate()
            var tom = request.uttrekksperiode.maanedTom?.asLocalDate()
            tom = if (tom == null) {
                LocalDate.now()
            } else {
                tom.plusMonths(1)
            }
            var inntektYtelseModell: InntektYtelseModell? = null
            for (aktoer in request.identListe) {
                if (aktoer is PersonIdent) {
                    val fnr = aktoer.personIdent
                    inntektYtelseModell = inntektYtelseIndeks.getInntektYtelseModell(fnr)
                } else if (aktoer is AktoerId) {
                    val aktoerId = aktoer.aktoerId
                    inntektYtelseModell = inntektYtelseIndeks.getInntektYtelseModellFraAktørId(aktoerId)
                }
                if (inntektYtelseModell != null) {
                    val modell = inntektYtelseModell.inntektskomponentModell
                    val arbeidsInntektIdent = HentInntektlistBolkMapper.makeArbeidsInntektIdent(modell, aktoer)
                    val listeOnskedeMnd: MutableList<ArbeidsInntektMaaned> = ArrayList()
                    for (aarMaaned in arbeidsInntektIdent.arbeidsInntektMaaned) {
                        val am = aarMaaned.aarMaaned
                        val nyAm = am.asLocalDate()
                        if (nyAm == fom || nyAm.isAfter(fom) && nyAm.isBefore(tom) || nyAm == tom) {
                            listeOnskedeMnd.add(aarMaaned)
                        }
                    }
                    arbeidsInntektIdent.arbeidsInntektMaaned.clear()
                    arbeidsInntektIdent.arbeidsInntektMaaned.addAll(listeOnskedeMnd)
                    response.arbeidsInntektIdentListe.add(arbeidsInntektIdent)
                }
            }
        }
        return response
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/inntekt/v3/Inntekt_v3/hentForventetInntektRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "hentForventetInntekt",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3",
        className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentForventetInntekt"
    )
    @ResponseWrapper(
        localName = "hentForventetInntektResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3",
        className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentForventetInntektResponse"
    )
    override fun hentForventetInntekt(@WebParam(name = "request") hentForventetInntektRequest: HentForventetInntektRequest) =
        HentForventetInntektResponse().apply {
            ident = hentForventetInntektRequest.ident
        }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/inntekt/v3/Inntekt_v3/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3", className = "no.nav.tjeneste.virksomhet.inntekt.v3.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3", className = "no.nav.tjeneste.virksomhet.inntekt.v3.PingResponse")
    override fun ping() = Unit

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/inntekt/v3/Inntekt_v3/hentInntektListeRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "hentInntektListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3", className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentInntektListe")
    @ResponseWrapper(localName = "hentInntektListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3", className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentInntektListeResponse")
    override fun hentInntektListe(@WebParam(name = "request") hentInntektListeRequest: HentInntektListeRequest) =
        throw NotImplementedException()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/inntekt/v3/Inntekt_v3/hentInntektListeForOpplysningspliktigRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "hentInntektListeForOpplysningspliktig", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3", className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentInntektListeForOpplysningspliktig")
    @ResponseWrapper(localName = "hentInntektListeForOpplysningspliktigResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3", className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentInntektListeForOpplysningspliktigResponse")
    override fun hentInntektListeForOpplysningspliktig(@WebParam(name = "request") hentInntektListeForOpplysningspliktigRequest: HentInntektListeForOpplysningspliktigRequest) =
        throw NotImplementedException()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/inntekt/v3/Inntekt_v3/hentAbonnerteInntekterBolkRequest")
    @WebResult(name = "response")
    @RequestWrapper(
        localName = "hentAbonnerteInntekterBolk",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3",
        className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentAbonnerteInntekterBolk"
    )
    @ResponseWrapper(
        localName = "hentAbonnerteInntekterBolkResponse",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3",
        className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentAbonnerteInntekterBolkResponse"
    )
    override fun hentAbonnerteInntekterBolk(@WebParam(name = "request") hentAbonnerteInntekterBolkRequest: HentAbonnerteInntekterBolkRequest) =
        HentAbonnerteInntekterBolkResponse()

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/inntekt/v3/Inntekt_v3/hentDetaljerteAbonnerteInntekterRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "hentDetaljerteAbonnerteInntekter", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3", className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentDetaljerteAbonnerteInntekter")
    @ResponseWrapper(localName = "hentDetaljerteAbonnerteInntekterResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3", className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentDetaljerteAbonnerteInntekterResponse")
    override fun hentDetaljerteAbonnerteInntekter(@WebParam(name = "request") hentDetaljerteAbonnerteInntekterRequest: HentDetaljerteAbonnerteInntekterRequest) =
        HentDetaljerteAbonnerteInntekterResponse()

    private fun getIdentFromAktoer(aktoer: Aktoer): String {
        return if (aktoer is PersonIdent) {
            aktoer.personIdent
        } else if (aktoer is AktoerId) {
            // TODO: Konverter AktoerId til PersonIdent
            aktoer.aktoerId
        } else {
            throw UnsupportedOperationException("Aktoertype ikke støttet")
        }
    }
}
