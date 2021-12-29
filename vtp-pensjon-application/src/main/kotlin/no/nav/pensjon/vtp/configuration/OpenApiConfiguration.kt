package no.nav.pensjon.vtp.configuration

import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfiguration {
    @Bean
    fun publicApi(): GroupedOpenApi? {
        return GroupedOpenApi.builder()
            .group("vtp-pensjon")
            .pathsToMatch("/**")
            .build()
    }

    @Bean
    fun vtpPensjonOpenApi(): OpenAPI? {
        return OpenAPI()
            .info(
                Info().title("VTP Pensjon")
            )
            .externalDocs(
                ExternalDocumentation()
                    .description("SOAP-tjenester")
                    .url("http://localhost:8060/soap")
            )
    }
}
