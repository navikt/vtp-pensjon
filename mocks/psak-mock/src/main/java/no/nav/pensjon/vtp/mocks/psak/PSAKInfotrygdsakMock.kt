package no.nav.pensjon.vtp.mocks.psak

import no.nav.inf.psak.infotrygdsak.HentSaksInfoListeFaultPenGeneriskMsg
import no.nav.inf.psak.infotrygdsak.HentSaksInfoListeFaultPenPersonIkkeFunnetMsg
import no.nav.inf.psak.infotrygdsak.ObjectFactory
import no.nav.inf.psak.infotrygdsak.PSAKInfotrygdsak
import no.nav.lib.pen.psakpselv.asbo.ASBOPenSaksInfo
import no.nav.lib.pen.psakpselv.asbo.ASBOPenSaksInfoListe
import no.nav.pensjon.vtp.core.annotations.SoapService
import javax.jws.*
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-cons-pen-psak-infotrygdsakWeb/sca/PSAKInfotrygdsakWSEXP"])
@WebService(targetNamespace = "http://nav-cons-pen-psak-infotrygdsak/no/nav/inf", name = "PSAKInfotrygdsak")
@XmlSeeAlso(ObjectFactory::class, no.nav.lib.pen.psakpselv.fault.ObjectFactory::class, no.nav.lib.pen.psakpselv.asbo.ObjectFactory::class)
@HandlerChain(file = "/Handler-chain.xml")
class PSAKInfotrygdsakMock : PSAKInfotrygdsak {
    @WebMethod
    @RequestWrapper(localName = "hentSaksInfoListe", targetNamespace = "http://nav-cons-pen-psak-infotrygdsak/no/nav/inf", className = "no.nav.inf.psak.infotrygdsak.HentSaksInfoListe")
    @ResponseWrapper(localName = "hentSaksInfoListeResponse", targetNamespace = "http://nav-cons-pen-psak-infotrygdsak/no/nav/inf", className = "no.nav.inf.psak.infotrygdsak.HentSaksInfoListeResponse")
    @WebResult(name = "hentSaksInfoListeResponse", targetNamespace = "")
    @Throws(HentSaksInfoListeFaultPenGeneriskMsg::class, HentSaksInfoListeFaultPenPersonIkkeFunnetMsg::class)
    override fun hentSaksInfoListe(@WebParam(name = "hentSaksInfoListeRequest", targetNamespace = "") asboPenSaksInfo: ASBOPenSaksInfo): ASBOPenSaksInfoListe {
        val response = ASBOPenSaksInfoListe()
        response.saksInfoListe = emptyArray()
        return response
    }
}
