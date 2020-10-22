package no.nav.pensjon.vtp.application.configuration;

import javax.xml.ws.WebServiceFeature;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

@Configuration
public class SoapWebServiceConfig {
    @Autowired
    private Bus bus;

    @Bean
    public Object endpointRegistration(TestscenarioBuilderRepository testscenarioBuilderRepository, JournalRepository journalRepository, GsakRepo gsakRepo) {
        setup(testscenarioBuilderRepository, journalRepository, gsakRepo);
        return new Object();
    }

    public void setup(TestscenarioBuilderRepository repo, JournalRepository journalRepository, GsakRepo gsakRepo) {

        //publishWebService(new SecurityTokenServiceMockImpl(), "/SecurityTokenServiceProvider/");

        // TODO NB! disse "access wsdl on..." er tvilsomme, da de de r/aktoeturnerer WSDL/XSD *generert* fra JAXB-klassene, ikke originaldokumentene
        publishWebService(new AktoerServiceMockImpl(repo), "erregister/ws/Aktoer/v2");
        // access wsdl on http://localhost:7999/aktoer?wsdl
        publishWebService(new SakServiceMockImpl(gsakRepo), "/nav-gsak-ws/SakV1");
        // access wsdl on http://localhost:7999/sak?wsdl
        publishWebService(new PersonServiceMockImpl(repo), "/tpsws/ws/Person/v3");
        // access wsdl on http://localhost:7999/person?wsdl
        publishWebService(new JournalV2ServiceMockImpl(journalRepository), "/joark/Journal/v2");
        // access wsdl on http://localhost:7999/journal?wsdl
        publishWebService(new JournalV3ServiceMockImpl(journalRepository), "/joark/Journal/v3");
        publishWebService(new InngaaendeJournalServiceMockImpl(journalRepository), "/joark/InngaaendeJournal/v1");
        publishWebService(new BehandleInngaaendeJournalV1ServiceMock(journalRepository),"/services/behandleinngaaendejournal/v1");
        // publishWebService(new OppgavebehandlingServiceMockImpl(gsakRepo), "/nav-gsak-ws/BehandleOppgaveV1");
        // access wsdl on http://localhost:7999/oppgavebehandling?wsdl
        publishWebService(new BehandleOppgaveServiceMockImpl(gsakRepo), "/nav-gsak-ws/BehandleOppgaveV1");
        // access wsdl on http://localhost:7999/behandleoppgave?wsdl
        publishWebService(new BehandleSak2ServiceMockImpl(gsakRepo, repo), "/nav-gsak-ws/BehandleSakV2");
        // access wsdl on http://localhost:7999/behandlesakV2?wsdl
        publishWebService(new KodeverkServiceMockImpl(repo), "/kodeverk/ws/Kodeverk/v2");

        publishWebService(new DokumentproduksjonV2MockImpl(journalRepository), "/dokprod/ws/dokumentproduksjon/v2");
        publishWebService(new MeldekortUtbetalingsgrunnlagMockImpl(repo), "/ail_ws/MeldekortUtbetalingsgrunnlag_v1");
        publishWebService(new ArbeidsevnevurderingV1Mock(), "/ail_ws/Arbeidsevnevurdering_v1");
        publishWebService(new YtelseskontraktV3Mock(),"/ail_ws/Ytelseskontrakt_v3");
        publishWebService(new MedlemServiceMockImpl(repo), "/medl2/ws/Medlemskap/v2");
        publishWebService(new ArbeidsfordelingMockImpl(repo), "/norg2/ws/Arbeidsfordeling/v1");
        publishWebService(new OrganisasjonEnhetMock(), "/norg2/ws/OrganisasjonEnhet/v2");
        publishWebService(new InntektMockImpl(repo), "/inntektskomponenten-ws/inntekt/v3/Inntekt");
        publishWebService(new OppgaveServiceMockImpl(), "/nav-gsak-ws/OppgaveV3");
        publishWebService(new ArbeidsforholdMockImpl(repo), "/aareg-core/ArbeidsforholdService/v3");
        publishWebService(new ArbeidsforholdMockImpl(repo), "/aareg-services/ArbeidsforholdService/v3");
        publishWebService(new OrganisasjonV4MockImpl(repo), "/ereg/ws/OrganisasjonService/v4");
        publishWebService(new OrganisasjonV5MockImpl(repo),"/ereg/ws/OrganisasjonService/v5");
        publishWebService(new BehandleJournalV3ServiceMockImpl(),"/services/behandlejournal/v3");
        publishWebService(new TilbakekrevingServiceMockImpl(), "/tilbakekreving/services/tilbakekrevingService");
        publishWebService(new EgenAnsattServiceMockImpl(), "soap/tpsws/EgenAnsatt_v1");
        publishWebService(new InnsynJournalServiceMockImpl(), "soap/joark/InnsynJournal/v2");
        publishWebService(new PsakPersonServiceMockImpl(repo), "/esb/nav-cons-pen-psak-personWeb/sca/PSAKPersonWSEXP");
        publishWebService(new PenPersonServiceMockImpl(repo), "/esb/nav-cons-pen-pen-personWeb/sca/PENPersonWSEXP");
        publishWebService(new PselvPersonServiceMockImpl(repo), "/esb/nav-cons-pen-pselv-personWeb/sca/PSELVPersonWSEXP");
        publishWebService(new PersonV2ServiceMockImpl(repo), "/esb/nav-tjeneste-person_v2Web/sca/PersonWSEXP");
        publishWebService(new HenvendelseMock(), "/esb/nav-cons-pen-psak-henvendelseWeb/sca/PSAKHenvendelseWSEXP");
        publishWebService(new SakMock(), "/esb/nav-tjeneste-sak_v1Web/sca/SakWSEXP");
        publishWebService(new JournalMock(),"/esb/nav-tjeneste-journal_v2Web/sca/JournalWSEXP");
        publishWebService(new PsakNavOrgEnhetMock(), "/esb/nav-cons-pen-psak-navorgenhetWeb/sca/PSAKNAVOrgEnhetWSEXP");
        publishWebService(new PenNavOrgEnhetMock(), "/esb/nav-cons-pen-pen-navorgenhetWeb/sca/PENNAVOrgEnhetWSEXP");
        publishWebService(new DialogMock(),"/henvendelse/services/domene.Virksomhet/Dialog_v1");
        publishWebService(new DigitalKontaktinformasjonV1Mock(),"/ws/DigitalKontaktinformasjon/v1");
        publishWebService(new PenTjenestePensjonMock(), "/esb/nav-cons-pen-pen-tjenestepensjonWeb/sca/PENTjenestepensjonWSEXP");
        publishWebService(new OppgaveBehandlingMock(), "/esb/nav-tjeneste-oppgavebehandling_v2Web/sca/OppgavebehandlingWSEXP");
        publishWebService(new MedlemskapMock(), "/esb/nav-tjeneste-medlemskap_v1Web/sca/MedlemskapWSEXP");
        publishWebService(new PENFullmaktMock(), "/esb/nav-cons-pen-pen-fullmaktWeb/sca/PENFullmaktWSEXP");
        publishWebService(new PENOppdragMock(), "/esb/nav-cons-pen-pen-oppdragWeb/sca/PENOppdragWSEXP");
        publishWebService(new PSAKPPEN015Mock(), "/esb/nav-cons-pen-psak-ppen015Web/sca/PSAKPPEN015WSEXP");
        publishWebService(new PENInntektMock(),"/esb/nav-cons-pen-pen-inntektWeb/sca/PENInntektWSEXP");
        publishWebService(new GetApplicationVersionMock(),"/esb/nav-cons-test-getapplicationversionWeb/sca/TESTGetApplicationVersionWSEXP");
        publishWebService(new PsakSamhandlerMock(),"/esb/nav-cons-pen-psak-samhandlerWeb/sca/PSAKSamhandlerWSEXP");
        publishWebService(new NavAnsattServiceMockImpl(), "/esb/nav-cons-pen-psak-navansattWeb/sca/PSAKNAVAnsattWSEXP");
    }

    private void publishWebService(Object ws, String path, WebServiceFeature... features ) {
        new EndpointImpl(bus, ws).publish(path);
    }
}
