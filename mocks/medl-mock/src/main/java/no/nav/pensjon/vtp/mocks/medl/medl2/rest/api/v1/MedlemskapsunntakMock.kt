package no.nav.pensjon.vtp.mocks.medl.medl2.rest.api.v1

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.NotNull

@RestController
@RequestMapping(value = ["rest/medl2/api/v1/medlemskapsunntak"])
@Api(tags = ["Medlemskapsunntak"])
class MedlemskapsunntakMock {
    @GetMapping(value = ["/{unntakId}"])
    @ApiOperation("Henter ut informasjon om et spesifikt medlemskapsunntak.")
    fun hentMedlemskapsunntak(
        @ApiParam("Hvorvidt man er interessert i å se sporingsinformasjonen som er registrert på perioden.") @RequestParam(
            "inkluderSporingsinfo"
        ) inkluderSporing: Boolean?,
        @ApiParam(
            value = "Den funksjonelle ID'en til perioden man ønsker å hente.",
            required = true
        ) @PathVariable("unntakId") unntakId: @NotNull Long?
    ) = null

    @GetMapping
    @ApiOperation("Henter ut medlemskapsunntak for en spesifikk person.")
    fun hentMedlemskapsunntakIPeriode(
        @ApiParam("Dersom man ikke ønsker å hente perioder uansett status kan man spesifisere hvilke man er interessert i her.") @RequestParam(
            "statuser"
        ) statuser: Set<String?>?,
        @ApiParam("Hvorvidt man er interessert i å se sporingsinformasjonen som er registrert på periodene som hentes ut.") @RequestParam(
            "inkluderSporingsinfo"
        ) sporing: Boolean
    ): List<Any> = emptyList()
}
