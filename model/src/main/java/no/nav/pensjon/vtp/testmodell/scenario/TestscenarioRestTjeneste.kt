package no.nav.pensjon.vtp.testmodell.scenario

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import no.nav.pensjon.vtp.testmodell.pensjon_testdata.PensjonTestdataService
import no.nav.pensjon.vtp.testmodell.scenario.dto.TestscenarioDto
import no.nav.pensjon.vtp.testmodell.scenario.dto.TestscenarioPersonopplysningDto
import no.nav.pensjon.vtp.testmodell.scenario.dto.TestscenariodataDto
import no.nav.pensjon.vtp.testmodell.repo.Testscenario
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioService
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplateRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*

@RestController
@Api(tags = ["Testscenario"])
@RequestMapping("/api/testscenarios")
class TestscenarioRestTjeneste(private val templateRepository: TestscenarioTemplateRepository, private val testscenarioService: TestscenarioService, private val pensjonTestdataService: PensjonTestdataService) {
    @GetMapping(produces = [APPLICATION_JSON_VALUE])
    @ApiOperation(value = "", notes = "Henter alle templates som er initiert i minnet til VTP", responseContainer = "List", response = TestscenarioDto::class)
    fun hentInitialiserteCaser() =
            testscenarioService.findAll()
                    .map { konverterTilTestscenarioDto(it) }

    @GetMapping(value = ["/{id}"], produces = [APPLICATION_JSON_VALUE])
    @ApiOperation(value = "", notes = "Returnerer testscenario som matcher id", response = TestscenarioDto::class)
    fun hentScenario(@PathVariable("id") id: String) =
            testscenarioService.getTestscenario(id)
                    ?.let { ok(konverterTilTestscenarioDto(it)) }
                    ?: notFound().build<TestscenarioDto>()

    @PostMapping(value = ["/{key}"], produces = [APPLICATION_JSON_VALUE])
    @ApiOperation(value = "", notes = "Initialiserer et test scenario basert på angitt template key i VTPs eksempel templates", response = TestscenarioDto::class)
    fun initialiserTestscenario(@PathVariable("key") templateKey: String) =
            templateRepository.finn(templateKey)
                    ?.let {
                        val testscenario = testscenarioService.opprettTestscenario(it)
                        pensjonTestdataService.opprettData(testscenario)
                        status(CREATED)
                                .body(konverterTilTestscenarioDto(testscenario))
                    }
                    ?: notFound().build<TestscenarioDto>()

    @PostMapping(produces = [APPLICATION_JSON_VALUE])
    @ApiOperation(value = "", notes = "Initialiserer et testscenario basert på angitt json streng og returnerer det initialiserte objektet", response = TestscenarioDto::class)
    fun initialiserTestScenario(@RequestBody testscenarioJson: String) =
            status(CREATED)
                    .body(konverterTilTestscenarioDto(testscenarioService.opprettTestscenarioFraJsonString(testscenarioJson)))

    @DeleteMapping("/{id}")
    @ApiOperation(value = "", notes = "Sletter et initialisert testscenario som matcher id")
    fun slettScenario(@PathVariable("id") id: String): ResponseEntity<*> {
        logger.info("Sletter testscenario med id: [{}]", id)
        testscenarioService.slettScenario(id)
        return noContent().build<Any>()
    }

    private fun konverterTilTestscenarioDto(testscenario: Testscenario): TestscenarioDto {
        return konverterTilTestscenarioDto(testscenario, testscenario.templateNavn.replaceFirst("[-_].+$".toRegex(), ""), testscenario.templateNavn)
    }

    private fun konverterTilTestscenarioDto(testscenario: Testscenario, templateKey: String?, templateName: String) =
            TestscenarioDto(
                templateKey = templateKey,
                templateNavn = templateName,
                testscenarioId = testscenario.id,
                variabler = testscenario.vars,
                personopplysninger = TestscenarioPersonopplysningDto(
                        testscenario.personopplysninger.søker.ident,
                        testscenario.personopplysninger.annenPart?.ident,
                        testscenario.personopplysninger.søker.aktørIdent,
                        testscenario.personopplysninger.annenPart?.aktørIdent,
                        testscenario.personopplysninger.søker.fødselsdato
                ),
                scenariodataDto = TestscenariodataDto(
                        testscenario.søkerInntektYtelse?.inntektskomponentModell,
                        testscenario.søkerInntektYtelse?.arbeidsforholdModell
                ),
                scenariodataAnnenpartDto = testscenario.annenpartInntektYtelse?.let { TestscenariodataDto(it.inntektskomponentModell, it.arbeidsforholdModell) }
        )

    companion object {
        private val logger = LoggerFactory.getLogger(TestscenarioRestTjeneste::class.java)
    }
}
