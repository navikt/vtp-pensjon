package no.nav.pensjon.vtp.testmodell.inntektytelse.arena

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

data class ArenaVedtak(
    @JsonProperty("kravMottattDato")
    val kravMottattDato: LocalDate? = null,

    @JsonProperty("fom")
    var fom: LocalDate? = null,

    @JsonProperty("tom")
    var tom: LocalDate? = null,

    @JsonProperty("vedtakDato")
    val vedtakDato: LocalDate? = null,

    @JsonProperty("status")
    var status: VedtakStatus,

    @JsonProperty("dagsats")
    val dagsats: BigDecimal,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("meldekort")
    val meldekort: List<ArenaMeldekort> = ArrayList()
)
