package no.nav.pensjon.vtp.mocks.virksomhet.arbeidsfordeling.v3;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import no.nav.pensjon.vtp.mocks.virksomhet.arbeidsforhold.v3.ArbeidsforholdMockImpl;
import no.nav.pensjon.vtp.testmodell.identer.IdenterIndeks;
import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseIndeks;
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseIndeks;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonIndeks;
import no.nav.pensjon.vtp.testmodell.repo.Testscenario;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioService;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplate;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplateRepository;
import no.nav.pensjon.vtp.testmodell.repo.impl.BasisdataProviderFileImpl;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioRepositoryImpl;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioFraTemplateMapper;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioServiceImpl;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioTemplateLoader;
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioTemplateRepositoryImpl;
import no.nav.pensjon.vtp.testmodell.virksomhet.VirksomhetIndeks;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.binding.FinnArbeidsforholdPrArbeidstakerUgyldigInput;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.NorskIdent;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Regelverker;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.meldinger.FinnArbeidsforholdPrArbeidstakerRequest;

public class ArbeidsforholdMockTest {
    private final PersonIndeks personIndeks = new PersonIndeks();
    private final InntektYtelseIndeks inntektYtelseIndeks = new InntektYtelseIndeks();
    private final OrganisasjonIndeks organisasjonIndeks = new OrganisasjonIndeks();

    private final TestscenarioRepository testscenarioRepository = new TestscenarioRepositoryImpl();
    private final TestscenarioService testRepo = getTestscenarioRepository();

    private TestscenarioServiceImpl getTestscenarioRepository() {
        try {
            AdresseIndeks adresseIndeks = BasisdataProviderFileImpl.loadAdresser();
            VirksomhetIndeks virksomhetIndeks = BasisdataProviderFileImpl.loadVirksomheter();
            TestscenarioFraTemplateMapper testscenarioFraTemplateMapper = new TestscenarioFraTemplateMapper(adresseIndeks, new IdenterIndeks(), virksomhetIndeks);
            return new TestscenarioServiceImpl(
                    testscenarioFraTemplateMapper, testscenarioRepository, personIndeks, inntektYtelseIndeks, organisasjonIndeks);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private TestscenarioTemplateRepository templateRepository;

    @Before
    public void setup() {
        TestscenarioTemplateLoader loader = new TestscenarioTemplateLoader();

        templateRepository = new TestscenarioTemplateRepositoryImpl(loader.load());
    }

    @Test
    public void finnArbeidsforholdPrArbeidstakerTest(){
        TestscenarioTemplate template = templateRepository.finn("50")
                .orElseThrow(() -> new RuntimeException("Unable to find template using key"));

        Testscenario testscenario = testRepo.opprettTestscenario(template);

        ArbeidsforholdMockImpl arbeidsforholdMock = new ArbeidsforholdMockImpl(inntektYtelseIndeks, personIndeks);

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
        } catch (FinnArbeidsforholdPrArbeidstakerUgyldigInput finnArbeidsforholdPrArbeidstakerSikkerhetsbegrensning) {
            finnArbeidsforholdPrArbeidstakerSikkerhetsbegrensning.printStackTrace();
        }
    }
}
