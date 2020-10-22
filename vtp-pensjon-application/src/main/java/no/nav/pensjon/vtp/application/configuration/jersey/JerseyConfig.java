package no.nav.pensjon.vtp.application.configuration.jersey;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import no.nav.InstitusjonOppholdMock;
import no.nav.PDLMock;
import no.nav.aktoerregister.rest.api.v1.AktoerIdentMock;
import no.nav.dokarkiv.JournalpostMock;
import no.nav.dokdistfordeling.DokdistfordelingMock;
import no.nav.pensjon.vtp.miscellaneous.api.journalforing.JournalforingRestTjeneste;
import no.nav.pensjon.vtp.miscellaneous.api.sak.SakRestTjeneste;
import no.nav.pensjon.vtp.miscellaneous.api.scenario.TestscenarioRestTjeneste;
import no.nav.pensjon.vtp.miscellaneous.api.scenario.TestscenarioTemplateRestTjeneste;
import no.nav.pensjon.vtp.miscellaneous.rest.IsAliveImpl;
import no.nav.pensjon.vtp.miscellaneous.rest.IsReadyImpl;
import no.nav.pensjon.vtp.miscellaneous.rest.PeproxyResource;
import no.nav.infotrygdks.InfotrygdKontantstotteMock;
import no.nav.medl2.rest.api.v1.MedlemskapPingMock;
import no.nav.medl2.rest.api.v1.MedlemskapsunntakMock;
import no.nav.oppgave.OppgaveInternalAliveMockImpl;
import no.nav.oppgave.OppgaveKontantstotteMockImpl;
import no.nav.oppgave.OppgaveMockImpl;
import no.nav.pensjon.vtp.auth.LoginService;
import no.nav.pensjon.vtp.auth.Oauth2RestService;
import no.nav.pensjon.vtp.auth.PdpRestTjeneste;
import no.nav.pensjon.vtp.auth.STSRestTjeneste;
import no.nav.pensjon.vtp.auth.azuread.AzureAdNAVAnsattService;
import no.nav.pensjon.vtp.auth.azuread.MicrosoftGraphApiMock;
import no.nav.psak.aktoerregister.rest.api.v1.PsakAktoerIdentMock;
import no.nav.sigrun.SigrunMock;
import no.nav.pensjon.vtp.mocks.fpformidling.FpFormidlingMock;
import no.nav.pensjon.vtp.mocks.virksomhet.arbeidsfordeling.rest.ArbeidsfordelingRestMock;
import no.nav.pensjon.vtp.mocks.virksomhet.arbeidsfordeling.rest.EnhetRestMock;
import no.nav.pensjon.vtp.mocks.virksomhet.infotrygd.rest.InfotrygdGrunnlagMock;
import no.nav.tps.proxy.api.v1.innsyn.InnsynMock;
import no.nav.vtp.DummyRestTjeneste;
import no.nav.vtp.DummyRestTjenesteBoolean;
import no.nav.vtp.EnonicMock;
import no.nav.vtp.hentinntektlistebolk.HentInntektlisteBolkREST;

@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        // funksjonelle mocks for rest
        register(SigrunMock.class);
        register(JournalpostMock.class);
        register(InfotrygdKontantstotteMock.class);
        register(InfotrygdGrunnlagMock.class);
        register(ArbeidsfordelingRestMock.class);
        register(EnhetRestMock.class);
        register(TestscenarioTemplateRestTjeneste.class);
        register(TestscenarioRestTjeneste.class);
        register(JournalforingRestTjeneste.class);
        register(SakRestTjeneste.class);
        register(HentInntektlisteBolkREST.class);
        register(FpFormidlingMock.class);
        register(DummyRestTjeneste.class);
        register(DummyRestTjenesteBoolean.class);
        register(EnonicMock.class);
        register(MedlemskapPingMock.class);
        register(MedlemskapsunntakMock.class);
        register(OppgaveKontantstotteMockImpl.class);
        register(OppgaveMockImpl.class);
        register(OppgaveInternalAliveMockImpl.class);
        register(AktoerIdentMock.class);
        register(PsakAktoerIdentMock.class);
        register(InnsynMock.class);
        register(DokdistfordelingMock.class);
        register(STSRestTjeneste.class);
        register(InstitusjonOppholdMock.class);
        register(PDLMock.class);

        // tekniske ting
        register(Oauth2RestService.class);
        register(LoginService.class);
        register(AzureAdNAVAnsattService.class);
        register(PeproxyResource.class);
        register(MicrosoftGraphApiMock.class);
        register(PdpRestTjeneste.class);

        register(IsAliveImpl.class);
        register(IsReadyImpl.class);
    }

}
