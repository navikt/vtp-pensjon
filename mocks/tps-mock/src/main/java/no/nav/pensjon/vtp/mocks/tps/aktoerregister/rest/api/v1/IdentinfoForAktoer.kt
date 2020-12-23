package no.nav.pensjon.vtp.mocks.tps.aktoerregister.rest.api.v1

data class IdentinfoForAktoer(
    val identer: List<Identinfo>? = null,
    val feilmelding: String? = null
)
