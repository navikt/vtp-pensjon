package no.nav.pensjon.vtp.configuration

import no.nav.pensjon.vtp.snitch.HandlerToResponseHeaderInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfiguration : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(HandlerToResponseHeaderInterceptor())
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH")
    }
}
