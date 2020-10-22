package no.nav.pensjon.vtp.server;

import java.lang.reflect.Method;

import javax.xml.ws.Endpoint;
import javax.xml.ws.WebServiceFeature;

import org.eclipse.jetty.http.spi.HttpSpiContextHandler;
import org.eclipse.jetty.http.spi.JettyHttpContext;
import org.eclipse.jetty.http.spi.JettyHttpServer;

import no.nav.pensjon.vtp.auth.SecurityTokenServiceMockImpl;
import no.nav.pensjon.vtp.mocks.dialog.DialogMock;
import no.nav.pensjon.vtp.mocks.egenansatt.pip.egen.ansatt.v1.EgenAnsattServiceMockImpl;
import no.nav.pensjon.vtp.mocks.esb.getapplicationversion.GetApplicationVersionMock;
import no.nav.pensjon.vtp.mocks.navansatt.NavAnsattServiceMockImpl;
import no.nav.pensjon.vtp.mocks.oppdrag.okonomi.tilbakekrevingservice.TilbakekrevingServiceMockImpl;
import no.nav.pensjon.vtp.mocks.organisasjonenhet.v2.OrganisasjonEnhetMock;
import no.nav.pensjon.vtp.mocks.psak.HenvendelseMock;
import no.nav.pensjon.vtp.mocks.psak.JournalMock;
import no.nav.pensjon.vtp.mocks.psak.MedlemskapMock;
import no.nav.pensjon.vtp.mocks.psak.OppgaveBehandlingMock;
import no.nav.pensjon.vtp.mocks.psak.PENFullmaktMock;
import no.nav.pensjon.vtp.mocks.psak.PENInntektMock;
import no.nav.pensjon.vtp.mocks.psak.PENOppdragMock;
import no.nav.pensjon.vtp.mocks.psak.PSAKPPEN015Mock;
import no.nav.pensjon.vtp.mocks.psak.PenNavOrgEnhetMock;
import no.nav.pensjon.vtp.mocks.psak.PenPersonServiceMockImpl;
import no.nav.pensjon.vtp.mocks.psak.PenTjenestePensjonMock;
import no.nav.pensjon.vtp.mocks.psak.PersonV2ServiceMockImpl;
import no.nav.pensjon.vtp.mocks.psak.PsakNavOrgEnhetMock;
import no.nav.pensjon.vtp.mocks.psak.PsakPersonServiceMockImpl;
import no.nav.pensjon.vtp.mocks.psak.PsakSamhandlerMock;
import no.nav.pensjon.vtp.mocks.psak.PselvPersonServiceMockImpl;
import no.nav.pensjon.vtp.mocks.psak.SakMock;
import no.nav.pensjon.vtp.mocks.tjenestespesifikasjoner.DigitalKontaktinformasjonV1Mock;
import no.nav.pensjon.vtp.mocks.virksomhet.aktoer.v2.AktoerServiceMockImpl;
import no.nav.pensjon.vtp.mocks.virksomhet.arbeidsfordeling.v1.ArbeidsfordelingMockImpl;
import no.nav.pensjon.vtp.mocks.virksomhet.arbeidsforhold.v3.ArbeidsforholdMockImpl;
import no.nav.pensjon.vtp.mocks.virksomhet.arena.arbeidsevnevurdering.ArbeidsevnevurderingV1Mock;
import no.nav.pensjon.vtp.mocks.virksomhet.arena.meldekort.MeldekortUtbetalingsgrunnlagMockImpl;
import no.nav.pensjon.vtp.mocks.virksomhet.arena.ytelseskontrakt.YtelseskontraktV3Mock;
import no.nav.pensjon.vtp.mocks.virksomhet.behandleinngaaendejournal.v1.BehandleInngaaendeJournalV1ServiceMock;
import no.nav.pensjon.vtp.mocks.virksomhet.behandlejournal.v3.BehandleJournalV3ServiceMockImpl;
import no.nav.pensjon.vtp.mocks.virksomhet.behandleoppgave.v1.BehandleOppgaveServiceMockImpl;
import no.nav.pensjon.vtp.mocks.virksomhet.behandlesak.v2.BehandleSak2ServiceMockImpl;
import no.nav.pensjon.vtp.mocks.virksomhet.dokumentproduksjon.v2.DokumentproduksjonV2MockImpl;
import no.nav.pensjon.vtp.mocks.virksomhet.inngaaendejournal.v1.InngaaendeJournalServiceMockImpl;
import no.nav.pensjon.vtp.mocks.virksomhet.innsynjournal.v2.InnsynJournalServiceMockImpl;
import no.nav.pensjon.vtp.mocks.virksomhet.inntekt.v3.InntektMockImpl;
import no.nav.pensjon.vtp.mocks.virksomhet.journal.v2.JournalV2ServiceMockImpl;
import no.nav.pensjon.vtp.mocks.virksomhet.journal.v3.JournalV3ServiceMockImpl;
import no.nav.pensjon.vtp.mocks.virksomhet.kodeverk.v2.KodeverkServiceMockImpl;
import no.nav.pensjon.vtp.mocks.virksomhet.medlemskap.v2.MedlemServiceMockImpl;
import no.nav.pensjon.vtp.mocks.virksomhet.oppgave.v3.OppgaveServiceMockImpl;
import no.nav.pensjon.vtp.mocks.virksomhet.organisasjon.v4.OrganisasjonV4MockImpl;
import no.nav.pensjon.vtp.mocks.virksomhet.organisasjon.v5.OrganisasjonV5MockImpl;
import no.nav.pensjon.vtp.mocks.virksomhet.person.v3.PersonServiceMockImpl;
import no.nav.pensjon.vtp.mocks.virksomhet.sak.v1.GsakRepo;
import no.nav.pensjon.vtp.mocks.virksomhet.sak.v1.SakServiceMockImpl;
import no.nav.pensjon.vtp.testmodell.repo.JournalRepository;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioBuilderRepository;

