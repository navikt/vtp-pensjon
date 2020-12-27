package no.nav.pensjon.vtp.testmodell.inntektytelse.trex

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue

@Suppress("unused", "SpellCheckingInspection", "EnumEntryName")
enum class BehandlingstemaKode {
    @JsonEnumDefaultValue
    UKJENT,
    AP,
    FP,
    FU,
    FØ,
    SV,
    SP,
    OM,
    PB,
    OP,
    PP,
    PI,
    PN
}
