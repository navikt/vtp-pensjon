package no.nav.pensjon.vtp.mocks.medl.medl2.rest.api.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import no.nav.pensjon.vtp.core.annotations.JaxrsResource;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Set;

import static no.nav.pensjon.vtp.mocks.medl.medl2.rest.api.v1.MedlemskapsunntakApiParams.*;

@JaxrsResource
@Path("medl2/api/v1/medlemskapsunntak")
@Produces(MediaType.APPLICATION_JSON)
@Api(tags = {"Medlemskapsunntak"})
public class MedlemskapsunntakMock {
    @GET
    @Path("/{unntakId}")
    @ApiOperation(API_OPERATION_MEDLEMSKAPSUNNTAK)
    public Object hentMedlemskapsunntak(
            @ApiParam(API_PARAM_INKLUDER_SPORINGSINFO) @QueryParam("inkluderSporingsinfo") Boolean inkluderSporing,
            @ApiParam(value = API_PARAM_UNNTAK_ID, required = true) @NotNull @PathParam("unntakId") Long unntakId) {
        return null;
    }

    @GET
    @ApiOperation(API_OPERATION_MEDLEMSKAPSUNNTAK_I_PERIODE)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Object> hentMedlemskapsunntakIPeriode(
            @ApiParam(API_PARAM_STATUSER) @QueryParam("statuser") Set<String> statuser,
            @ApiParam(API_PARAM_INKLUDER_SPORINGSINFO_PERSON) @QueryParam("inkluderSporingsinfo") boolean sporing) {

        return List.of();
    }
}

