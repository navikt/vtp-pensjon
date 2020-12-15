package no.nav.pensjon.vtp.testmodell.inntektytelse.trex

import java.time.LocalDate

data class Periode(
    val fom: LocalDate? = null,
    val tom: LocalDate? = null
)
