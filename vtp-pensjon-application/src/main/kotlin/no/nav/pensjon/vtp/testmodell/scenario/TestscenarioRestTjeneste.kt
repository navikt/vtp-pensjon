package no.nav.pensjon.vtp.testmodell.scenario

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.pensjon.vtp.testmodell.pensjon_testdata.PensjonTestdataScenario
import no.nav.pensjon.vtp.testmodell.pensjon_testdata.PensjonTestdataService
import no.nav.pensjon.vtp.testmodell.repo.Testscenario
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplateRepository
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioService
import no.nav.pensjon.vtp.testmodell.scenario.dto.OpprettSakDto
import no.nav.pensjon.vtp.testmodell.scenario.dto.TestscenarioDto
import no.nav.pensjon.vtp.testmodell.scenario.dto.TestscenarioPersonopplysningDto
import no.nav.pensjon.vtp.testmodell.scenario.dto.TestscenariodataDto
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException

@RestController
@Tag(name = "Testscenario")
@RequestMapping("/api/testscenarios")
class TestscenarioRestTjeneste(
    private val templateRepository: TestscenarioTemplateRepository,
    private val testscenarioService: TestscenarioService,
    private val pensjonTestdataService: PensjonTestdataService
) {
    @GetMapping
    @Operation(
        description = "Henter alle templates som er initiert i minnet til VTP",
    )
    fun hentInitialiserteCaser() =
        testscenarioService.findAll()
            .map { konverterTilTestscenarioDto(it) }

    @GetMapping("/{id}")
    @Operation(summary = "", description = "Returnerer testscenario som matcher id")
    fun hentScenario(@PathVariable("id") id: String) =
        testscenarioService.getTestscenario(id)
            ?.let { ok(konverterTilTestscenarioDto(it)) }
            ?: notFound().build()

    @PostMapping("/{key}")
    @Operation(
        description = "Initialiserer et test scenario basert på angitt template key i VTPs eksempel templates",
    )
    fun initialiserTestscenario(@PathVariable("key") templateKey: String) =
        templateRepository.finn(templateKey)
            ?.let {
                val testscenario = testscenarioService.opprettTestscenario(it)
                pensjonTestdataService.opprettData(testscenario)
                status(CREATED)
                    .body(konverterTilTestscenarioDto(testscenario))
            }
            ?: notFound().build()

    @PostMapping
    @Operation(
        description = "Initialiserer et testscenario basert på angitt json streng og returnerer det initialiserte objektet",
    )
    fun initialiserTestScenario(@RequestBody testscenarioJson: String) =
        status(CREATED)
            .body(konverterTilTestscenarioDto(testscenarioService.opprettTestscenarioFraJsonString(testscenarioJson)))

    @DeleteMapping("/{id}")
    @Operation(summary = "", description = "Sletter et initialisert testscenario som matcher id")
    fun slettScenario(@PathVariable("id") id: String): ResponseEntity<*> {
        testscenarioService.slettScenario(id)
        return noContent().build<Any>()
    }

    @GetMapping("/cases")
    @Operation(summary = "", description = "Henter alle scenarios i pensjon-testdata")
    fun hentPensjonTestdataTestScenarios(): ResponseEntity<List<PensjonTestdataScenario>> =
        ok(pensjonTestdataService.hentScenarios())

    @PostMapping("/cases")
    @Operation(summary = "", description = "Oppretter valgt scenario i pensjon-testdata for initialisert testscenario id")
    fun opprettPensjonTestdataTestScenario(@RequestBody dt: OpprettSakDto): ResponseEntity<String>? {
        return try {
            testscenarioService.getTestscenario(dt.testScenarioId)
                ?.let {
                    pensjonTestdataService.opprettData(it)
                    val opprettTestdataScenario = pensjonTestdataService.opprettTestdataScenario(it, dt.caseId)
                    status(CREATED).body(opprettTestdataScenario)
                }
        } catch (e: HttpClientErrorException) {
            status(BAD_REQUEST).body(e.message)
        }
    }

    private fun konverterTilTestscenarioDto(testscenario: Testscenario): TestscenarioDto {
        return konverterTilTestscenarioDto(
            testscenario,
            testscenario.templateNavn.replaceFirst("[-_].+$".toRegex(), ""),
            testscenario.templateNavn
        )
    }

    private fun konverterTilTestscenarioDto(testscenario: Testscenario, templateKey: String?, templateName: String) =
        TestscenarioDto(
            templateKey = templateKey,
            templateNavn = templateName,
            testscenarioId = testscenario.id,
            variabler = testscenario.vars,
            personopplysninger = TestscenarioPersonopplysningDto(
                soekerIdent = testscenario.personopplysninger.søker.ident,
                soekerStatsborgerskap = testscenario.personopplysninger.søker.statsborgerskap.orEmpty().joinToString { it.land.toString() },
                annenPartStatsborgerskap = testscenario.personopplysninger.annenPart?.statsborgerskap.orEmpty().joinToString { it.land.toString() },
                annenpartIdent = testscenario.personopplysninger.annenPart?.ident,
                soekerAktoerIdent = testscenario.personopplysninger.søker.aktørIdent,
                annenPartAktoerIdent = testscenario.personopplysninger.annenPart?.aktørIdent,
                fødselsdato = testscenario.personopplysninger.søker.fødselsdato
            ),
            scenariodataDto = TestscenariodataDto(
                testscenario.søkerInntektYtelse?.inntektskomponentModell,
                testscenario.søkerInntektYtelse?.arbeidsforholdModell
            ),
            scenariodataAnnenpartDto = testscenario.annenpartInntektYtelse?.let {
                TestscenariodataDto(
                    it.inntektskomponentModell,
                    it.arbeidsforholdModell
                )
            }
        )
}
