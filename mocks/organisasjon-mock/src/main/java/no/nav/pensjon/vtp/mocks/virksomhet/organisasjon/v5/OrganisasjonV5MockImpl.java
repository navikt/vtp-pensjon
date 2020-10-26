package no.nav.pensjon.vtp.mocks.virksomhet.organisasjon.v5;

import java.util.Optional;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.pensjon.vtp.core.annotations.SoapService;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonIndeks;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonModell;
import no.nav.tjeneste.virksomhet.organisasjon.v5.binding.HentOrganisasjonUgyldigInput;
import no.nav.tjeneste.virksomhet.organisasjon.v5.binding.OrganisasjonV5;
import no.nav.tjeneste.virksomhet.organisasjon.v5.feil.UgyldigInput;
import no.nav.tjeneste.virksomhet.organisasjon.v5.informasjon.Organisasjon;
import no.nav.tjeneste.virksomhet.organisasjon.v5.meldinger.FinnOrganisasjonRequest;
import no.nav.tjeneste.virksomhet.organisasjon.v5.meldinger.FinnOrganisasjonResponse;
import no.nav.tjeneste.virksomhet.organisasjon.v5.meldinger.FinnOrganisasjonsendringerListeRequest;
import no.nav.tjeneste.virksomhet.organisasjon.v5.meldinger.FinnOrganisasjonsendringerListeResponse;
import no.nav.tjeneste.virksomhet.organisasjon.v5.meldinger.HentNoekkelinfoOrganisasjonRequest;
import no.nav.tjeneste.virksomhet.organisasjon.v5.meldinger.HentNoekkelinfoOrganisasjonResponse;
import no.nav.tjeneste.virksomhet.organisasjon.v5.meldinger.HentOrganisasjonRequest;
import no.nav.tjeneste.virksomhet.organisasjon.v5.meldinger.HentOrganisasjonResponse;
import no.nav.tjeneste.virksomhet.organisasjon.v5.meldinger.HentOrganisasjonsnavnBolkRequest;
import no.nav.tjeneste.virksomhet.organisasjon.v5.meldinger.HentOrganisasjonsnavnBolkResponse;
import no.nav.tjeneste.virksomhet.organisasjon.v5.meldinger.HentVirksomhetsOrgnrForJuridiskOrgnrBolkRequest;
import no.nav.tjeneste.virksomhet.organisasjon.v5.meldinger.HentVirksomhetsOrgnrForJuridiskOrgnrBolkResponse;
import no.nav.tjeneste.virksomhet.organisasjon.v5.meldinger.ValiderOrganisasjonRequest;
import no.nav.tjeneste.virksomhet.organisasjon.v5.meldinger.ValiderOrganisasjonResponse;

@SoapService(path = "/ereg/ws/OrganisasjonService/v5")
@WebService(
        name = "Organisasjon_v5",
        targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v5"
)
public class OrganisasjonV5MockImpl implements OrganisasjonV5 {
    private static final Logger LOG = LoggerFactory.getLogger(OrganisasjonV5MockImpl.class);

    private final OrganisasjonIndeks organisasjonIndeks;

    public OrganisasjonV5MockImpl(OrganisasjonIndeks organisasjonIndeks) {
        this.organisasjonIndeks = organisasjonIndeks;
    }

    @Override
    public void ping() {
        LOG.info("invoke: ping");
    }

    @Override
    public FinnOrganisasjonResponse finnOrganisasjon(FinnOrganisasjonRequest finnOrganisasjonRequest) {
        LOG.info("invoke: finnOrganisasjon");
        return null;
    }

    @Override
    public HentOrganisasjonsnavnBolkResponse hentOrganisasjonsnavnBolk(HentOrganisasjonsnavnBolkRequest hentOrganisasjonsnavnBolkRequest) {
        LOG.info("invoke: hentOrganisasjonsnavnBolk");
        return null;
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjon/v5/BindinghentOrganisasjon/")
    @WebResult(name = "response")
    @RequestWrapper(localName = "hentOrganisasjon", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v5",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v5.HentOrganisasjon")
    @ResponseWrapper(localName = "hentOrganisasjonResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v5",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v5.HentOrganisasjonResponse")
    public HentOrganisasjonResponse hentOrganisasjon(@WebParam(name = "request") HentOrganisasjonRequest request) throws HentOrganisasjonUgyldigInput {
        LOG.info("invoke: hentOrganisasjon");
        LOG.info("orgnummer: [{}], inkluderAnsatte: [{}], inkluderHierarki: [{}], inkluderInkluderHistorikk: [{}]", request.getOrgnummer(), request.isInkluderAnsatte(), request.isInkluderHierarki(),request.isInkluderHistorikk());
        if (request.getOrgnummer() != null) {
            HentOrganisasjonResponse response = new HentOrganisasjonResponse();
            Optional<OrganisasjonModell> organisasjonModell = organisasjonIndeks.getOrganisasjon(request.getOrgnummer());
            if (organisasjonModell.isPresent()) {
                OrganisasjonModell modell = organisasjonModell.get();
                Organisasjon organisasjon = OrganisasjonsMapper.mapOrganisasjonFraModell(modell);
                response.setOrganisasjon(organisasjon);
            }
            return response;
        } else {
            throw new HentOrganisasjonUgyldigInput("Orgnummer ikke angitt", new UgyldigInput());
        }
    }

    @Override
    public HentNoekkelinfoOrganisasjonResponse hentNoekkelinfoOrganisasjon(HentNoekkelinfoOrganisasjonRequest hentNoekkelinfoOrganisasjonRequest) {
        LOG.info("invoke: hentNoekkelinfoOrganisasjon");
        return null;
    }

    @Override
    public ValiderOrganisasjonResponse validerOrganisasjon(ValiderOrganisasjonRequest validerOrganisasjonRequest) {
        LOG.info("invoke: validerOrganisasjon");
        return null;
    }

    @Override
    public HentVirksomhetsOrgnrForJuridiskOrgnrBolkResponse hentVirksomhetsOrgnrForJuridiskOrgnrBolk(HentVirksomhetsOrgnrForJuridiskOrgnrBolkRequest hentVirksomhetsOrgnrForJuridiskOrgnrBolkRequest) {
        LOG.info("invoke: hentVirksomhetsOrgnrForJuridiskOrgnrBolk");
        return null;
    }

    @Override
    public FinnOrganisasjonsendringerListeResponse finnOrganisasjonsendringerListe(FinnOrganisasjonsendringerListeRequest finnOrganisasjonsendringerListeRequest) {
        LOG.info("invoke: finnOrganisasjonsendringerListe");
        return null;
    }
}
