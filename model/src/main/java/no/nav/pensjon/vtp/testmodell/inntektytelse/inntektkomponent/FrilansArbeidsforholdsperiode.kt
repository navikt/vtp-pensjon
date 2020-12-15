package no.nav.pensjon.vtp.testmodell.inntektytelse.inntektkomponent

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

@JsonIgnoreProperties(ignoreUnknown = true)
data class FrilansArbeidsforholdsperiode(
    @JsonProperty("frilansFom")
    val frilansFom: LocalDate,

    @JsonProperty("frilansTom")
    val frilansTom: LocalDate? = null,

    @JsonProperty("orgnr")
    val orgnr: String? = null,

    @JsonProperty("aktorId")
    val aktorId: String? = null,

    @JsonProperty("stillingsprosent")
    val stillingsprosent: Int? = null,

    @JsonProperty("arbeidsgiver")
    val personligArbeidsgiver: String? = null,
) {
    val arbeidsforholdstype: String
        get() = "frilanserOppdragstakerHonorarPersonerMm"
}
