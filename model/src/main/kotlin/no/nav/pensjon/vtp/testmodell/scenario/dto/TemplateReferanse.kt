package no.nav.pensjon.vtp.testmodell.scenario.dto

import com.fasterxml.jackson.annotation.JsonProperty

/** Beskriver en template,inklusiv liste av variable og deres verdier.  */
data class TemplateReferanse(
    @JsonProperty("key") val key: String,
    @JsonProperty("navn") val navn: String
)
