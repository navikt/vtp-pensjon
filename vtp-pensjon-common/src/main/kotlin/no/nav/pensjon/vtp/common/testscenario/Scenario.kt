package no.nav.pensjon.vtp.common.testscenario

import java.time.LocalDate


data class VtpPensjonTestScenario(
    var testScenarioId: String? = null,
    var pid: String? = null,
    var foedselsdato: LocalDate? = null,
    var kortnavn: String? = null,
    var fornavn: String? = null,
    var mellomnavn: String? = null,
    var etternavn: String? = null,
    var diskresjonskode: String? = null,
    var sivilstand: String? = null,
    var dodsdato: LocalDate? = null,
)
