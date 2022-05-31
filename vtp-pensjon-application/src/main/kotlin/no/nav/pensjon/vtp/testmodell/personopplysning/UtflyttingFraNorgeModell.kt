package no.nav.pensjon.vtp.testmodell.personopplysning

import java.time.LocalDate

data class UtflyttingFraNorgeModell(
    val tilflyttingsland: String,
    val utflyttingsdato: LocalDate
)
