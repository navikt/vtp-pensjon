package no.nav.pensjon.vtp.mocks.esb.getapplicationversion

import nav_lib_test.no.nav.inf.ObjectFactory
import nav_lib_test.no.nav.inf.binding.TESTGetApplicationVersion
import nav_lib_test.no.nav.test.asbo.ASBOTestGetApplicationVersionRequest
import nav_lib_test.no.nav.test.asbo.ASBOTestGetApplicationVersionResponse
import no.nav.pensjon.vtp.annotations.SoapService
import javax.jws.HandlerChain
import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebResult
import javax.jws.WebService
import javax.xml.bind.annotation.XmlSeeAlso
import javax.xml.ws.RequestWrapper
import javax.xml.ws.ResponseWrapper

@SoapService(path = ["/esb/nav-cons-test-getapplicationversionWeb/sca/TESTGetApplicationVersionWSEXP"])
@WebService(name = "TESTGetApplicationVersion", targetNamespace = "http://nav-lib-test/no/nav/inf")
@XmlSeeAlso(
    ObjectFactory::class, nav_lib_test.no.nav.test.asbo.ObjectFactory::class
)
@HandlerChain(file = "/Handler-chain.xml")
class GetApplicationVersionMock : TESTGetApplicationVersion {
    @WebMethod
    @WebResult(name = "joarkGetApplicationVersionResponse")
    @RequestWrapper(
        localName = "joarkGetApplicationVersion",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.JoarkGetApplicationVersion"
    )
    @ResponseWrapper(
        localName = "joarkGetApplicationVersionResponse",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.JoarkGetApplicationVersionResponse"
    )
    override fun joarkGetApplicationVersion(@WebParam(name = "joarkGetApplicationVersionRequest") var1: ASBOTestGetApplicationVersionRequest) =
        createResponse()

    @WebMethod
    @WebResult(name = "aaGetApplicationVersionResponse")
    @RequestWrapper(
        localName = "aaGetApplicationVersion",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.AaGetApplicationVersion"
    )
    @ResponseWrapper(
        localName = "aaGetApplicationVersionResponse",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.AaGetApplicationVersionResponse"
    )
    override fun aaGetApplicationVersion(@WebParam(name = "aaGetApplicationVersionRequest") var1: ASBOTestGetApplicationVersionRequest) =
        createResponse()

    @WebMethod
    @WebResult(name = "inntGetApplicationVersionResponse")
    @RequestWrapper(
        localName = "inntGetApplicationVersion",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.InntGetApplicationVersion"
    )
    @ResponseWrapper(
        localName = "inntGetApplicationVersionResponse",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.InntGetApplicationVersionResponse"
    )
    override fun inntGetApplicationVersion(@WebParam(name = "inntGetApplicationVersionRequest") var1: ASBOTestGetApplicationVersionRequest) =
        createResponse()

    @WebMethod
    @WebResult(name = "instGetApplicationVersionResponse")
    @RequestWrapper(
        localName = "instGetApplicationVersion",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.InstGetApplicationVersion"
    )
    @ResponseWrapper(
        localName = "instGetApplicationVersionResponse",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.InstGetApplicationVersionResponse"
    )
    override fun instGetApplicationVersion(@WebParam(name = "instGetApplicationVersionRequest") var1: ASBOTestGetApplicationVersionRequest) =
        createResponse()

    @WebMethod
    @WebResult(name = "medlGetApplicationVersionResponse")
    @RequestWrapper(
        localName = "medlGetApplicationVersion",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.MedlGetApplicationVersion"
    )
    @ResponseWrapper(
        localName = "medlGetApplicationVersionResponse",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.MedlGetApplicationVersionResponse"
    )
    override fun medlGetApplicationVersion(@WebParam(name = "medlGetApplicationVersionRequest") var1: ASBOTestGetApplicationVersionRequest) =
        createResponse()

