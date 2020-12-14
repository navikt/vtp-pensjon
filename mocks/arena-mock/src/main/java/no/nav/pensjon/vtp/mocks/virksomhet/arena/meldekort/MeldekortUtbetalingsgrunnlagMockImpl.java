package no.nav.pensjon.vtp.mocks.virksomhet.arena.meldekort;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;

import no.nav.pensjon.vtp.testmodell.FeilKode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.pensjon.vtp.core.annotations.SoapService;
import no.nav.pensjon.vtp.mocks.virksomhet.arena.meldekort.modell.ArenaMUMapper;
import no.nav.pensjon.vtp.felles.ConversionUtils;
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks;
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.pensjon.vtp.testmodell.inntektytelse.arena.ArenaModell;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.binding.FinnMeldekortUtbetalingsgrunnlagListeAktoerIkkeFunnet;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.binding.FinnMeldekortUtbetalingsgrunnlagListeSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.binding.FinnMeldekortUtbetalingsgrunnlagListeUgyldigInput;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.binding.MeldekortUtbetalingsgrunnlagV1;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.feil.AktoerIkkeFunnet;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.feil.ForretningsmessigUnntak;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.feil.Sikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.feil.UgyldigInput;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.informasjon.AktoerId;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.meldinger.FinnMeldekortUtbetalingsgrunnlagListeRequest;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.meldinger.FinnMeldekortUtbetalingsgrunnlagListeResponse;
import no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.meldinger.ObjectFactory;

@SuppressWarnings("ValidExternallyBoundObject")
@SoapService(path = "/ail_ws/MeldekortUtbetalingsgrunnlag_v1")
@Addressing
@WebService(endpointInterface = "no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.binding.MeldekortUtbetalingsgrunnlagV1")
@HandlerChain(file = "/Handler-chain.xml")
public class MeldekortUtbetalingsgrunnlagMockImpl implements MeldekortUtbetalingsgrunnlagV1 {

    private static final Logger LOG = LoggerFactory.getLogger(MeldekortUtbetalingsgrunnlagMockImpl.class);

    private static final String FAULTINFO_FEILAARSAK = "Feilaarsak";
    private static final String FAULTINFO_FEILKILDE = "Mock MeldekortUtbetalingsgrunnlag";

    private static final ObjectFactory objectFactory = new ObjectFactory();
    private static final ArenaMUMapper arenaMapper = new ArenaMUMapper();
    private final InntektYtelseIndeks inntektYtelseIndeks;

    public MeldekortUtbetalingsgrunnlagMockImpl(InntektYtelseIndeks inntektYtelseIndeks) {
        this.inntektYtelseIndeks = inntektYtelseIndeks;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/meldekortutbetalingsgrunnlag/v1/meldekortutbetalingsgrunnlag_v1/FinnMeldekortUtbetalingsgrunnlagListeRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "finnMeldekortUtbetalingsgrunnlagListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/meldekortutbetalingsgrunnlag/v1", className = "no.nav.tjeneste.virksomhet.arena.meldekort.finnMeldekortUtbetalingsgrunnlagListe")
    @ResponseWrapper(localName = "finnMeldekortUtbetalingsgrunnlagListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/ytelseskontrakt/v1", className = "no.nav.tjeneste.virksomhet.arena.meldekort.finnMeldekortUtbetalingsgrunnlagListeResponse")
    @Override
    public FinnMeldekortUtbetalingsgrunnlagListeResponse finnMeldekortUtbetalingsgrunnlagListe(@WebParam(name = "request") FinnMeldekortUtbetalingsgrunnlagListeRequest finnMeldekortUtbetalingsgrunnlagListeRequest)
            throws FinnMeldekortUtbetalingsgrunnlagListeAktoerIkkeFunnet, FinnMeldekortUtbetalingsgrunnlagListeSikkerhetsbegrensning,
            FinnMeldekortUtbetalingsgrunnlagListeUgyldigInput {

        FinnMeldekortUtbetalingsgrunnlagListeResponse response = objectFactory.createFinnMeldekortUtbetalingsgrunnlagListeResponse();
        AktoerId aktoerId = (AktoerId) finnMeldekortUtbetalingsgrunnlagListeRequest.getIdent();
        String aktørId = aktoerId.getAktoerId();
        LOG.info("finnMeldekortUtbetalingsgrunnlagListe. AktoerIdent: " + aktørId);

        if (aktørId == null) {
            UgyldigInput faultInfo = lagUgyldigInput(null);
            throw new FinnMeldekortUtbetalingsgrunnlagListeUgyldigInput(faultInfo.getFeilmelding(), faultInfo);
        }
        Optional<InntektYtelseModell> iyIndeksOpt = inntektYtelseIndeks.getInntektYtelseModellFraAktørId(aktørId);
        if (iyIndeksOpt.isEmpty()) {
            return response;
        }
        InntektYtelseModell inntektYtelseModell = iyIndeksOpt.get();
        ArenaModell arenaModell = inntektYtelseModell.getArenaModell();
        FeilKode feilkode = arenaModell.getFeilkode();
        if (feilkode != null) {
            try {
                haandterExceptions(feilkode, aktørId);
            } catch (Exception e) {
                LOG.error("Error ", e);
                throw e;
            }
        }

        return arenaMapper.mapArenaSaker(finnMeldekortUtbetalingsgrunnlagListeRequest, response, arenaModell.getSaker());

    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/meldekortutbetalingsgrunnlag/v1/meldekortutbetalingsgrunnlag_v1/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/meldekortutbetalingsgrunnlag/v1", className = "no.nav.tjeneste.virksomhet.arena.meldekort.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/meldekortutbetalingsgrunnlag/v1", className = "no.nav.tjeneste.virksomhet.arena.meldekort.PingResponse")
    @Override
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }

