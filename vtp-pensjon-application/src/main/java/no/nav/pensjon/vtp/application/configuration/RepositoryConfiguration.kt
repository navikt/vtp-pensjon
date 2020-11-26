package no.nav.pensjon.vtp.application.configuration

import no.nav.pensjon.vtp.testmodell.ansatt.AnsatteIndeks
import no.nav.pensjon.vtp.testmodell.brev.BrevMetadataIndeks
import no.nav.pensjon.vtp.testmodell.enheter.EnheterIndeks
import no.nav.pensjon.vtp.testmodell.pensjon_testdata.PensjonTestdataService
import no.nav.pensjon.vtp.testmodell.pensjon_testdata.PensjonTestdataServiceImpl
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseIndeks
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplateRepository
import no.nav.pensjon.vtp.testmodell.repo.impl.BasisdataProviderFileImpl
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioTemplateLoader
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioTemplateRepositoryImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.IOException

@Configuration
class RepositoryConfiguration {
    @Bean
    @Throws(IOException::class)
    fun adresseIndeks(): AdresseIndeks {
        return BasisdataProviderFileImpl.loadAdresser()
    }

    @Bean
    fun ansatteIndeks(): AnsatteIndeks {
        return BasisdataProviderFileImpl.loadAnsatte()
    }

    @Bean
    fun testscenarioTemplateRepository(): TestscenarioTemplateRepository {
        return TestscenarioTemplateRepositoryImpl(TestscenarioTemplateLoader().load())
    }

    @Bean
    fun pensjonTestdataService(@Value("\${pensjon.testdata.url}") pensjonTestdataUrl: String): PensjonTestdataService {
        return PensjonTestdataServiceImpl(pensjonTestdataUrl)
    }

    @Bean
    @Throws(IOException::class)
    fun enheterIndeks(): EnheterIndeks {
        return BasisdataProviderFileImpl.loadEnheter()
    }

    @Bean
    fun brevMetadataIndeks(): BrevMetadataIndeks {
        return BasisdataProviderFileImpl.loadBrevMetadata()
    }
}
