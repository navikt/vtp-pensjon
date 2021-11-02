package no.nav.pensjon.vtp.mocks.samboer

import org.springframework.hateoas.RepresentationModel
import java.time.LocalDate

data class SamboerDTO(
    val fnrInnmelder: String,
    val fnrMotpart: String,
    val gyldigFraOgMed: LocalDate,
    val gyldigTilOgMed: LocalDate?,
    val opprettetAv: String,
) : RepresentationModel<SamboerDTO>()

data class AvsluttForhold(
    val dato: LocalDate
)
