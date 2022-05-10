package no.nav.pensjon.vtp.mocks.popp.kjerne

data class Pensjonspoeng(
    val pensjonspoengId: Long? = null,
    val fnr: Pid? = null,
    val fnrOmsorgFor: Pid? = null,
    val kilde: String? = null,
    val pensjonspoengType: String? = null,
    val inntekt: Inntekt? = null,
    val omsorg: Omsorg? = null,
    val ar: Int? = null,
    val anvendtPi: Int? = null,
    val poeng: Double? = null,
    val maxUforegrad: Int? = null,
    val changeStamp: ChangeStamp? = null,
)
