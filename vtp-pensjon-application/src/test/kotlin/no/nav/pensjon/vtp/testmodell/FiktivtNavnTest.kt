package no.nav.pensjon.vtp.testmodell

import no.nav.pensjon.vtp.testmodell.kodeverk.Kjønn
import no.nav.pensjon.vtp.testmodell.util.FiktivtNavn
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FiktivtNavnTest {
    @Test
    fun fiktiv_navn_skal_fungere() {
        val randomFemaleName = FiktivtNavn.getRandomName(Kjønn.K)
        assertTrue(randomFemaleName.fornavn.length > 1)
        assertTrue(randomFemaleName.etternavn.length > 1)

        val randomMaleName = FiktivtNavn.getRandomName(Kjønn.M)
        assertTrue(randomMaleName.fornavn.length > 1)
        assertTrue(randomMaleName.etternavn.length > 1)
    }
}
