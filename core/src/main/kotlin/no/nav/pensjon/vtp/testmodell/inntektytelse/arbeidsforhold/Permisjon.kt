package no.nav.pensjon.vtp.testmodell.inntektytelse.arbeidsforhold

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class Permisjon(
    @JsonProperty("stillingsprosent")
    val stillingsprosent: Int? = null,

    @JsonProperty("fomGyldighetsperiode")
    val fomGyldighetsperiode: LocalDate? = null,

    @JsonProperty("tomGyldighetsperiode")
    val tomGyldighetsperiode: LocalDate? = null,

    @JsonProperty("permisjonType")
    val permisjonstype: Permisjonstype? = null
)
