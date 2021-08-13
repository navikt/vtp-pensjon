package no.nav.pensjon.vtp.testmodell.inntektytelse.arena

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import no.nav.pensjon.vtp.testmodell.inntektytelse.RelatertYtelseTema

data class ArenaSak(
    @JsonProperty("saksnummer")
    var saksnummer: String? = null,

    @JsonProperty("tema")
    var tema: RelatertYtelseTema,

    @JsonProperty("status")
    var status: SakStatus,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("vedtak")
    val vedtak: List<ArenaVedtak> = ArrayList()
)
