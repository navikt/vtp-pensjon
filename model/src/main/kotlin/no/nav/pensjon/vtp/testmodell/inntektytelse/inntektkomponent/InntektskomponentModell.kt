package no.nav.pensjon.vtp.testmodell.inntektytelse.inntektkomponent

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.util.ArrayList

class InntektskomponentModell {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("inntektsperioder")
    var inntektsperioder: MutableList<Inntektsperiode> = ArrayList()

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("frilansarbeidsforholdperioder")
    var frilansarbeidsforholdperioder: MutableList<FrilansArbeidsforholdsperiode> = ArrayList()

    val frilansarbeidsforholdperioderSplittMånedlig: List<FrilansArbeidsforholdsperiode>
        get() = frilansarbeidsforholdperioder
            .flatMap { splittFrilansArbeidsforholdTilMånedligeIntervall(it) }

    @JsonIgnore
    fun inntektsperioderSplittMånedlig(): List<Inntektsperiode> = inntektsperioder
        .flatMap { splittInntektsperioderTilMånedligeIntervall(it) }

    private fun splittFrilansArbeidsforholdTilMånedligeIntervall(fap: FrilansArbeidsforholdsperiode): List<FrilansArbeidsforholdsperiode> {
        val frilansArbeidsforholdsperioderPerMåned: MutableList<FrilansArbeidsforholdsperiode> = ArrayList()

        val tomDato = fap.frilansTom ?: LocalDate.now()
        var dateCounter = fap.frilansFom.withDayOfMonth(1)

        while (!dateCounter.isEqual(tomDato.withDayOfMonth(1))) {
            frilansArbeidsforholdsperioderPerMåned.add(
                FrilansArbeidsforholdsperiode(
                    frilansFom = dateCounter.withDayOfMonth(1),
                    frilansTom = dateCounter.withDayOfMonth(dateCounter.lengthOfMonth()),
                    orgnr = fap.orgnr,
                    stillingsprosent = fap.stillingsprosent,
                    aktorId = fap.aktorId,
                    personligArbeidsgiver = fap.personligArbeidsgiver
                )
            )
            dateCounter = dateCounter.plusMonths(1)
        }
        return frilansArbeidsforholdsperioderPerMåned
    }

    private fun splittInntektsperioderTilMånedligeIntervall(ip: Inntektsperiode): List<Inntektsperiode> {
        val inntektsperioderPaaMaaned: MutableList<Inntektsperiode> = ArrayList()
        val tomDato = if (ip.tom != null) ip.tom else LocalDate.now()
        var dateCounter = ip.fom.withDayOfMonth(1)

        while (!dateCounter.isEqual(tomDato.withDayOfMonth(1))) {
            inntektsperioderPaaMaaned.add(
                Inntektsperiode(
                    fom = dateCounter.withDayOfMonth(1),
                    tom = dateCounter.withDayOfMonth(dateCounter.lengthOfMonth()),
                    beløp = ip.beløp,
                    orgnr = ip.orgnr,
                    aktorId = null,
                    type = ip.type,
                    fordel = ip.fordel,
                    beskrivelse = ip.beskrivelse,
                    skatteOgAvgiftsregel = ip.skatteOgAvgiftsregel,
                    inngaarIGrunnlagForTrekk = ip.inngaarIGrunnlagForTrekk,
                    utloeserArbeidsgiveravgift = ip.utloeserArbeidsgiveravgift,
                    personligArbeidsgiver = ip.personligArbeidsgiver
                )
            )
            dateCounter = dateCounter.plusMonths(1)
        }

        return inntektsperioderPaaMaaned
    }
}
