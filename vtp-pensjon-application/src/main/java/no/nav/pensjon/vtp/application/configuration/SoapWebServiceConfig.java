package no.nav.pensjon.vtp.application.configuration;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

import javax.annotation.PostConstruct;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import no.nav.pensjon.vtp.core.annotations.SoapService;

@Configuration
public class SoapWebServiceConfig {
    private final ApplicationContext applicationContext;
    private final Bus bus;

    public SoapWebServiceConfig(ApplicationContext applicationContext, Bus bus) {
        this.applicationContext = applicationContext;
        this.bus = bus;
    }

    @PostConstruct
    public void registerSoapServices() {
        // TODO: This SOAP Service is missing
        //publishWebService(new SecurityTokenServiceMockImpl(), "/SecurityTokenServiceProvider/");

        applicationContext.getBeansWithAnnotation(SoapService.class)
                .forEach((name, service) ->
                        stream(ofNullable(service.getClass().getAnnotation(SoapService.class))
                                .map(SoapService::path)
                                .orElseThrow(() -> new RuntimeException("Missing @" + SoapService.class.getSimpleName() + " on class " + service.getClass().getSimpleName())))
                                .forEach(path -> new EndpointImpl(bus, service).publish(path))
                );
    }
}
