package no.nav.pensjon.vtp.application.configuration

import ch.qos.logback.access.servlet.TeeFilter
import ch.qos.logback.access.tomcat.LogbackValve
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class TomcatAccessLogCustomizer : WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    @Value("\${verbose}")
    var verbose = false
    override fun customize(factory: TomcatServletWebServerFactory) {
        val logbackValve = LogbackValve()
        logbackValve.name = "Logback Access"
        if (verbose) {
            logbackValve.filename = "logback-access-verbose.xml"
        } else {
            logbackValve.filename = "logback-access.xml"
        }
        factory.addEngineValves(logbackValve)
    }

    @ConditionalOnProperty("verbose")
    @Bean
    fun requestLoggingFilter(): FilterRegistrationBean<TeeFilter> {
        return FilterRegistrationBean(TeeFilter())
    }
}