    private void haandterExceptions(FeilKode kode, String ident)
            throws FinnMeldekortUtbetalingsgrunnlagListeUgyldigInput, FinnMeldekortUtbetalingsgrunnlagListeAktoerIkkeFunnet,
            FinnMeldekortUtbetalingsgrunnlagListeSikkerhetsbegrensning {

        switch (kode) {
            case UgyldigInput: {
                UgyldigInput faultInfo = lagUgyldigInput(ident);
                throw new FinnMeldekortUtbetalingsgrunnlagListeUgyldigInput(faultInfo.getFeilmelding(), faultInfo);
            }
            case PersonIkkeFunnet: {
                AktoerIkkeFunnet faultInfo = lagAktoerIkkeFunnet();
                throw new FinnMeldekortUtbetalingsgrunnlagListeAktoerIkkeFunnet(faultInfo.getFeilmelding(), faultInfo);
            }
            case Sikkerhetsbegrensning: {
                Sikkerhetsbegrensning faultInfo = lagSikkerhetsbegrensning(ident);
                throw new FinnMeldekortUtbetalingsgrunnlagListeSikkerhetsbegrensning(faultInfo.getFeilmelding(), faultInfo);
            }
            default:
                // ikke noe
                break;
        }
    }

    private UgyldigInput lagUgyldigInput(String ident) {
        no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.feil.ObjectFactory ofl = new no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.feil.ObjectFactory();
        UgyldigInput faultInfo = ofl.createUgyldigInput();
        faultInfo.setFeilmelding("Aktør \"" + ident + "\" er ugyldig input");
        populerMedStandardVerdier(faultInfo);
        return faultInfo;
    }

    private Sikkerhetsbegrensning lagSikkerhetsbegrensning(String ident) {
        no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.feil.ObjectFactory ofl = new no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.feil.ObjectFactory();
        Sikkerhetsbegrensning faultInfo = ofl.createSikkerhetsbegrensning();
        faultInfo.setFeilmelding("PersonModell med aktørId \"" + ident + "\" har sikkerhetsbegrensning");
        populerMedStandardVerdier(faultInfo);
        return faultInfo;
    }

    private AktoerIkkeFunnet lagAktoerIkkeFunnet() {
        no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.feil.ObjectFactory ofl = new no.nav.tjeneste.virksomhet.meldekortutbetalingsgrunnlag.v1.feil.ObjectFactory();
        AktoerIkkeFunnet faultInfo = ofl.createAktoerIkkeFunnet();
        faultInfo.setFeilmelding("Aktør er ikke funnet");
        populerMedStandardVerdier(faultInfo);
        return faultInfo;
    }

    private void populerMedStandardVerdier(ForretningsmessigUnntak faultInfo) {
        faultInfo.setFeilaarsak(FAULTINFO_FEILAARSAK);
        faultInfo.setFeilkilde(FAULTINFO_FEILKILDE);
        faultInfo.setTidspunkt(now());
    }

    private XMLGregorianCalendar now() {
        return ConversionUtils.convertToXMLGregorianCalendar(LocalDateTime.now());
    }
}
