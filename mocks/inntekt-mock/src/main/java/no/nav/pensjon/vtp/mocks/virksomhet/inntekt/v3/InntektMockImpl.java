package no.nav.pensjon.vtp.mocks.virksomhet.inntekt.v3;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.pensjon.vtp.core.annotations.SoapService;
import no.nav.pensjon.vtp.felles.ConversionUtils;
import no.nav.pensjon.vtp.mocks.virksomhet.inntekt.v3.modell.HentInntektlistBolkMapper;
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks;
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.pensjon.vtp.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell;
import no.nav.tjeneste.virksomhet.inntekt.v3.binding.HentInntektListeBolkUgyldigInput;
import no.nav.tjeneste.virksomhet.inntekt.v3.binding.InntektV3;
import no.nav.tjeneste.virksomhet.inntekt.v3.feil.UgyldigInput;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.Aktoer;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.AktoerId;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.ArbeidsInntektIdent;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.ArbeidsInntektMaaned;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.PersonIdent;
import no.nav.tjeneste.virksomhet.inntekt.v3.meldinger.HentAbonnerteInntekterBolkRequest;
import no.nav.tjeneste.virksomhet.inntekt.v3.meldinger.HentAbonnerteInntekterBolkResponse;
import no.nav.tjeneste.virksomhet.inntekt.v3.meldinger.HentDetaljerteAbonnerteInntekterRequest;
import no.nav.tjeneste.virksomhet.inntekt.v3.meldinger.HentDetaljerteAbonnerteInntekterResponse;
import no.nav.tjeneste.virksomhet.inntekt.v3.meldinger.HentForventetInntektRequest;
import no.nav.tjeneste.virksomhet.inntekt.v3.meldinger.HentForventetInntektResponse;
import no.nav.tjeneste.virksomhet.inntekt.v3.meldinger.HentInntektListeBolkRequest;
import no.nav.tjeneste.virksomhet.inntekt.v3.meldinger.HentInntektListeBolkResponse;
import no.nav.tjeneste.virksomhet.inntekt.v3.meldinger.HentInntektListeForOpplysningspliktigRequest;
import no.nav.tjeneste.virksomhet.inntekt.v3.meldinger.HentInntektListeForOpplysningspliktigResponse;
import no.nav.tjeneste.virksomhet.inntekt.v3.meldinger.HentInntektListeRequest;
import no.nav.tjeneste.virksomhet.inntekt.v3.meldinger.HentInntektListeResponse;

