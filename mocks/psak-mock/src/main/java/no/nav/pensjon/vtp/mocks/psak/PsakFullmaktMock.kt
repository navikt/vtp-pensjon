package no.nav.pensjon.vtp.mocks.psak

import no.nav.inf.psak.fullmakt.*
import no.nav.lib.pen.psakpselv.asbo.fullmakt.ASBOPenFullmakt
import no.nav.lib.pen.psakpselv.asbo.fullmakt.ASBOPenFullmaktListe
import no.nav.lib.pen.psakpselv.asbo.fullmakt.ASBOPenHentFullmaktListeRequest
import no.nav.pensjon.vtp.core.annotations.SoapService
import javax.jws.HandlerChain
import javax.jws.WebMethod
import javax.jws.WebResult
import javax.jws.WebService
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-cons-pen-psak-fullmaktWeb/sca/PSAKFullmaktWSEXP"])
@WebService(targetNamespace = "http://nav-cons-pen-psak-fullmakt/no/nav/inf", name = "PSAKFullmakt")
@XmlSeeAlso(no.nav.lib.pen.psakpselv.fault.fullmakt.ObjectFactory::class, no.nav.lib.pen.psakpselv.fault.ObjectFactory::class, no.nav.lib.pen.psakpselv.asbo.fullmakt.ObjectFactory::class, no.nav.inf.psak.fullmakt.ObjectFactory::class)
@HandlerChain(file = "/Handler-chain.xml")
class PsakFullmaktMock : PSAKFullmakt {

    @WebMethod
    @RequestWrapper(localName = "hentFullmaktListe", targetNamespace = "http://nav-cons-pen-psak-fullmakt/no/nav/inf", className = "no.nav.inf.psak.fullmakt.HentFullmaktListe")
    @ResponseWrapper(localName = "hentFullmaktListeResponse", targetNamespace = "http://nav-cons-pen-psak-fullmakt/no/nav/inf", className = "no.nav.inf.psak.fullmakt.HentFullmaktListeResponse")
    @WebResult(name = "hentFullmaktListeResponse", targetNamespace = "")
    @Throws(HentFullmaktListeFaultPenGeneriskMsg::class)
    override fun hentFullmaktListe(asboPenHentFullmaktListeRequest: ASBOPenHentFullmaktListeRequest): ASBOPenFullmaktListe {
        return ASBOPenFullmaktListe()
    }

    @WebMethod
    @RequestWrapper(localName = "lagreFullmakt", targetNamespace = "http://nav-cons-pen-psak-fullmakt/no/nav/inf", className = "no.nav.inf.psak.fullmakt.LagreFullmakt")
    @ResponseWrapper(localName = "lagreFullmaktResponse", targetNamespace = "http://nav-cons-pen-psak-fullmakt/no/nav/inf", className = "no.nav.inf.psak.fullmakt.LagreFullmaktResponse")
    @WebResult(name = "lagreFullmaktResponse", targetNamespace = "")
    @Throws(LagreFullmaktFaultPenGeneriskMsg::class, LagreFullmaktFaultPenFullmaktIkkeFunnetMsg::class)
    override fun lagreFullmakt(asboPenFullmakt: ASBOPenFullmakt): ASBOPenFullmakt {
        return asboPenFullmakt
    }

    @WebMethod
    @RequestWrapper(localName = "slettFullmakt", targetNamespace = "http://nav-cons-pen-psak-fullmakt/no/nav/inf", className = "no.nav.inf.psak.fullmakt.SlettFullmakt")
    @ResponseWrapper(localName = "slettFullmaktResponse", targetNamespace = "http://nav-cons-pen-psak-fullmakt/no/nav/inf", className = "no.nav.inf.psak.fullmakt.SlettFullmaktResponse")
    @WebResult(name = "slettFullmaktResponse", targetNamespace = "")
    @Throws(SlettFullmaktFaultPenGeneriskMsg::class, SlettFullmaktFaultPenFullmaktIkkeFunnetMsg::class)
    override fun slettFullmakt(asboPenFullmakt: ASBOPenFullmakt): ASBOPenFullmakt {
        return asboPenFullmakt
    }

    @WebMethod
    @RequestWrapper(localName = "opprettFullmakt", targetNamespace = "http://nav-cons-pen-psak-fullmakt/no/nav/inf", className = "no.nav.inf.psak.fullmakt.OpprettFullmakt")
    @ResponseWrapper(localName = "opprettFullmaktResponse", targetNamespace = "http://nav-cons-pen-psak-fullmakt/no/nav/inf", className = "no.nav.inf.psak.fullmakt.OpprettFullmaktResponse")
    @WebResult(name = "opprettFullmaktResponse", targetNamespace = "")
    @Throws(OpprettFullmaktFaultPenGeneriskMsg::class)
    override fun opprettFullmakt(asboPenFullmakt: ASBOPenFullmakt): ASBOPenFullmakt {
        return asboPenFullmakt
    }
}