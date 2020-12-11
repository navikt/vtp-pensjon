package no.nav.pensjon.vtp.testmodell.repo

data class TemplateVariable(
    val targetClass: Class<*>,
    val name: String,
    val path: String,
    val defaultVerdi: String?
)
