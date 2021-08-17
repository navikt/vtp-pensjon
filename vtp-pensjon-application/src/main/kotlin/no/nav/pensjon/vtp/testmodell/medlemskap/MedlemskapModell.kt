package no.nav.pensjon.vtp.testmodell.medlemskap

data class MedlemskapModell(
    val perioder: List<MedlemskapperiodeModell> = ArrayList()
)
