package no.nav.pensjon.vtp.application.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableOpenApi
public class SpringFoxConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30).pathMapping("/rest")
                .apiInfo(new ApiInfoBuilder()
                        .title("VTP-pensjon REST-API Documentasjon")
                        .description("Oversikt over støttede SOAP-tjenester finnes her: http://localhost:8060/soap")
                        .build())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}
