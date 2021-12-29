package no.nav.pensjon.vtp.mocks.psak

import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.pensjon.vtp.testmodell.brev.BrevMetadataIndeks
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Brev")
@RequestMapping("/rest/api/brevdata/allBrev")
class BrevMetadataMockService(private val brevMetadataIndeks: BrevMetadataIndeks) {

    @GetMapping
    fun getBrevMetadata() = brevMetadataIndeks.brevMetadata
}
