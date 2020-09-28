package no.nav.esb.getapplicationversion;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import nav_lib_test.no.nav.inf.ObjectFactory;
import nav_lib_test.no.nav.inf.binding.TESTGetApplicationVersion;
import nav_lib_test.no.nav.test.asbo.ASBOTestGetApplicationVersionRequest;
import nav_lib_test.no.nav.test.asbo.ASBOTestGetApplicationVersionResponse;

@WebService(
        name = "TESTGetApplicationVersion",
        targetNamespace = "http://nav-lib-test/no/nav/inf"
)
@XmlSeeAlso({ObjectFactory.class, nav_lib_test.no.nav.test.asbo.ObjectFactory.class})
@HandlerChain(file = "Handler-chain.xml")
public class GetApplicationVersionMock implements TESTGetApplicationVersion {
    @WebMethod
    @WebResult(
            name = "joarkGetApplicationVersionResponse"
    )
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
    @Override
    public ASBOTestGetApplicationVersionResponse joarkGetApplicationVersion(@WebParam(name = "joarkGetApplicationVersionRequest") ASBOTestGetApplicationVersionRequest var1) {
        return createResponse();
    }

    @WebMethod
    @WebResult(
            name = "aaGetApplicationVersionResponse"
    )
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
    @Override
    public ASBOTestGetApplicationVersionResponse aaGetApplicationVersion(@WebParam(name = "aaGetApplicationVersionRequest") ASBOTestGetApplicationVersionRequest var1) {
        return createResponse();
    }

    @WebMethod
    @WebResult(
            name = "inntGetApplicationVersionResponse"
    )
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
    @Override
    public ASBOTestGetApplicationVersionResponse inntGetApplicationVersion(@WebParam(name = "inntGetApplicationVersionRequest") ASBOTestGetApplicationVersionRequest var1) {
        return createResponse();
    }

    @WebMethod
    @WebResult(
            name = "instGetApplicationVersionResponse"
    )
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
    @Override
    public ASBOTestGetApplicationVersionResponse instGetApplicationVersion(@WebParam(name = "instGetApplicationVersionRequest") ASBOTestGetApplicationVersionRequest var1) {
        return createResponse();
    }

    @WebMethod
    @WebResult(
            name = "medlGetApplicationVersionResponse"
    )
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
    @Override
    public ASBOTestGetApplicationVersionResponse medlGetApplicationVersion(@WebParam(name = "medlGetApplicationVersionRequest") ASBOTestGetApplicationVersionRequest var1) {
        return createResponse();
    }

    @WebMethod
    @WebResult(
            name = "norgGetApplicationVersionResponse"
    )
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
    @Override
    public ASBOTestGetApplicationVersionResponse norgGetApplicationVersion(@WebParam(name = "norgGetApplicationVersionRequest") ASBOTestGetApplicationVersionRequest var1) {
        return createResponse();
    }

    @WebMethod
    @WebResult(
            name = "sfeGetApplicationVersionResponse"
    )
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
    @Override
    public ASBOTestGetApplicationVersionResponse sfeGetApplicationVersion(@WebParam(name = "sfeGetApplicationVersionRequest") ASBOTestGetApplicationVersionRequest var1) {
        return createResponse();
    }

    @WebMethod
    @WebResult(
            name = "tpGetApplicationVersionResponse"
    )
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
    @Override
    public ASBOTestGetApplicationVersionResponse tpGetApplicationVersion(@WebParam(name = "tpGetApplicationVersionRequest") ASBOTestGetApplicationVersionRequest var1) {
        return createResponse();
    }

    @WebMethod
    @WebResult(
            name = "tpsGetApplicationVersionResponse"
    )
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
    @Override
    public ASBOTestGetApplicationVersionResponse tpsGetApplicationVersion(@WebParam(name = "tpsGetApplicationVersionRequest") ASBOTestGetApplicationVersionRequest var1) {
        return createResponse();
    }

    @WebMethod
    @WebResult(
            name = "tssGetApplicationVersionResponse"
    )
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
    @Override
    public ASBOTestGetApplicationVersionResponse tssGetApplicationVersion(@WebParam(name = "tssGetApplicationVersionRequest") ASBOTestGetApplicationVersionRequest var1) {
        return createResponse();
    }

    @WebMethod
    @WebResult(
            name = "osGetApplicationVersionResponse"
    )
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
    @Override
    public ASBOTestGetApplicationVersionResponse osGetApplicationVersion(@WebParam(name = "osGetApplicationVersionRequest") ASBOTestGetApplicationVersionRequest var1) {
        return createResponse();
    }

    @WebMethod
    @WebResult(
            name = "trekkGetApplicationVersionResponse"
    )
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
    @Override
    public ASBOTestGetApplicationVersionResponse trekkGetApplicationVersion(@WebParam(name = "trekkGetApplicationVersionRequest") ASBOTestGetApplicationVersionRequest var1) {
        return createResponse();
    }

    @WebMethod
    @WebResult(
            name = "urGetApplicationVersionResponse"
    )
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
    @Override
    public ASBOTestGetApplicationVersionResponse urGetApplicationVersion(@WebParam(name = "urGetApplicationVersionRequest") ASBOTestGetApplicationVersionRequest var1) {
        return createResponse();
    }

