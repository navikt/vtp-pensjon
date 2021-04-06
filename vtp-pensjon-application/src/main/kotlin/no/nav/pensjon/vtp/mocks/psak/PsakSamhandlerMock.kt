package no.nav.pensjon.vtp.mocks.psak

import no.nav.inf.psak.samhandler.PSAKSamhandler
import no.nav.lib.pen.psakpselv.asbo.samhandler.*
import no.nav.lib.pen.psakpselv.fault.samhandler.ObjectFactory
import no.nav.pensjon.vtp.annotations.SoapService
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import javax.jws.*
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-cons-pen-psak-samhandlerWeb/sca/PSAKSamhandlerWSEXP"])
@WebService(targetNamespace = "http://nav-cons-pen-psak-samhandler/no/nav/inf", name = "PSAKSamhandler")
@XmlSeeAlso(
    ObjectFactory::class,
    no.nav.inf.psak.samhandler.ObjectFactory::class,
    no.nav.lib.pen.psakpselv.fault.ObjectFactory::class,
    no.nav.lib.pen.psakpselv.asbo.ObjectFactory::class,
    no.nav.lib.pen.psakpselv.asbo.samhandler.ObjectFactory::class
)
@HandlerChain(file = "/Handler-chain.xml")
class PsakSamhandlerMock : PSAKSamhandler {
    @WebMethod
    @RequestWrapper(
        localName = "lagreSamhandler",
        targetNamespace = "http://nav-cons-pen-psak-samhandler/no/nav/inf",
        className = "no.nav.inf.psak.samhandler.LagreSamhandler"
    )
    @ResponseWrapper(
        localName = "lagreSamhandlerResponse",
        targetNamespace = "http://nav-cons-pen-psak-samhandler/no/nav/inf",
        className = "no.nav.inf.psak.samhandler.LagreSamhandlerResponse"
    )
    @WebResult(name = "lagreSamhandlerResponse")
    override fun lagreSamhandler(@WebParam(name = "lagreSamhandlerRequest") asboPenSamhandler: ASBOPenSamhandler) =
        throw NotImplementedException()

    @WebMethod
    @RequestWrapper(
        localName = "finnSamhandler",
        targetNamespace = "http://nav-cons-pen-psak-samhandler/no/nav/inf",
        className = "no.nav.inf.psak.samhandler.FinnSamhandler"
    )
    @ResponseWrapper(
        localName = "finnSamhandlerResponse",
        targetNamespace = "http://nav-cons-pen-psak-samhandler/no/nav/inf",
        className = "no.nav.inf.psak.samhandler.FinnSamhandlerResponse"
    )
    @WebResult(name = "finnSamhandlerResponse")
    override fun finnSamhandler(@WebParam(name = "finnSamhandlerRequest") asboPenFinnSamhandlerRequest: ASBOPenFinnSamhandlerRequest) =
        ASBOPenSamhandlerListe().apply {
            samhandlere = arrayOf(createDummySamhandler())
        }

    @WebMethod
    @RequestWrapper(
        localName = "opprettSamhandler",
        targetNamespace = "http://nav-cons-pen-psak-samhandler/no/nav/inf",
        className = "no.nav.inf.psak.samhandler.OpprettSamhandler"
    )
    @ResponseWrapper(
        localName = "opprettSamhandlerResponse",
        targetNamespace = "http://nav-cons-pen-psak-samhandler/no/nav/inf",
        className = "no.nav.inf.psak.samhandler.OpprettSamhandlerResponse"
    )
    @WebResult(name = "opprettSamhandlerResponse")
    override fun opprettSamhandler(@WebParam(name = "opprettSamhandlerRequest") asboPenSamhandler: ASBOPenSamhandler) =
        throw NotImplementedException()

    @WebMethod
    @RequestWrapper(
        localName = "hentSamhandler",
        targetNamespace = "http://nav-cons-pen-psak-samhandler/no/nav/inf",
        className = "no.nav.inf.psak.samhandler.HentSamhandler"
    )
    @ResponseWrapper(
        localName = "hentSamhandlerResponse",
        targetNamespace = "http://nav-cons-pen-psak-samhandler/no/nav/inf",
        className = "no.nav.inf.psak.samhandler.HentSamhandlerResponse"
    )
    @WebResult(name = "hentSamhandlerResponse")
    override fun hentSamhandler(@WebParam(name = "hentSamhandlerRequest") asboPenHentSamhandlerRequest: ASBOPenHentSamhandlerRequest) =
        createDummySamhandler()

    private fun createDummySamhandler() = ASBOPenSamhandler().apply {
        samhandlerType = "AFPO"
        navn = "AFP-ORDNINGEN I SPEKTEROMRÅDET"
        idType = null
        offentligId = "123412341234"
        avdelinger = arrayOf(
            ASBOPenAvdeling().apply {
                avdelingNavn = "Ikke samordningspliktig"
                avdelingType = "SPES"
                avdelingsnr = "1234567890"
                idTSSEkstern = "123123123"
            }
        )
    }
}
