package no.nav.pensjon.vtp.testmodell.inntektytelse.arena

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.LocalDate

data class ArenaMeldekort(
    @JsonProperty("fom")
    val fom: LocalDate? = null,

    @JsonProperty("tom")
    val tom: LocalDate? = null,

    @JsonProperty("dagsats")
    val dagsats: BigDecimal,

    @JsonProperty("beløp")
    val beløp: BigDecimal,

    @JsonProperty("utbetalingsgrad")
    val utbetalingsgrad: BigDecimal
)
