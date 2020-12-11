package no.nav.pensjon.vtp.testmodell.load

import no.nav.pensjon.vtp.testmodell.kodeverk.Endringstype
import no.nav.pensjon.vtp.testmodell.personopplysning.Landkode
import java.time.LocalDate

data class StatsborgerskapTemplate(
    val fom: LocalDate?,
    val tom: LocalDate?,
    val endringstype: Endringstype?,
    val endringstidspunkt: LocalDate?,
    val land: Landkode
)
