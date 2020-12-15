package no.nav.pensjon.vtp.testmodell.inntektytelse.trex

data class Vedtak(
    val periode: Periode? = null,
    val utbetalingsgrad: Int = 0
)
