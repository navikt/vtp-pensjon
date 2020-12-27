package no.nav.pensjon.vtp.mocks.fpformidling.dto

import no.nav.tjeneste.virksomhet.kodeverk.v2.informasjon.Kodeverk
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

@Suppress("unused")
class BrevmalDto(
    @NotNull @Pattern(regexp = "[A-Z]{6}")
    val kode: String,
    @NotNull
    val navn: String,
    @NotNull
    val restriksjon: Kodeverk,
    val tilgjengelig: Boolean = false,
)
