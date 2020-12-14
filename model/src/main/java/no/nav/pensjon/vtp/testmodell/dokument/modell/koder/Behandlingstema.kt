package no.nav.pensjon.vtp.testmodell.dokument.modell.koder

@Suppress("unused")
enum class Behandlingstema(val code: String) {
    ENGANGSSTONAD_ADOPSJON("ab0027"),
    ENGANGSSTONAD_FOEDSEL("ab0050"),
    FORELDREPENGER_FOEDSEL("ab0047"),
    FORELDREPENGER_ADOPSJON("ab0072"),
    FORELDREPENGER("ab0326"),
    SVANGERSKAPSPENGER("ab0126");

    companion object {
        fun fromCode(code: String) = values().first { it.code == code }
    }
}
