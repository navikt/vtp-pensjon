package no.nav.pensjon.vtp.mocks.samboer

import org.springframework.hateoas.RepresentationModel
import java.time.LocalDate

data class SamboerDTO(
    val pidBruker: String,
    val pidSamboer: String,
    val datoFom: LocalDate,
    val datoTom: LocalDate?,
    val registrertAv: String,
) : RepresentationModel<SamboerDTO>()
