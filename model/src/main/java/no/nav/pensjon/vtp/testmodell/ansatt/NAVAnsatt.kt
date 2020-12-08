package no.nav.pensjon.vtp.testmodell.ansatt

data class NAVAnsatt(
        val cn: String,
        val givenname: String,
        val sn: String,
        val displayName: String,
        val email: String,
        val groups: List<String>,
        val enheter: List<String>
)
