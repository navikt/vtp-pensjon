package no.nav.pensjon.vtp.mocks.fpformidling.dto

import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class DokumentProdusertDto(
    @Valid @NotNull
    val behandlingUuid: UUID,
    @NotNull @Size(min = 1, max = 100)
    val dokumentMal: String
)
