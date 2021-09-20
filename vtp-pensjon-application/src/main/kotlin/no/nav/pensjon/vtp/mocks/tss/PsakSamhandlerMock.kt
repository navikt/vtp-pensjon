package no.nav.pensjon.vtp.mocks.tss

import no.nav.inf.psak.samhandler.HentSamhandlerFaultPenSamhandlerIkkeFunnetMsg
import no.nav.inf.psak.samhandler.PSAKSamhandler
import no.nav.lib.pen.psakpselv.asbo.samhandler.ASBOPenFinnSamhandlerRequest
import no.nav.lib.pen.psakpselv.asbo.samhandler.ASBOPenHentSamhandlerRequest
import no.nav.lib.pen.psakpselv.asbo.samhandler.ASBOPenSamhandler
import no.nav.lib.pen.psakpselv.asbo.samhandler.ASBOPenSamhandlerListe
import no.nav.lib.pen.psakpselv.fault.samhandler.ObjectFactory
import no.nav.pensjon.vtp.annotations.SoapService
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import no.nav.pensjon.vtp.testmodell.tss.SamhandlerRepository
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
class PsakSamhandlerMock(
    private val samhandlerRepository: SamhandlerRepository
) : PSAKSamhandler {
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
    override fun finnSamhandler(@WebParam(name = "finnSamhandlerRequest") request: ASBOPenFinnSamhandlerRequest) =
        ASBOPenSamhandlerListe().apply {
            samhandlere = samhandlerRepository.findAll()
                .filter {
                    (request.navn == null || request.navn == it.navn) &&
                        (request.samhandlerType == null || request.samhandlerType == it.samhandlerType) &&
                        (request.idType == null || request.idType == it.idType) &&
                        (request.offentligId == null || request.offentligId == it.offentligId)
                }
                .map {
                    ASBOPenSamhandler().apply {
                        navn = it.navn
                        navn = it.navn
                        sprak = it.sprak
                        samhandlerType = it.samhandlerType
                        offentligId = it.offentligId
                        idType = it.idType
                        avdelinger = it.avdelinger
                        alternativeIder = emptyArray()
                    }
                }
                .toTypedArray()
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
    override fun hentSamhandler(@WebParam(name = "hentSamhandlerRequest") request: ASBOPenHentSamhandlerRequest) =
        samhandlerRepository.findByTssEksternId(request.idTSSEkstern)
            ?.let {
                ASBOPenSamhandler().apply {
                    navn = it.navn
                    sprak = it.sprak
                    samhandlerType = it.samhandlerType
                    offentligId = it.offentligId
                    idType = it.idType
                    avdelinger = arrayOf(it.avdelinger.first { a -> a.idTSSEkstern == request.idTSSEkstern })
                    alternativeIder = it.alternativeIder
                }
            }
            ?: throw HentSamhandlerFaultPenSamhandlerIkkeFunnetMsg("Samhandler med tssEksternId=${request.idTSSEkstern} ikke funnet")
}
