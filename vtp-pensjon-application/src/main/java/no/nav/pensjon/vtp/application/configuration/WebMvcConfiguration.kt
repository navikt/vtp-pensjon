package no.nav.pensjon.vtp.application.configuration

import no.nav.pensjon.vtp.utilities.HandlerToResponseHeaderInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class WebMvcConfiguration : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(HandlerToResponseHeaderInterceptor())
    }
}
