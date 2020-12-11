package no.nav.pensjon.vtp.mocks.navansatt

import no.nav.inf.psak.navansatt.HentNAVAnsattEnhetListeFaultPenGeneriskMsg
import no.nav.inf.psak.navansatt.HentNAVAnsattEnhetListeFaultPenNAVAnsattIkkeFunnetMsg
import no.nav.inf.psak.navansatt.HentNAVAnsattFaultPenNAVAnsattIkkeFunnetMsg
import no.nav.inf.psak.navansatt.PSAKNAVAnsatt
import no.nav.lib.pen.psakpselv.asbo.ASBOPenFagomrade
import no.nav.lib.pen.psakpselv.asbo.ASBOPenFagomradeListe
import no.nav.lib.pen.psakpselv.asbo.navansatt.ASBOPenNAVAnsatt
import no.nav.lib.pen.psakpselv.asbo.navansatt.ASBOPenNAVAnsattListe
import no.nav.lib.pen.psakpselv.asbo.navorgenhet.ASBOPenNAVEnhet
import no.nav.lib.pen.psakpselv.asbo.navorgenhet.ASBOPenNAVEnhetListe
import no.nav.pensjon.vtp.core.annotations.SoapService
import no.nav.pensjon.vtp.testmodell.ansatt.AnsatteIndeks
import no.nav.pensjon.vtp.testmodell.enheter.EnheterIndeks
import no.nav.pensjon.vtp.testmodell.enheter.Norg2Modell
import javax.jws.HandlerChain
import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult
import javax.jws.WebService
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper
import javax.xml.ws.soap.Addressing

@SoapService(path = ["/esb/nav-cons-pen-psak-navansattWeb/sca/PSAKNAVAnsattWSEXP"])
@Addressing
@WebService(name = "PSAKNAVAnsatt", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf")
@HandlerChain(file = "/Handler-chain.xml")
class NavAnsattServiceMockImpl(private val ansatteIndeks: AnsatteIndeks, private val enheterIndeks: EnheterIndeks) : PSAKNAVAnsatt {
    @WebMethod
    @RequestWrapper(localName = "hentNAVAnsattEnhetListe", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsattEnhetListe")
    @ResponseWrapper(localName = "hentNAVAnsattEnhetListeResponse", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsattEnhetListeResponse")
    @WebResult(name = "hentNAVAnsattEnhetListeResponse")
    @Throws(HentNAVAnsattEnhetListeFaultPenGeneriskMsg::class, HentNAVAnsattEnhetListeFaultPenNAVAnsattIkkeFunnetMsg::class)
    override fun hentNAVAnsattEnhetListe(@WebParam(name = "hentNAVAnsattEnhetListeRequest") asboPenNAVAnsatt: ASBOPenNAVAnsatt): ASBOPenNAVEnhetListe {
        val ansatt = ansatteIndeks.findByCn(asboPenNAVAnsatt.ansattId) ?: throw HentNAVAnsattEnhetListeFaultPenNAVAnsattIkkeFunnetMsg("NAV-ansatt '" + asboPenNAVAnsatt.ansattId + "' ikke funnet.")

        return ASBOPenNAVEnhetListe().apply {
            navEnheter = enheterIndeks.findByEnhetIdIn(ansatt.enheter).map { asAsboPenNAVEnhet(it) }.toTypedArray()
        }
    }

    private fun asAsboPenNAVEnhet(enhet: Norg2Modell): ASBOPenNAVEnhet {
        return ASBOPenNAVEnhet().apply {
            enhetsId = enhet.enhetId
            enhetsNavn = enhet.navn
            orgEnhetsId = "7000"
        }
    }

    @WebMethod
    @RequestWrapper(localName = "hentNAVAnsattFagomradeListe", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsattFagomradeListe")
    @ResponseWrapper(localName = "hentNAVAnsattFagomradeListeResponse", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsattFagomradeListeResponse")
    @WebResult(name = "hentNAVAnsattFagomradeListeResponse")
    override fun hentNAVAnsattFagomradeListe(@WebParam(name = "hentNAVAnsattFagomradeListeRequest") asboPenNAVAnsatt: ASBOPenNAVAnsatt): ASBOPenFagomradeListe {
        return ASBOPenFagomradeListe().apply {
            fagomrader = arrayOf(
                ASBOPenFagomrade().apply {
                    fagomradeBeskrivelse = "Spesialkompetanse for pepperkakebaking"
                    fagomradeKode = "424242"
                    gyldig = true
                    trekkgruppeKode = "567890"
                }
            )
        }
    }

    @WebMethod
    @RequestWrapper(localName = "hentNAVAnsattListe", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsattListe")
    @ResponseWrapper(localName = "hentNAVAnsattListeResponse", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsattListeResponse")
    @WebResult(name = "hentNAVAnsattListeResponse")
    override fun hentNAVAnsattListe(@WebParam(name = "hentNAVAnsattListeRequest") enhet: ASBOPenNAVEnhet): ASBOPenNAVAnsattListe {
        return ASBOPenNAVAnsattListe().apply {
            navAnsatte = ansatteIndeks.findByEnhetsId(enhet.enhetsId)
                .map {
                    ASBOPenNAVAnsatt().apply {
                        ansattId = it.cn
                        ansattNavn = it.displayName
                        fornavn = it.givenname
                        etternavn = it.sn
                    }
                }
                .toTypedArray()
        }
    }

    @WebMethod
    @RequestWrapper(localName = "hentNAVAnsatt", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsatt")
    @ResponseWrapper(localName = "hentNAVAnsattResponse", targetNamespace = "http://nav-cons-pen-psak-navansatt/no/nav/inf", className = "no.nav.inf.psak.navansatt.HentNAVAnsattResponse")
    @WebResult(name = "hentNAVAnsattResponse")
    @Throws(HentNAVAnsattFaultPenNAVAnsattIkkeFunnetMsg::class)
    override fun hentNAVAnsatt(@WebParam(name = "hentNAVAnsattRequest") asboPenNAVAnsatt: ASBOPenNAVAnsatt): ASBOPenNAVAnsatt {
        val ansatt = ansatteIndeks.findByCn(asboPenNAVAnsatt.ansattId)
            ?: ansatteIndeks.findBySnIgnoreCase("PensjonSaksbehandler")
            ?: throw HentNAVAnsattFaultPenNAVAnsattIkkeFunnetMsg("NAV-ansatt '" + asboPenNAVAnsatt.ansattId + "' ikke funnet")

        asboPenNAVAnsatt.ansattNavn = ansatt.displayName
        asboPenNAVAnsatt.fornavn = ansatt.givenname
        asboPenNAVAnsatt.etternavn = ansatt.sn
        return asboPenNAVAnsatt
    }
}
