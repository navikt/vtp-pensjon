package no.nav.pensjon.vtp.miscellaneous.api.sak

@Suppress("SpellCheckingInspection")
data class OpprettSakRequestDTO(
    val lokalIdent: List<String>,
    val fagområde: String,
    val fagsystem: String,
    val sakstype: String
)
