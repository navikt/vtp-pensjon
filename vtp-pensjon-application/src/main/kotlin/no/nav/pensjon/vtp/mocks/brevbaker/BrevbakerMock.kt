package no.nav.pensjon.vtp.mocks.brevbaker

import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.pensjon.brev.api.model.*
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

    @GetMapping("/templates/{name}")
    fun getTemplateDescription(@PathVariable("name") templateName: String): TemplateDescription =
        TemplateDescription(
            name = templateName,
            base = "PensjonLatex",
            letterDataClass = "OmsorgEgenAutoDto",
            languages = listOf(LanguageCode.BOKMAL, LanguageCode.NYNORSK, LanguageCode.ENGLISH)
        )

    @GetMapping("/isAlive")
    fun ping() = "Brevbaker kjører i vtp-pensjon"
}
