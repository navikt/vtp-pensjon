package no.nav.pensjon.vtp.testmodell.load

import no.nav.pensjon.vtp.testmodell.kodeverk.Rolle

data class FamilierelasjonTemplate(
    val rolle: Rolle,
    val til: String,
    val sammeBosted: Boolean?
)
