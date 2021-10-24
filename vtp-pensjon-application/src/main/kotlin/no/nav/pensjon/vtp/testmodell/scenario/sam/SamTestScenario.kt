package no.nav.pensjon.vtp.testmodell.scenario.sam

import no.nav.tjeneste.virksomhet.pensjonsvedtaksamordning.v1.informasjon.pensjonsvedtaksamordning.*
import java.time.LocalDate

data class SamTestScenario(
    val testScenarioId: String?,
    val pid: String? = null,
    val kortnavn: String? = null,
    val fornavn: String? = null,
    val mellomnavn: String? = null,
    val etternavn: String? = null,
    val diskresjonskode: String? = null,
    val sivilstand: String? = null,
    val dodsdato: LocalDate? = null,

    val tjenestepensjon: Tjenestepensjon? = null,
)

data class Tjenestepensjon(
    val forhold: Set<Forhold>,
)

data class Forhold(
    val tpNr: String?,
    val forholdId: Long?,
    val ytelser: Set<Ytelse>,
)

data class Ytelse(
    val type: String,
    val ytelseId: Long?,
    val innmeldtFom: LocalDate,
    val iverksattFom: LocalDate?,
    val iverksattTom: LocalDate?,
)
