package no.nav.pensjon.vtp.mocks.medl.medl2.rest.api.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

import static no.nav.pensjon.vtp.mocks.medl.medl2.rest.api.v1.MedlemskapsunntakApiParams.*;

@RestController
@RequestMapping(value = "medl2/api/v1/medlemskapsunntak", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = {"Medlemskapsunntak"})
public class MedlemskapsunntakMock {
    @GetMapping(value = "/{unntakId}")
    @ApiOperation(API_OPERATION_MEDLEMSKAPSUNNTAK)
    public Object hentMedlemskapsunntak(
            @ApiParam(API_PARAM_INKLUDER_SPORINGSINFO) @RequestParam("inkluderSporingsinfo") Boolean inkluderSporing,
            @ApiParam(value = API_PARAM_UNNTAK_ID, required = true) @NotNull @PathVariable("unntakId") Long unntakId) {
        return null;
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(API_OPERATION_MEDLEMSKAPSUNNTAK_I_PERIODE)
    public List<Object> hentMedlemskapsunntakIPeriode(
            @ApiParam(API_PARAM_STATUSER) @RequestParam("statuser") Set<String> statuser,
            @ApiParam(API_PARAM_INKLUDER_SPORINGSINFO_PERSON) @RequestParam("inkluderSporingsinfo") boolean sporing) {

        return List.of();
    }
}

