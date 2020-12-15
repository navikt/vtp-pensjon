package no.nav.pensjon.vtp.testmodell.inntektytelse.trex

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty

data class Arbeidsforhold(
    @JsonProperty("orgnr") @JsonAlias("arbeidsgiverOrgnr") val orgnr: Orgnummer,
    @JsonProperty("inntekt") @JsonAlias("inntektForPerioden") val inntekt: Int,
    @JsonProperty("inntektsperiode") val inntektperiode: Inntektsperiode,
    @JsonProperty("refusjon") val refusjon: Boolean
)
