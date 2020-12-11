package no.nav.pensjon.vtp.mocks.psak

import no.nav.inf.psak.arenasak.HentSaksInfoListeFaultPenGeneriskMsg
import no.nav.inf.psak.arenasak.ObjectFactory
import no.nav.inf.psak.arenasak.PSAKArenasak
import no.nav.lib.pen.psakpselv.asbo.ASBOPenSaksInfo
import no.nav.lib.pen.psakpselv.asbo.ASBOPenSaksInfoListe
import no.nav.pensjon.vtp.core.annotations.SoapService
import javax.jws.*
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-cons-pen-psak-arenasakWeb/sca/PSAKArenasakWSEXP"])
@WebService(targetNamespace = "http://nav-cons-pen-psak-arenasak/no/nav/inf", name = "PSAKArenasak")
@XmlSeeAlso(ObjectFactory::class, no.nav.lib.pen.psakpselv.fault.ObjectFactory::class, no.nav.lib.pen.psakpselv.asbo.ObjectFactory::class)
@HandlerChain(file = "/Handler-chain.xml")
class ArenaSakMock : PSAKArenasak {
    @WebMethod
    @RequestWrapper(localName = "hentSaksInfoListe", targetNamespace = "http://nav-cons-pen-psak-arenasak/no/nav/inf", className = "no.nav.inf.psak.arenasak.HentSaksInfoListe")
    @ResponseWrapper(localName = "hentSaksInfoListeResponse", targetNamespace = "http://nav-cons-pen-psak-arenasak/no/nav/inf", className = "no.nav.inf.psak.arenasak.HentSaksInfoListeResponse")
    @WebResult(name = "hentSakListeResponse", targetNamespace = "")
    @Throws(HentSaksInfoListeFaultPenGeneriskMsg::class)
    override fun hentSaksInfoListe(@WebParam(name = "hentSakListeRequest", targetNamespace = "") asboPenSaksInfo: ASBOPenSaksInfo): ASBOPenSaksInfoListe {
        val response = ASBOPenSaksInfoListe()
        response.saksInfoListe = emptyArray()
        return response
    }
}
