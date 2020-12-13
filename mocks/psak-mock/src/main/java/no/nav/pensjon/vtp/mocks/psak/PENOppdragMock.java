package no.nav.pensjon.vtp.mocks.psak;

import no.nav.inf.pen.oppdrag.ObjectFactory;
import no.nav.inf.pen.oppdrag.PENOppdrag;
import no.nav.lib.pen.psakpselv.asbo.oppdrag.ASBOPenSimuleringsresultatLinjeListe;
import no.nav.pensjon.vtp.core.annotations.SoapService;
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException;

import javax.jws.*;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@SoapService(path = "/esb/nav-cons-pen-pen-oppdragWeb/sca/PENOppdragWSEXP")
@WebService(targetNamespace = "http://nav-cons-pen-pen-oppdrag/no/nav/inf", name = "PENOppdrag")
@XmlSeeAlso({ObjectFactory.class, no.nav.lib.pen.psakpselv.fault.ObjectFactory.class, no.nav.lib.pen.psakpselv.fault.oppdrag.ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.oppdrag.ObjectFactory.class})
@HandlerChain(file = "/Handler-chain.xml")
public class PENOppdragMock implements PENOppdrag {

    @Override
    @WebMethod
    @RequestWrapper(localName = "sendAsynkronOppdragsavstemmingListe", targetNamespace = "http://nav-cons-pen-pen-oppdrag/no/nav/inf", className = "no.nav.inf.pen.oppdrag.SendAsynkronOppdragsavstemmingListe")
    @ResponseWrapper(localName = "sendAsynkronOppdragsavstemmingListeResponse", targetNamespace = "http://nav-cons-pen-pen-oppdrag/no/nav/inf", className = "no.nav.inf.pen.oppdrag.SendAsynkronOppdragsavstemmingListeResponse")
    public void sendAsynkronOppdragsavstemmingListe(
            @WebParam(name = "sendAsynkronOppdragsavstemmingListeRequest")
                    no.nav.lib.pen.psakpselv.asbo.oppdrag.ASBOPenSendAsynkOppdragsavstemmingListeRequest sendAsynkronOppdragsavstemmingListeRequest
    ) {
        throw new NotImplementedException();
    }

    @Override
    @WebMethod
    @RequestWrapper(localName = "hentOppdragSimulering", targetNamespace = "http://nav-cons-pen-pen-oppdrag/no/nav/inf", className = "no.nav.inf.pen.oppdrag.HentOppdragSimulering")
    @ResponseWrapper(localName = "hentOppdragSimuleringResponse", targetNamespace = "http://nav-cons-pen-pen-oppdrag/no/nav/inf", className = "no.nav.inf.pen.oppdrag.HentOppdragSimuleringResponse")
    @WebResult(name = "hentOppdragSimuleringResponse")
    public no.nav.lib.pen.psakpselv.asbo.oppdrag.ASBOPenSimuleringsresultatLinjeListe hentOppdragSimulering(
            @WebParam(name = "hentOppdragSimuleringRequest")
                    no.nav.lib.pen.psakpselv.asbo.oppdrag.ASBOPenOppdragsmelding hentOppdragSimuleringRequest
    ) {
        return new ASBOPenSimuleringsresultatLinjeListe();
    }
}
