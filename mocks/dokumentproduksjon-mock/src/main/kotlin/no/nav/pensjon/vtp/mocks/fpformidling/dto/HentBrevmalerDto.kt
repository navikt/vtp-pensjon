package no.nav.pensjon.vtp.mocks.fpformidling.dto

import javax.validation.constraints.NotNull

data class HentBrevmalerDto(
    @NotNull
    val brevmalDtoListe: List<BrevmalDto>
)
