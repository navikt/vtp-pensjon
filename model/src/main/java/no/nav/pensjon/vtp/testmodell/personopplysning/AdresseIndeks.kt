package no.nav.pensjon.vtp.testmodell.personopplysning

import java.util.*

class AdresseIndeks {
    private val adresser: MutableList<AdresseModell> = ArrayList()

    @Synchronized
    fun leggTil(adresse: AdresseModell) {
        adresser.add(adresse)
    }

    @Synchronized
    fun finn(adresseType: AdresseType, landkode: Landkode): AdresseModell {
        return adresser.stream()
            .filter { a: AdresseModell -> a.adresseType == adresseType }
            .filter { a: AdresseModell -> landkode == a.land }
            .filter { a: AdresseModell -> AdresseRefModell::class.java != a.javaClass }
            .findFirst()
            .orElseThrow { IllegalArgumentException("Fant ingen adresser i indeks for type=$adresseType, landkode=$landkode") }
    }

    /** bytt ut en generisk referansemal med en adressse fra katalogen.  */
    @Synchronized
    fun finnFra(ref: AdresseRefModell): AdresseModell {
        val adresse = finn(ref.adresseType, ref.land)

        return adresse.clone(
            // overskriv hvis felter er satt på ref
            fom = ref.fom ?: adresse.fom,
            tom = ref.tom ?: adresse.tom
        )
    }
}
