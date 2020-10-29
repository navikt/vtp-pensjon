package no.nav.pensjon.vtp.application.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import ch.qos.logback.access.servlet.TeeFilter;
import ch.qos.logback.access.tomcat.LogbackValve;

@Component
public class TomcatAccessLogCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    @Value("${verbose}")
    public boolean verbose;
 
    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        final LogbackValve logbackValve = new LogbackValve();
        logbackValve.setName("Logback Access");
        if (verbose) {
            logbackValve.setFilename("logback-access-verbose.xml");
        } else {
            logbackValve.setFilename("logback-access.xml");
        }
        factory.addEngineValves(logbackValve);
    }

    @ConditionalOnProperty("verbose")
    @Bean
    public FilterRegistrationBean<TeeFilter> requestLoggingFilter() {
        return new FilterRegistrationBean<>(new TeeFilter());
    }
}
