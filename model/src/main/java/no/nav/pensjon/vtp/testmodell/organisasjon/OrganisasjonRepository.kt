package no.nav.pensjon.vtp.testmodell.organisasjon

import org.springframework.data.repository.Repository
import java.util.*

interface OrganisasjonRepository : Repository<OrganisasjonModell, String> {
    fun findById(orgnr: String): Optional<OrganisasjonModell?>?
    fun saveAll(modeller: Iterable<OrganisasjonModell?>?)
}
