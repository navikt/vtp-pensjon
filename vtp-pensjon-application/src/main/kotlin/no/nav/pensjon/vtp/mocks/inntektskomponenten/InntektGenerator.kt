package no.nav.pensjon.vtp.mocks.inntektskomponenten

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.temporal.TemporalAdjusters.lastDayOfMonth

internal fun opprettForventetInntektBruker(
    fnr: String,
    aarList: List<String>
) = ForventetInntektBruker(
    norskident = fnr,
    forventetInntektListe = aarList.map { it.createForventeInntekt() }
)

private fun String.createForventeInntekt() =
    ForventetInntekt(
        aar = this,
        beloep =  1000,
        type= "UFR_forv_arbeidsinnt",
        hendelse =  "Registrert",
        informasjonsopphav = "Arbeidsgiver",
        endringstidspunkt =  LocalDateTime.now()
    )

internal fun hentInntekterResponseApi(fnr: String, fom: YearMonth, tom: YearMonth) =
    HentInntekterResponse(
        ident = Aktoer(fnr),
        arbeidsInntektMaaned = listOf(
            arbeidsInntektMaanedApi(fom),
            arbeidsInntektMaanedApi(tom),
        )
    )

private fun arbeidsInntektMaanedApi(mnd: YearMonth) =
    ArbeidsInntektMaaned(
        aarMaaned = mnd,
        arbeidsInntektInformasjon = abeidsInntektInformasjonApi(LocalDate.of(mnd.year, mnd.monthValue, 1).minusMonths(1))
    )

private fun abeidsInntektInformasjonApi(dato: LocalDate) =
    ArbeidsInntektInformasjon(
        listOf(
            inntektApi(tilleggsinformasjonAldersUforeEtterlatteAFPKrigApi(dato)),
            inntektApi(tilleggsinformasjonEtterbetalingApi(dato))
        )
    )

private fun inntektApi(tilleggsinformasjon: Tilleggsinformasjon? = null) =
    Inntekt(
        beloep = 100.0,
        inntektType = InntektType.PENSJON_ELLER_TRYGD,
        tilleggsinformasjon = tilleggsinformasjon
    )

private fun tilleggsinformasjonAldersUforeEtterlatteAFPKrigApi(dato: LocalDate) =
    Tilleggsinformasjon(
        kategori = "Periode",
        tilleggsinformasjonDetaljer = AldersUfoereEtterlatteAvtalefestetOgKrigspensjon(
            detaljerType = "ALDERSUFOEREETTERLATTEAVTALEFESTETOGKRIGSPENSJON",
            tidsromFom = dato,
            tidsromTom = dato.with(lastDayOfMonth())
        )
    )

private fun tilleggsinformasjonEtterbetalingApi(dato: LocalDate) =
    Tilleggsinformasjon(
        kategori = "Periode",
        tilleggsinformasjonDetaljer = Etterbetalingsperiode(
            detaljerType = "ETTERBETALINGSPERIODE",
            etterbetalingsperiodeFom = dato,
            etterbetalingsperiodeTom = dato.with(lastDayOfMonth())
        )
    )

internal fun hentDetaljerteInntekterBolkResponseApi(abonnerteInntekterIdentOgPeriodeListe: List<AbonnerteInntekterIdentOgPeriode>) =
    HentAbonnerteInntekterBolkResponse(
        abonnerteInntekterPerIdentListe = abonnerteInntekterIdentOgPeriodeListe.map { abonnerteInntekterPerIdentApi(it.ident, it.spoerringPeriodeFom, it.spoerringPeriodeTom) },
        unntakForIdentListe = emptyList()
    )


private fun abonnerteInntekterPerIdentApi(aktoer: Aktoer, spoerringPeriodeFom: LocalDate, spoerringPeriodeTom: LocalDate) =
    AbonnerteInntekterPerIdent(
        ident = aktoer,
        abonnerteInntekterMaanedListe = listOf(
            abonnerteInntekterMaanedApi(spoerringPeriodeFom),
            abonnerteInntekterMaanedApi(spoerringPeriodeTom)
        )
    )

private fun abonnerteInntekterMaanedApi(dato: LocalDate) =
    AbonnerteInntekterMaaned(
        beloep = 10000.0,
        maaned = YearMonth.of(dato.year, dato.monthValue),
        sumOpplysningspliktigListe = listOf(
            sumOpplysningspliktigApi()
        )
    )

private fun sumOpplysningspliktigApi() =
    SumOpplysningspliktig(
        beloep = BigDecimal.valueOf(500),
        opplysningspliktig = Aktoer(
            identifikator = "99999",
            aktoerType = AktoerType.ORGANISASJON
        )
    )

