package no.nav.pensjon.vtp.testmodell.inntektytelse.arbeidsforhold

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class Arbeidsavtale(
    @JsonProperty("yrke")
    val yrke: Yrke? = null,

    @JsonProperty("avlønningstype")
    val avlønningstype: Avlønningstype? = null,

    @JsonProperty("avtaltArbeidstimerPerUke")
    val avtaltArbeidstimerPerUke: Int? = null,

    @JsonProperty("stillingsprosent")
    val stillingsprosent: Int? = null,

    @JsonProperty("beregnetAntallTimerPerUke")
    val beregnetAntallTimerPerUke: Int? = null,

    @JsonProperty("sisteLønnsendringsdato")
    val sisteLønnnsendringsdato: LocalDate? = null,

    @JsonProperty("fomGyldighetsperiode")
    val fomGyldighetsperiode: LocalDate? = null,

    @JsonProperty("tomGyldighetsperiode")
    val tomGyldighetsperiode: LocalDate? = null
)
