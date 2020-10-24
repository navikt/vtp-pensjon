package no.nav.pensjon.vtp.mocks.virksomhet.arbeidsfordeling.v3;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import no.nav.pensjon.vtp.testmodell.repo.Testscenario;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplate;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplateRepository;
import no.nav.pensjon.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioTemplateRepositoryImpl;
import no.nav.pensjon.vtp.mocks.virksomhet.arbeidsforhold.v3.ArbeidsforholdMockImpl;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.binding.FinnArbeidsforholdPrArbeidstakerSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.binding.FinnArbeidsforholdPrArbeidstakerUgyldigInput;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.NorskIdent;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Regelverker;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.meldinger.FinnArbeidsforholdPrArbeidstakerRequest;

public class ArbeidsforholdMockTest {

    private TestscenarioRepository testRepo;

    private TestscenarioTemplateRepository templateRepository;

    @Before
    public void setup() throws IOException {
        System.setProperty("scenarios.dir", "../../model/scenarios");
        TestscenarioTemplateRepositoryImpl templateRepositoryImpl = new TestscenarioTemplateRepositoryImpl();
        templateRepositoryImpl.load();

        templateRepository = templateRepositoryImpl;
        testRepo = new TestscenarioRepositoryImpl(new BasisdataProviderFileImpl());
    }

    @Test
    public void finnArbeidsforholdPrArbeidstakerTest(){
        TestscenarioTemplate template = templateRepository.finn("50");

        Testscenario testscenario = testRepo.opprettTestscenario(template);

        ArbeidsforholdMockImpl arbeidsforholdMock = new ArbeidsforholdMockImpl(testRepo.getPersonIndeks(), testRepo);

        FinnArbeidsforholdPrArbeidstakerRequest finnArbeidsforholdPrArbeidstakerRequest = new FinnArbeidsforholdPrArbeidstakerRequest();

        NorskIdent norskIdent = new NorskIdent();
        norskIdent.setIdent(testscenario.getVariabelContainer().getVar("for1"));
        finnArbeidsforholdPrArbeidstakerRequest.setIdent(norskIdent);

        Regelverker regelverk = new Regelverker();
        regelverk.setKodeverksRef("A_ORDNINGEN");
        finnArbeidsforholdPrArbeidstakerRequest.setRapportertSomRegelverk(regelverk);

        testscenario.getSøkerInntektYtelse();


        try {
            arbeidsforholdMock.finnArbeidsforholdPrArbeidstaker(finnArbeidsforholdPrArbeidstakerRequest);
        } catch (FinnArbeidsforholdPrArbeidstakerSikkerhetsbegrensning | FinnArbeidsforholdPrArbeidstakerUgyldigInput finnArbeidsforholdPrArbeidstakerSikkerhetsbegrensning) {
            finnArbeidsforholdPrArbeidstakerSikkerhetsbegrensning.printStackTrace();
        }
    }
}
