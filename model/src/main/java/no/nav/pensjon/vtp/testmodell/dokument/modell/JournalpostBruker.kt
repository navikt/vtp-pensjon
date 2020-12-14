package no.nav.pensjon.vtp.testmodell.dokument.modell

import no.nav.pensjon.vtp.testmodell.dokument.modell.koder.BrukerType

data class JournalpostBruker(
    val ident: String,
    val brukerType: BrukerType
)
