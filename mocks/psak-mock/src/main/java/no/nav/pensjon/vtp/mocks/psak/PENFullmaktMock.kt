package no.nav.pensjon.vtp.mocks.psak

import no.nav.inf.pen.fullmakt.PENFullmakt
import no.nav.lib.pen.psakpselv.asbo.fullmakt.ASBOPenFinnFullmaktmottagereRequest
import no.nav.lib.pen.psakpselv.asbo.fullmakt.ASBOPenFullmaktListe
import no.nav.lib.pen.psakpselv.fault.ObjectFactory
import no.nav.pensjon.vtp.core.annotations.SoapService
import javax.jws.HandlerChain
import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult
import javax.jws.WebService
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-cons-pen-pen-fullmaktWeb/sca/PENFullmaktWSEXP"])
@WebService(targetNamespace = "http://nav-cons-pen-pen-fullmakt/no/nav/inf", name = "PENFullmakt")
@XmlSeeAlso(
    ObjectFactory::class,
    no.nav.lib.pen.psakpselv.asbo.fullmakt.ObjectFactory::class,
    no.nav.inf.pen.fullmakt.ObjectFactory::class
)
@HandlerChain(file = "/Handler-chain.xml")
class PENFullmaktMock : PENFullmakt {
    @WebMethod
    @RequestWrapper(
        localName = "finnFullmaktmottagere",
        targetNamespace = "http://nav-cons-pen-pen-fullmakt/no/nav/inf",
        className = "no.nav.inf.pen.fullmakt.FinnFullmaktmottagere"
    )
    @ResponseWrapper(
        localName = "finnFullmaktmottagereResponse",
        targetNamespace = "http://nav-cons-pen-pen-fullmakt/no/nav/inf",
        className = "no.nav.inf.pen.fullmakt.FinnFullmaktmottagereResponse"
    )
    @WebResult(name = "finnFullmaktmottagereResponse", targetNamespace = "")
    override fun finnFullmaktmottagere(
        @WebParam(
            name = "finnFullmaktmottagereRequest",
            targetNamespace = ""
        ) finnFullmaktmottagereRequest: ASBOPenFinnFullmaktmottagereRequest
    ) = ASBOPenFullmaktListe()
}
