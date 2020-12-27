package no.nav.pensjon.vtp.testmodell.load

import no.nav.pensjon.vtp.testmodell.kodeverk.Endringstype
import no.nav.pensjon.vtp.testmodell.kodeverk.Sivilstander
import java.time.LocalDate

data class SivilstandTemplate(
    val fom: LocalDate?,
    val tom: LocalDate?,
    val endringstype: Endringstype?,
    val endringstidspunkt: LocalDate?,
    val kode: Sivilstander
)
