package no.nav.pensjon.vtp.testmodell.inntektytelse.trex

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue

@Suppress("unused", "SpellCheckingInspection", "EnumEntryName")
enum class InntektsperiodeKode {
    @JsonEnumDefaultValue
    UKJENT,
    M,
    U,
    D,
    Å,
    F,
    X,
    Y
}
