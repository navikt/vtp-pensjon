package no.nav.pensjon.vtp.testmodell.scenario.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class TestscenarioPersonopplysningDto(
    @JsonProperty("søkerIdent")
    val søkerIdent: String? = null,

    @JsonProperty("annenpartIdent")
    val annenpartIdent: String? = null,

    @JsonProperty("søkerAktørIdent")
    val søkerAktørIdent: String? = null,

    @JsonProperty("annenpartAktørIdent")
    val annenPartAktørIdent: String? = null,

    @JsonProperty("fødselsdato")
    val fødselsdato: LocalDate? = null
)
