package no.nav.pensjon.vtp.mocks.virksomhet.kodeverk.v2;

import no.nav.pensjon.vtp.core.annotations.SoapService;
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException;
import no.nav.tjeneste.virksomhet.kodeverk.v2.KodeverkPortType;
import no.nav.tjeneste.virksomhet.kodeverk.v2.meldinger.FinnKodeverkListeRequest;
import no.nav.tjeneste.virksomhet.kodeverk.v2.meldinger.FinnKodeverkListeResponse;
import no.nav.tjeneste.virksomhet.kodeverk.v2.meldinger.HentKodeverkRequest;
import no.nav.tjeneste.virksomhet.kodeverk.v2.meldinger.HentKodeverkResponse;

import javax.jws.*;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;

@SoapService(path = "/kodeverk/ws/Kodeverk/v2")
@Addressing
@WebService(endpointInterface = "no.nav.tjeneste.virksomhet.kodeverk.v2.KodeverkPortType")
@HandlerChain(file = "/Handler-chain.xml")
public class KodeverkServiceMockImpl implements KodeverkPortType {
    @Override
    public void ping() {
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/kodeverk/v2/KodeverkPortType/hentKodeverkRequest")
    @WebResult(name="response")
    @RequestWrapper(localName = "hentKodeverk", targetNamespace = "http://nav.no/tjeneste/virksomhet/kodeverk/v2/", className = "no.nav.tjeneste.virksomhet.kodeverk.v2.HentKodeverk")
    @ResponseWrapper(localName = "hentKodeverkResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/kodeverk/v2/", className = "no.nav.tjeneste.virksomhet.kodeverk.v2.HentKodeverkResponse")
    public HentKodeverkResponse hentKodeverk(@WebParam(name="respons") HentKodeverkRequest request) {
        throw new NotImplementedException();
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/kodeverk/v2/KodeverkPortType/finnKodeverkListeRequest")
    @WebResult(name="response")
    @RequestWrapper(localName = "finnKodeverkListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/kodeverk/v2/", className = "no.nav.tjeneste.virksomhet.kodeverk.v2.FinnKodeverkListe")
    @ResponseWrapper(localName = "finnKodeverkListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/kodeverk/v2/", className = "no.nav.tjeneste.virksomhet.kodeverk.v2.FinnKodeverkListeResponse")
    public FinnKodeverkListeResponse finnKodeverkListe(@WebParam(name="respons") FinnKodeverkListeRequest request) {
        throw new NotImplementedException();
    }
}
