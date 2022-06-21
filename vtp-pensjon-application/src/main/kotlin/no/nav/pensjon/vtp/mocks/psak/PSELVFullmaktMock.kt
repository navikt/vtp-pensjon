package no.nav.pensjon.vtp.mocks.psak

import no.nav.inf.pselv.fullmakt.PSELVFullmakt
import no.nav.lib.pen.psakpselv.asbo.fullmakt.ASBOPenFullmakt
import no.nav.lib.pen.psakpselv.asbo.fullmakt.ASBOPenFullmaktListe
import no.nav.lib.pen.psakpselv.asbo.fullmakt.ASBOPenHentFullmaktListeRequest
import no.nav.lib.pen.psakpselv.fault.ObjectFactory
import no.nav.pensjon.vtp.annotations.SoapService
import no.nav.pensjon.vtp.testmodell.fullmakt.FullmaktModell
import no.nav.pensjon.vtp.testmodell.fullmakt.FullmaktRepository
import java.util.*
import javax.jws.HandlerChain
import javax.jws.WebMethod
import javax.jws.WebResult
import javax.jws.WebService
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-cons-pen-pselv-fullmaktWeb/sca/PSELVFullmaktWSEXP"])
@WebService(targetNamespace = "http://nav-cons-pen-pselv-fullmakt/no/nav/inf", name = "PSELVFullmakt")
@XmlSeeAlso(
    ObjectFactory::class,
    no.nav.lib.pen.psakpselv.asbo.fullmakt.ObjectFactory::class,
    no.nav.inf.pen.fullmakt.ObjectFactory::class
)
@HandlerChain(file = "/Handler-chain.xml")
class PSELVFullmaktMock(private val fullmaktRepository: FullmaktRepository) : PSELVFullmakt {

    @WebMethod
    @RequestWrapper(
            localName = "hentFullmaktListe",
            targetNamespace = "http://nav-cons-pen-pselv-fullmakt/no/nav/inf",
            className = "no.nav.inf.pselv.fullmakt.HentFullmaktListe"
    )
    @ResponseWrapper(
            localName = "hentFullmaktListeResponse",
            targetNamespace = "http://nav-cons-pen-pselv-fullmakt/no/nav/inf",
            className = "no.nav.inf.pselv.fullmakt.HentFullmaktListeResponse"
    )
    @WebResult(name = "hentFullmaktListeResponse", targetNamespace = "")
    override fun hentFullmaktListe(request: ASBOPenHentFullmaktListeRequest): ASBOPenFullmaktListe {
        val fullmaktListe = ASBOPenFullmaktListe()

        fullmaktRepository.findById(request.fnr)
            ?.map { it.fullmakt }
            ?.toTypedArray()
            ?.let { fullmaktListe.fullmakter = it }

        return fullmaktListe
    }

    override fun lagreFullmakt(request: ASBOPenFullmakt?): ASBOPenFullmakt = ASBOPenFullmakt()

    override fun opprettFullmakt(request: ASBOPenFullmakt): ASBOPenFullmakt {
        fullmaktRepository.save(
            FullmaktModell(
                pid = request.fullmaktsgiver,
                fullmakt = request.also { it.fullmaktId = UUID.randomUUID().toString() }
            )
        )
        fullmaktRepository.save(
            FullmaktModell(
                pid = request.fullmaktshaver,
                fullmakt = request.also { it.fullmaktId = UUID.randomUUID().toString() }
            )
        )
        return request
    }

    override fun slettFullmakt(request: ASBOPenFullmakt?): ASBOPenFullmakt = ASBOPenFullmakt()
    override fun lagreFullmaktSistBrukt(request: ASBOPenFullmakt?): ASBOPenFullmakt = ASBOPenFullmakt()
}
