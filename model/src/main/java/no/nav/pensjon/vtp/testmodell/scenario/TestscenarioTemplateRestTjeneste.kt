package no.nav.pensjon.vtp.testmodell.scenario

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import no.nav.pensjon.vtp.testmodell.scenario.dto.TemplateDto
import no.nav.pensjon.vtp.testmodell.scenario.dto.TemplateReferanse
import no.nav.pensjon.vtp.testmodell.repo.TemplateVariable
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplateRepository
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.function.Consumer

@RestController
@Api(tags = ["Testscenario/templates"])
@RequestMapping("/api/testscenario/templates")
class TestscenarioTemplateRestTjeneste(private val templateRepository: TestscenarioTemplateRepository) {
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiOperation(value = "templates", notes = "Liste av tilgjengelig Testscenario Templates", response = TemplateReferanse::class, responseContainer = "List")
    fun listTestscenarioTemplates() =
            templateRepository.templates()
                    .map { TemplateReferanse(it.templateKey, it.templateNavn) }
                    .sortedWith( compareBy { it.navn } )

    @GetMapping(value = ["/{key}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiOperation(value = "templates", notes = "Beskrivelse av template, inklusiv påkrevde variable (og evt. default verdier", response = TemplateDto::class)
    fun beskrivTestscenarioTemplate(@PathVariable("key") key: String) =
            templateRepository.finn(key)
                    ?.let {
                        val map = TreeMap<String, String?>()
                        it.defaultVars.forEach { key: String, value: String? -> map.putIfAbsent(key, value) }
                        it.expectedVars.forEach(Consumer { v: TemplateVariable -> map.putIfAbsent(v.name, null) })
                        ok(TemplateDto(key, it.templateNavn, map))
                    }
                    ?: notFound().build<TemplateDto>()
}
