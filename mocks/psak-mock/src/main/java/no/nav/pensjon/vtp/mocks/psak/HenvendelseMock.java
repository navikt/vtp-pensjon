package no.nav.pensjon.vtp.mocks.psak;

import no.nav.inf.psak.henvendelse.ObjectFactory;
import no.nav.inf.psak.henvendelse.PSAKHenvendelse;
import no.nav.lib.pen.psakpselv.asbo.henvendelse.ASBOPenHentStatistikkRequest;
import no.nav.lib.pen.psakpselv.asbo.henvendelse.ASBOPenHenvendelse;
import no.nav.lib.pen.psakpselv.asbo.henvendelse.ASBOPenHenvendelseListe;
import no.nav.lib.pen.psakpselv.asbo.henvendelse.ASBOPenStatistikk;
import no.nav.pensjon.vtp.core.annotations.SoapService;
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException;

import javax.jws.*;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@SoapService(path = "/esb/nav-cons-pen-psak-henvendelseWeb/sca/PSAKHenvendelseWSEXP")
@WebService(targetNamespace = "http://nav-cons-pen-psak-henvendelse/no/nav/inf", name = "PSAKHenvendelse")
@XmlSeeAlso({no.nav.lib.pen.psakpselv.fault.ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.henvendelse.ObjectFactory.class, no.nav.lib.pen.psakpselv.fault.henvendelse.ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.oppgave.ObjectFactory.class, ObjectFactory.class})
@HandlerChain(file = "/Handler-chain.xml")
public class HenvendelseMock implements PSAKHenvendelse {
    @Override
    public ASBOPenHenvendelse lagreHenvendelse(ASBOPenHenvendelse lagreHenvendelseRequest) {
        throw new NotImplementedException();
    }

    @Override
    public ASBOPenStatistikk hentStatistikkAntall(ASBOPenHentStatistikkRequest hentStatstikkAntallRequest) {
        throw new NotImplementedException();
    }

    @Override
    public ASBOPenStatistikk hentStatistikkHenvendelseGjelder(ASBOPenHentStatistikkRequest hentStatistikkHenvendelseGjelderRequest) {
        throw new NotImplementedException();
    }

    @Override
    public ASBOPenStatistikk hentStatistikkPrType(ASBOPenHentStatistikkRequest hentStatistikkPrTypeRequest) {
        throw new NotImplementedException();
    }

    @Override
    public Boolean erTidsbrukAktivert(ASBOPenHenvendelse erTidsbrukAktivertRequest) {
        throw new NotImplementedException();
    }

    @Override
    public ASBOPenHenvendelse opprettHenvendelse(ASBOPenHenvendelse opprettHenvendelseRequest) {
        return opprettHenvendelseRequest;
    }

    @Override
    public ASBOPenStatistikk hentStatistikkPrKanal(ASBOPenHentStatistikkRequest hentStatistikkPrKanalRequest) {
        throw new NotImplementedException();
    }

    @Override
    @WebMethod
    @RequestWrapper(localName = "hentHenvendelseListe", targetNamespace = "http://nav-cons-pen-psak-henvendelse/no/nav/inf", className = "no.nav.inf.psak.henvendelse.HentHenvendelseListe")
    @ResponseWrapper(localName = "hentHenvendelseListeResponse", targetNamespace = "http://nav-cons-pen-psak-henvendelse/no/nav/inf", className = "no.nav.inf.psak.henvendelse.HentHenvendelseListeResponse")
    @WebResult(name = "hentHenvendelseListeResponse")
    public no.nav.lib.pen.psakpselv.asbo.henvendelse.ASBOPenHenvendelseListe hentHenvendelseListe(
            @WebParam(name = "hentHenvendelseListeRequest")
                    no.nav.lib.pen.psakpselv.asbo.henvendelse.ASBOPenHenvendelse hentHenvendelseListeRequest
    ) {
        return new ASBOPenHenvendelseListe();
    }

    @Override
    public ASBOPenHenvendelse hentHenvendelse(ASBOPenHenvendelse hentHenvendelseRequest) {
        throw new NotImplementedException();
    }
}
