package no.nav.pensjon.vtp.application.configuration;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

import ch.qos.logback.access.tomcat.LogbackValve;

@Component
public class TomcatAccessLogCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
 
    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        LogbackValve logbackValve = new LogbackValve();
        logbackValve.setName("Logback Access");
        logbackValve.setFilename("logback-access.xml");
        factory.addEngineValves(logbackValve);
    }
}