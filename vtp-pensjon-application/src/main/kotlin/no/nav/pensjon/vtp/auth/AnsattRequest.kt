package no.nav.pensjon.vtp.auth

data class AnsattRequest(
    val groups: List<String>,
    val units: List<String>,
)
