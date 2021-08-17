package no.nav.pensjon.vtp.testmodell.inntektytelse.sigrun

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class SigrunModell(
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("inntektsår")
    val inntektsår: List<Inntektsår> = ArrayList()
)
