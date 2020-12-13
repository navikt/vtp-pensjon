package no.nav.pensjon.vtp.mocks.virksomhet.organisasjon.v4;

import no.nav.pensjon.vtp.core.annotations.SoapService;
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonModell;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonRepository;
import no.nav.tjeneste.virksomhet.organisasjon.v4.binding.HentNoekkelinfoOrganisasjonUgyldigInput;
import no.nav.tjeneste.virksomhet.organisasjon.v4.binding.HentOrganisasjonUgyldigInput;
import no.nav.tjeneste.virksomhet.organisasjon.v4.binding.OrganisasjonV4;
import no.nav.tjeneste.virksomhet.organisasjon.v4.feil.UgyldigInput;
import no.nav.tjeneste.virksomhet.organisasjon.v4.informasjon.Organisasjon;
import no.nav.tjeneste.virksomhet.organisasjon.v4.meldinger.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.*;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.soap.Addressing;
import java.util.Optional;

@SoapService(path = "/ereg/ws/OrganisasjonService/v4")
@Addressing
@WebService(name = "Organisasjon_v4", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4")
@HandlerChain(file="/Handler-chain.xml")
public class OrganisasjonV4MockImpl implements OrganisasjonV4 {

    private static final Logger LOG = LoggerFactory.getLogger(OrganisasjonV4MockImpl.class);

    private final OrganisasjonRepository organisasjonRepository;

    public OrganisasjonV4MockImpl(OrganisasjonRepository organisasjonRepository) {
        this.organisasjonRepository = organisasjonRepository;
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjon/v4/Organisasjon_v4/finnOrganisasjonRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "finnOrganisasjon", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v4.FinnOrganisasjon")
    @ResponseWrapper(localName = "finnOrganisasjonResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v4.FinnOrganisasjonResponse")
    public FinnOrganisasjonResponse finnOrganisasjon(@WebParam(name = "request") FinnOrganisasjonRequest finnOrganisasjonRequest) {
        throw new NotImplementedException();
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjon/v4/Organisasjon_v4/pingRequest")
    @RequestWrapper(localName = "ping", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4", className = "no.nav.tjeneste.virksomhet.organisasjon.v4.Ping")
    @ResponseWrapper(localName = "pingResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4", className = "no.nav.tjeneste.virksomhet.organisasjon.v4.PingResponse")
    public void ping() {
        LOG.info("Ping mottatt og besvart");
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjon/v4/Organisasjon_v4/hentOrganisasjonsnavnBolkRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "hentOrganisasjonsnavnBolk", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v4.HentOrganisasjonsnavnBolk")
    @ResponseWrapper(localName = "hentOrganisasjonsnavnBolkResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v4.HentOrganisasjonsnavnBolkResponse")
    public HentOrganisasjonsnavnBolkResponse hentOrganisasjonsnavnBolk(@WebParam(name = "request") HentOrganisasjonsnavnBolkRequest hentOrganisasjonsnavnBolkRequest) {
        throw new NotImplementedException();
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjon/v4/Organisasjon_v4/hentOrganisasjonRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "hentOrganisasjon", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v4.HentOrganisasjon")
    @ResponseWrapper(localName = "hentOrganisasjonResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v4.HentOrganisasjonResponse")
    public HentOrganisasjonResponse hentOrganisasjon(@WebParam(name = "request") HentOrganisasjonRequest request) throws HentOrganisasjonUgyldigInput{

        LOG.info("hentOrganisasjon. Orgnummer: {}", request.getOrgnummer());
        if (request.getOrgnummer() != null) {
            HentOrganisasjonResponse response = new HentOrganisasjonResponse();
            //response.setOrganisasjon(orggen.lagOrganisasjon(request.getOrgnummer()));
            Optional<OrganisasjonModell> organisasjonModell = organisasjonRepository.findById(request.getOrgnummer());
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
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjon/v4/Organisasjon_v4/hentNoekkelinfoOrganisasjonRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "hentNoekkelinfoOrganisasjon", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v4.HentNoekkelinfoOrganisasjon")
    @ResponseWrapper(localName = "hentNoekkelinfoOrganisasjonResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v4.HentNoekkelinfoOrganisasjonResponse")
    public HentNoekkelinfoOrganisasjonResponse hentNoekkelinfoOrganisasjon(@WebParam(name = "request") HentNoekkelinfoOrganisasjonRequest request) throws
            HentNoekkelinfoOrganisasjonUgyldigInput{

        LOG.info("hentNoekkelinfoOrganisasjon. Orgnummer: {}", request.getOrgnummer());
        if (request.getOrgnummer() != null) {

            HentNoekkelinfoOrganisasjonResponse response = new HentNoekkelinfoOrganisasjonResponse();
            OrganisasjonGenerator orggen = new OrganisasjonGenerator();
            Organisasjon org = orggen.lagOrganisasjon(request.getOrgnummer());

            response.setOrgnummer(request.getOrgnummer());
            response.setNavn(org.getNavn());
            response.setEnhetstype(orggen.lagEnhetstype(request.getOrgnummer()));
            return response;
        } else {
            throw new HentNoekkelinfoOrganisasjonUgyldigInput("Orgnummer ikke angitt", new UgyldigInput());
        }
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjon/v4/Organisasjon_v4/validerOrganisasjonRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "validerOrganisasjon", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v4.ValiderOrganisasjon")
    @ResponseWrapper(localName = "validerOrganisasjonResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v4.ValiderOrganisasjonResponse")
    public ValiderOrganisasjonResponse validerOrganisasjon(@WebParam(name = "request") ValiderOrganisasjonRequest request) {
        throw new NotImplementedException();
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjon/v4/Organisasjon_v4/hentVirksomhetsOrgnrForJuridiskOrgnrBolkRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "hentVirksomhetsOrgnrForJuridiskOrgnrBolk", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v4.HentVirksomhetsOrgnrForJuridiskOrgnrBolk")
    @ResponseWrapper(localName = "hentVirksomhetsOrgnrForJuridiskOrgnrBolkResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v4.HentVirksomhetsOrgnrForJuridiskOrgnrBolkResponse")
    public HentVirksomhetsOrgnrForJuridiskOrgnrBolkResponse hentVirksomhetsOrgnrForJuridiskOrgnrBolk(@WebParam(name = "request") HentVirksomhetsOrgnrForJuridiskOrgnrBolkRequest hentVirksomhetsOrgnrForJuridiskOrgnrBolkRequest)  {
        throw new NotImplementedException();
    }

    @Override
    @WebMethod(action = "http://nav.no/tjeneste/virksomhet/organisasjon/v4/Organisasjon_v4/finnOrganisasjonsendringerListeRequest")
    @WebResult(name = "response")
    @RequestWrapper(localName = "finnOrganisasjonsendringerListe", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v4.FinnOrganisasjonsendringerListe")
    @ResponseWrapper(localName = "finnOrganisasjonsendringerListeResponse", targetNamespace = "http://nav.no/tjeneste/virksomhet/organisasjon/v4",
            className = "no.nav.tjeneste.virksomhet.organisasjon.v4.FinnOrganisasjonsendringerListeResponse")
    public FinnOrganisasjonsendringerListeResponse finnOrganisasjonsendringerListe(@WebParam(name = "request") FinnOrganisasjonsendringerListeRequest finnOrganisasjonsendringerListeRequest) {
        throw new NotImplementedException();
    }
}
