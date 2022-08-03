package no.nav.pensjon.vtp.testmodell.personopplysning

import java.time.LocalDate

data class SamboerforholdModell(
    val id: String,
    val pidSamboer: String,
    val datoFom: LocalDate,
    var datoTom: LocalDate?,
    val opprettetAv: String,
    var annullert: Boolean = false
)