    @WebMethod
    @WebResult(name = "norgGetApplicationVersionResponse")
    @RequestWrapper(
        localName = "norgGetApplicationVersion",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.NorgGetApplicationVersion"
    )
    @ResponseWrapper(
        localName = "norgGetApplicationVersionResponse",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.NorgGetApplicationVersionResponse"
    )
    override fun norgGetApplicationVersion(@WebParam(name = "norgGetApplicationVersionRequest") var1: ASBOTestGetApplicationVersionRequest) =
        createResponse()

    @WebMethod
    @WebResult(name = "sfeGetApplicationVersionResponse")
    @RequestWrapper(
        localName = "sfeGetApplicationVersion",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.SfeGetApplicationVersion"
    )
    @ResponseWrapper(
        localName = "sfeGetApplicationVersionResponse",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.SfeGetApplicationVersionResponse"
    )
    override fun sfeGetApplicationVersion(@WebParam(name = "sfeGetApplicationVersionRequest") var1: ASBOTestGetApplicationVersionRequest) =
        createResponse()

    @WebMethod
    @WebResult(name = "tpGetApplicationVersionResponse")
    @RequestWrapper(
        localName = "tpGetApplicationVersion",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.TpGetApplicationVersion"
    )
    @ResponseWrapper(
        localName = "tpGetApplicationVersionResponse",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.TpGetApplicationVersionResponse"
    )
    override fun tpGetApplicationVersion(@WebParam(name = "tpGetApplicationVersionRequest") var1: ASBOTestGetApplicationVersionRequest) =
        createResponse()

    @WebMethod
    @WebResult(name = "tpsGetApplicationVersionResponse")
    @RequestWrapper(
        localName = "tpsGetApplicationVersion",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.TpsGetApplicationVersion"
    )
    @ResponseWrapper(
        localName = "tpsGetApplicationVersionResponse",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.TpsGetApplicationVersionResponse"
    )
    override fun tpsGetApplicationVersion(@WebParam(name = "tpsGetApplicationVersionRequest") var1: ASBOTestGetApplicationVersionRequest) =
        createResponse()

    @WebMethod
    @WebResult(name = "tssGetApplicationVersionResponse")
    @RequestWrapper(
        localName = "tssGetApplicationVersion",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.TssGetApplicationVersion"
    )
    @ResponseWrapper(
        localName = "tssGetApplicationVersionResponse",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.TssGetApplicationVersionResponse"
    )
    override fun tssGetApplicationVersion(@WebParam(name = "tssGetApplicationVersionRequest") var1: ASBOTestGetApplicationVersionRequest) =
        createResponse()

    @WebMethod
    @WebResult(name = "osGetApplicationVersionResponse")
    @RequestWrapper(
        localName = "osGetApplicationVersion",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.OsGetApplicationVersion"
    )
    @ResponseWrapper(
        localName = "osGetApplicationVersionResponse",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.OsGetApplicationVersionResponse"
    )
    override fun osGetApplicationVersion(@WebParam(name = "osGetApplicationVersionRequest") var1: ASBOTestGetApplicationVersionRequest) =
        createResponse()

    @WebMethod
    @WebResult(name = "trekkGetApplicationVersionResponse")
    @RequestWrapper(
        localName = "trekkGetApplicationVersion",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.TrekkGetApplicationVersion"
    )
    @ResponseWrapper(
        localName = "trekkGetApplicationVersionResponse",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.TrekkGetApplicationVersionResponse"
    )
    override fun trekkGetApplicationVersion(@WebParam(name = "trekkGetApplicationVersionRequest") var1: ASBOTestGetApplicationVersionRequest) =
        createResponse()

    @WebMethod
    @WebResult(name = "urGetApplicationVersionResponse")
    @RequestWrapper(
        localName = "urGetApplicationVersion",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.UrGetApplicationVersion"
    )
    @ResponseWrapper(
        localName = "urGetApplicationVersionResponse",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.UrGetApplicationVersionResponse"
    )
    override fun urGetApplicationVersion(@WebParam(name = "urGetApplicationVersionRequest") var1: ASBOTestGetApplicationVersionRequest) =
        createResponse()

