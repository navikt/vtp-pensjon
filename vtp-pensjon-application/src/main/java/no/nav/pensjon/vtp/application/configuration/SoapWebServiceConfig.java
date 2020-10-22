package no.nav.pensjon.vtp.application.configuration;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

import javax.annotation.PostConstruct;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import no.nav.pensjon.vtp.core.annotations.SoapService;

@Configuration
public class SoapWebServiceConfig implements ApplicationContextAware {
    private final Bus bus;
    private ApplicationContext applicationContext;

    public SoapWebServiceConfig(Bus bus) {
        this.bus = bus;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
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
