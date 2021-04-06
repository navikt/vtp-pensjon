package no.nav.pensjon.vtp.testmodell.kodeverk

/** NAV kodeverk: http://nav.no/kodeverk/Kodeverk/Diskresjonskoder.  */
@Suppress("unused")
enum class Diskresjonskoder(val kode: Int = 0) {
    KLIE,
    MILI,
    PEND,
    SPFO(7),
    SPSF(6),
    SVAL,
    UFB,
    URIK,
    UDEF;
}
