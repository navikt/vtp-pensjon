package no.nav.oppgave;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(tags = "Oppgave Mock")
@Path("oppgave/api/v1/internal/alive")
public class OppgaveInternalAliveMockImpl {
    @GET
    public Response isAlive() {
        return Response.ok().build();
    }
}
