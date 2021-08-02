package no.nav.pensjon.vtp.testmodell.dkif

data class DkifResponse(
    val kontaktinfo: Map<String, Kontaktinfo>?,
    val feil: Map<String, Feil>?
)
