package no.nav.pensjon.vtp.testmodell.repo.impl

import com.fasterxml.jackson.core.type.TypeReference
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplate
import no.nav.pensjon.vtp.testmodell.util.JsonMapper
import no.nav.pensjon.vtp.testmodell.util.VariabelContainer
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

const val LOCATION_PATTERN = "classpath:/scenarios/**/vars.json"

class TestscenarioTemplateLoader {
    private val jsonMapper = JsonMapper()

    fun load(): Map<String, TestscenarioTemplate> {
        return locateVarsResources()
            .map { loadFileTestscenarioTemplate(it) }
            .associateBy({ it.templateKey }, { it })
    }

    private fun locateVarsResources(): List<Resource> {
        return PathMatchingResourcePatternResolver().getResources(LOCATION_PATTERN).toList()
    }

    private fun loadFileTestscenarioTemplate(resource: Resource): TestscenarioTemplate {
        val dir = resource.url.toString().substring(0, resource.url.toString().lastIndexOf("/"))
        val templateName = URLDecoder.decode(dir.substring(dir.lastIndexOf("/") + 1), StandardCharsets.UTF_8)
        val variables = loadTemplateVars(resource)
        return FileTestscenarioTemplate("$dir/", templateName, variables)
    }

    private fun loadTemplateVars(resource: Resource): VariabelContainer {
        resource.inputStream.use {
            return VariabelContainer(
                jsonMapper.lagObjectMapper()
                    .readValue(it, object : TypeReference<HashMap<String, String>>() {})
            )
        }
    }
}
