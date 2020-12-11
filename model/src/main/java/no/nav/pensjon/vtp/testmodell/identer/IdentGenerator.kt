package no.nav.pensjon.vtp.testmodell.identer

interface IdentGenerator {
    /**
     * Bruk denne når kjønn ikke har betydning for anvendt FNR. (Bør normalt brukes slik at en sikrer at applikasjonen ikke gjør antagelser om
     * koding av kjønn i FNR.
     */
    fun tilfeldigFnr(): String

    /** Returnerer FNR for mann > 18 år
     * @param foedselsdato
     */
    fun tilfeldigMannFnr(foedselsdato: String?): String

    /** Returnerer FNR for kvinne > 18 år
     * @param foedselsdato
     */
    fun tilfeldigKvinneFnr(foedselsdato: String?): String

    /** Returnerer FNR for barn (tilfeldig kjønn) < 18 år  */
    fun tilfeldigBarnUnderTreAarFnr(): String

    /** Returnerer DNR for kvinne > 18 år  */
    fun tilfeldigKvinneDnr(): String
}
