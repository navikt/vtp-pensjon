package no.nav.pensjon.vtp.testmodell.dokdist

data class DistribuerJournalpostBestilling(
    val bestillingsId: String? = null,
    val journalPostId: String? = null,
    val dokumentProdApp: String? = null,
    val bestillendeFagsystem: String? = null,
    val batchId: String? = null,
    val adresse: Adresse? = null,
)

data class Adresse(
    val adresselinje1: String? = null,
    val adresselinje2: String? = null,
    val adresselinje3: String? = null,
    val adressetype: String? = null,
    val land: String? = null,
    val postnummer: String? = null,
    val poststed: String? = null
)
