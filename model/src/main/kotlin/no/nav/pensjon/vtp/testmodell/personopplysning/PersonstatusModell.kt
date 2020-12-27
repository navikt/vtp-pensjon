package no.nav.pensjon.vtp.testmodell.personopplysning

import no.nav.pensjon.vtp.testmodell.kodeverk.Endringstype
import no.nav.pensjon.vtp.testmodell.kodeverk.Personstatuser
import java.time.LocalDate

data class PersonstatusModell(
    val kode: Personstatuser,
    val fom: LocalDate? = null,
    val tom: LocalDate? = null,
    val endringstype: Endringstype? = null,
    val endringstidspunkt: LocalDate? = null
)
