package no.nav.pensjon.vtp.testmodell.identer

import no.nav.pensjon.vtp.testmodell.enums.Kjonn
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class IdenterIndeks {
    private val identer: MutableMap<String, LokalIdentIndeks> = ConcurrentHashMap()
    private val identGenerator: IdentGenerator = FiktiveFnr()

    fun getIdenter(unikScenarioId: String): LokalIdentIndeks {
        return identer.computeIfAbsent(unikScenarioId) { LokalIdentIndeks(it, identGenerator) }
    }

    fun getRandomFnr(kjonn: Kjonn) = identGenerator.tilfeldigFnr(kjonn)
}
