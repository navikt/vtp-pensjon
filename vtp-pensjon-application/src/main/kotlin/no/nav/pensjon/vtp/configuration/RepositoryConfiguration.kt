package no.nav.pensjon.vtp.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import no.nav.pensjon.vtp.mocks.tss.SamhandlerRepository
import no.nav.pensjon.vtp.testmodell.ansatt.AnsatteIndeks
import no.nav.pensjon.vtp.testmodell.brev.BrevMetadataIndeks
import no.nav.pensjon.vtp.testmodell.enheter.EnheterIndeks
import no.nav.pensjon.vtp.testmodell.pensjon_testdata.PensjonTestdataService
import no.nav.pensjon.vtp.testmodell.pensjon_testdata.PensjonTestdataServiceImpl
import no.nav.pensjon.vtp.testmodell.pensjon_testdata.PensjonTestdataServiceNull
import no.nav.pensjon.vtp.testmodell.personopplysning.AdresseIndeks
import no.nav.pensjon.vtp.testmodell.repo.TestscenarioTemplateRepository
import no.nav.pensjon.vtp.testmodell.repo.impl.BasisdataProviderFileImpl
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioTemplateLoader
import no.nav.pensjon.vtp.testmodell.repo.impl.TestscenarioTemplateRepositoryImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RepositoryConfiguration(
    private val objectMapper: ObjectMapper
) {
    @Bean
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
    fun pensjonTestdataService(@Value("\${pensjon.testdata.url}") pensjonTestdataUrl: String?): PensjonTestdataService =
        when {
            pensjonTestdataUrl.isNullOrBlank() -> PensjonTestdataServiceNull()
            else -> PensjonTestdataServiceImpl(pensjonTestdataUrl)
        }

    @Bean
    fun enheterIndeks(): EnheterIndeks {
        return BasisdataProviderFileImpl.loadEnheter()
    }

    @Bean
    fun brevMetadataIndeks(): BrevMetadataIndeks {
        return BasisdataProviderFileImpl.loadBrevMetadata()
    }

    @Bean
    fun samhandlerRepository() = SamhandlerRepository(readResource("/basedata/tss.json"))

    private inline fun <reified T> readResource(name: String) =
        objectMapper.readValue<T>(getResource(name))

    private fun getResource(name: String) =
        javaClass.getResource(name) ?: throw RuntimeException("Missing resource '$name'")
}
