package no.nav.pensjon.vtp.application.configuration;

import javax.annotation.PostConstruct;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import no.nav.pensjon.vtp.core.annotations.JaxrsResource;

@Component
public class JerseyConfig extends ResourceConfig {
    private final ApplicationContext applicationContext;

    public JerseyConfig(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void registerJaxrsResources() {
        applicationContext.getBeansWithAnnotation(JaxrsResource.class)
                .forEach((name, bean) -> register(bean));
    }
}
