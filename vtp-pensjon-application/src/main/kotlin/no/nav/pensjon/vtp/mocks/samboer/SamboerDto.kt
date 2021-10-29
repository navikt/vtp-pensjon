package no.nav.pensjon.vtp.mocks.samboer

import java.net.URI
import java.time.LocalDate

data class SamboerRequest(
    val fnrInnmelder: String,
    val fnrMotpart: String,
    val gyldigFraOgMed: LocalDate,
    val gyldigTilOgMed: LocalDate?,
    val opprettetAv: String,
)

data class SamboerResponse(
    val fnrInnmelder: String,
    val fnrMotpart: String,
    val gyldigFraOgMed: LocalDate,
    val gyldigTilOgMed: LocalDate?,
    val opprettetAv: String,
    val _links: Links,
) {
    data class Links(
        val self: Link,
        val avslutt: Link,
    )
}

data class Link(
    val href: URI,
)

data class AvsluttForhold(
    val dato: LocalDate
)
