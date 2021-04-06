package no.nav.pensjon.vtp.mocks.fpformidling.dto

import java.util.*
import javax.validation.constraints.NotNull

@Suppress("unused")
data class TekstFraSaksbehandlerDto(
    @NotNull
    val behandlingUuid: UUID,
    val vedtaksbrev: String? = null,
    val avklarFritekst: String? = null,
    val tittel: String? = null,
    val fritekst: String? = null
)
