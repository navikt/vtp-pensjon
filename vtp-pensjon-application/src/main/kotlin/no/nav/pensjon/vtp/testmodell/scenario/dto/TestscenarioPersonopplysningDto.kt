package no.nav.pensjon.vtp.testmodell.scenario.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class TestscenarioPersonopplysningDto(
    @JsonProperty("soekerIdent")
    val soekerIdent: String? = null,

    @JsonProperty("annenpartIdent")
    val annenpartIdent: String? = null,

    @JsonProperty("soekerAktoerIdent")
    val soekerAktoerIdent: String? = null,

    @JsonProperty("annenPartAktoerIdent")
    val annenPartAktoerIdent: String? = null,

    @JsonProperty("fødselsdato")
    val fødselsdato: LocalDate? = null
)
