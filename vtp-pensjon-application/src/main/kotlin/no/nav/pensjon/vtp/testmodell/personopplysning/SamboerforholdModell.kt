package no.nav.pensjon.vtp.testmodell.personopplysning

import java.time.LocalDate

data class SamboerforholdModell(
    val id: String,
    val innmelder: String,
    val motpart: String,
    val fraOgMed: LocalDate,
    val tilOgMed: LocalDate?,
    val opprettetAv: String,
)
