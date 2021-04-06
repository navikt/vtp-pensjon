package no.nav.pensjon.vtp.testmodell.organisasjon

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class OrganisasjonModeller(
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("modeller")
    val modeller: List<OrganisasjonModell> = ArrayList()
)
