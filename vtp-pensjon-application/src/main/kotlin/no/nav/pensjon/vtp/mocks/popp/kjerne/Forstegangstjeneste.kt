package no.nav.pensjon.vtp.mocks.popp.kjerne

import java.util.*

data class Forstegangstjeneste(
    val forstegangstjenesteId: Long? = null,
    val fnr: Pid? = null,
    val forstegangstjenestePeriodeListe: List<ForstegangstjenestePeriode>? = null,
    val changeStamp: ChangeStamp? = null,
    val kilde: String? = null,
    val rapportType: String? = null,
    val tjenestestartDato: Date? = null,
    val dimitteringDato: Date? = null,
)
