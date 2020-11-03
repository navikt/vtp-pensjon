package no.nav.pensjon.vtp.miscellaneous.api.scenario;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import no.nav.pensjon.vtp.kontrakter.TestscenarioDto;
import no.nav.pensjon.vtp.kontrakter.TestscenarioPersonopplysningDto;
import no.nav.pensjon.vtp.kontrakter.TestscenariodataDto;
import no.nav.pensjon.vtp.miscellaneous.api.pensjon_testdata.PensjonTestdataService;
import no.nav.pensjon.vtp.testmodell.inntektytelse.arbeidsforhold.ArbeidsforholdModell;
import no.nav.pensjon.vtp.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.BarnModell;
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModell;
import no.nav.pensjon.vtp.testmodell.repo.Testscenario;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioRepository;
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@Api(tags = {"Testscenario"})
@RequestMapping("/api/testscenarios")
public class TestscenarioRestTjeneste {
    private static final Logger logger = LoggerFactory.getLogger(TestscenarioRestTjeneste.class);

    private static final String TEMPLATE_KEY = "key";
    private static final String SCENARIO_ID = "id";

    private final TestscenarioTemplateRepository templateRepository;
    private final TestscenarioRepository testscenarioRepository;
    private final PensjonTestdataService pensjonTestdataService;

