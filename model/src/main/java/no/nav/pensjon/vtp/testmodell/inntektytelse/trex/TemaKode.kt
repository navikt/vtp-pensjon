package no.nav.pensjon.vtp.testmodell.inntektytelse.trex

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue

@Suppress("unused", "SpellCheckingInspection")
enum class TemaKode {
    @JsonEnumDefaultValue
    UKJENT,
    FA,
    SP,
    BS
}
