package no.nav.pensjon.vtp.testmodell.inntektytelse.arbeidsforhold

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.util.*

data class Arbeidsforhold(
    @JsonProperty("arbeidsavtaler")
    val arbeidsavtaler: List<Arbeidsavtale>,

    @JsonProperty("permisjoner")
    val permisjoner: List<Permisjon>? = ArrayList(),

    @JsonProperty("arbeidsforholdId")
    var arbeidsforholdId: String,

    @JsonProperty("arbeidsforholdIdNav")
    val arbeidsforholdIdnav: Long = ArbeidsforholdIdNav.next(),

    @JsonProperty("ansettelsesperiodeTom")
    val ansettelsesperiodeTom: LocalDate?,

    @JsonProperty("ansettelsesperiodeFom")
    val ansettelsesperiodeFom: LocalDate,

    @JsonProperty("arbeidsforholdstype")
    val arbeidsforholdstype: Arbeidsforholdstype,

    @JsonProperty("timeposteringer")
    val timeposteringer: List<AntallTimerIPerioden>?,

    @JsonProperty("arbeidsgiverOrgnr")
    val arbeidsgiverOrgnr: String?,

    @JsonProperty("opplyserOrgnr")
    val opplyserOrgnr: String?,

    @JsonProperty("arbeidsgiverAktorId")
    val arbeidsgiverAktorId: String?
)
