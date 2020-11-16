package no.nav.pensjon.vtp.testmodell.load

import no.nav.pensjon.vtp.testmodell.kodeverk.Endringstype
import no.nav.pensjon.vtp.testmodell.kodeverk.Personstatuser
import java.time.LocalDate

data class PersonstatusTemplate(
        val fom: LocalDate?,
        val tom: LocalDate?,
        val endringstype: Endringstype?,
        val endringstidspunkt: LocalDate?,
        val kode: Personstatuser
)
