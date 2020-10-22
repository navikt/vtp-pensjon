package no.nav.pensjon.vtp.mocks.enonic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/enonic")
public class EnonicMock {
    private static final Logger logger = LoggerFactory.getLogger(EnonicMock.class);

    @GET
    @Path("{var:.+}")
    public Response get(@PathParam("var") String path) {
        logger.info("Fikk en forespørsel på {}", path);
        return Response.ok().build();
    }
}