package no.nav.pensjon.vtp.testmodell.inntektytelse.arena

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import no.nav.pensjon.vtp.testmodell.FeilKode

data class ArenaModell(
    @JsonProperty("feilkode")
    val feilkode: FeilKode? = null,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("saker")
    val saker: List<ArenaSak> = ArrayList()
)
