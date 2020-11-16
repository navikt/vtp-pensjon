package no.nav.pensjon.vtp.testmodell.repo

import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseModell
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonModell
import no.nav.pensjon.vtp.testmodell.personopplysning.Personopplysninger

data class Testscenario(
        val templateNavn: String,
        val id: String,
        val personopplysninger: Personopplysninger,
        val søkerInntektYtelse: InntektYtelseModell?,
        val annenpartInntektYtelse: InntektYtelseModell?,
        val organisasjonModeller: List<OrganisasjonModell>,
        val vars: Map<String, String>
)
