package no.nav.pensjon.vtp.testmodell.inntektytelse.sigrun

import com.fasterxml.jackson.annotation.JsonProperty

data class Inntektsår(
    @JsonProperty("år")
    val år: String,

    @JsonProperty("oppføring")
    val oppføring: List<Oppføring>
)
