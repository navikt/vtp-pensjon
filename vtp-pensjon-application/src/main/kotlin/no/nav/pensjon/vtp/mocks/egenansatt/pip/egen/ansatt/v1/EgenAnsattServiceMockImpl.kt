package no.nav.pensjon.vtp.mocks.egenansatt.pip.egen.ansatt.v1

import no.nav.pensjon.vtp.annotations.SoapService
import no.nav.tjeneste.pip.egen.ansatt.v1.EgenAnsattV1
import no.nav.tjeneste.pip.egen.ansatt.v1.WSHentErEgenAnsattEllerIFamilieMedEgenAnsattRequest
import no.nav.tjeneste.pip.egen.ansatt.v1.WSHentErEgenAnsattEllerIFamilieMedEgenAnsattResponse
import javax.jws.HandlerChain
import javax.jws.WebMethod
import javax.jws.WebResult
import javax.jws.WebService
import javax.jws.soap.SOAPBinding
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper
import javax.xml.ws.soap.Addressing

@SoapService(path = ["soap/tpsws/EgenAnsatt_v1"])
@Addressing
@WebService(name = "EgenAnsatt_v1", targetNamespace = "http://nav.no/tjeneste/pip/egenAnsatt/v1/")
@HandlerChain(file = "/Handler-chain.xml")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
class EgenAnsattServiceMockImpl : EgenAnsattV1 {
    @WebMethod(action = "http://nav.no/tjeneste/pip/egenAnsatt/v1/EgenAnsatt_v1/pingRequest")
    @RequestWrapper(
        localName = "ping",
        targetNamespace = "http://nav.no/tjeneste/pip/egenAnsatt/v1/",
        className = "no.nav.tjeneste.pip.egen.ansatt.v1.Ping"
    )
    @ResponseWrapper(
        localName = "pingResponse",
        targetNamespace = "http://nav.no/tjeneste/pip/egenAnsatt/v1/",
        className = "no.nav.tjeneste.pip.egen.ansatt.v1.PingResponse"
    )
    override fun ping() = Unit

    @WebMethod(action = "http://nav.no/tjeneste/pip/egenAnsatt/v1/EgenAnsatt_v1/hentErEgenAnsattEllerIFamilieMedEgenAnsattRequest")
    @RequestWrapper(
        localName = "hentErEgenAnsattEllerIFamilieMedEgenAnsatt",
        targetNamespace = "http://nav.no/tjeneste/pip/egenAnsatt/v1/",
        className = "no.nav.tjeneste.pip.egen.ansatt.v1.HentErEgenAnsattEllerIFamilieMedEgenAnsatt"
    )
    @ResponseWrapper(
        localName = "hentErEgenAnsattEllerIFamilieMedEgenAnsattResponse",
        targetNamespace = "http://nav.no/tjeneste/pip/egenAnsatt/v1/",
        className = "no.nav.tjeneste.pip.egen.ansatt.v1.HentErEgenAnsattEllerIFamilieMedEgenAnsattResponse"
    )
    @WebResult(name = "response")
    override fun hentErEgenAnsattEllerIFamilieMedEgenAnsatt(wsHentErEgenAnsattEllerIFamilieMedEgenAnsattRequest: WSHentErEgenAnsattEllerIFamilieMedEgenAnsattRequest) =
        WSHentErEgenAnsattEllerIFamilieMedEgenAnsattResponse().apply {
            isEgenAnsatt = false
        }
}