public class SoapWebServiceConfig {

    private final JettyHttpServer jettyHttpServer;

    public SoapWebServiceConfig(JettyHttpServer jettyHttpServer) {
        this.jettyHttpServer = jettyHttpServer;
    }

    public void setup(TestscenarioBuilderRepository repo, JournalRepository journalRepository, GsakRepo gsakRepo) {

        publishWebService(new SecurityTokenServiceMockImpl(), "/soap/SecurityTokenServiceProvider/");

        // TODO NB! disse "access wsdl on..." er tvilsomme, da de de returnerer WSDL/XSD *generert* fra JAXB-klassene, ikke originaldokumentene
        publishWebService(new AktoerServiceMockImpl(repo), "/soap/aktoerregister/ws/Aktoer/v2");
        // access wsdl on http://localhost:7999/aktoer?wsdl
        publishWebService(new SakServiceMockImpl(gsakRepo), "/soap/nav-gsak-ws/SakV1");
        // access wsdl on http://localhost:7999/sak?wsdl
        publishWebService(new PersonServiceMockImpl(repo), "/soap/tpsws/ws/Person/v3");
        // access wsdl on http://localhost:7999/person?wsdl
        publishWebService(new JournalV2ServiceMockImpl(journalRepository), "/soap/joark/Journal/v2");
        // access wsdl on http://localhost:7999/journal?wsdl
        publishWebService(new JournalV3ServiceMockImpl(journalRepository), "/soap/joark/Journal/v3");
        publishWebService(new InngaaendeJournalServiceMockImpl(journalRepository), "/soap/joark/InngaaendeJournal/v1");
        publishWebService(new BehandleInngaaendeJournalV1ServiceMock(journalRepository),"/soap/services/behandleinngaaendejournal/v1");
        // publishWebService(new OppgavebehandlingServiceMockImpl(gsakRepo), "/soap/nav-gsak-ws/BehandleOppgaveV1");
        // access wsdl on http://localhost:7999/oppgavebehandling?wsdl
        publishWebService(new BehandleOppgaveServiceMockImpl(gsakRepo), "/soap/nav-gsak-ws/BehandleOppgaveV1");
        // access wsdl on http://localhost:7999/behandleoppgave?wsdl
        publishWebService(new BehandleSak2ServiceMockImpl(gsakRepo, repo), "/soap/nav-gsak-ws/BehandleSakV2");
        // access wsdl on http://localhost:7999/behandlesakV2?wsdl
        publishWebService(new KodeverkServiceMockImpl(repo), "/soap/kodeverk/ws/Kodeverk/v2");

        publishWebService(new DokumentproduksjonV2MockImpl(journalRepository), "/soap/dokprod/ws/dokumentproduksjon/v2");
        publishWebService(new MeldekortUtbetalingsgrunnlagMockImpl(repo), "/soap/ail_ws/MeldekortUtbetalingsgrunnlag_v1");
        publishWebService(new ArbeidsevnevurderingV1Mock(), "/soap/ail_ws/Arbeidsevnevurdering_v1");
        publishWebService(new YtelseskontraktV3Mock(),"/soap/ail_ws/Ytelseskontrakt_v3");
        publishWebService(new MedlemServiceMockImpl(repo), "/soap/medl2/ws/Medlemskap/v2");
        publishWebService(new ArbeidsfordelingMockImpl(repo), "/soap/norg2/ws/Arbeidsfordeling/v1");
        publishWebService(new OrganisasjonEnhetMock(), "/soap/norg2/ws/OrganisasjonEnhet/v2");
        publishWebService(new InntektMockImpl(repo), "/soap/inntektskomponenten-ws/inntekt/v3/Inntekt");
        publishWebService(new OppgaveServiceMockImpl(), "/soap/nav-gsak-ws/OppgaveV3");
        publishWebService(new ArbeidsforholdMockImpl(repo), "/soap/aareg-core/ArbeidsforholdService/v3");
        publishWebService(new ArbeidsforholdMockImpl(repo), "/soap/aareg-services/ArbeidsforholdService/v3");
        publishWebService(new OrganisasjonV4MockImpl(repo), "/soap/ereg/ws/OrganisasjonService/v4");
        publishWebService(new OrganisasjonV5MockImpl(repo),"/soap/ereg/ws/OrganisasjonService/v5");
        publishWebService(new BehandleJournalV3ServiceMockImpl(),"/soap/services/behandlejournal/v3");
        publishWebService(new TilbakekrevingServiceMockImpl(), "/soap/tilbakekreving/services/tilbakekrevingService");
        publishWebService(new EgenAnsattServiceMockImpl(), "soap/tpsws/EgenAnsatt_v1");
        publishWebService(new InnsynJournalServiceMockImpl(), "soap/joark/InnsynJournal/v2");
        publishWebService(new NavAnsattServiceMockImpl(), "/soap/esb/nav-cons-pen-psak-navansattWeb/sca/PSAKNAVAnsattWSEXP");
        publishWebService(new PsakPersonServiceMockImpl(repo), "/soap/esb/nav-cons-pen-psak-personWeb/sca/PSAKPersonWSEXP");
        publishWebService(new PenPersonServiceMockImpl(repo), "/soap/esb/nav-cons-pen-pen-personWeb/sca/PENPersonWSEXP");
        publishWebService(new PselvPersonServiceMockImpl(repo), "/soap/esb/nav-cons-pen-pselv-personWeb/sca/PSELVPersonWSEXP");
        publishWebService(new PersonV2ServiceMockImpl(repo), "/soap/esb/nav-tjeneste-person_v2Web/sca/PersonWSEXP");
        publishWebService(new HenvendelseMock(), "/soap/esb/nav-cons-pen-psak-henvendelseWeb/sca/PSAKHenvendelseWSEXP");
        publishWebService(new SakMock(), "/soap/esb/nav-tjeneste-sak_v1Web/sca/SakWSEXP");
        publishWebService(new JournalMock(),"/soap/esb/nav-tjeneste-journal_v2Web/sca/JournalWSEXP");
        publishWebService(new PsakNavOrgEnhetMock(), "/soap/esb/nav-cons-pen-psak-navorgenhetWeb/sca/PSAKNAVOrgEnhetWSEXP");
        publishWebService(new PenNavOrgEnhetMock(), "/soap/esb/nav-cons-pen-pen-navorgenhetWeb/sca/PENNAVOrgEnhetWSEXP");
        publishWebService(new DialogMock(),"/soap/henvendelse/services/domene.Virksomhet/Dialog_v1");
        publishWebService(new DigitalKontaktinformasjonV1Mock(),"/soap/ws/DigitalKontaktinformasjon/v1");
        publishWebService(new PenTjenestePensjonMock(), "/soap/esb/nav-cons-pen-pen-tjenestepensjonWeb/sca/PENTjenestepensjonWSEXP");
        publishWebService(new OppgaveBehandlingMock(), "/soap/esb/nav-tjeneste-oppgavebehandling_v2Web/sca/OppgavebehandlingWSEXP");
        publishWebService(new MedlemskapMock(), "/soap/esb/nav-tjeneste-medlemskap_v1Web/sca/MedlemskapWSEXP");
        publishWebService(new PENFullmaktMock(), "/soap/esb/nav-cons-pen-pen-fullmaktWeb/sca/PENFullmaktWSEXP");
        publishWebService(new PENOppdragMock(), "/soap/esb/nav-cons-pen-pen-oppdragWeb/sca/PENOppdragWSEXP");
        publishWebService(new PSAKPPEN015Mock(), "/soap/esb/nav-cons-pen-psak-ppen015Web/sca/PSAKPPEN015WSEXP");
        publishWebService(new PENInntektMock(),"/soap/esb/nav-cons-pen-pen-inntektWeb/sca/PENInntektWSEXP");
        publishWebService(new GetApplicationVersionMock(),"/soap/esb/nav-cons-test-getapplicationversionWeb/sca/TESTGetApplicationVersionWSEXP");
        publishWebService(new PsakSamhandlerMock(),"/soap/esb/nav-cons-pen-psak-samhandlerWeb/sca/PSAKSamhandlerWSEXP");
    }

    private void publishWebService(Object ws, String path, WebServiceFeature... features ) {
        // binder sammen jetty, tjeneste til JAX-WS Endpoint
        JettyHttpContext context = buildHttpContext(path);
        Endpoint endpoint = Endpoint.create(ws, features);
        endpoint.publish(context);
    }

    private JettyHttpContext buildHttpContext(String contextString) {
        JettyHttpContext ctx = (JettyHttpContext) jettyHttpServer.createContext(contextString);
        try {
            Method method = JettyHttpContext.class.getDeclaredMethod("getJettyContextHandler");
            method.setAccessible(true);
            HttpSpiContextHandler contextHandler = (HttpSpiContextHandler) method.invoke(ctx);
            contextHandler.start();
        } catch (Exception e) {
            throw new IllegalStateException("Kunne ikke starte server", e);
        }
        return ctx;
    }
}
