package no.nav.pensjon.vtp.mocks.fpformidling.dto

import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

data class DokumentbestillingDto(
    @NotNull
    val behandlingUuid: UUID,
    @NotNull
    val ytelseType: String,
    @NotNull @Pattern(regexp = "[A-Z]{6}")
    val dokumentMal: String,
    @Pattern(regexp = "[A-ZÆØÅ0-9]{1,100}")
    val historikkAktør: String? = null,
    val tittel: String? = null,
    val fritekst: String? = null,
    @Pattern(regexp = "[A-ZÆØÅ0-9]{1,100}")
    val arsakskode: String? = null,
    val gjelderVedtak: Boolean = false,
    val erOpphevetKlage: Boolean = false
)
