package no.nav.pensjon.vtp.testmodell.scenario

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.pensjon.vtp.testmodell.repo.TemplateVariable
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplateRepository
import no.nav.pensjon.vtp.testmodell.scenario.dto.TemplateDto
import no.nav.pensjon.vtp.testmodell.scenario.dto.TemplateReferanse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@Tag(name = "Testscenario/templates")
@RequestMapping("/api/testscenario/templates")
class TestscenarioTemplateRestTjeneste(private val templateRepository: TestscenarioTemplateRepository) {
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        summary = "templates",
        description = "Liste av tilgjengelig Testscenario Templates",
    )
    fun listTestscenarioTemplates() =
        templateRepository.templates()
            .map { TemplateReferanse(it.templateKey, it.templateName) }
            .sortedWith(compareBy { it.navn })

    @GetMapping(value = ["/{key}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        summary = "templates",
        description = "Beskrivelse av template, inklusiv påkrevde variable (og evt. default verdier",
    )
    fun beskrivTestscenarioTemplate(@PathVariable("key") key: String) =
        templateRepository.finn(key)
            ?.let {
                val map = TreeMap<String, String?>()
                it.vars.forEach { key: String, value: String? -> map.putIfAbsent(key, value) }
                it.getExpectedVars().forEach { v: TemplateVariable -> map.putIfAbsent(v.name, null) }
                ok(TemplateDto(key, it.templateName, map))
            }
            ?: notFound().build()
}
