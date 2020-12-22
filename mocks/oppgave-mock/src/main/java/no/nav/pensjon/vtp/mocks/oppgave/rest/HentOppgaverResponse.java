package no.nav.pensjon.vtp.mocks.oppgave.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class HentOppgaverResponse {
    private final int antallTreffTotalt;
    private final List<ObjectNode> oppgaver;

    HentOppgaverResponse(List<ObjectNode> oppgaver) {
        this.antallTreffTotalt = oppgaver.size();
        this.oppgaver = oppgaver;
    }

    @JsonProperty("oppgaver")
    @ApiModelProperty(value = "Liste over oppgaver")
    public List<ObjectNode> getOppgaver() {
        return oppgaver;
    }

    @JsonProperty("antallTreffTotalt")
    @ApiModelProperty(value = "Totalt antall oppgaver funnet med dette søket")
    public long getAntallTreffTotalt() {
        return antallTreffTotalt;
    }
}