    @WebMethod
    @WebResult(name = "penGetApplicationVersionResponse")
    @RequestWrapper(
        localName = "penGetApplicationVersion",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.PenGetApplicationVersion"
    )
    @ResponseWrapper(
        localName = "penGetApplicationVersionResponse",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.PenGetApplicationVersionResponse"
    )
    override fun penGetApplicationVersion(@WebParam(name = "penGetApplicationVersionRequest") var1: ASBOTestGetApplicationVersionRequest) =
        createResponse()

    @WebMethod
    @WebResult(name = "poppGetApplicationVersionResponse")
    @RequestWrapper(
        localName = "poppGetApplicationVersion",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.PoppGetApplicationVersion"
    )
    @ResponseWrapper(
        localName = "poppGetApplicationVersionResponse",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.PoppGetApplicationVersionResponse"
    )
    override fun poppGetApplicationVersion(@WebParam(name = "poppGetApplicationVersionRequest") var1: ASBOTestGetApplicationVersionRequest) =
        createResponse()

    @WebMethod
    @WebResult(name = "arenaInternGetApplicationVersionResponse")
    @RequestWrapper(
        localName = "arenaInternGetApplicationVersion",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.ArenaInternGetApplicationVersion"
    )
    @ResponseWrapper(
        localName = "arenaInternGetApplicationVersionResponse",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.ArenaInternGetApplicationVersionResponse"
    )
    override fun arenaInternGetApplicationVersion(@WebParam(name = "arenaInternGetApplicationVersionRequest") var1: ASBOTestGetApplicationVersionRequest) =
        createResponse()

    @WebMethod
    @WebResult(name = "bisysGetApplicationVersionResponse")
    @RequestWrapper(
        localName = "bisysGetApplicationVersion",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.BisysGetApplicationVersion"
    )
    @ResponseWrapper(
        localName = "bisysGetApplicationVersionResponse",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.BisysGetApplicationVersionResponse"
    )
    override fun bisysGetApplicationVersion(@WebParam(name = "bisysGetApplicationVersionRequest") var1: ASBOTestGetApplicationVersionRequest) =
        createResponse()

    @WebMethod
    @WebResult(name = "bprofGetApplicationVersionResponse")
    @RequestWrapper(
        localName = "bprofGetApplicationVersion",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.BprofGetApplicationVersion"
    )
    @ResponseWrapper(
        localName = "bprofGetApplicationVersionResponse",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.BprofGetApplicationVersionResponse"
    )
    override fun bprofGetApplicationVersion(@WebParam(name = "bprofGetApplicationVersionRequest") var1: ASBOTestGetApplicationVersionRequest) =
        createResponse()

    @WebMethod
    @WebResult(name = "gsakGetApplicationVersionResponse")
    @RequestWrapper(
        localName = "gsakGetApplicationVersion",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.GsakGetApplicationVersion"
    )
    @ResponseWrapper(
        localName = "gsakGetApplicationVersionResponse",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.GsakGetApplicationVersionResponse"
    )
    override fun gsakGetApplicationVersion(@WebParam(name = "gsakGetApplicationVersionRequest") var1: ASBOTestGetApplicationVersionRequest) =
        createResponse()

    @WebMethod
    @WebResult(name = "infotGetApplicationVersionResponse")
    @RequestWrapper(
        localName = "infotGetApplicationVersion",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.InfotGetApplicationVersion"
    )
    @ResponseWrapper(
        localName = "infotGetApplicationVersionResponse",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.InfotGetApplicationVersionResponse"
    )
    override fun infotGetApplicationVersion(@WebParam(name = "infotGetApplicationVersionRequest") var1: ASBOTestGetApplicationVersionRequest) =
        createResponse()

    @WebMethod
    @WebResult(name = "adGetApplicationVersionResponse")
    @RequestWrapper(
        localName = "adGetApplicationVersion",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.AdGetApplicationVersion"
    )
    @ResponseWrapper(
        localName = "adGetApplicationVersionResponse",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.AdGetApplicationVersionResponse"
    )
    override fun adGetApplicationVersion(@WebParam(name = "adGetApplicationVersionRequest") var1: ASBOTestGetApplicationVersionRequest) =
        createResponse()

