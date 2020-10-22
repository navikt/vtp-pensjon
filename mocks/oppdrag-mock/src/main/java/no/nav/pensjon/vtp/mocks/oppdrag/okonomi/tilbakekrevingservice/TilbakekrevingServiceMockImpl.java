package no.nav.pensjon.vtp.mocks.oppdrag.okonomi.tilbakekrevingservice;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.soap.Addressing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.okonomi.tilbakekrevingservice.KravgrunnlagAnnulerRequest;
import no.nav.okonomi.tilbakekrevingservice.KravgrunnlagAnnulerResponse;
import no.nav.okonomi.tilbakekrevingservice.KravgrunnlagHentDetaljRequest;
import no.nav.okonomi.tilbakekrevingservice.KravgrunnlagHentDetaljResponse;
import no.nav.okonomi.tilbakekrevingservice.KravgrunnlagHentListeRequest;
import no.nav.okonomi.tilbakekrevingservice.KravgrunnlagHentListeResponse;
import no.nav.okonomi.tilbakekrevingservice.TilbakekrevingPortType;
import no.nav.okonomi.tilbakekrevingservice.TilbakekrevingsvedtakRequest;
import no.nav.okonomi.tilbakekrevingservice.TilbakekrevingsvedtakResponse;

@Addressing
@WebService(name = "TilbakekrevingPortType", targetNamespace = "http://okonomi.nav.no/tilbakekrevingService/")
@HandlerChain(file = "/Handler-chain.xml")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public class TilbakekrevingServiceMockImpl implements TilbakekrevingPortType {

    private static Logger LOG = LoggerFactory.getLogger(TilbakekrevingServiceMockImpl.class);

    @Override
    @WebMethod
    @WebResult(name = "tilbakekrevingsvedtakResponse", targetNamespace = "http://okonomi.nav.no/tilbakekrevingService/", partName = "parameters")
    public TilbakekrevingsvedtakResponse tilbakekrevingsvedtak(TilbakekrevingsvedtakRequest tilbakekrevingsvedtakRequest) {
        LOG.info("Sendt TilbakekrevingsvedtakRequest.");
        return TilbakekrevingServiceMapper.opprettTilbakekrevingVedtakResponse(tilbakekrevingsvedtakRequest);
    }

    @Override
    @WebMethod
    @WebResult(name = "kravgrunnlagHentListeResponse", targetNamespace = "http://okonomi.nav.no/tilbakekrevingService/", partName = "parameters")
    public KravgrunnlagHentListeResponse kravgrunnlagHentListe(KravgrunnlagHentListeRequest kravgrunnlagHentListeRequest) {
        return  TilbakekrevingServiceMapper.opprettKravgrunnlagHentListeResponse();
    }

    @Override
    @WebMethod
    @WebResult(name = "kravgrunnlagHentDetaljResponse", targetNamespace = "http://okonomi.nav.no/tilbakekrevingService/", partName = "parameters")
    public KravgrunnlagHentDetaljResponse kravgrunnlagHentDetalj(KravgrunnlagHentDetaljRequest kravgrunnlagHentDetaljRequest) {
        LOG.info("Hent grunnlag.");
        return TilbakekrevingServiceMapper.opprettKravgrunnlagHentDetaljResponse();
    }

    @Override
    @WebMethod
    @WebResult(name = "kravgrunnlagAnnulerResponse", targetNamespace = "http://okonomi.nav.no/tilbakekrevingService/", partName = "parameters")
    public KravgrunnlagAnnulerResponse kravgrunnlagAnnuler(KravgrunnlagAnnulerRequest kravgrunnlagAnnulerRequest) {
        LOG.info("Annuler grunnlag.");
        return TilbakekrevingServiceMapper.opprettKravgrunnlagAnnulerResponse(kravgrunnlagAnnulerRequest);
    }
}
