package no.nav.pensjon.vtp.configuration.graphql.model

data class Identliste(
    val identer: List<IdentInformasjon>
)

data class IdentlisteBolk(
    val ident: String,
    val identer: Set<IdentInformasjon>?,
    val code: String
)

data class IdentInformasjon(
    val ident: String,
    val gruppe: IdentGruppe,
    val historisk: Boolean
)

enum class IdentGruppe {
    AKTORID,
    FOLKEREGISTERIDENT,
    NPID
}

enum class HentIdenterBolkCode {
    ok,
    bad_request,
    not_found
}
