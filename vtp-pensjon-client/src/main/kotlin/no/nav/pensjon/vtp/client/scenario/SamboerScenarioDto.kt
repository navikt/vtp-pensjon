package no.nav.pensjon.vtp.client.scenario

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class SamboerScenarioDto(
    @JsonProperty("personopplysninger")
    val personopplysningerDto: PersonopplysningerDto
)

data class PersonopplysningerDto(
    @JsonProperty("soekerIdent")
    val soekerIdent: String,
    @JsonProperty("soekerStatsborgerskap")
    val soekerStatsborgerskap: String,
    @JsonProperty("annenpartIdent")
    val annenpartIdent: String,
    @JsonProperty("annenPartStatsborgerskap")
    val annenPartStatsborgerskap: String,
    @JsonProperty("soekerAktoerIdent")
    val soekerAktoerIdent: String,
    @JsonProperty("annenPartAktoerIdent")
    val annenPartAktoerIdent: String,
    @JsonProperty("fødselsdato")
    val fødselsdato: String?,
)
