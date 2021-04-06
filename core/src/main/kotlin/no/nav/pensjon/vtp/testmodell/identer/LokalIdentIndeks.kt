package no.nav.pensjon.vtp.testmodell.identer

import no.nav.pensjon.vtp.testmodell.kodeverk.Kjønn
import java.util.concurrent.ConcurrentHashMap

/** konverterer lokale identer brukt i testcase til utvalgte fødselsnummer hentet fra syntetisk liste.  */
class LokalIdentIndeks(private val unikScenarioId: String, private val identGenerator: IdentGenerator) {
    private val identer: MutableMap<String, String> = ConcurrentHashMap()

    fun getVoksenIdentForLokalIdent(lokalIdent: String, kjønn: Kjønn): String {
        return if (lokalIdent.matches("^\\d+$".toRegex())) {
            identer.computeIfAbsent(key(lokalIdent)) { lokalIdent }
        } else identer.computeIfAbsent(key(lokalIdent)) {
            if (kjønn == Kjønn.M) identGenerator.tilfeldigMannFnr(
                null
            ) else identGenerator.tilfeldigKvinneFnr(null)
        }
    }

    fun getVoksenIdentForLokalIdent(lokalIdent: String, kjønn: Kjønn, foedselsdato: String?): String {
        return if (lokalIdent.matches("^\\d+$".toRegex())) {
            identer.computeIfAbsent(key(lokalIdent)) { lokalIdent }
        } else identer.computeIfAbsent(key(lokalIdent)) {
            if (kjønn == Kjønn.M) identGenerator.tilfeldigMannFnr(
                foedselsdato
            ) else identGenerator.tilfeldigKvinneFnr(foedselsdato)
        }
    }

    private fun key(lokalIdent: String): String {
        return "$unikScenarioId::$lokalIdent"
    }

    fun getIdent(lokalIdent: String): String? {
        return identer[key(lokalIdent)]
    }
}
