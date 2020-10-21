package no.nav.pensjon.vtp.application.configuration;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import no.nav.medl2.rest.api.v1.MedlemskapPingMock;
import no.nav.pensjon.vtp.auth.LoginService;
import no.nav.pensjon.vtp.auth.Oauth2RestService;

@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(LoginService.class);
        register(MedlemskapPingMock.class);
        register(Oauth2RestService.class);
    }
}
