package no.nav.pensjon.vtp.testmodell.load

import no.nav.pensjon.vtp.testmodell.kodeverk.Endringstype
import no.nav.pensjon.vtp.testmodell.kodeverk.GeografiskTilknytningType
import java.time.LocalDate

data class GeografiskTilknytningTemplate(
        val fom: LocalDate?,
        val tom: LocalDate?,
        val endringstype: Endringstype?,
        val endringstidspunkt: LocalDate?,
        val type: GeografiskTilknytningType?,
        val kode: String
)
