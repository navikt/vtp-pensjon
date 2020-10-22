package no.nav.pensjon.vtp.mocks.psak;

import no.nav.inf.pen.inntekt.HentInntektListeFaultPenGeneriskMsg;
import no.nav.inf.pen.inntekt.ObjectFactory;
import no.nav.inf.pen.inntekt.PENInntekt;
import no.nav.lib.pen.psakpselv.asbo.inntekt.ASBOPenInntekt;
import no.nav.lib.pen.psakpselv.asbo.inntekt.ASBOPenInntektListe;

import javax.jws.*;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(targetNamespace = "http://nav-cons-pen-pen-inntekt/no/nav/inf", name = "PENInntekt")
@XmlSeeAlso({no.nav.lib.pen.psakpselv.fault.ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.ObjectFactory.class, no.nav.lib.pen.psakpselv.fault.inntekt.ObjectFactory.class, no.nav.lib.pen.psakpselv.asbo.inntekt.ObjectFactory.class, ObjectFactory.class})
@HandlerChain(file = "/Handler-chain.xml")
public class PENInntektMock implements PENInntekt {

    @Override
    @WebMethod
    @RequestWrapper(localName = "hentInntektListe", targetNamespace = "http://nav-cons-pen-pen-inntekt/no/nav/inf", className = "no.nav.inf.pen.inntekt.HentInntektListe")
    @ResponseWrapper(localName = "hentInntektListeResponse", targetNamespace = "http://nav-cons-pen-pen-inntekt/no/nav/inf", className = "no.nav.inf.pen.inntekt.HentInntektListeResponse")
    @WebResult(name = "hentInntektListeResponse", targetNamespace = "")
    public no.nav.lib.pen.psakpselv.asbo.inntekt.ASBOPenInntektListe hentInntektListe(
            @WebParam(name = "hentInntektListeRequest", targetNamespace = "")
                    no.nav.lib.pen.psakpselv.asbo.inntekt.ASBOPenHentInntektListeRequest hentInntektListeRequest
    ) throws HentInntektListeFaultPenGeneriskMsg {
        ASBOPenInntektListe asboPenInntektListe = new ASBOPenInntektListe();
        asboPenInntektListe.setInntekter(new ASBOPenInntekt[0]);
        return asboPenInntektListe;
    }
}
