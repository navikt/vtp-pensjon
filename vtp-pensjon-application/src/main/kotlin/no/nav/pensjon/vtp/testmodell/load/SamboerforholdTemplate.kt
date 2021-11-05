package no.nav.pensjon.vtp.testmodell.load

import java.time.LocalDate

class SamboerforholdTemplate(
    val fraOgMed: LocalDate,
    val tilOgMed: LocalDate?,
    val opprettetAv: String,
)
