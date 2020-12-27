package no.nav.pensjon.vtp.mocks.virksomhet.inntekt.hentinntektlistebolk.modell

import no.nav.pensjon.vtp.testmodell.inntektytelse.inntektkomponent.FrilansArbeidsforholdsperiode
import no.nav.pensjon.vtp.testmodell.inntektytelse.inntektkomponent.InntektType
import no.nav.pensjon.vtp.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell
import no.nav.pensjon.vtp.testmodell.inntektytelse.inntektkomponent.Inntektsperiode
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonModellRepository
import no.nav.tjenester.aordningen.inntektsinformasjon.*
import no.nav.tjenester.aordningen.inntektsinformasjon.inntekt.Inntekt
import org.springframework.stereotype.Component
import java.lang.IllegalStateException
import java.lang.RuntimeException
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth
import java.util.*

@Component
class HentInntektlisteBolkMapperRest(private val personModellRepository: PersonModellRepository) {
    fun makeArbeidsInntektIdent(
        modell: InntektskomponentModell,
        aktoer: Aktoer?,
        fom: YearMonth,
        tom: YearMonth?
    ): ArbeidsInntektIdent {
        val arbeidsInntektIdent = ArbeidsInntektIdent()
        arbeidsInntektIdent.ident = aktoer
        arbeidsInntektIdent.arbeidsInntektMaaned =
            ArrayList()
        var runningMonth = fom
        while (runningMonth.isBefore(tom)) {
            arbeidsInntektIdent.arbeidsInntektMaaned.add(
                ArbeidsInntektMaaned().apply {
                    arbeidsInntektInformasjon = makeArbeidsInntektInformasjonForMaaned(modell, runningMonth)
                    aarMaaned = runningMonth
                }
            )
            runningMonth = runningMonth.plusMonths(1)
        }
        return arbeidsInntektIdent
    }

    private fun makeArbeidsInntektInformasjonForMaaned(
        modell: InntektskomponentModell,
        måned: YearMonth
    ): ArbeidsInntektInformasjon {
        return ArbeidsInntektInformasjon().apply {
            arbeidsforholdListe =
                arbeidsforholdFrilanserListeFraModellListeForMaaned(
                    modell.frilansarbeidsforholdperioderSplittMånedlig,
                    måned
                )
            inntektListe = inntektListeFraModell(modell.inntektsperioderSplittMånedlig(), måned)
        }
    }

    private fun arbeidsforholdFrilanserListeFraModellListeForMaaned(
        modellPeriode: List<FrilansArbeidsforholdsperiode>,
        måned: YearMonth
    ): List<ArbeidsforholdFrilanser> {
        val frilansArbeidsforholdsperiodeList = modellPeriode
            .filter { localDateTimeInYearMonth(it.frilansFom, måned) }

        return frilansArbeidsforholdsperiodeList
            .map {
                ArbeidsforholdFrilanser().apply {
                    frilansPeriodeFom = it.frilansFom
                    frilansPeriodeTom = it.frilansTom
                    arbeidsforholdstype = it.arbeidsforholdstype
                    stillingsprosent = it.stillingsprosent?.toDouble()
                    arbeidsgiver =
                        if (it.orgnr != null && it.orgnr != "") Aktoer.newOrganisasjon(it.orgnr) else Aktoer.newAktoerId(
                            personModellRepository.findById(it.personligArbeidsgiver!!)
                                ?.aktørIdent
                                ?: throw RuntimeException("Unknown personlig arbeidsgiver")
                        )
                }
            }
    }

    private fun inntektListeFraModell(modellPeriode: List<Inntektsperiode>, måned: YearMonth): List<Inntekt> {
        return modellPeriode
            .filter { localDateTimeInYearMonth(ldt = it.tom, yearMonth = måned) }
            .map {
                Inntekt().apply {
                    inntektType = fraModellInntektstype(it.type)
                    beloep = BigDecimal(it.beløp)
                    beskrivelse = it.beskrivelse
                    fordel = it.fordel?.name
                    virksomhet = if (it.orgnr != null && it.orgnr != "") {
                        Aktoer.newOrganisasjon(
                            it.orgnr
                        )
                    } else {
                        it.personligArbeidsgiver
                            ?.let {
                                personModellRepository.findById(it)
                                    ?.let {
                                        Aktoer.newAktoerId(it.aktørIdent)
                                    }
                            }
                            ?: throw RuntimeException("Unknown personlig arbeidsgiver")
                    }
                    opptjeningsperiodeFom = it.fom
                    opptjeningsperiodeTom = it.tom
                    utbetaltIMaaned = måned
                    skatteOgAvgiftsregel = it.skatteOgAvgiftsregel
                    inngaarIGrunnlagForTrekk = it.inngaarIGrunnlagForTrekk
                    utloeserArbeidsgiveravgift = it.utloeserArbeidsgiveravgift
                }
            }
    }

    companion object {
        private fun localDateTimeInYearMonth(ldt: LocalDate?, yearMonth: YearMonth): Boolean {
            return YearMonth.of(ldt!!.year, ldt.month).compareTo(yearMonth) == 0
        }

        private fun fraModellInntektstype(modellType: InntektType?): no.nav.tjenester.aordningen.inntektsinformasjon.inntekt.InntektType {
            if (modellType == InntektType.Lønnsinntekt) {
                return no.nav.tjenester.aordningen.inntektsinformasjon.inntekt.InntektType.LOENNSINNTEKT
            }
            if (modellType == InntektType.Næringsinntekt) {
                return no.nav.tjenester.aordningen.inntektsinformasjon.inntekt.InntektType.NAERINGSINNTEKT
            }
            if (modellType == InntektType.PensjonEllerTrygd) {
                return no.nav.tjenester.aordningen.inntektsinformasjon.inntekt.InntektType.PENSJON_ELLER_TRYGD
            }
            if (modellType == InntektType.YtelseFraOffentlige) {
                return no.nav.tjenester.aordningen.inntektsinformasjon.inntekt.InntektType.YTELSE_FRA_OFFENTLIGE
            }
            throw IllegalStateException("Inntektstype kunne ikke konverteres: $modellType")
        }
    }
}
