package no.nav.pensjon.vtp.mocks.psak

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import no.nav.pensjon.vtp.testmodell.brev.BrevMetadata
import no.nav.pensjon.vtp.testmodell.brev.BrevMetadataIndeks
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(tags = ["Brev"])
@RequestMapping("/rest/api/brevdata")
class BrevMetadataMockService(private val brevMetadataIndeks: BrevMetadataIndeks) {

    @GetMapping("allBrev", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiOperation(value = "Brevdata", notes = "Mock impl av brevmetadata.")
    fun getBrevMetadata(@RequestParam(value = "includeXsd", defaultValue = "false") includeXsd: Boolean) : List<BrevMetadata> {
        return brevMetadataIndeks.getAll()
    }
}