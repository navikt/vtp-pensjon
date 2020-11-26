package no.nav.pensjon.vtp.mocks.psak

import io.swagger.annotations.Api
import no.nav.pensjon.vtp.testmodell.brev.BrevMetadataIndeks
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(tags = ["Brev"])
@RequestMapping("/rest/api/brevdata/allBrev")
class BrevMetadataMockService(private val brevMetadataIndeks: BrevMetadataIndeks) {

    @GetMapping
    fun getBrevMetadata() = brevMetadataIndeks.brevMetadata
}