@SoapService(path = "/inntektskomponenten-ws/inntekt/v3/Inntekt")
@Addressing
@WebService(name = "Inntekt_v3", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3")
@HandlerChain(file = "/Handler-chain.xml")
public class InntektMockImpl implements InntektV3 {

    private static final Logger LOG = LoggerFactory.getLogger(InntektMockImpl.class);

    private final InntektYtelseIndeks inntektYtelseIndeks;

    public InntektMockImpl(InntektYtelseIndeks inntektYtelseIndeks) {
        this.inntektYtelseIndeks = inntektYtelseIndeks;
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/inntekt/v3/Inntekt_v3/hentInntektListeBolkRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "hentInntektListeBolk", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3",
            className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentInntektListeBolk")
    @ResponseWrapper(localName = "hentInntektListeBolkResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3",
            className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentInntektListeBolkResponse")
    public HentInntektListeBolkResponse hentInntektListeBolk(@WebParam(name = "request") HentInntektListeBolkRequest request) throws HentInntektListeBolkUgyldigInput {

        LOG.info("hentInntektListeBolk. AktoerIdentListe: {}", request.getIdentListe().stream().map(this::getIdentFromAktoer).collect(Collectors.joining(",")));

        if(request.getFormaal() == null || request.getAinntektsfilter() == null){
            LOG.warn("Request ugyldig. Mangler Formål eller A-inntektsfilter. ");
            throw new HentInntektListeBolkUgyldigInput("Formål eller A-inntektsfilter mangler", new UgyldigInput());
        }

        HentInntektListeBolkResponse response = new HentInntektListeBolkResponse();

        if (request.getIdentListe() != null
                && !request.getIdentListe().isEmpty()
                && request.getUttrekksperiode() != null) {

            LocalDate fom = ConversionUtils.convertToLocalDate(request.getUttrekksperiode().getMaanedFom());
            LocalDate tom = ConversionUtils.convertToLocalDate(request.getUttrekksperiode().getMaanedTom());
            if (tom == null) {
                tom = LocalDate.now();
            } else { tom = tom.plusMonths(1);}

            Optional<InntektYtelseModell> inntektYtelseModell = Optional.empty();
            for (Aktoer aktoer : request.getIdentListe()) {
                if(aktoer instanceof PersonIdent){
                    String fnr = ((PersonIdent) aktoer).getPersonIdent();
                    inntektYtelseModell = inntektYtelseIndeks.getInntektYtelseModell(fnr);
                } else if (aktoer instanceof AktoerId){
                    String aktoerId = ((AktoerId) aktoer).getAktoerId();
                    inntektYtelseModell = inntektYtelseIndeks.getInntektYtelseModellFraAktørId(aktoerId);
                }
                if (inntektYtelseModell.isPresent()) {
                    InntektskomponentModell modell = inntektYtelseModell.get().getInntektskomponentModell();

                    ArbeidsInntektIdent arbeidsInntektIdent = HentInntektlistBolkMapper.makeArbeidsInntektIdent(modell, aktoer);
                    List<ArbeidsInntektMaaned> listeOnskedeMnd = new ArrayList<>();
                    for (ArbeidsInntektMaaned aarMaaned : arbeidsInntektIdent.getArbeidsInntektMaaned()) {
                        XMLGregorianCalendar am = aarMaaned.getAarMaaned();
                        LocalDate nyAm = ConversionUtils.convertToLocalDate(am);
                        if (nyAm.equals(fom) || (nyAm.isAfter(fom) && nyAm.isBefore(tom)) || nyAm.equals(tom)) {
                            listeOnskedeMnd.add(aarMaaned);
                        }
                    }
                    arbeidsInntektIdent.getArbeidsInntektMaaned().clear();
                    arbeidsInntektIdent.getArbeidsInntektMaaned().addAll(listeOnskedeMnd);

                    response.getArbeidsInntektIdentListe().add(arbeidsInntektIdent);

                }
            }
        }


        return response;
    }


    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/inntekt/v3/Inntekt_v3/hentForventetInntektRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "hentForventetInntekt", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3",
            className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentForventetInntekt")
    @ResponseWrapper(localName = "hentForventetInntektResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3",
            className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentForventetInntektResponse")
    public HentForventetInntektResponse hentForventetInntekt(@WebParam(name = "request") HentForventetInntektRequest hentForventetInntektRequest) {
        HentForventetInntektResponse hentForventetInntektResponse = new HentForventetInntektResponse();
        hentForventetInntektResponse.setIdent(hentForventetInntektRequest.getIdent());
        return hentForventetInntektResponse;
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/inntekt/v3/Inntekt_v3/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3", className = "no.nav.tjeneste.virksomhet.inntekt.v3.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3", className = "no.nav.tjeneste.virksomhet.inntekt.v3.PingResponse")
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/inntekt/v3/Inntekt_v3/hentInntektListeRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "hentInntektListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3",
            className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentInntektListe")
    @ResponseWrapper(localName = "hentInntektListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3",
            className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentInntektListeResponse")
    public HentInntektListeResponse hentInntektListe(@WebParam(name = "request") HentInntektListeRequest hentInntektListeRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/inntekt/v3/Inntekt_v3/hentInntektListeForOpplysningspliktigRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "hentInntektListeForOpplysningspliktig", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3",
            className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentInntektListeForOpplysningspliktig")
    @ResponseWrapper(localName = "hentInntektListeForOpplysningspliktigResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3",
            className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentInntektListeForOpplysningspliktigResponse")
    public HentInntektListeForOpplysningspliktigResponse hentInntektListeForOpplysningspliktig(@WebParam(name = "request") HentInntektListeForOpplysningspliktigRequest hentInntektListeForOpplysningspliktigRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/inntekt/v3/Inntekt_v3/hentAbonnerteInntekterBolkRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "hentAbonnerteInntekterBolk", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3",
            className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentAbonnerteInntekterBolk")
    @ResponseWrapper(localName = "hentAbonnerteInntekterBolkResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3",
            className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentAbonnerteInntekterBolkResponse")
    public HentAbonnerteInntekterBolkResponse hentAbonnerteInntekterBolk(@WebParam(name = "request") HentAbonnerteInntekterBolkRequest hentAbonnerteInntekterBolkRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/inntekt/v3/Inntekt_v3/hentDetaljerteAbonnerteInntekterRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "hentDetaljerteAbonnerteInntekter", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3",
            className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentDetaljerteAbonnerteInntekter")
    @ResponseWrapper(localName = "hentDetaljerteAbonnerteInntekterResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/inntekt/v3",
            className = "no.nav.tjeneste.virksomhet.inntekt.v3.HentDetaljerteAbonnerteInntekterResponse")
    public HentDetaljerteAbonnerteInntekterResponse hentDetaljerteAbonnerteInntekter(@WebParam(name = "request") HentDetaljerteAbonnerteInntekterRequest hentDetaljerteAbonnerteInntekterRequest) {
        throw new UnsupportedOperationException("Ikke implementert");
    }


    private String getIdentFromAktoer(Aktoer aktoer) {
        if (aktoer instanceof PersonIdent) {
            return ((PersonIdent) aktoer).getPersonIdent();
        } else if (aktoer instanceof AktoerId) {
            //TODO: Konverter AktoerId til PersonIdent
            return ((AktoerId) aktoer).getAktoerId();
        } else {
            throw new UnsupportedOperationException("Aktoertype ikke støttet");
        }
    }



}
