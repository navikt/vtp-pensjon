package no.nav.pensjon.vtp.mocks.inntektskomponenten
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

data class OpprettAbonnementResponse(val abonnementId: Int)

data class ForventetInntektBruker(
    val norskident: String?,
    val forventetInntektListe: List<ForventetInntekt> = emptyList(),
)
data class ForventetInntekt (
    val aar: String,
    val beloep: Int,
    val type: String,
    val hendelse: String? = null,
    val informasjonsopphav: String? = null,
    val endringstidspunkt: LocalDateTime? = null
)

data class HentInntekterRequest(
    val ainntektsfilter: String,
    val formaal: String,
    val ident: Aktoer,
    val maanedFom: YearMonth,
    val maanedTom: YearMonth,
)

data class HentInntekterBolkRequest(
    val ainntektsfilter: String,
    val formaal: String,
    val abonnerteInntekterIdentOgPeriodeListe: List<AbonnerteInntekterIdentOgPeriode>
)

data class AbonnerteInntekterIdentOgPeriode(
    val ident: Aktoer,
    val spoerringPeriodeFom: LocalDate,
    val spoerringPeriodeTom: LocalDate,
)

data class Aktoer(
    val identifikator: String,
    val aktoerType: AktoerType? = AktoerType.NATURLIG_IDENT
)

enum class AktoerType{
    AKTOER_ID, NATURLIG_IDENT, ORGANISASJON, UTENLANDSK_IDENT
}

data class HentInntekterResponse(
    val ident: Aktoer?,
    val arbeidsInntektMaaned: List<ArbeidsInntektMaaned> = emptyList()
)

data class ArbeidsInntektMaaned(
    val aarMaaned: YearMonth,
    val arbeidsInntektInformasjon: ArbeidsInntektInformasjon?
)

data class ArbeidsInntektInformasjon(
    val inntektListe: List<Inntekt> = emptyList()
)

data class Inntekt(
    val beloep: Double,
    val tilleggsinformasjon: Tilleggsinformasjon? = null,
    val inntektType: InntektType,
    val beskrivelse: String? = null,
)

class Tilleggsinformasjon(
    val kategori: String,
    val tilleggsinformasjonDetaljer: TilleggsinformasjonDetaljer?
)

abstract class TilleggsinformasjonDetaljer {
    abstract val detaljerType: String?
}

data class AldersUfoereEtterlatteAvtalefestetOgKrigspensjon(
    override val detaljerType: String?,
    val tidsromFom: LocalDate,
    val tidsromTom: LocalDate,
): TilleggsinformasjonDetaljer()

data class Etterbetalingsperiode(
    override val detaljerType: String?,
    val etterbetalingsperiodeFom: LocalDate,
    val etterbetalingsperiodeTom: LocalDate,
): TilleggsinformasjonDetaljer()

enum class InntektType {
    LOENNSINNTEKT, NAERINGSINNTEKT, PENSJON_ELLER_TRYGD, YTELSE_FRA_OFFENTLIGE
}

data class HentAbonnerteInntekterBolkResponse(
    val abonnerteInntekterPerIdentListe: List<AbonnerteInntekterPerIdent> = emptyList(),
    val unntakForIdentListe: List<UnntakForIdent> = emptyList()
)

data class AbonnerteInntekterPerIdent(
    val ident: Aktoer,
    val abonnerteInntekterMaanedListe: List<AbonnerteInntekterMaaned> = emptyList()
)

data class AbonnerteInntekterMaaned(
    val beloep: Double,
    val avviksbeskrivelse: String? = null,
    val maaned: YearMonth,
    val sumOpplysningspliktigListe: List<SumOpplysningspliktig> = emptyList(),
)

data class SumOpplysningspliktig(
    val beloep: BigDecimal,
    val avviksbeskrivelse: String? = null,
    val opplysningspliktig: Aktoer
)

data class UnntakForIdent(
    val ident: Aktoer,
    val unntaksmelding: String? = null
)
