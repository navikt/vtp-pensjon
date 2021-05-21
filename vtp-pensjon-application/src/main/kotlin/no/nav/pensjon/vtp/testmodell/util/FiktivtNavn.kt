package no.nav.pensjon.vtp.testmodell.util

import no.nav.pensjon.vtp.testmodell.kodeverk.Kjønn
import no.nav.pensjon.vtp.testmodell.kodeverk.Sivilstander
import no.nav.pensjon.vtp.testmodell.load.PersonTemplate
import no.nav.pensjon.vtp.testmodell.personopplysning.PersonNavn
import java.util.*
import java.util.Locale.getDefault

object FiktivtNavn {
    private val etternavn = loadNames("/basedata/etternavn.txt")
    private val fornavnKvinner = loadNames("/basedata/fornavn-kvinner.txt")
    private val fornavnMenn = loadNames("/basedata/fornavn-menn.txt")

    private fun getRandom(liste: List<String>): String {
        return liste[Random().nextInt(liste.size)]
    }

    private fun loadNames(resourceName: String): List<String> =
        FiktivtNavn::class.java.getResourceAsStream(resourceName)
            .bufferedReader()
            .readLines()
            .map { navn -> navn.replaceFirstChar { if (it.isLowerCase()) it.titlecase(getDefault()) else it.toString() } }

    fun getRandomName(kjønn: Kjønn): PersonNavn {
        return if (kjønn == Kjønn.K) {
            getRandomFemaleName(getRandom(etternavn))
        } else {
            getRandomMaleName(getRandom(etternavn))
        }
    }

    fun getRandomName(kjønn: Kjønn, etternavn: String): PersonNavn {
        return if (kjønn == Kjønn.K) {
            getRandomFemaleName(etternavn)
        } else {
            getRandomMaleName(etternavn)
        }
    }

    /**
     * Tar høyde for at gifte folk deler etternavn.
     */
    fun getAnnenPartName(søker: PersonTemplate, etternavn: String, kjønn: Kjønn): PersonNavn {
        val gift = søker.sivilstand?.map { it.kode }?.contains(Sivilstander.GIFT)
        return if (gift != null && gift) {
            getRandomName(kjønn, etternavn)
        } else {
            getRandomName(kjønn)
        }
    }

    fun getRandomFemaleName(lastName: String): PersonNavn {
        return PersonNavn(getRandom(fornavnKvinner), lastName, Kjønn.K)
    }

    fun getRandomMaleName(lastName: String): PersonNavn {
        return PersonNavn(getRandom(fornavnMenn), lastName, Kjønn.M)
    }
}
