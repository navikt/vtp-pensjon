package no.nav.pensjon.vtp.mocks.virksomhet.inntekt.v3.modell

import no.nav.pensjon.vtp.core.util.asLocalDate
import no.nav.pensjon.vtp.core.util.asXMLGregorianCalendar
import no.nav.pensjon.vtp.testmodell.exceptions.NotImplementedException
import no.nav.pensjon.vtp.testmodell.inntektytelse.inntektkomponent.InntektType
import no.nav.pensjon.vtp.testmodell.inntektytelse.inntektkomponent.InntektskomponentModell
import no.nav.pensjon.vtp.testmodell.inntektytelse.inntektkomponent.Inntektsperiode
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.*
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

object HentInntektlistBolkMapper {
    fun makeArbeidsInntektIdent(modell: InntektskomponentModell, aktoer: Aktoer): ArbeidsInntektIdent {
        val arbeidsInntektIdent = ArbeidsInntektIdent().apply {
            ident = aktoer
        }

        val inntektsliste = mapInntektFraModell(modell.inntektsperioderSplittMånedlig(), aktoer)

        val inntektsMåneder: MutableMap<String, MutableList<Inntekt>> = HashMap()
        for (inntekt in inntektsliste) {
            val localDate = inntekt.opptjeningsperiode.startDato.asLocalDate()
            val key = "${localDate.year}-${localDate.month}"

            inntektsMåneder.computeIfAbsent(key) { ArrayList() }.add(inntekt)
        }

        for (monthYearDate in getMonthYearsWithData(modell)) {
            val arbeidsInntektMaaned = ArbeidsInntektMaaned().apply {
                arbeidsInntektInformasjon = ArbeidsInntektInformasjon()
                aarMaaned = monthYearDate.asXMLGregorianCalendar()
            }

            val key = "${monthYearDate.year}-${monthYearDate.month}"
            inntektsMåneder[key]
                ?.let {
                    arbeidsInntektMaaned.arbeidsInntektInformasjon.inntektListe.addAll(it)
                    arbeidsInntektIdent.arbeidsInntektMaaned.add(arbeidsInntektMaaned)
                }
        }

        return arbeidsInntektIdent
    }

    private fun mapInntektFraModell(modellList: List<Inntektsperiode>, aktoer: Aktoer) = modellList
        .mapNotNull { modellPeriode ->
            when (modellPeriode.type) {
                InntektType.Lønnsinntekt -> {
                    lagLoennsinntekt(modellPeriode, aktoer)
                }
                InntektType.Næringsinntekt -> {
                    throw NotImplementedException()
                }
                InntektType.PensjonEllerTrygd -> {
                    throw NotImplementedException()
                }
                InntektType.YtelseFraOffentlige -> {
                    lagYtelseFraOffentlige(modellPeriode, aktoer)
                }
                else -> null
            }
        }

    private fun lagYtelseFraOffentlige(ip: Inntektsperiode, aktoer: Aktoer) =
        YtelseFraOffentlige().apply {
            beloep = ip.beløp.toBigDecimal()
            beskrivelse = YtelseFraOffentligeBeskrivelse().apply {
                value = ip.beskrivelse
            }
            inntektsmottaker = aktoer
            utbetaltIPeriode = ip.fom.asXMLGregorianCalendar()
            opptjeningsperiode = Periode().apply {
                startDato = ip.fom.asXMLGregorianCalendar()
                sluttDato = ip.tom?.asXMLGregorianCalendar()
            }
        }

    private fun lagLoennsinntekt(ip: Inntektsperiode, aktoer: Aktoer) = Loennsinntekt().apply {
        val arbeidsgiver =
            if (ip.orgnr != null && ip.orgnr != "") {
                lagOrganisation(ip.orgnr)
            } else {
                ip.personligArbeidsgiver
                    ?.let {
                        lagPersonIdent(it)
                    }
            }

        beloep = BigDecimal(ip.beløp)
        beskrivelse = Loennsbeskrivelse().apply {
            value = ip.beskrivelse
        }
        isUtloeserArbeidsgiveravgift = true
        isInngaarIGrunnlagForTrekk = true
        inntektsmottaker = aktoer

        virksomhet = arbeidsgiver
        opplysningspliktig = arbeidsgiver
        utbetaltIPeriode = ip.fom.asXMLGregorianCalendar()

        opptjeningsperiode = Periode().apply {
            startDato = ip.fom.asXMLGregorianCalendar()
            sluttDato = ip.tom?.asXMLGregorianCalendar()
        }

        // levereringstidspunkt
        // inntektsstatus
        // inntektsperiodetype
        // inntektskilde
        // fordel
    }

    private fun lagOrganisation(orgNummer: String?) = Organisasjon().apply {
        orgnummer = orgNummer
    }

    private fun lagPersonIdent(fnr: String) = PersonIdent().apply {
        this.personIdent = fnr
    }

    // TODO: Skriv om til å både inntekt og arbeidsforhold
    private fun getMonthYearsWithData(modell: InntektskomponentModell): List<LocalDate> {
        val inntektsperioderByMonth = modell.inntektsperioderSplittMånedlig()

        if (inntektsperioderByMonth.isEmpty()) {
            return ArrayList()
        }
        val minDate = inntektsperioderByMonth.minOf { it.fom }
        val maxDate: LocalDate? = inntektsperioderByMonth.mapNotNull { it.tom }.maxOfOrNull { it }

        val monthList: MutableList<LocalDate> = ArrayList()
        var date = minDate

        while (date.isBefore(maxDate)) {
            monthList.add(date)
            date = date.plusMonths(1)
        }

        return monthList
    }
}
