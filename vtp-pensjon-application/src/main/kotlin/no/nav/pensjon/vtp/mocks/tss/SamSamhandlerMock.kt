package no.nav.pensjon.vtp.mocks.tss

import nav_cons_sto_sam_samhandler.no.nav.inf.HentSamhandlerFaultStoGeneriskMsg
import nav_cons_sto_sam_samhandler.no.nav.inf.HentSamhandlerFaultStoSamhandlerIkkeFunnetMsg
import nav_cons_sto_sam_samhandler.no.nav.inf.SAMSamhandler
import nav_lib_cons_sto_sam.no.nav.lib.sto.sam.asbo.ASBOStoAdresse
import nav_lib_cons_sto_sam.no.nav.lib.sto.sam.asbo.ObjectFactory
import nav_lib_cons_sto_sam.no.nav.lib.sto.sam.asbo.samhandler.ASBOStoAvdeling
import nav_lib_cons_sto_sam.no.nav.lib.sto.sam.asbo.samhandler.ASBOStoHentSamhandlerRequest
import nav_lib_cons_sto_sam.no.nav.lib.sto.sam.asbo.samhandler.ASBOStoKonto
import nav_lib_cons_sto_sam.no.nav.lib.sto.sam.asbo.samhandler.ASBOStoSamhandler
import nav_lib_cons_sto_sam.no.nav.lib.sto.sam.fault.FaultStoGenerisk
import nav_lib_cons_sto_sam.no.nav.lib.sto.sam.fault.samhandler.FaultStoSamhandlerIkkeFunnet
import no.nav.lib.pen.psakpselv.asbo.ASBOPenAdresse
import no.nav.pensjon.vtp.annotations.SoapService
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import no.nav.pensjon.vtp.testmodell.tss.SamhandlerRepository
import javax.jws.*
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-cons-sto-sam-samhandlerWeb/sca/SAMSamhandlerWSEXP"])
@WebService(targetNamespace = "http://nav-cons-sto-sam-samhandler/no/nav/inf", name = "SAMSamhandler")
@XmlSeeAlso(
    ObjectFactory::class,
    nav_lib_cons_sto_sam.no.nav.lib.sto.sam.asbo.samhandler.ObjectFactory::class,
    nav_lib_cons_sto_sam.no.nav.lib.sto.sam.fault.samhandler.ObjectFactory::class,
    nav_cons_sto_sam_samhandler.no.nav.inf.ObjectFactory::class,
    nav_lib_cons_sto_sam.no.nav.lib.sto.sam.fault.ObjectFactory::class
)
@HandlerChain(file = "/Handler-chain.xml")
class SamSamhandlerMock(
    private val samhandlerRepository: SamhandlerRepository
) : SAMSamhandler {
    @WebMethod
    @RequestWrapper(
        localName = "lagreSamhandler",
        targetNamespace = "http://nav-cons-sto-sam-samhandler/no/nav/inf",
        className = "nav_cons_sto_sam_samhandler.no.nav.inf.LagreSamhandler"
    )
    @ResponseWrapper(
        localName = "lagreSamhandlerResponse",
        targetNamespace = "http://nav-cons-sto-sam-samhandler/no/nav/inf",
        className = "nav_cons_sto_sam_samhandler.no.nav.inf.LagreSamhandlerResponse"
    )
    @WebResult(name = "lagreSamhandlerResponse", targetNamespace = "")
    override fun lagreSamhandler(@WebParam(name = "lagreSamhandlerRequest", targetNamespace = "") request: ASBOStoSamhandler?): ASBOStoSamhandler =
        throw NotImplementedException()

    @WebMethod
    @RequestWrapper(
        localName = "opprettSamhandler",
        targetNamespace = "http://nav-cons-sto-sam-samhandler/no/nav/inf",
        className = "nav_cons_sto_sam_samhandler.no.nav.inf.OpprettSamhandler"
    )
    @ResponseWrapper(
        localName = "opprettSamhandlerResponse",
        targetNamespace = "http://nav-cons-sto-sam-samhandler/no/nav/inf",
        className = "nav_cons_sto_sam_samhandler.no.nav.inf.OpprettSamhandlerResponse"
    )
    @WebResult(name = "opprettSamhandlerResponse", targetNamespace = "")
    override fun opprettSamhandler(@WebParam(name = "opprettSamhandlerRequest", targetNamespace = "") request: ASBOStoSamhandler?): ASBOStoSamhandler =
        throw NotImplementedException()

    @WebMethod
    @RequestWrapper(
        localName = "hentSamhandler",
        targetNamespace = "http://nav-cons-sto-sam-samhandler/no/nav/inf",
        className = "nav_cons_sto_sam_samhandler.no.nav.inf.HentSamhandler"
    )
    @ResponseWrapper(
        localName = "hentSamhandlerResponse",
        targetNamespace = "http://nav-cons-sto-sam-samhandler/no/nav/inf",
        className = "nav_cons_sto_sam_samhandler.no.nav.inf.HentSamhandlerResponse"
    )
    @WebResult(name = "hentSamhandlerResponse", targetNamespace = "")
    override fun hentSamhandler(@WebParam(name = "hentSamhandlerRequest", targetNamespace = "") request: ASBOStoHentSamhandlerRequest?): ASBOStoSamhandler =
        if (request == null) {
            throw HentSamhandlerFaultStoGeneriskMsg("Request was null", FaultStoGenerisk())
        } else {
            samhandlerRepository.findByTssEksternId(request.idTSSEkstern)
                ?.let {
                    ASBOStoSamhandler().apply {
                        navn = it.navn
                        sprak = it.sprak
                        samhandlerType = it.samhandlerType
                        offentligId = it.offentligId
                        idType = it.idType
                        avdelinger = arrayOf(it.avdelinger.first { a -> a.idTSSEkstern == request.idTSSEkstern }).map {
                            ASBOStoAvdeling().apply {
                                idTSSEkstern = it.idTSSEkstern
                                avdelingNavn = it.avdelingNavn
                                avdelingType = it.avdelingType
                                avdelingnr = it.avdelingsnr
                                kontaktperson = it.kontaktperson
                                telefon = it.telefon
                                epost = it.ePost
                                mobil = it.mobil
                                kontoer = it.kontoer.map {
                                    ASBOStoKonto().apply {
                                        kontoType = it.kontoType
                                        kontonummer = it.kontonummer
                                        banknavn = it.banknavn
                                        bankkode = it.bankkode
                                        swiftkode = it.swiftkode
                                        valuta = it.valuta
                                        bankadresse = it.bankadresse?.asASBOStoAdresse()
                                    }
                                }.toTypedArray()
                                uAdresse = it.uAdresse?.asASBOStoAdresse()
                                aAdresse = it.aAdresse?.asASBOStoAdresse()
                                pAdresse = it.pAdresse?.asASBOStoAdresse()
                                tAdresse = it.tAdresse?.asASBOStoAdresse()
                            }
                        }.toTypedArray()
                    }
                }
                ?: throw HentSamhandlerFaultStoSamhandlerIkkeFunnetMsg(
                    "Samhandler med tssEksternId=${request.idTSSEkstern} ikke funnet",
                    FaultStoSamhandlerIkkeFunnet()
                )
        }

    private fun ASBOPenAdresse.asASBOStoAdresse() = ASBOStoAdresse().also {
        it.adresselinje1 = adresselinje1
        it.adresselinje2 = adresselinje2
        it.adresselinje3 = adresselinje3
        it.postnr = postNr
        it.poststed = poststed
        it.landkode = landNr
        it.land = land
    }
}
