package no.nav.pensjon.vtp.mocks.samboer.proxy

import java.time.LocalDate

data class SamboerProxyDTO(
    val fnrInnmelder: String,
    val fnrMotpart: String,
    val gyldigFraOgMed: LocalDate,
    val gyldigTilOgMed: LocalDate?,
    val opprettetAv: String,
)
