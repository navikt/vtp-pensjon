package no.nav.pensjon.vtp.testmodell.inntektytelse.inntektkomponent

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Inntektsperiode(
    @JsonProperty("fom")
    val fom: LocalDate,

    @JsonProperty("tom")
    val tom: LocalDate? = null,

    @JsonProperty("beløp")
    val beløp: Int,

    @JsonProperty("orgnr")
    val orgnr: String? = null,

    @JsonProperty("aktorId")
    private val aktorId: String? = null,

    @JsonProperty("type")
    val type: InntektType? = null,

    @JsonProperty("fordel")
    val fordel: InntektFordel? = null,

    @JsonProperty("beskrivelse")
    val beskrivelse: String? = null,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("skatteOgAvgiftsregel")
    val skatteOgAvgiftsregel: String? = null,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("inngaarIGrunnlagForTrekk")
    val inngaarIGrunnlagForTrekk: Boolean? = null,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("utloeserArbeidsgiveravgift")
    val utloeserArbeidsgiveravgift: Boolean? = null,

    @JsonProperty("arbeidsgiver")
    val personligArbeidsgiver: String? = null
)
