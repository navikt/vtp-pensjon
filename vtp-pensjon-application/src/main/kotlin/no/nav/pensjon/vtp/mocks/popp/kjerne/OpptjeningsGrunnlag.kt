package no.nav.pensjon.vtp.mocks.popp.kjerne

data class OpptjeningsGrunnlag(
    val fnr: Pid,
    val inntektListe: List<Inntekt> = emptyList(),
    val omsorgListe: List<Omsorg> = emptyList(),
    val dagpengerListe: List<Dagpenger> = emptyList(),
    val forstegangstjeneste: Forstegangstjeneste? = null,
)
