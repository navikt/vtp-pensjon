package no.nav.pensjon.vtp.testmodell.scenario.pensjon

import java.time.LocalDate

data class Tjenestepensjon(
    val forhold: Set<Forhold>,
)

data class Forhold(
    val tpNr: String?,
    val forholdId: Long?,
    val ytelser: Set<Ytelse>,
)

data class Ytelse(
    val type: String,
    val ytelseId: Long?,
    val innmeldtFom: LocalDate,
    val iverksattFom: LocalDate?,
    val iverksattTom: LocalDate?,
)
