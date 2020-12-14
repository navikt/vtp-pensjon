package no.nav.pensjon.vtp.testmodell.kodeverk

@Suppress("unused")
enum class Rolle(val forelder: Boolean = false) {
    EKTE,
    BARN,
    FARA(true),
    MORA(true),
    SAMB,
    REPA,
    MMOR(true);

    fun erForelder(): Boolean {
        return forelder
    }
}
