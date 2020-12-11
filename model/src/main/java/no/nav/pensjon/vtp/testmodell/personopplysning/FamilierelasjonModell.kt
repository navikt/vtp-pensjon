package no.nav.pensjon.vtp.testmodell.personopplysning

import no.nav.pensjon.vtp.testmodell.kodeverk.Rolle

data class FamilierelasjonModell(
    val rolle: Rolle,
    val til: String,
    val sammeBosted: Boolean?
) {
    fun getRolleKode(): String {
        return rolle.name
    }
}
