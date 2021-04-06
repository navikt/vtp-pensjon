package no.nav.pensjon.vtp.mocks.oppgave.sak

@Suppress("SpellCheckingInspection")
data class OpprettSakRequestDTO(
    val lokalIdent: List<String>,
    val fagområde: String,
    val fagsystem: String,
    val sakstype: String
)
