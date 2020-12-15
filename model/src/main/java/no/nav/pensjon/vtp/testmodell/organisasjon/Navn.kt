package no.nav.pensjon.vtp.testmodell.organisasjon

import com.fasterxml.jackson.annotation.JsonProperty

data class Navn(
    @JsonProperty("navnelinje")
    val navnelinje: List<String>
)
