package no.nav.pensjon.vtp.testmodell

import no.nav.pensjon.vtp.testmodell.kodeverk.Kjønn
import no.nav.pensjon.vtp.testmodell.util.FiktivtNavn
import org.junit.Assert
import org.junit.Test

class FiktivtNavnTest {
    @Test
    fun fiktiv_navn_skal_fungere() {
        val randomFemaleName = FiktivtNavn.getRandomName(Kjønn.K)
        Assert.assertTrue(randomFemaleName.fornavn.length > 1)
        Assert.assertTrue(randomFemaleName.etternavn.length > 1)

        val randomMaleName = FiktivtNavn.getRandomName(Kjønn.M)
        Assert.assertTrue(randomMaleName.fornavn.length > 1)
        Assert.assertTrue(randomMaleName.etternavn.length > 1)
    }
}
