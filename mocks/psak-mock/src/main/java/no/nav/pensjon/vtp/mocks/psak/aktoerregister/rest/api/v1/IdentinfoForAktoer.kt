package no.nav.pensjon.vtp.mocks.psak.aktoerregister.rest.api.v1

data class IdentinfoForAktoer(
    val identer: List<Identinfo>? = null,
    val feilmelding: String? = null,
)
