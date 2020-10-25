package no.nav.pensjon.vtp.mocks.dummy;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import no.nav.pensjon.vtp.core.annotations.JaxrsResource;

@JaxrsResource
@Api(tags = {"Utilities"})
@Path("/dummy/boolean")
public class DummyRestTjenesteBoolean {
    private static final Logger LOG = LoggerFactory.getLogger(DummyRestTjenesteBoolean.class);

    @GET
    @Path("/true/{var:.+}")
    @ApiOperation(value = "get", notes = ("Returnerer 200 response boolean true"))
    public Response getTrue() {
        LOG.info("Fikk en forespørsel på DummyRestTjenesteTrue");
        return Response.ok().entity(true).build();
    }

    @GET
    @Path("/false/{var:.+}")
    @ApiOperation(value = "get", notes = ("Returnerer 200 response boolean false"))
    public Response getFalse() {
        LOG.info("Fikk en forespørsel på DummyRestTjenesteFalse");
        return Response.ok().entity(false).build();
    }
}