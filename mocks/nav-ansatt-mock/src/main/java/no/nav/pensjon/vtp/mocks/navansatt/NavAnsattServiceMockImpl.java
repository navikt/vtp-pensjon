package no.nav.pensjon.vtp.mocks.navansatt;

import no.nav.pensjon.vtp.core.annotations.SoapService;
import no.nav.pensjon.vtp.testmodell.ansatt.AnsatteIndeks;
import no.nav.pensjon.vtp.testmodell.ansatt.NAVAnsatt;
import no.nav.pensjon.vtp.testmodell.enheter.EnheterIndeks;
import no.nav.pensjon.vtp.testmodell.enheter.Norg2Modell;
import no.nav.inf.psak.navansatt.*;
import no.nav.lib.pen.psakpselv.asbo.ASBOPenFagomrade;
import no.nav.lib.pen.psakpselv.asbo.ASBOPenFagomradeListe;
import no.nav.lib.pen.psakpselv.asbo.navansatt.ASBOPenNAVAnsatt;
import no.nav.lib.pen.psakpselv.asbo.navansatt.ASBOPenNAVAnsattListe;
import no.nav.lib.pen.psakpselv.asbo.navorgenhet.ASBOPenNAVEnhet;
import no.nav.lib.pen.psakpselv.asbo.navorgenhet.ASBOPenNAVEnhetListe;

import javax.jws.*;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;
import java.util.ArrayList;
import java.util.List;

@SoapService(path = "/esb/nav-cons-pen-psak-navansattWeb/sca/PSAKNAVAnsattWSEXP")
@Addressing
@WebService(name = "PSAKNAVAnsatt", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf")
@HandlerChain(file = "/Handler-chain.xml")
public class NavAnsattServiceMockImpl implements PSAKNAVAnsatt {
    private final AnsatteIndeks ansatteIndeks;
    private final EnheterIndeks enheterIndeks;

    public NavAnsattServiceMockImpl(AnsatteIndeks ansatteIndeks, EnheterIndeks enheterIndeks) {
        this.ansatteIndeks = ansatteIndeks;
        this.enheterIndeks = enheterIndeks;
    }

    @WebMethod
    @RequestWrapper(localName = "hentNAVAnsattEnhetListe", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsattEnhetListe")
    @ResponseWrapper(localName = "hentNAVAnsattEnhetListeResponse", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsattEnhetListeResponse")
    @WebResult(name = "hentNAVAnsattEnhetListeResponse")
    @Override
    public ASBOPenNAVEnhetListe hentNAVAnsattEnhetListe(@WebParam(name = "hentNAVAnsattEnhetListeRequest") ASBOPenNAVAnsatt asboPenNAVAnsatt) throws HentNAVAnsattEnhetListeFaultPenGeneriskMsg, HentNAVAnsattEnhetListeFaultPenNAVAnsattIkkeFunnetMsg {
        NAVAnsatt ansatt = ansatteIndeks.hentNAVAnsatt(asboPenNAVAnsatt.getAnsattId()).orElseThrow(() -> new HentNAVAnsattEnhetListeFaultPenNAVAnsattIkkeFunnetMsg("NAV-ansatt '" + asboPenNAVAnsatt.getAnsattId() + "' ikke funnet."));

        List<ASBOPenNAVEnhet> enheter = new ArrayList<>();
        for (String enhetId: ansatt.enheter) {
            Norg2Modell enhet = enheterIndeks.finnByEnhetId(enhetId).orElseThrow(() -> new HentNAVAnsattEnhetListeFaultPenGeneriskMsg("Enhet ikke funnet: " + enhetId));
            ASBOPenNAVEnhet asboEnhet = new ASBOPenNAVEnhet();
            asboEnhet.setEnhetsId(enhet.getEnhetId());
            asboEnhet.setEnhetsNavn(enhet.getNavn());
            asboEnhet.setOrgEnhetsId("7000");
            enheter.add(asboEnhet);
        }

        ASBOPenNAVEnhetListe response = new ASBOPenNAVEnhetListe();
        response.setNAVEnheter(enheter.toArray(ASBOPenNAVEnhet[]::new));
        return response;
    }

    @WebMethod
    @RequestWrapper(localName = "hentNAVAnsattFagomradeListe", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsattFagomradeListe")
    @ResponseWrapper(localName = "hentNAVAnsattFagomradeListeResponse", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsattFagomradeListeResponse")
    @WebResult(name = "hentNAVAnsattFagomradeListeResponse")
    @Override
    public ASBOPenFagomradeListe hentNAVAnsattFagomradeListe(@WebParam(name = "hentNAVAnsattFagomradeListeRequest") ASBOPenNAVAnsatt asboPenNAVAnsatt) {
        ASBOPenFagomradeListe liste = new ASBOPenFagomradeListe();
        ASBOPenFagomrade omrade = new ASBOPenFagomrade();
        omrade.setFagomradeBeskrivelse("Spesialkompetanse for pepperkakebaking");
        omrade.setFagomradeKode("424242");
        omrade.setGyldig(true);
        omrade.setTrekkgruppeKode("567890");
        liste.setFagomrader(new ASBOPenFagomrade[] { omrade });
        return liste;
    }

    @WebMethod
    @RequestWrapper(localName = "hentNAVAnsattListe", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsattListe")
    @ResponseWrapper(localName = "hentNAVAnsattListeResponse", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsattListeResponse"
    )
    @WebResult(name = "hentNAVAnsattListeResponse")
    @Override
    public ASBOPenNAVAnsattListe hentNAVAnsattListe(@WebParam(name = "hentNAVAnsattListeRequest") ASBOPenNAVEnhet enhet) {
        ASBOPenNAVAnsattListe liste = new ASBOPenNAVAnsattListe();
        liste.setNAVAnsatte(ansatteIndeks.hentAlleAnsatte()
                .filter(ansatt -> ansatt.enheter.contains(enhet.getEnhetsId()))
                .map(ansatt -> {
            ASBOPenNAVAnsatt asboAnsatt = new ASBOPenNAVAnsatt();
            asboAnsatt.setAnsattId(ansatt.cn);
            asboAnsatt.setAnsattNavn(ansatt.displayName);
            asboAnsatt.setFornavn(ansatt.givenname);
            asboAnsatt.setEtternavn(ansatt.sn);
            return asboAnsatt;
        }).toArray(ASBOPenNAVAnsatt[]::new));
        return liste;
    }

    @WebMethod
    @RequestWrapper(localName = "hentNAVAnsatt", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsatt")
    @ResponseWrapper(localName = "hentNAVAnsattResponse", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsattResponse")
    @WebResult(name = "hentNAVAnsattResponse")
    @Override
    public ASBOPenNAVAnsatt hentNAVAnsatt(@WebParam(name = "hentNAVAnsattRequest") ASBOPenNAVAnsatt asboPenNAVAnsatt) throws HentNAVAnsattFaultPenNAVAnsattIkkeFunnetMsg {
        NAVAnsatt ansatt = ansatteIndeks.hentNAVAnsatt(asboPenNAVAnsatt.getAnsattId()).orElseThrow(() -> new HentNAVAnsattFaultPenNAVAnsattIkkeFunnetMsg("NAV-ansatt '" + asboPenNAVAnsatt.getAnsattId() + "' ikke funnet."));
        asboPenNAVAnsatt.setAnsattNavn(ansatt.displayName);
        asboPenNAVAnsatt.setFornavn(ansatt.givenname);
        asboPenNAVAnsatt.setEtternavn(ansatt.sn);
        return asboPenNAVAnsatt;
    }
}