    @WebMethod
    @WebResult(name = "samGetApplicationVersionResponse")
    @RequestWrapper(
        localName = "samGetApplicationVersion",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.SamGetApplicationVersion"
    )
    @ResponseWrapper(
        localName = "samGetApplicationVersionResponse",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.SamGetApplicationVersionResponse"
    )
    override fun samGetApplicationVersion(@WebParam(name = "samGetApplicationVersionRequest") var1: ASBOTestGetApplicationVersionRequest) =
        createResponse()

    @WebMethod
    @WebResult(name = "arenaOppgaveGetApplicationVersionResponse")
    @RequestWrapper(
        localName = "arenaOppgaveGetApplicationVersion",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.ArenaOppgaveGetApplicationVersion"
    )
    @ResponseWrapper(
        localName = "arenaOppgaveGetApplicationVersionResponse",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.ArenaOppgaveGetApplicationVersionResponse"
    )
    override fun arenaOppgaveGetApplicationVersion(@WebParam(name = "arenaOppgaveGetApplicationVersionRequest") var1: ASBOTestGetApplicationVersionRequest) =
        createResponse()

    @WebMethod
    @WebResult(name = "arenaOrganisasjonGetApplicationVersionResponse")
    @RequestWrapper(
        localName = "arenaOrganisasjonGetApplicationVersion",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.ArenaOrganisasjonGetApplicationVersion"
    )
    @ResponseWrapper(
        localName = "arenaOrganisasjonGetApplicationVersionResponse",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.ArenaOrganisasjonGetApplicationVersionResponse"
    )
    override fun arenaOrganisasjonGetApplicationVersion(@WebParam(name = "arenaOrganisasjonGetApplicationVersionRequest") var1: ASBOTestGetApplicationVersionRequest) =
        createResponse()

    @WebMethod
    @WebResult(name = "arenaSakVedtakGetApplicationVersionResponse")
    @RequestWrapper(
        localName = "arenaSakVedtakGetApplicationVersion",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.ArenaSakVedtakGetApplicationVersion"
    )
    @ResponseWrapper(
        localName = "arenaSakVedtakGetApplicationVersionResponse",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.ArenaSakVedtakGetApplicationVersionResponse"
    )
    override fun arenaSakVedtakGetApplicationVersion(@WebParam(name = "arenaSakVedtakGetApplicationVersionRequest") var1: ASBOTestGetApplicationVersionRequest) =
        createResponse()

    @WebMethod
    @WebResult(name = "infotSakGetApplicationVersionResponse")
    @RequestWrapper(
        localName = "infotSakGetApplicationVersion",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.InfotSakGetApplicationVersion"
    )
    @ResponseWrapper(
        localName = "infotSakGetApplicationVersionResponse",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.InfotSakGetApplicationVersionResponse"
    )
    override fun infotSakGetApplicationVersion(@WebParam(name = "infotSakGetApplicationVersionRequest") var1: ASBOTestGetApplicationVersionRequest) =
        createResponse()

    @WebMethod
    @WebResult(name = "infotSakVedtakGetApplicationVersionResponse")
    @RequestWrapper(
        localName = "infotSakVedtakGetApplicationVersion",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.InfotSakVedtakGetApplicationVersion"
    )
    @ResponseWrapper(
        localName = "infotSakVedtakGetApplicationVersionResponse",
        targetNamespace = "http://nav-lib-test/no/nav/inf",
        className = "nav_lib_test.no.nav.inf.InfotSakVedtakGetApplicationVersionResponse"
    )
    override fun infotSakVedtakGetApplicationVersion(@WebParam(name = "infotSakVedtakGetApplicationVersionRequest") var1: ASBOTestGetApplicationVersionRequest) =
        createResponse()

    private fun createResponse() =
        ASBOTestGetApplicationVersionResponse().apply {
            status = "OK"
        }
}
