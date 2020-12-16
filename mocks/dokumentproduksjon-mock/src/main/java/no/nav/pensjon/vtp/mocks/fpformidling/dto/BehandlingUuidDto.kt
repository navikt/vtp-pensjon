package no.nav.pensjon.vtp.mocks.fpformidling.dto

import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class BehandlingUuidDto(
    @NotNull @Valid
    val behandlingUuid: UUID
)
