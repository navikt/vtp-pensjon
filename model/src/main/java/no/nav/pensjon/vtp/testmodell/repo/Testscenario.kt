package no.nav.pensjon.vtp.testmodell.repo

import no.nav.pensjon.vtp.testmodell.inntektytelse.InntektYtelseModell
import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonModell
import no.nav.pensjon.vtp.testmodell.personopplysning.Personopplysninger
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Testscenario(
        @Id
        val id: String,
        val templateNavn: String,
        val personopplysninger: Personopplysninger,
        val søkerInntektYtelse: InntektYtelseModell?,
        val annenpartInntektYtelse: InntektYtelseModell?,
        val organisasjonModeller: List<OrganisasjonModell>,
        val vars: Map<String, String?>
)
