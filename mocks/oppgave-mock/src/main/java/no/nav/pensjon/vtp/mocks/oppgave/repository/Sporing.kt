package no.nav.pensjon.vtp.mocks.oppgave.repository

import no.nav.pensjon.vtp.testmodell.enheter.Norg2Modell

class Sporing(val ansattIdent: String, private val enhet: Norg2Modell) {
    val enhetId: String
        get() = enhet.enhetId
    val enhetNavn: String
        get() = enhet.navn
}
