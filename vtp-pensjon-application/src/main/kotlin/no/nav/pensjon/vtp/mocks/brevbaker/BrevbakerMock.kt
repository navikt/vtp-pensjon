package no.nav.pensjon.vtp.mocks.brevbaker

import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.LetterRequest
import no.nav.pensjon.brev.api.model.LetterResponse
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@Tag(name = "Brevbaker")
@RequestMapping("rest/brevbaker")
class BrevbakerMock {

    @PostMapping("/letter")
    fun genererPDF(
        @RequestBody letterRequest: LetterRequest,
    ): LetterResponse =
        LetterResponse(
            base64pdf = Base64.getEncoder().encodeToString(javaClass.getResource("/brevbaker-response.pdf")!!.readBytes()),
            letterMetadata = LetterMetadata(letterRequest.template, false)
        )

    @GetMapping("/isAlive")
    fun ping() = "Brevbaker kjører i vtp-pensjon"
}
