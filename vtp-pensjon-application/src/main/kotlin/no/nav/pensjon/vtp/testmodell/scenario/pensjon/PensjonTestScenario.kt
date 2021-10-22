package no.nav.pensjon.vtp.testmodell.scenario.pensjon

import java.time.LocalDate

data class PensjonTestScenario(
    val fnr: String? = null,
    val kortnavn: String? = null,
    val fornavn: String? = null,
    val mellomnavn: String? = null,
    val etternavn: String? = null,
    val diskresjonskode: String? = null,
    val sivilstand: String? = null,
    val dodsdato: LocalDate? = null,

    val tjenestepensjon: Tjenestepensjon? = null
)
