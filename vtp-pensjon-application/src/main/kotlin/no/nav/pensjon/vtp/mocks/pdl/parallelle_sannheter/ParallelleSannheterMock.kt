package no.nav.pensjon.vtp.mocks.pdl.parallelle_sannheter

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Pensjon Parallelle-Sannheter")
@RequestMapping("/api")
class ParallelleSannheterMock {

    private val emptyAvklaring = ObjectMapper().readTree("")

    @PostMapping("/*")
    fun avklar(@RequestBody itemList: JsonNode) = responseTemplate(
        type = itemList.fieldNames().next().toString(), avklaring = itemList.firstOrNull()?.firstOrNull()
    )

    private fun responseTemplate(type: String, avklaring: JsonNode?) = "{ \"$type\": [${avklaring ?: emptyAvklaring}]"
}
