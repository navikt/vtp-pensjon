package no.nav.pensjon.vtp.testmodell.medlemskap

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeName
import no.nav.pensjon.vtp.testmodell.personopplysning.Landkode
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicLong

/**
 * Medlemskapperiode med defaults, de kan overstyrs av json struktur hvis satt
 */
@JsonTypeName("medlemskapperiode")
data class MedlemskapperiodeModell(
    @JsonProperty("id")
    val id: Long = ID_GENERATOR.getAndIncrement(),

    @JsonProperty("fom")
    val fom: LocalDate? = LocalDate.now().minusYears(1),

    @JsonProperty("tom")
    val tom: LocalDate? = LocalDate.now().plusYears(3),

    @JsonProperty("besluttetDato")
    val besluttetDato: LocalDate? = LocalDate.now().minusYears(1),

    @JsonProperty("land")
    val landkode: Landkode = Landkode.DEU, // EØS land

    @JsonProperty("trygdedekning")
    val dekningType: DekningType? = DekningType.IHT_AVTALE, // setter til en uavklart kode default.

    @JsonProperty("kilde")
    val kilde: MedlemskapKildeType? = MedlemskapKildeType.TPS,

    @JsonProperty("lovvalgType")
    val lovvalgType: LovvalgType? = LovvalgType.ENDL,

    @JsonProperty("status")
    val status: PeriodeStatus = PeriodeStatus.UAVK
) {
    companion object {
        private val ID_GENERATOR = AtomicLong(100000000)
    }
}
