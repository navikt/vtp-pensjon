package no.nav.pensjon.vtp.application.configuration

import no.nav.pensjon.vtp.testmodell.organisasjon.OrganisasjonRepository
import no.nav.pensjon.vtp.testmodell.repo.impl.BasisdataProviderFileImpl.loadOrganisasjoner
import org.springframework.stereotype.Component
import java.io.IOException
import javax.annotation.PostConstruct

@Component
class DataLoader(private val organisasjonRepository: OrganisasjonRepository) {
    @PostConstruct
    @Throws(IOException::class)
    fun loadData() {
        loadOrganisasjoner(organisasjonRepository)
    }
}
