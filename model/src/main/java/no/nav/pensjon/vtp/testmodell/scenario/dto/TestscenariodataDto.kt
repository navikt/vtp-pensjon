package no.nav.pensjon.vtp.testmodell.scenario.dto

import com.fasterxml.jackson.annotation.JsonProperty
import no.nav.pensjon.vtp.testmodell.inntektytelse.arbeidsforhold.ArbeidsforholdModell
import no.nav.pensjon.vtp.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell

data class TestscenariodataDto(
    @JsonProperty("inntektskomponent")
    val inntektskomponentModell: InntektskomponentModell?,
    @JsonProperty("aareg")
    val arbeidsforholdModell: ArbeidsforholdModell?
)
