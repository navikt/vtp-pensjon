package no.nav.pensjon.vtp.testmodell.inntektytelse.trex

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.ArrayList

data class TRexModell(
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("foreldrepenger")
    val foreldrepenger: List<Grunnlag> = ArrayList(),

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("svangerskapspenger")
    val svangerskapspenger: List<Grunnlag> = ArrayList(),

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("sykepenger")
    val sykepenger: List<Grunnlag> = ArrayList(),

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("barnsykdom")
    val barnsykdom: List<Grunnlag> = ArrayList()
)
