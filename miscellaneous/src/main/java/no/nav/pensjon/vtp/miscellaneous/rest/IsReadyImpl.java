package no.nav.pensjon.vtp.miscellaneous.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import no.nav.pensjon.vtp.core.annotations.JaxrsResource;

@JaxrsResource
@Api(tags = { "isReady" })
@Path("/isReady")
public class IsReadyImpl {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "isReady", notes = ("Sjekker om systemet er ready for NAIS"))
    public String buildPermitResponse() {
        return "OK";
    }
}
