package no.nav.pensjon.vtp.testmodell.medlemskap

import java.util.*

data class MedlemskapModell(
    val perioder: List<MedlemskapperiodeModell> = ArrayList()
)
