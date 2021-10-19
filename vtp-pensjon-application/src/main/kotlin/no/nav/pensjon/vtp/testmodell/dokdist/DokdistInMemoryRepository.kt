package no.nav.pensjon.vtp.testmodell.dokdist

import org.springframework.stereotype.Repository
import java.util.*

@Repository
class DokdistInMemoryRepository : DokdistRepository {
    private val bestillinger = HashMap<String, DistribuerJournalpostBestilling>()

    override fun save(bestilling: DistribuerJournalpostBestilling): DistribuerJournalpostBestilling {
        val bestillingsId = bestilling.bestillingsId?.takeIf { it.isNotEmpty() } ?: genererBestillingsId()

        return bestilling.copy(bestillingsId = bestillingsId)
            .also { bestillinger[bestillingsId] = bestilling }
    }

    override fun finnDistribuerJournalpostBestillingMedBestillingsId(bestillingsId: String): DistribuerJournalpostBestilling? =
        bestillinger[bestillingsId]

    override fun finnAlleBestillingsId(): List<String> =
        bestillinger.keys.toList()

    fun genererBestillingsId() = UUID.randomUUID().toString()
}
