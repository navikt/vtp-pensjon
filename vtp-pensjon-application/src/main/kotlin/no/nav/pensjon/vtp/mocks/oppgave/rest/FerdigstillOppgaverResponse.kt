package no.nav.pensjon.vtp.mocks.oppgave.rest

import io.swagger.v3.oas.annotations.media.Schema

data class FerdigstillOppgaverResponse(
    val feilet: Int = 0,
    val suksess: Int = 0,
    val totalt: Int = 0,

    @Schema(description = "HTTP-kode -> oppgaveID")
    val data: Map<Int, List<Long>> = HashMap()
)
