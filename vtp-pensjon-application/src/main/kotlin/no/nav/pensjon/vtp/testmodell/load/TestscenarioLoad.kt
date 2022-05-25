package no.nav.pensjon.vtp.testmodell.load

import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseModell
import no.nav.pensjon.vtp.testmodell.krr.DigitalKontaktinformasjon
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonModell
import no.nav.pensjon.vtp.testmodell.util.VariabelContainer

class TestscenarioLoad(
    val templateNavn: String,
    val personopplysninger: PersonopplysningerTemplate,
    val søkerInntektYtelse: InntektYtelseModell?,
    val annenpartInntektYtelse: InntektYtelseModell?,
    val organisasjonModeller: List<OrganisasjonModell>,
    val digitalKontaktinformasjon: DigitalKontaktinformasjon?,
    val variabelContainer: VariabelContainer
) {
    override fun toString(): String {
        return "<template, $templateNavn>"
    }
}
