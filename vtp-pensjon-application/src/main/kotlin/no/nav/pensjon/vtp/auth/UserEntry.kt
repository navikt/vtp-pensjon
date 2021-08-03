package no.nav.pensjon.vtp.auth

import no.nav.pensjon.vtp.testmodell.ansatt.NAVAnsatt

data class UserEntry(
    val username: String,
    val displayName: String,
    val redirect: String,
    val details: NAVAnsatt
)
