package no.nav.pensjon.vtp.testmodell.dokdist

interface DokdistRepository {
    fun save(bestilling: DistribuerJournalpostBestilling): DistribuerJournalpostBestilling
    fun finnDistribuerJournalpostBestillingMedBestillingsId(bestillingsId: String): DistribuerJournalpostBestilling?
    fun finnAlleBestillingsId(): List<String>
}
