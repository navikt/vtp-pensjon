package no.nav.pensjon.vtp.mocks.oppdrag.okonomi.tilbakekrevingservice

import no.nav.okonomi.tilbakekrevingservice.*
import no.nav.pensjon.vtp.core.annotations.SoapService
import javax.jws.HandlerChain
import javax.jws.WebMethod
import javax.jws.WebResult
import javax.jws.WebService
import javax.jws.soap.SOAPBinding
import javax.xml.ws.soap.Addressing

@SoapService(path = ["/tilbakekreving/services/tilbakekrevingService"])
@Addressing
@WebService(name = "TilbakekrevingPortType", targetNamespace = "http://okonomi.nav.no/tilbakekrevingService/")
@HandlerChain(file = "/Handler-chain.xml")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
class TilbakekrevingServiceMockImpl : TilbakekrevingPortType {
    @WebMethod
    @WebResult(
        name = "tilbakekrevingsvedtakResponse",
        targetNamespace = "http://okonomi.nav.no/tilbakekrevingService/",
        partName = "parameters"
    )
    override fun tilbakekrevingsvedtak(tilbakekrevingsvedtakRequest: TilbakekrevingsvedtakRequest) =
        opprettTilbakekrevingVedtakResponse(tilbakekrevingsvedtakRequest)

    @WebMethod
    @WebResult(
        name = "kravgrunnlagHentListeResponse",
        targetNamespace = "http://okonomi.nav.no/tilbakekrevingService/",
        partName = "parameters"
    )
    override fun kravgrunnlagHentListe(kravgrunnlagHentListeRequest: KravgrunnlagHentListeRequest) =
        opprettKravgrunnlagHentListeResponse()

    @WebMethod
    @WebResult(
        name = "kravgrunnlagHentDetaljResponse",
        targetNamespace = "http://okonomi.nav.no/tilbakekrevingService/",
        partName = "parameters"
    )
    override fun kravgrunnlagHentDetalj(kravgrunnlagHentDetaljRequest: KravgrunnlagHentDetaljRequest) =
        opprettKravgrunnlagHentDetaljResponse()

    @WebMethod
    @WebResult(
        name = "kravgrunnlagAnnulerResponse",
        targetNamespace = "http://okonomi.nav.no/tilbakekrevingService/",
        partName = "parameters"
    )
    override fun kravgrunnlagAnnuler(kravgrunnlagAnnulerRequest: KravgrunnlagAnnulerRequest) =
        opprettKravgrunnlagAnnulerResponse(kravgrunnlagAnnulerRequest)
}
