package no.nav.pensjon.vtp.mocks.psak;

import static java.util.Optional.ofNullable;

import no.nav.pensjon.vtp.testmodell.enheter.EnheterIndeks;
import no.nav.pensjon.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.inf.psak.navorgenhet.ObjectFactory;
import no.nav.inf.psak.navorgenhet.*;
import no.nav.lib.pen.psakpselv.asbo.navorgenhet.*;

import javax.jws.*;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import java.io.IOException;
import java.util.Collections;

import static no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl.getInstance;
import static no.nav.pensjon.vtp.mocks.psak.util.PenNAVEnhetUtil.getAsboPenNAVEnhet;

@WebService(targetNamespace = "http://nav-cons-pen-psak-navorgenhet/no/nav/inf", name = "PSAKNAVOrgEnhet")
@XmlSeeAlso({no.nav.lib.pen.psakpselv.fault.ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.ObjectFactory.class, no.nav.lib.pen.psakpselv.fault.navorgenhet.ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.navorgenhet.ObjectFactory.class, ObjectFactory.class})
@HandlerChain(file = "/Handler-chain.xml")
public class PsakNavOrgEnhetMock implements PSAKNAVOrgEnhet {
    private final EnheterIndeks enheterIndeks;

    public PsakNavOrgEnhetMock() {
        try {
            this.enheterIndeks = getInstance(BasisdataProviderFileImpl.getInstance()).getEnheterIndeks();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ASBOPenNAVEnhetListe hentSpesialenhetTilPerson(ASBOPenHentSpesialEnhetTilPersonRequest hentSpesialenhetTilPersonRequest) throws HentSpesialenhetTilPersonFaultPenNAVEnhetIkkeFunnetMsg, HentSpesialenhetTilPersonFaultPenPersonIkkeFunnetMsg, HentSpesialenhetTilPersonFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod
    @RequestWrapper(localName = "hentNAVEnhet", targetNamespace = "http://nav-cons-pen-psak-navorgenhet/no/nav/inf", className = "no.nav.inf.psak.navorgenhet.HentNAVEnhet")
    @ResponseWrapper(localName = "hentNAVEnhetResponse", targetNamespace = "http://nav-cons-pen-psak-navorgenhet/no/nav/inf", className = "no.nav.inf.psak.navorgenhet.HentNAVEnhetResponse")
    @WebResult(name = "hentNAVEnhetResponse")
    public no.nav.lib.pen.psakpselv.asbo.navorgenhet.ASBOPenNAVEnhet hentNAVEnhet(
            @WebParam(name = "hentNAVEnhetRequest")
                    no.nav.lib.pen.psakpselv.asbo.navorgenhet.ASBOPenNAVEnhet hentNAVEnhetRequest
    ) throws HentNAVEnhetFaultPenNAVEnhetIkkeFunnetMsg, HentNAVEnhetFaultPenGeneriskMsg {
        return enheterIndeks.finnByEnhetId(ofNullable(hentNAVEnhetRequest.getEnhetsId()).orElseThrow(() -> new HentNAVEnhetFaultPenGeneriskMsg("EnhetsId was null")))
                .map(m -> {
                    ASBOPenNAVEnhet e  = new ASBOPenNAVEnhet();
                    e.setEnhetsId(m.getEnhetId());
                    e.setEnhetsNavn(m.getNavn());
                    return e;
                })
                .orElseThrow(HentNAVEnhetFaultPenNAVEnhetIkkeFunnetMsg::new);
    }

    @Override
    public ASBOPenNAVEnhetListe hentNAVEnhetGruppeListe(ASBOPenNAVEnhet hentNAVEnhetGruppeListeRequest) throws HentNAVEnhetGruppeListeFaultPenNAVEnhetIkkeFunnetMsg, HentNAVEnhetGruppeListeFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    @WebMethod
    @RequestWrapper(localName = "finnNAVEnhet", targetNamespace = "http://nav-cons-pen-psak-navorgenhet/no/nav/inf", className = "no.nav.inf.psak.navorgenhet.FinnNAVEnhet")
    @ResponseWrapper(localName = "finnNAVEnhetResponse", targetNamespace = "http://nav-cons-pen-psak-navorgenhet/no/nav/inf", className = "no.nav.inf.psak.navorgenhet.FinnNAVEnhetResponse")
    @WebResult(name = "finnNAVEnhetResponse", targetNamespace = "")
    public no.nav.lib.pen.psakpselv.asbo.navorgenhet.ASBOPenNAVEnhetListe finnNAVEnhet(
            @WebParam(name = "finnNAVEnhetRequest", targetNamespace = "")
                    no.nav.lib.pen.psakpselv.asbo.navorgenhet.ASBOPenFinnNAVEnhetRequest finnNAVEnhetRequest
    ) throws FinnNAVEnhetFaultPenGeneriskMsg {
        ASBOPenNAVEnhetListe asboPenNAVEnhetListe = new ASBOPenNAVEnhetListe();
        asboPenNAVEnhetListe.setNAVEnheter(Collections.singletonList(getAsboPenNAVEnhet()).toArray(ASBOPenNAVEnhet[]::new));
        return asboPenNAVEnhetListe;
    }

    @Override
    public ASBOPenNAVEnhetListe hentNAVEnhetListe(ASBOPenHentNAVEnhetListeRequest hentNAVEnhetListeRequest) throws HentNAVEnhetListeFaultPenNAVEnhetIkkeFunnetMsg, HentNAVEnhetListeFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenNAVEnhetListe hentForvaltningsenhetTilPersonListe(ASBOPenHentForvaltningsenhetTilPersonListeRequest hentForvaltningsenhetTilPersonListeRequest) throws HentForvaltningsenhetTilPersonListeFaultPenNAVEnhetIkkeFunnetMsg, HentForvaltningsenhetTilPersonListeFaultPenPersonManglerEnhetMsg, HentForvaltningsenhetTilPersonListeFaultPenPersonIkkeFunnetMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }

    @Override
    public ASBOPenNAVEnhetListe finnArenaNAVEnhetListe(ASBOPenFinnArenaNAVEnhetListeRequest finnArenaNAVEnhetListeRequest) throws FinnArenaNAVEnhetListeFaultPenGeneriskMsg {
        throw new UnsupportedOperationException("Ikke implementert");
    }
}
