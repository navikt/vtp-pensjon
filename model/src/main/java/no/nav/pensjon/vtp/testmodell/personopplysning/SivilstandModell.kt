package no.nav.pensjon.vtp.testmodell.personopplysning

import no.nav.pensjon.vtp.testmodell.kodeverk.Endringstype
import no.nav.pensjon.vtp.testmodell.kodeverk.Sivilstander
import java.time.LocalDate

data class SivilstandModell(
        val kode: Sivilstander,
        val fom: LocalDate? = null,
        val tom: LocalDate? = null,
        val endringstype: Endringstype? = null,
        val endringstidspunkt: LocalDate? = null
)
