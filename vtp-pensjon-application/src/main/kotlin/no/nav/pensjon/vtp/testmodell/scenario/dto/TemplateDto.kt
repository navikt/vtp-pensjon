package no.nav.pensjon.vtp.testmodell.scenario.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

/** Beskriver en template,inklusiv liste av variable og deres verdier.  */
data class TemplateDto(
    @JsonProperty("key") val key: String,
    @JsonProperty("navn") val navn: String,
    @JsonInclude(content = JsonInclude.Include.NON_EMPTY)
    @JsonProperty("variabler") val variabler: Map<String, String?> = HashMap()
)