    @WebMethod
    @WebResult(
            name = "penGetApplicationVersionResponse"
    )
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
    @Override
    public ASBOTestGetApplicationVersionResponse penGetApplicationVersion(@WebParam(name = "penGetApplicationVersionRequest") ASBOTestGetApplicationVersionRequest var1) {
        return createResponse();
    }

    @WebMethod
    @WebResult(
            name = "poppGetApplicationVersionResponse"
    )
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
    @Override
    public ASBOTestGetApplicationVersionResponse poppGetApplicationVersion(@WebParam(name = "poppGetApplicationVersionRequest") ASBOTestGetApplicationVersionRequest var1) {
        return createResponse();
    }

    @WebMethod
    @WebResult(
            name = "arenaInternGetApplicationVersionResponse"
    )
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
    @Override
    public ASBOTestGetApplicationVersionResponse arenaInternGetApplicationVersion(@WebParam(name = "arenaInternGetApplicationVersionRequest") ASBOTestGetApplicationVersionRequest var1) {
        return createResponse();
    }

    @WebMethod
    @WebResult(
            name = "bisysGetApplicationVersionResponse"
    )
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
    @Override
    public ASBOTestGetApplicationVersionResponse bisysGetApplicationVersion(@WebParam(name = "bisysGetApplicationVersionRequest") ASBOTestGetApplicationVersionRequest var1) {
        return createResponse();
    }

    @WebMethod
    @WebResult(
            name = "bprofGetApplicationVersionResponse"
    )
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
    @Override
    public ASBOTestGetApplicationVersionResponse bprofGetApplicationVersion(@WebParam(name = "bprofGetApplicationVersionRequest") ASBOTestGetApplicationVersionRequest var1) {
        return createResponse();
    }

    @WebMethod
    @WebResult(
            name = "gsakGetApplicationVersionResponse"
    )
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
    @Override
    public ASBOTestGetApplicationVersionResponse gsakGetApplicationVersion(@WebParam(name = "gsakGetApplicationVersionRequest") ASBOTestGetApplicationVersionRequest var1) {
        return createResponse();
    }

    @WebMethod
    @WebResult(
            name = "infotGetApplicationVersionResponse"
    )
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
    @Override
    public ASBOTestGetApplicationVersionResponse infotGetApplicationVersion(@WebParam(name = "infotGetApplicationVersionRequest") ASBOTestGetApplicationVersionRequest var1) {
        return createResponse();
    }

    @WebMethod
    @WebResult(
            name = "adGetApplicationVersionResponse"
    )
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
    @Override
    public ASBOTestGetApplicationVersionResponse adGetApplicationVersion(@WebParam(name = "adGetApplicationVersionRequest") ASBOTestGetApplicationVersionRequest var1) {
        return createResponse();
    }

    @WebMethod
    @WebResult(
            name = "samGetApplicationVersionResponse"
    )
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
    @Override
    public ASBOTestGetApplicationVersionResponse samGetApplicationVersion(@WebParam(name = "samGetApplicationVersionRequest") ASBOTestGetApplicationVersionRequest var1) {
        return createResponse();
    }

    @WebMethod
    @WebResult(
            name = "arenaOppgaveGetApplicationVersionResponse"
    )
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
    @Override
    public ASBOTestGetApplicationVersionResponse arenaOppgaveGetApplicationVersion(@WebParam(name = "arenaOppgaveGetApplicationVersionRequest") ASBOTestGetApplicationVersionRequest var1) {
        return createResponse();
    }

    @WebMethod
    @WebResult(
            name = "arenaOrganisasjonGetApplicationVersionResponse"
    )
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
    @Override
    public ASBOTestGetApplicationVersionResponse arenaOrganisasjonGetApplicationVersion(@WebParam(name = "arenaOrganisasjonGetApplicationVersionRequest") ASBOTestGetApplicationVersionRequest var1) {
        return createResponse();
    }

    @WebMethod
    @WebResult(
            name = "arenaSakVedtakGetApplicationVersionResponse"
    )
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
    @Override
    public ASBOTestGetApplicationVersionResponse arenaSakVedtakGetApplicationVersion(@WebParam(name = "arenaSakVedtakGetApplicationVersionRequest") ASBOTestGetApplicationVersionRequest var1) {
        return createResponse();
    }

    @WebMethod
    @WebResult(
            name = "infotSakGetApplicationVersionResponse"
    )
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
    @Override
    public ASBOTestGetApplicationVersionResponse infotSakGetApplicationVersion(@WebParam(name = "infotSakGetApplicationVersionRequest") ASBOTestGetApplicationVersionRequest var1) {
        return createResponse();
    }

    @WebMethod
    @WebResult(
            name = "infotSakVedtakGetApplicationVersionResponse"
    )
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
    @Override
    public ASBOTestGetApplicationVersionResponse infotSakVedtakGetApplicationVersion(@WebParam(name = "infotSakVedtakGetApplicationVersionRequest") ASBOTestGetApplicationVersionRequest var1) {
        return createResponse();
    }

    private ASBOTestGetApplicationVersionResponse createResponse() {
        ASBOTestGetApplicationVersionResponse response = new ASBOTestGetApplicationVersionResponse();
        response.setStatus("OK");
        return response;
    }
}
