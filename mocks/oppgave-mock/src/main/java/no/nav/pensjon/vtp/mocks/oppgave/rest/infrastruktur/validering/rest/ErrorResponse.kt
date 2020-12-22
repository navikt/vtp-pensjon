package no.nav.pensjon.vtp.mocks.oppgave.rest.infrastruktur.validering.rest

data class ErrorResponse(
    val uuid: String,
    val feilmelding: String
)
