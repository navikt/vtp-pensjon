package no.nav.pensjon.vtp.mocks.psak

import no.nav.inf.psak.henvendelse.PSAKHenvendelse
import no.nav.lib.pen.psakpselv.asbo.henvendelse.ASBOPenHentStatistikkRequest
import no.nav.lib.pen.psakpselv.asbo.henvendelse.ASBOPenHenvendelse
import no.nav.lib.pen.psakpselv.asbo.henvendelse.ASBOPenHenvendelseListe
import no.nav.lib.pen.psakpselv.asbo.henvendelse.ASBOPenStatistikk
import no.nav.lib.pen.psakpselv.fault.ObjectFactory
import no.nav.pensjon.vtp.core.annotations.SoapService
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import javax.jws.HandlerChain
import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult
import javax.jws.WebService
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-cons-pen-psak-henvendelseWeb/sca/PSAKHenvendelseWSEXP"])
@WebService(targetNamespace = "http://nav-cons-pen-psak-henvendelse/no/nav/inf", name = "PSAKHenvendelse")
@XmlSeeAlso(
    ObjectFactory::class,
    no.nav.lib.pen.psakpselv.asbo.ObjectFactory::class,
    no.nav.lib.pen.psakpselv.asbo.henvendelse.ObjectFactory::class,
    no.nav.lib.pen.psakpselv.fault.henvendelse.ObjectFactory::class,
    no.nav.lib.pen.psakpselv.asbo.oppgave.ObjectFactory::class,
    no.nav.inf.psak.henvendelse.ObjectFactory::class
)
@HandlerChain(file = "/Handler-chain.xml")
class HenvendelseMock : PSAKHenvendelse {
    override fun lagreHenvendelse(lagreHenvendelseRequest: ASBOPenHenvendelse): ASBOPenHenvendelse =
        throw NotImplementedException()

    override fun hentStatistikkAntall(hentStatstikkAntallRequest: ASBOPenHentStatistikkRequest): ASBOPenStatistikk =
        throw NotImplementedException()

    override fun hentStatistikkHenvendelseGjelder(hentStatistikkHenvendelseGjelderRequest: ASBOPenHentStatistikkRequest): ASBOPenStatistikk =
        throw NotImplementedException()

    override fun hentStatistikkPrType(hentStatistikkPrTypeRequest: ASBOPenHentStatistikkRequest): ASBOPenStatistikk =
        throw NotImplementedException()

    override fun erTidsbrukAktivert(erTidsbrukAktivertRequest: ASBOPenHenvendelse) =
        throw NotImplementedException()

    override fun opprettHenvendelse(opprettHenvendelseRequest: ASBOPenHenvendelse) =
        opprettHenvendelseRequest

    override fun hentStatistikkPrKanal(hentStatistikkPrKanalRequest: ASBOPenHentStatistikkRequest) =
        throw NotImplementedException()

    @WebMethod
    @RequestWrapper(
        localName = "hentHenvendelseListe",
        targetNamespace = "http://nav-cons-pen-psak-henvendelse/no/nav/inf",
        className = "no.nav.inf.psak.henvendelse.HentHenvendelseListe"
    )
    @ResponseWrapper(
        localName = "hentHenvendelseListeResponse",
        targetNamespace = "http://nav-cons-pen-psak-henvendelse/no/nav/inf",
        className = "no.nav.inf.psak.henvendelse.HentHenvendelseListeResponse"
    )
    @WebResult(name = "hentHenvendelseListeResponse")
    override fun hentHenvendelseListe(
        @WebParam(name = "hentHenvendelseListeRequest") hentHenvendelseListeRequest: ASBOPenHenvendelse
    ) = ASBOPenHenvendelseListe()

    override fun hentHenvendelse(hentHenvendelseRequest: ASBOPenHenvendelse) =
        throw NotImplementedException()
}
