package no.nav.pensjon.vtp.mocks.psak

import no.nav.inf.psak.samordning.*
import no.nav.lib.pen.psakpselv.asbo.person.ASBOPenPerson
import no.nav.lib.pen.psakpselv.asbo.samordning.*
import no.nav.lib.pen.psakpselv.fault.ObjectFactory
import no.nav.pensjon.vtp.core.annotations.SoapService
import javax.jws.*
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-cons-pen-psak-samordningWeb/sca/PSAKSamordningWSEXP"])
@WebService(targetNamespace = "http://nav-cons-pen-psak-samordning/no/nav/inf", name = "PSAKSamordning")
@XmlSeeAlso(no.nav.lib.pen.psakpselv.asbo.samordning.ObjectFactory::class, ObjectFactory::class, no.nav.lib.pen.psakpselv.asbo.ObjectFactory::class, no.nav.lib.pen.psakpselv.fault.samordning.ObjectFactory::class, no.nav.lib.pen.psakpselv.asbo.tjenestepensjon.ObjectFactory::class, no.nav.lib.pen.psakpselv.asbo.person.ObjectFactory::class, no.nav.inf.psak.samordning.ObjectFactory::class)
@HandlerChain(file = "/Handler-chain.xml")
class PSAKSamordningMock : PSAKSamordning {
    @WebMethod
    @RequestWrapper(localName = "hentSamordningsdata", targetNamespace = "http://nav-cons-pen-psak-samordning/no/nav/inf", className = "no.nav.inf.psak.samordning.HentSamordningsdata")
    @ResponseWrapper(localName = "hentSamordningsdataResponse", targetNamespace = "http://nav-cons-pen-psak-samordning/no/nav/inf", className = "no.nav.inf.psak.samordning.HentSamordningsdataResponse")
    @WebResult(name = "hentSamordningsdataResponse", targetNamespace = "")
    @Throws(HentSamordningsdataFaultPersonIkkeFunnetMsg::class, HentSamordningsdataFaultPenYtelseIkkeIverksattMsg::class, HentSamordningsdataFaultPenGeneriskMsg::class)
    override fun hentSamordningsdata(@WebParam(name = "hentSamordningsdataRequest", targetNamespace = "") request: ASBOPenHentSamordningsdataRequest): ASBOPenSamordningsdata {
        val data = ASBOPenSamordningsdata()
        val asboPenPerson = ASBOPenPerson()
        asboPenPerson.fodselsnummer = request.fnr
        data.person = asboPenPerson
        return data
    }

    @WebMethod
    @RequestWrapper(localName = "opprettTPSamordning", targetNamespace = "http://nav-cons-pen-psak-samordning/no/nav/inf", className = "no.nav.inf.psak.samordning.OpprettTPSamordning")
    @ResponseWrapper(localName = "opprettTPSamordningResponse", targetNamespace = "http://nav-cons-pen-psak-samordning/no/nav/inf", className = "no.nav.inf.psak.samordning.OpprettTPSamordningResponse")
    @WebResult(name = "opprettTPSamordningResponse", targetNamespace = "")
    @Throws(OpprettTPSamordningFaultPenPersonIkkeFunnetMsg::class, OpprettTPSamordningFautPenGeneriskMsg::class, OpprettTPSamordningFaultPenYtelseAlleredeRegistrertMsg::class)
    override fun opprettTPSamordning(@WebParam(name = "opprettTPSamordningRequest", targetNamespace = "") request: ASBOPenOpprettTPSamordningRequest): ASBOPenSamordningsdata {
        val data = ASBOPenSamordningsdata()
        val asboPenPerson = ASBOPenPerson()
        asboPenPerson.fodselsnummer = request.fnr
        data.person = asboPenPerson
        return data
    }

    @WebMethod
    @RequestWrapper(localName = "slettTPSamordning", targetNamespace = "http://nav-cons-pen-psak-samordning/no/nav/inf", className = "no.nav.inf.psak.samordning.SlettTPSamordning")
    @ResponseWrapper(localName = "slettTPSamordningResponse", targetNamespace = "http://nav-cons-pen-psak-samordning/no/nav/inf", className = "no.nav.inf.psak.samordning.SlettTPSamordningResponse")
    @Throws(SlettTPSamordningFaultPenKombinasjonInputMsg::class, SlettTPSamordningFaultPersonIkkeFunnetMsg::class, SlettTPSamordningFaultPenGeneriskMsg::class)
    override fun slettTPSamordning(@WebParam(name = "slettTPSamordningRequest", targetNamespace = "") request: ASBOPenSlettTPSamordningRequest) {
        TODO("Ikke implementert")
    }

    @WebMethod
    @RequestWrapper(localName = "hentSamordningsVedtaksListe", targetNamespace = "http://nav-cons-pen-psak-samordning/no/nav/inf", className = "no.nav.inf.psak.samordning.HentSamordningsVedtaksListe")
    @ResponseWrapper(localName = "hentSamordningsVedtaksListeResponse", targetNamespace = "http://nav-cons-pen-psak-samordning/no/nav/inf", className = "no.nav.inf.psak.samordning.HentSamordningsVedtaksListeResponse")
    @WebResult(name = "hentSamordningsVedtaksListeResponse", targetNamespace = "")
    @Throws(HentSamordningsVedtaksListeFaultPenSamIdIkkeGyldigMsg::class, HentSamordningsVedtaksListeFaultPenSakIdIkkeGyldigMsg::class, HentSamordningsVedtaksListeFaultPersonIkkeFunnetMsg::class, HentSamordningsVedtaksListeFaultPenVedtakIkkeGyldigMsg::class, HentSamordningsVedtaksListeFaultPenGeneriskMsg::class)
    override fun hentSamordningsVedtaksListe(@WebParam(name = "hentSamordningsVedtaksListeRequest", targetNamespace = "") request: ASBOPenHentSamordningsVedtaksListeRequest): ASBOPenSamordningPerson {
        val person = ASBOPenSamordningPerson()
        person.fnr = request.fnr
        return person
    }

    @WebMethod
    @RequestWrapper(localName = "opprettRefusjonskrav", targetNamespace = "http://nav-cons-pen-psak-samordning/no/nav/inf", className = "no.nav.inf.psak.samordning.OpprettRefusjonskrav")
    @ResponseWrapper(localName = "opprettRefusjonskravResponse", targetNamespace = "http://nav-cons-pen-psak-samordning/no/nav/inf", className = "no.nav.inf.psak.samordning.OpprettRefusjonskravResponse")
    @Throws(OpprettRefusjonskravFaultRefKravAlleredeRegElUtenforFristMsg::class)
    override fun opprettRefusjonskrav(@WebParam(name = "opprettRefusjonskravRequest", targetNamespace = "") request: ASBOPenOpprettRefusjonskravRequest) {
        TODO("Ikke implementert")
    }
}