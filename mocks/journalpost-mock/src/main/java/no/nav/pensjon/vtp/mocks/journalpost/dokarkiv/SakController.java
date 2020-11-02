package no.nav.pensjon.vtp.mocks.journalpost.dokarkiv;

import io.swagger.annotations.*;
import no.nav.dokarkiv.generated.model.Sak;
import no.nav.dokarkiv.generated.model.SakJson;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@RestController
@Api(tags = {"Dokarkiv"})
@RequestMapping("/api/v1")
public class SakController {

    @ApiOperation(value = "Finner saker for angitte søkekriterier", nickname = "finnSakerUsingGET", notes = "", response = SakJson.class, responseContainer = "List", authorizations = {
            @Authorization(value = "Authorization"),
            @Authorization(value = "Basic"),
            @Authorization(value = "Saml")
    }, tags={ "sak-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = SakJson.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Ugyldig input"),
            @ApiResponse(code = 401, message = "Konsument mangler gyldig token"),
            @ApiResponse(code = 500, message = "Ukjent feilsituasjon har oppstått i Sak"),
            @ApiResponse(code = 503, message = "En eller flere tjenester som sak er avhengig av er ikke tilgjengelige eller svarer ikke.") })
    @RequestMapping(value = "/saker",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET)
    ResponseEntity<List<Sak>> finnSakerUsingGET(@ApiParam(value = "") @RequestHeader(value="X-Correlation-ID", required = false) String xCorrelationID, @ApiParam(value = "Filtrering på saker opprettet for en aktør (person)") @Valid @RequestParam(value = "aktoerId") String aktoerId, @ApiParam(value = "Filtrering på applikasjon (iht felles kodeverk)") @Valid @RequestParam(value = "applikasjon", required = false) String applikasjon, @ApiParam(value = "Filtrering på fagsakNr") @RequestParam(value = "fagsakNr", required = false) String fagsakNr, @ApiParam(value = "Filtrering på saker opprettet for en organisasjon") @RequestParam(value = "orgnr", required = false) String orgnr, @ApiParam(value = "Filtrering på tema (iht felles kodeverk)") @RequestParam(value = "tema", required = false) List<String> tema){
        return ResponseEntity.ok(Collections.emptyList());
    }

    @ApiOperation(value = "Henter sak for en gitt id", nickname = "hentSakUsingGET", notes = "", response = SakJson.class, authorizations = {
            @Authorization(value = "Authorization"),
            @Authorization(value = "Basic"),
            @Authorization(value = "Saml")
    }, tags={ "sak-controller", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = SakJson.class),
            @ApiResponse(code = 401, message = "Konsument mangler gyldig token"),
            @ApiResponse(code = 403, message = "Konsument har ikke tilgang til å gjennomføre handlingen"),
            @ApiResponse(code = 404, message = "Det finnes ingen sak for angitt id"),
            @ApiResponse(code = 500, message = "Ukjent feilsituasjon har oppstått i Sak"),
            @ApiResponse(code = 503, message = "En eller flere tjenester som sak er avhengig av er ikke tilgjengelige eller svarer ikke.") })
    @RequestMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET)
    ResponseEntity<Sak> hentSakUsingGET(@ApiParam(value = "" ,required=false) @RequestHeader(value="X-Correlation-ID", required=false) String xCorrelationID,@ApiParam(value = "id",required=true) @PathVariable("id") Long id){
        Sak dummySak = new Sak();
        dummySak.setFagsakId(id.toString());
        dummySak.setArkivsaksnummer(id.toString());
        dummySak.setArkivsaksystem(Sak.ArkivsaksystemEnum.PSAK);
        return ResponseEntity.ok(dummySak);
    }

}
