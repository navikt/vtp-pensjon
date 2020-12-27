package no.nav.pensjon.vtp.testmodell.personopplysning

enum class AdresseType {
    BOSTEDSADRESSE, POSTADRESSE, MIDLERTIDIG_POSTADRESSE, UKJENT_ADRESSE;

    fun getTpsKode(landKode: Landkode?): String {
        return if (this == MIDLERTIDIG_POSTADRESSE) {
            if (Landkode.NOR == landKode) "MIDLERTIDIG_POSTADRESSE_NORGE" else "MIDLERTIDIG_POSTADRESSE_UTLAND"
        } else {
            name
        }
    }
}
