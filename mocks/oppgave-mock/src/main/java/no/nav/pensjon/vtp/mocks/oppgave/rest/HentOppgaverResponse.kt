package no.nav.pensjon.vtp.mocks.oppgave.rest

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.node.ObjectNode
import io.swagger.annotations.ApiModelProperty

data class HentOppgaverResponse(
    @JsonProperty("antallTreffTotalt")
    @ApiModelProperty(value = "Totalt antall oppgaver funnet med dette søket")
    val antallTreffTotalt: Int,

    @JsonProperty("oppgaver")
    @ApiModelProperty(value = "Liste over oppgaver")
    val oppgaver: List<ObjectNode>
)