    public TestscenarioRestTjeneste(TestscenarioTemplateRepository templateRepository, TestscenarioRepository testscenarioRepository, PensjonTestdataService pensjonTestdataService) {
        this.templateRepository = templateRepository;
        this.testscenarioRepository = testscenarioRepository;
        this.pensjonTestdataService = pensjonTestdataService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "", notes = "Henter alle templates som er initiert i minnet til VTP", responseContainer = "List", response = TestscenarioDto.class)
    public List<TestscenarioDto> hentInitialiserteCaser() {
        Map<String, Testscenario> testscenarios = testscenarioRepository.getTestscenarios();
        List<TestscenarioDto> testscenarioList = new ArrayList<>();

        testscenarios.forEach((key, testscenario) -> {
            if (testscenario.getTemplateNavn() != null) {
                testscenarioList.add(konverterTilTestscenarioDto(testscenario, testscenario.getTemplateNavn()));
            } else {
                testscenarioList.add(konverterTilTestscenarioDto(testscenario));
            }
        });

        return testscenarioList;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "", notes = "Returnerer testscenario som matcher id", response = TestscenarioDto.class)
    public ResponseEntity hentScenario(@PathVariable(SCENARIO_ID) String id){
        if (testscenarioRepository.getTestscenario(id) != null) {
            Testscenario testscenario = testscenarioRepository.getTestscenario(id);
            return ResponseEntity.ok(konverterTilTestscenarioDto(testscenario, testscenario.getTemplateNavn()));
        } else {
            return ResponseEntity.noContent().build();
        }

    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value="", notes="Oppdaterer hele scenario som matcher id", response = TestscenarioDto.class)
    public ResponseEntity oppdaterHeleScenario(@PathVariable(SCENARIO_ID) String id, TestscenarioDto testscenarioDto){
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value="", notes="Oppdaterer deler av et scenario som matcher id", response = TestscenarioDto.class)
    public ResponseEntity endreScenario(@PathVariable(SCENARIO_ID) String id, String patchArray){
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @PostMapping(value = "/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "", notes = ("Initialiserer et test scenario basert på angitt template key i VTPs eksempel templates"), response = TestscenarioDto.class)
    public ResponseEntity initialiserTestscenario(@PathVariable(TEMPLATE_KEY) String templateKey) {
        return templateRepository.finn(templateKey)
                .map(template -> {
                    final Testscenario testscenario = testscenarioRepository.opprettTestscenario(template, new HashMap<>());
                    logger.info("Initialiserer testscenario i VTP fra template: [{}] med id: [{}] ", templateKey, testscenario.getId());

                    pensjonTestdataService.opprettData(testscenario);

                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(konverterTilTestscenarioDto(testscenario, testscenario.getTemplateNavn()));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "", notes = ("Initialiserer et testscenario basert på angitt json streng og returnerer det initialiserte objektet"), response = TestscenarioDto.class)
    public ResponseEntity initialiserTestScenario(String testscenarioJson) {
        Testscenario testscenario = testscenarioRepository.opprettTestscenarioFraJsonString(testscenarioJson, new HashMap<>());
        logger.info("Initialiserer testscenario med ekstern testdatadefinisjon. Opprettet med id: [{}] ", testscenario.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(konverterTilTestscenarioDto(testscenario, testscenario.getTemplateNavn()));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "", notes= "Sletter et initialisert testscenario som matcher id")
    public ResponseEntity slettScenario(@PathVariable(SCENARIO_ID) String id) {
        logger.info("Sletter testscenario med id: [{}]", id);
        if(testscenarioRepository.slettScenario(id)){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    private TestscenarioDto konverterTilTestscenarioDto(Testscenario testscenario) {
        return konverterTilTestscenarioDto(testscenario, null, null);
    }

    private TestscenarioDto konverterTilTestscenarioDto(Testscenario testscenario, String templateNavn) {
        String templateKey = null;
        if (templateNavn != null) {
            templateKey = templateNavn.replaceFirst("[-_].+$", "");
        }
        return konverterTilTestscenarioDto(testscenario, templateKey, templateNavn);
    }

    private TestscenarioDto konverterTilTestscenarioDto(Testscenario testscenario, String templateKey, String templateName) {
        String fnrSøker = testscenario.getPersonopplysninger().getSøker().getIdent();
        String fnrAnnenPart = null;
        String aktørIdAnnenPart = null;
        if(testscenario.getPersonopplysninger().getAnnenPart() != null) {
            fnrAnnenPart = testscenario.getPersonopplysninger().getAnnenPart().getIdent();
            aktørIdAnnenPart = testscenario.getPersonopplysninger().getAnnenPart().getAktørIdent();

        }
        String aktørIdSøker = testscenario.getPersonopplysninger().getSøker().getAktørIdent();
        fødselsdatoBarn(testscenario);
        TestscenarioPersonopplysningDto scenarioPersonopplysninger = new TestscenarioPersonopplysningDto(
                fnrSøker,
                fnrAnnenPart,
                aktørIdSøker,
                aktørIdAnnenPart,
                testscenario.getPersonopplysninger().getSøker().getFødselsdato());

        TestscenariodataDto scenariodata = new TestscenariodataDto();
        if(testscenario.getSøkerInntektYtelse() != null) {
            ArbeidsforholdModell arbeidsforholdModell = testscenario.getSøkerInntektYtelse().getArbeidsforholdModell();
            InntektskomponentModell inntektskomponentModell = testscenario.getSøkerInntektYtelse().getInntektskomponentModell();
            scenariodata = new TestscenariodataDto(inntektskomponentModell, arbeidsforholdModell);
        }

        TestscenariodataDto scenariodataAnnenpart = null;
        if (testscenario.getAnnenpartInntektYtelse() != null) {
            ArbeidsforholdModell arbeidsforholdModellAnnenpart = testscenario.getAnnenpartInntektYtelse().getArbeidsforholdModell();
            InntektskomponentModell inntektskomponentModellAnnenpart = testscenario.getAnnenpartInntektYtelse().getInntektskomponentModell();
            scenariodataAnnenpart = new TestscenariodataDto(inntektskomponentModellAnnenpart, arbeidsforholdModellAnnenpart);
        }

        return new TestscenarioDto(
                templateKey,
                templateName,
                testscenario.getId(),
                testscenario.getVariabelContainer().getVars(),
                scenarioPersonopplysninger,
                scenariodata,
                scenariodataAnnenpart);

    }

    private Optional<LocalDate> fødselsdatoBarn(Testscenario testscenario) {
        Optional<BarnModell> barnModell = testscenario.getPersonopplysninger().getFamilierelasjoner()
                .stream()
                .filter(modell -> modell.getTil() instanceof BarnModell)
                .map(modell -> ((BarnModell) modell.getTil()))
                .findFirst();

        return barnModell.map(PersonModell::getFødselsdato);
    }
}
