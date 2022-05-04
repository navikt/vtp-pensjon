package no.nav.pensjon.vtp.mocks.popp.kjerne

import no.nav.pensjon.vtp.mocks.popp.kjerne.ChangeStamp
import no.nav.pensjon.vtp.mocks.popp.kjerne.Pid

data class Dagpenger(
    val dagpengerId: Long? = null,
    val fnr: Pid? = null,
    val dagpengerType: String? = null,
    val rapportType: String? = null,
    val kilde: String? = null,
    val ar: Int? = null,
    val utbetalteDagpenger: Int? = null,
    val uavkortetDagpengegrunnlag: Int? = null,
    val ferietillegg: Int? = null,
    val barnetillegg: Int? = null,
    val changeStamp: ChangeStamp? = null,
)

