package no.nav.pensjon.vtp.mocks.psak;

import no.nav.inf.psak.samhandler.*;
import no.nav.lib.pen.psakpselv.asbo.samhandler.*;
import no.nav.pensjon.vtp.core.annotations.SoapService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.*;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@SoapService(path = "/esb/nav-cons-pen-psak-samhandlerWeb/sca/PSAKSamhandlerWSEXP")
@WebService(
        targetNamespace = "http://nav-cons-pen-psak-samhandler/no/nav/inf",
        name = "PSAKSamhandler"
)
@XmlSeeAlso({no.nav.lib.pen.psakpselv.fault.samhandler.ObjectFactory.class, no.nav.inf.psak.samhandler.ObjectFactory.class, no.nav.lib.pen.psakpselv.fault.ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.samhandler.ObjectFactory.class})
@HandlerChain(file = "/Handler-chain.xml")
public class PsakSamhandlerMock implements PSAKSamhandler {
    private static final Logger LOG = LoggerFactory.getLogger(PsakSamhandlerMock.class);

    @Override
    @WebMethod
    @RequestWrapper(
            localName = "lagreSamhandler",
            targetNamespace = "http://nav-cons-pen-psak-samhandler/no/nav/inf",
            className = "no.nav.inf.psak.samhandler.LagreSamhandler"
    )
    @ResponseWrapper(
            localName = "lagreSamhandlerResponse",
            targetNamespace = "http://nav-cons-pen-psak-samhandler/no/nav/inf",
            className = "no.nav.inf.psak.samhandler.LagreSamhandlerResponse"
    )
    @WebResult(
            name = "lagreSamhandlerResponse",
            targetNamespace = ""
    )
    public ASBOPenSamhandler lagreSamhandler(@WebParam(name = "lagreSamhandlerRequest", targetNamespace = "") ASBOPenSamhandler asboPenSamhandler) throws LagreSamhandlerFaultPenGeneriskMsg, LagreSamhandlerFaultPenSamhandlerIkkeFunnetMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod
    @RequestWrapper(
            localName = "finnSamhandler",
            targetNamespace = "http://nav-cons-pen-psak-samhandler/no/nav/inf",
            className = "no.nav.inf.psak.samhandler.FinnSamhandler"
    )
    @ResponseWrapper(
            localName = "finnSamhandlerResponse",
            targetNamespace = "http://nav-cons-pen-psak-samhandler/no/nav/inf",
            className = "no.nav.inf.psak.samhandler.FinnSamhandlerResponse"
    )
    @WebResult(
            name = "finnSamhandlerResponse",
            targetNamespace = ""
    )
    public ASBOPenSamhandlerListe finnSamhandler(@WebParam(name = "finnSamhandlerRequest", targetNamespace = "") ASBOPenFinnSamhandlerRequest asboPenFinnSamhandlerRequest) throws FinnSamhandlerFaultPenGeneriskMsg {
        LOG.info("Kall mot PsakSamhandlerMock.finnSamhandler");

        ASBOPenSamhandlerListe liste = new ASBOPenSamhandlerListe();
        ASBOPenSamhandler samhandler = createDummySamhandler();
        liste.setSamhandlere(new ASBOPenSamhandler[]{samhandler});

        return liste;
    }

    @Override
    @WebMethod
    @RequestWrapper(
            localName = "opprettSamhandler",
            targetNamespace = "http://nav-cons-pen-psak-samhandler/no/nav/inf",
            className = "no.nav.inf.psak.samhandler.OpprettSamhandler"
    )
    @ResponseWrapper(
            localName = "opprettSamhandlerResponse",
            targetNamespace = "http://nav-cons-pen-psak-samhandler/no/nav/inf",
            className = "no.nav.inf.psak.samhandler.OpprettSamhandlerResponse"
    )
    @WebResult(
            name = "opprettSamhandlerResponse",
            targetNamespace = ""
    )
    public ASBOPenSamhandler opprettSamhandler(@WebParam(name = "opprettSamhandlerRequest", targetNamespace = "") ASBOPenSamhandler asboPenSamhandler) throws OpprettSamhandlerFaultPenGeneriskMsg, OpprettSamhandlerFaultPenOffentligIdIkkeFunnetMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod
    @RequestWrapper(
            localName = "hentSamhandler",
            targetNamespace = "http://nav-cons-pen-psak-samhandler/no/nav/inf",
            className = "no.nav.inf.psak.samhandler.HentSamhandler"
    )
    @ResponseWrapper(
            localName = "hentSamhandlerResponse",
            targetNamespace = "http://nav-cons-pen-psak-samhandler/no/nav/inf",
            className = "no.nav.inf.psak.samhandler.HentSamhandlerResponse"
    )
    @WebResult(
            name = "hentSamhandlerResponse",
            targetNamespace = ""
    )
    public ASBOPenSamhandler hentSamhandler(@WebParam(name = "hentSamhandlerRequest", targetNamespace = "") ASBOPenHentSamhandlerRequest asboPenHentSamhandlerRequest) throws HentSamhandlerFaultPenGeneriskMsg, HentSamhandlerFaultPenSamhandlerIkkeFunnetMsg {
        LOG.info("Kall mot PsakSamhandlerMock.hentSamhandler");
        return createDummySamhandler();
    }

    private ASBOPenSamhandler createDummySamhandler() {
        ASBOPenSamhandler samhandler = new ASBOPenSamhandler();

        samhandler.setSamhandlerType("AFPO");
        samhandler.setNavn("AFP-ORDNINGEN I SPEKTEROMRÅDET");
        samhandler.setIdType(null);
        samhandler.setOffentligId("123412341234");
        ASBOPenAvdeling asboPenAvdeling = new ASBOPenAvdeling();
        asboPenAvdeling.setAvdelingNavn("Ikke samordningspliktig");
        asboPenAvdeling.setAvdelingType("SPES");
        asboPenAvdeling.setAvdelingsnr("1234567890");
        asboPenAvdeling.setIdTSSEkstern("123123123");
        samhandler.setAvdelinger(new ASBOPenAvdeling[]{asboPenAvdeling});
        return samhandler;
    }
}
