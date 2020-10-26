package no.nav.pensjon.vtp.mocks.virksomhet.arbeidsfordeling.v1;

import no.nav.pensjon.vtp.core.annotations.SoapService;
import no.nav.pensjon.vtp.testmodell.enheter.EnheterIndeks;
import no.nav.pensjon.vtp.testmodell.enheter.Norg2Modell;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.binding.ArbeidsfordelingV1;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.informasjon.*;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.meldinger.FinnAlleBehandlendeEnheterListeRequest;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.meldinger.FinnAlleBehandlendeEnheterListeResponse;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.meldinger.FinnBehandlendeEnhetListeRequest;
import no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.meldinger.FinnBehandlendeEnhetListeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.*;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SoapService(path = "/norg2/ws/Arbeidsfordeling/v1")
@Addressing
@WebService(name = "Arbeidsfordeling_v1", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/")
@HandlerChain(file = "/Handler-chain.xml")
public class ArbeidsfordelingMockImpl implements ArbeidsfordelingV1 {

    private static final Logger LOG = LoggerFactory.getLogger(ArbeidsfordelingMockImpl.class);
    private final EnheterIndeks enheterIndeks;

    public ArbeidsfordelingMockImpl(EnheterIndeks enheterIndeks) {
        this.enheterIndeks = enheterIndeks;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/Arbeidsfordeling_v1/finnBehandlendeEnhetListeRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "finnBehandlendeEnhetListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/", className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.FinnBehandlendeEnhetListe")
    @ResponseWrapper(localName = "finnBehandlendeEnhetListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/", className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.FinnBehandlendeEnhetListeResponse")
    @Override
    public FinnBehandlendeEnhetListeResponse finnBehandlendeEnhetListe(
                                                                       @WebParam(name = "request") FinnBehandlendeEnhetListeRequest request) {
        LOG.info("finnBehandlendeEnhetListe. Diskresjonskode: {}, tema: {}", getKodeverdi(request.getArbeidsfordelingKriterier().getDiskresjonskode()),
                getKodeverdi(request.getArbeidsfordelingKriterier().getTema()));
        Diskresjonskoder diskrKode = request.getArbeidsfordelingKriterier().getDiskresjonskode();
        Tema tema = request.getArbeidsfordelingKriterier().getTema();
        String diskrKodeStr = (diskrKode != null ? diskrKode.getValue() : null);
        String temaStr = tema == null ? null : tema.getValue();
        Organisasjonsenhet enhet = lagOrganisasjonsenhet(diskrKodeStr, temaStr);

        FinnBehandlendeEnhetListeResponse response = new FinnBehandlendeEnhetListeResponse();
        response.getBehandlendeEnhetListe().add(enhet);

        return response;
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/Arbeidsfordeling_v1/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/", className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/", className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.PingResponse")
    @Override
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }

    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/Arbeidsfordeling_v1/finnAlleBehandlendeEnheterListeRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "finnAlleBehandlendeEnheterListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/", className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.FinnAlleBehandlendeEnheterListe")
    @ResponseWrapper(localName = "finnAlleBehandlendeEnheterListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/arbeidsfordeling/v1/", className = "no.nav.tjeneste.virksomhet.arbeidsfordeling.v1.FinnAlleBehandlendeEnheterListeResponse")
    @Override
    public FinnAlleBehandlendeEnheterListeResponse finnAlleBehandlendeEnheterListe(
                                                                                   @WebParam(name = "request") FinnAlleBehandlendeEnheterListeRequest request) {

        LOG.info("finnAlleBehandlendeEnheterListe. Diskresjonskode: {}, tema: {}", getKodeverdi(request.getArbeidsfordelingKriterier().getDiskresjonskode()),
                getKodeverdi(request.getArbeidsfordelingKriterier().getTema()));
        Tema tema = request.getArbeidsfordelingKriterier().getTema();
        String temaStr = tema == null ? null : tema.getValue();
        FinnAlleBehandlendeEnheterListeResponse response = new FinnAlleBehandlendeEnheterListeResponse();
        enheterIndeks.getAlleEnheter().stream().filter(e -> skalEnhetMed(e, temaStr)).forEach(e -> response.getBehandlendeEnhetListe().add(lagEnhet(e)));
        return response;
    }

    private boolean skalEnhetMed(Norg2Modell enhet, String tema) {
        if (tema == null) return true;
        if ("FOR".equalsIgnoreCase(tema) && "YTA".equalsIgnoreCase(enhet.getType())) return false;
        if ("OMS".equalsIgnoreCase(tema) && "FPY".equalsIgnoreCase(enhet.getType())) return false;
        return true;
    }

    private Organisasjonsenhet lagOrganisasjonsenhet(String diskrKode, String tema) {

        List<String> spesielleDiskrKoder = Arrays.asList("UFB", "SPSF", "SPFO");

        Norg2Modell modell;
        if (diskrKode != null && spesielleDiskrKoder.contains(diskrKode)) {
            modell = enheterIndeks.finnByDiskresjonskode(diskrKode);
        }
        else {
            modell = Optional.ofNullable(enheterIndeks.finnByDiskresjonskode(tema))
                    .orElseGet(() -> enheterIndeks.finnByDiskresjonskode("NORMAL-"+tema));
        }

        return lagEnhet(modell);
    }

    private Organisasjonsenhet lagEnhet(Norg2Modell modell) {
        Organisasjonsenhet enhet = new Organisasjonsenhet();
        enhet.setEnhetId(modell.getEnhetId());
        enhet.setEnhetNavn(modell.getNavn());

        String status = modell.getStatus();
        if (status != null) {
            enhet.setStatus(Enhetsstatus.fromValue(status));
        }

        String typeStr = modell.getType();
        if (typeStr != null) {
            Enhetstyper type = new Enhetstyper();
            type.setValue(typeStr);
            enhet.setType(type);
        }

        return enhet;
    }

    private String getKodeverdi(Kodeverdi kodeverdi){
        if(kodeverdi == null){
            return "null";
        } else {
            return kodeverdi.getValue();
        }
    }

}