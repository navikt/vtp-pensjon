package no.nav;

import static java.util.Optional.ofNullable;

import static no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl.getInstance;

import no.nav.pensjon.vtp.testmodell.enheter.EnheterIndeks;
import no.nav.pensjon.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.inf.pen.navorgenhet.*;
import no.nav.lib.pen.psakpselv.asbo.navorgenhet.*;
import no.nav.lib.pen.psakpselv.fault.ObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.*;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import java.io.IOException;

@WebService(
        targetNamespace = "http://nav-cons-pen-pen-navorgenhet/no/nav/inf/PENNAVOrgEnhet",
        name = "PENNAVOrgEnhet"
)
@HandlerChain(file = "/Handler-chain.xml")
@XmlSeeAlso({ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.ObjectFactory.class, no.nav.lib.pen.psakpselv.fault.navorgenhet.ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.navorgenhet.ObjectFactory.class, no.nav.inf.pen.navorgenhet.ObjectFactory.class})
public class PenNavOrgEnhetMock implements PENNAVOrgEnhet {
    private static final Logger LOG = LoggerFactory.getLogger(PenNavOrgEnhetMock.class);

    private final EnheterIndeks enheterIndeks;

    public PenNavOrgEnhetMock() {
        try {
            this.enheterIndeks = getInstance(BasisdataProviderFileImpl.getInstance()).getEnheterIndeks();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @WebMethod
    @RequestWrapper(
            localName = "hentNAVEnhet",
            targetNamespace = "http://nav-cons-pen-pen-navorgenhet/no/nav/inf/PENNAVOrgEnhet",
            className = "no.nav.inf.pen.navorgenhet.HentNAVEnhet"
    )
    @ResponseWrapper(
            localName = "hentNAVEnhetResponse",
            targetNamespace = "http://nav-cons-pen-pen-navorgenhet/no/nav/inf/PENNAVOrgEnhet",
            className = "no.nav.inf.pen.navorgenhet.HentNAVEnhetResponse"
    )
    @WebResult(
            name = "hentNAVEnhetResponse"
    )
    public ASBOPenNAVEnhet hentNAVEnhet(@WebParam(name = "hentNAVEnhetRequest") ASBOPenNAVEnhet asboPenNAVEnhet) throws HentNAVEnhetFaultPenNAVEnhetIkkeFunnetMsg, HentNAVEnhetFaultPenGeneriskMsg {
        LOG.info("Kall mot PenNavOrgEnhetMock hentNAVEnhet med " + ofNullable(asboPenNAVEnhet).map(ASBOPenNAVEnhet::getEnhetsId).orElse(""));
        return enheterIndeks.finnByEnhetId(ofNullable(asboPenNAVEnhet.getEnhetsId()).orElseThrow(() -> new HentNAVEnhetFaultPenGeneriskMsg("EnhetsId was null")))
                .map(m -> {
                    ASBOPenNAVEnhet e  = new ASBOPenNAVEnhet();
                    e.setEnhetsId(m.getEnhetId());
                    e.setEnhetsNavn(m.getNavn());
                    return e;
                })
                .orElseThrow(HentNAVEnhetFaultPenNAVEnhetIkkeFunnetMsg::new);
    }

    @WebMethod
    @RequestWrapper(
            localName = "hentNAVEnhetListe",
            targetNamespace = "http://nav-cons-pen-pen-navorgenhet/no/nav/inf/PENNAVOrgEnhet",
            className = "no.nav.inf.pen.navorgenhet.HentNAVEnhetListe"
    )
    @ResponseWrapper(
            localName = "hentNAVEnhetListeResponse",
            targetNamespace = "http://nav-cons-pen-pen-navorgenhet/no/nav/inf/PENNAVOrgEnhet",
            className = "no.nav.inf.pen.navorgenhet.HentNAVEnhetListeResponse"
    )
    @WebResult(
            name = "hentNAVEnhetListeResponse",
            targetNamespace = ""
    )
    public ASBOPenNAVEnhetListe hentNAVEnhetListe(@WebParam(name = "hentNAVEnhetListeRequest", targetNamespace = "") ASBOPenHentNAVEnhetListeRequest asboPenHentNAVEnhetListeRequest) throws HentNAVEnhetListeFaultPenNAVEnhetIkkeFunnetMsg, HentNAVEnhetListeFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod
    @RequestWrapper(
            localName = "hentForvaltningsenhetTilPersonListe",
            targetNamespace = "http://nav-cons-pen-pen-navorgenhet/no/nav/inf/PENNAVOrgEnhet",
            className = "no.nav.inf.pen.navorgenhet.HentForvaltningsenhetTilPersonListe"
    )
    @ResponseWrapper(
            localName = "hentForvaltningsenhetTilPersonListeResponse",
            targetNamespace = "http://nav-cons-pen-pen-navorgenhet/no/nav/inf/PENNAVOrgEnhet",
            className = "no.nav.inf.pen.navorgenhet.HentForvaltningsenhetTilPersonListeResponse"
    )
    @WebResult(
            name = "hentForvaltningsenhetTilPersonListeResponse",
            targetNamespace = ""
    )
    public ASBOPenNAVEnhetListe hentForvaltningsenhetTilPersonListe(@WebParam(name = "hentForvaltningsenhetTilPersonListeRequest", targetNamespace = "") ASBOPenHentForvaltningsenhetTilPersonListeRequest asboPenHentForvaltningsenhetTilPersonListeRequest) throws HentForvaltningsenhetTilPersonListeFaultPenNAVEnhetIkkeFunnetMsg, HentForvaltningsenhetTilPersonListeFaultPenPersonManglerEnhetMsg, HentForvaltningsenhetTilPersonListeFaultPenPersonIkkeFunnetMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override@WebMethod
    @RequestWrapper(
            localName = "hentSpesialenhetTilPerson",
            targetNamespace = "http://nav-cons-pen-pen-navorgenhet/no/nav/inf/PENNAVOrgEnhet",
            className = "no.nav.inf.pen.navorgenhet.HentSpesialenhetTilPerson"
    )
    @ResponseWrapper(
            localName = "hentSpesialenhetTilPersonResponse",
            targetNamespace = "http://nav-cons-pen-pen-navorgenhet/no/nav/inf/PENNAVOrgEnhet",
            className = "no.nav.inf.pen.navorgenhet.HentSpesialenhetTilPersonResponse"
    )
    @WebResult(
            name = "hentSpesialenhetTilPersonResponse",
            targetNamespace = ""
    )
    public ASBOPenNAVEnhetListe hentSpesialenhetTilPerson(@WebParam(name = "hentSpesialenhetTilPersonRequest", targetNamespace = "") ASBOPenHentSpesialEnhetTilPersonRequest asboPenHentSpesialEnhetTilPersonRequest) throws HentSpesialenhetTilPersonFaultPenNAVEnhetIkkeFunnetMsg, HentSpesialenhetTilPersonFaultPenGeneriskMsg, HentSpesialenhetTilPersonFaultPenPersonIkkeFunnetMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }
}
