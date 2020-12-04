package no.nav.pensjon.vtp.application.configuration

import no.nav.pensjon.vtp.utilities.HandlerToResponseHeaderInterceptor
import org.springframework.boot.web.server.ErrorPage
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class WebMvcConfiguration : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(HandlerToResponseHeaderInterceptor())
    }

    // Typical Single-Page App configuration: handle not found (404) by returning the index.html page.
    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/notFound").setStatusCode(HttpStatus.OK).setViewName("forward:/index.html")
    }
    @Bean
    fun containerCustomizer(): WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
        return WebServerFactoryCustomizer { container ->
            container.addErrorPages(ErrorPage(HttpStatus.NOT_FOUND,
                    "/notFound"))
        }
    }
}
