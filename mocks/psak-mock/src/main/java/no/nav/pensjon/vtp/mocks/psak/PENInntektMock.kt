package no.nav.pensjon.vtp.mocks.psak

import no.nav.inf.pen.inntekt.PENInntekt
import no.nav.lib.pen.psakpselv.asbo.inntekt.ASBOPenHentInntektListeRequest
import no.nav.lib.pen.psakpselv.asbo.inntekt.ASBOPenInntektListe
import no.nav.lib.pen.psakpselv.fault.ObjectFactory
import no.nav.pensjon.vtp.core.annotations.SoapService
import javax.jws.*
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-cons-pen-pen-inntektWeb/sca/PENInntektWSEXP"])
@WebService(targetNamespace = "http://nav-cons-pen-pen-inntekt/no/nav/inf", name = "PENInntekt")
@XmlSeeAlso(
    ObjectFactory::class,
    no.nav.lib.pen.psakpselv.asbo.ObjectFactory::class,
    no.nav.lib.pen.psakpselv.fault.inntekt.ObjectFactory::class,
    no.nav.lib.pen.psakpselv.asbo.inntekt.ObjectFactory::class,
    no.nav.inf.pen.inntekt.ObjectFactory::class
)
@HandlerChain(file = "/Handler-chain.xml")
class PENInntektMock : PENInntekt {
    @WebMethod
    @RequestWrapper(
        localName = "hentInntektListe",
        targetNamespace = "http://nav-cons-pen-pen-inntekt/no/nav/inf",
        className = "no.nav.inf.pen.inntekt.HentInntektListe"
    )
    @ResponseWrapper(
        localName = "hentInntektListeResponse",
        targetNamespace = "http://nav-cons-pen-pen-inntekt/no/nav/inf",
        className = "no.nav.inf.pen.inntekt.HentInntektListeResponse"
    )
    @WebResult(name = "hentInntektListeResponse", targetNamespace = "")
    override fun hentInntektListe(
        @WebParam(
            name = "hentInntektListeRequest",
            targetNamespace = ""
        ) hentInntektListeRequest: ASBOPenHentInntektListeRequest
    ) = ASBOPenInntektListe().apply {
        inntekter = arrayOfNulls(0)
    }
}
