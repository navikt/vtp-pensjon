package no.nav.medl2.rest.api.v1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("medl2/api/ping")
public class MedlemskapPingMock {
    @GET
    public Response ping() {
        return Response.ok("pong").build();
    }
}

