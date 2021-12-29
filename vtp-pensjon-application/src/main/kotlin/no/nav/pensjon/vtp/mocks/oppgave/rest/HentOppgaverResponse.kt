package no.nav.pensjon.vtp.mocks.oppgave.rest

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class HentOppgaverResponse(
    @JsonProperty("antallTreffTotalt")
    @Schema(description = "Totalt antall oppgaver funnet med dette søket")
    val antallTreffTotalt: Int,

    @JsonProperty("oppgaver")
    @Schema(description = "Liste over oppgaver")
    val oppgaver: List<Oppgave>
)
