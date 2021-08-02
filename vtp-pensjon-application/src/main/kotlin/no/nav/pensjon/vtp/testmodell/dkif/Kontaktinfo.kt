package no.nav.pensjon.vtp.testmodell.dkif

data class Kontaktinfo(
    val personident: String,
    val kanVarsles: Boolean,
    val reservert: Boolean,
    val epostadresse: String? = null,
    val mobiltelefonnummer: String? = null,
    val sikkerDigitalPostkasse: SikkerDigitalPostkasse? = null,
    val spraak: String? = null
)
