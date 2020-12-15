package no.nav.pensjon.vtp.testmodell.inntektytelse.trex

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue

@Suppress("unused", "SpellCheckingInspection")
enum class StatusKode {
    @JsonEnumDefaultValue
    UKJENT,
    L,
    A,
    I
}
