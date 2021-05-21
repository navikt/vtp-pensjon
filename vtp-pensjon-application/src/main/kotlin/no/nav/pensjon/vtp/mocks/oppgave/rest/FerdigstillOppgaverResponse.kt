package no.nav.pensjon.vtp.mocks.oppgave.rest

import io.swagger.annotations.ApiModelProperty

data class FerdigstillOppgaverResponse(
    val feilet: Int = 0,
    val suksess: Int = 0,
    val totalt: Int = 0,

    @ApiModelProperty(value = "HTTP-kode -> oppgaveID")
    val data: Map<Int, List<Long>> = HashMap()
)
