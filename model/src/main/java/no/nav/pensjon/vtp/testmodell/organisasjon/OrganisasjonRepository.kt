package no.nav.pensjon.vtp.testmodell.organisasjon

import org.springframework.data.repository.Repository

interface OrganisasjonRepository : Repository<OrganisasjonModell, String> {
    fun findById(orgnr: String): OrganisasjonModell?
    fun saveAll(modeller: Iterable<OrganisasjonModell>)
}
