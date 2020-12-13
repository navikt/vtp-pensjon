package no.nav.pensjon.vtp.mocks.virksomhet.medlemskap.v2;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;

import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.pensjon.vtp.core.annotations.SoapService;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository;
import no.nav.tjeneste.virksomhet.medlemskap.v2.MedlemskapV2;
import no.nav.tjeneste.virksomhet.medlemskap.v2.PersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.medlemskap.v2.Sikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.medlemskap.v2.meldinger.HentPeriodeListeRequest;
import no.nav.tjeneste.virksomhet.medlemskap.v2.meldinger.HentPeriodeListeResponse;
import no.nav.tjeneste.virksomhet.medlemskap.v2.meldinger.HentPeriodeRequest;
import no.nav.tjeneste.virksomhet.medlemskap.v2.meldinger.HentPeriodeResponse;

@SoapService(path = "/medl2/ws/Medlemskap/v2")
@Addressing
@WebService(name = "Medlemskap_v2", targetNamespace = "http://nav.no/tjeneste/virksomhet/medlemskap/v2")
@HandlerChain(file="/Handler-chain.xml")
public class MedlemServiceMockImpl implements MedlemskapV2 {

    private static final Logger LOG = LoggerFactory.getLogger(MedlemServiceMockImpl.class);
    private final PersonModellRepository personModellRepository;

    public MedlemServiceMockImpl(PersonModellRepository personModellRepository){
        this.personModellRepository = personModellRepository;
    }

    @WebMethod
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentPeriode", targetNamespace = "http://nav.no/tjeneste/virksomhet/medlemskap/v2", className = "no.nav.tjeneste.virksomhet.medlemskap.v2.HentPeriode")
    @ResponseWrapper(localName = "hentPeriodeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/medlemskap/v2", className = "no.nav.tjeneste.virksomhet.medlemskap.v2.HentPeriodeResponse")
    @Action(input = "http://nav.no/tjeneste/virksomhet/medlemskap/v2/Medlemskap_v2/hentPeriodeRequest", output = "http://nav.no/tjeneste/virksomhet/medlemskap/v2/Medlemskap_v2/hentPeriodeResponse", fault = {
            @FaultAction(className = Sikkerhetsbegrensning.class, value = "http://nav.no/tjeneste/virksomhet/medlemskap/v2/Medlemskap_v2/hentPeriode/Fault/Sikkerhetsbegrensning")
    })
    @Override
    public HentPeriodeResponse hentPeriode(@WebParam(name = "request",targetNamespace = "") HentPeriodeRequest request) throws Sikkerhetsbegrensning {
        throw new NotImplementedException();
    }

    @WebMethod
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/medlemskap/v2", className = "no.nav.tjeneste.virksomhet.medlemskap.v2.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/medlemskap/v2", className = "no.nav.tjeneste.virksomhet.medlemskap.v2.PingResponse")
    @Action(input = "http://nav.no/tjeneste/virksomhet/medlemskap/v2/Medlemskap_v2/pingRequest", output = "http://nav.no/tjeneste/virksomhet/medlemskap/v2/Medlemskap_v2/pingResponse")
    @Override
    public void ping() { LOG.info("Ping mottatt og besvart");}

    @WebMethod
    @WebResult(name = "response", targetNamespace = "")
    @RequestWrapper(localName = "hentPeriodeListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/medlemskap/v2", className = "no.nav.tjeneste.virksomhet.medlemskap.v2.HentPeriodeListe")
    @ResponseWrapper(localName = "hentPeriodeListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/medlemskap/v2", className = "no.nav.tjeneste.virksomhet.medlemskap.v2.HentPeriodeListeResponse")
    @Action(input = "http://nav.no/tjeneste/virksomhet/medlemskap/v2/Medlemskap_v2/hentPeriodeListeRequest", output = "http://nav.no/tjeneste/virksomhet/medlemskap/v2/Medlemskap_v2/hentPeriodeListeResponse", fault = {
            @FaultAction(className = PersonIkkeFunnet.class, value = "http://nav.no/tjeneste/virksomhet/medlemskap/v2/Medlemskap_v2/hentPeriodeListe/Fault/PersonIkkeFunnet"),
            @FaultAction(className = Sikkerhetsbegrensning.class, value = "http://nav.no/tjeneste/virksomhet/medlemskap/v2/Medlemskap_v2/hentPeriodeListe/Fault/Sikkerhetsbegrensning")
    })
    @Override
    public HentPeriodeListeResponse hentPeriodeListe(@WebParam(name = "request",targetNamespace = "") HentPeriodeListeRequest request) throws PersonIkkeFunnet, Sikkerhetsbegrensning {

        LOG.info("hentPeriodeListe. IdentFNR: {}", request.getIdent().getValue());
        if (request.getIdent() != null) {
            String fnr = request.getIdent().getValue();
            HentPeriodeListeResponse response = new HentPeriodeListeResponse();
            new MedlemskapperioderAdapter(personModellRepository)
                    .finnMedlemsperioder(fnr)
                    .ifPresent(response::withPeriodeListe);
            return response;
        }
        throw new IllegalArgumentException("Request eller ident i request mangler");
    }
}
