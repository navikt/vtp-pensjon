package no.nav.pensjon.vtp.testmodell.organisasjon

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class OrganisasjonDetaljerModell(
    @JsonProperty("registreringsDato")
    val registreringsDato: LocalDate? = null,

    @JsonProperty("datoSistEndret")
    val datoSistEndret: LocalDate? = null
)
