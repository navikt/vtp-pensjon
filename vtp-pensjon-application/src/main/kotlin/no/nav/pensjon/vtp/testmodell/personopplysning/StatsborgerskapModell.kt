package no.nav.pensjon.vtp.testmodell.personopplysning

import no.nav.pensjon.vtp.testmodell.kodeverk.Endringstype
import java.time.LocalDate

data class StatsborgerskapModell(
    val land: Landkode?,
    val fom: LocalDate? = null,
    val tom: LocalDate? = null,
    val endringstype: Endringstype? = null,
    val endringstidspunkt: LocalDate? = null
)
