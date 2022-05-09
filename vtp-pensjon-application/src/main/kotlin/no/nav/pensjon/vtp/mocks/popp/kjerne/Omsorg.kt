package no.nav.pensjon.vtp.mocks.popp.kjerne

data class Omsorg(
    val omsorgId: Long? = null,
    val fnr: Pid? = null,
    val fnrOmsorgFor: Pid? = null,
    val omsorgType: String? = null,
    val kilde: String?,
    val ar: Int? = null,
    val changeStamp: ChangeStamp? = null,
)
