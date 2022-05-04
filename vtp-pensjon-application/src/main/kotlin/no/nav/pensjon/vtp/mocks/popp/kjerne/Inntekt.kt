package no.nav.pensjon.vtp.mocks.popp.kjerne

data class Inntekt(
    val inntektId: Long? = null,
    val fnr: Pid? = null,
    val kilde: String? = null,
    val kommune: String? = null,
    val piMerke: String? = null,
    val inntektAr: Int? = null,
    val belop: Long? = null,
    val changeStamp: ChangeStamp? = null,
    val inntektType: String? = null,
)
