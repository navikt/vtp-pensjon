package no.nav.pensjon.vtp.miscellaneous.rest;

import static javax.ws.rs.client.ClientBuilder.newClient;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;

import no.nav.pensjon.vtp.core.annotations.JaxrsResource;

/**
 * Mock of https://github.com/navikt/peproxy
 */
@JaxrsResource
@Path("/peproxy")
public class PeproxyResource {
    private final Client client = newClient();

    @GET
    public String proxy(@HeaderParam("target") String target) {
        return client.target(target).request().get(String.class);
    }
}
