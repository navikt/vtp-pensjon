package no.nav.pensjon.vtp.testmodell.inntektytelse.arbeidsforhold

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class ArbeidsforholdModell(
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("arbeidsforhold")
    val arbeidsforhold: List<Arbeidsforhold> = ArrayList()
)
