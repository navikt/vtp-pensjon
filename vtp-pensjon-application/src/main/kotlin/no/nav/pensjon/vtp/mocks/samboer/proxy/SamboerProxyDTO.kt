package no.nav.pensjon.vtp.mocks.samboer.proxy

import java.time.LocalDate

data class SamboerProxyDTO(
    val pidBruker: String,
    val pidSamboer: String,
    val datoFom: LocalDate,
    val datoTom: LocalDate?,
    val registrertAv: String,
)
