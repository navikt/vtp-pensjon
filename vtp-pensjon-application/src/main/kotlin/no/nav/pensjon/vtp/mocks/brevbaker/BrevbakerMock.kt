package no.nav.pensjon.vtp.mocks.brevbaker

import io.swagger.v3.oas.annotations.tags.Tag
import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.maler.Brevkode
import org.springframework.web.bind.annotation.*
import java.util.*

val mockBrevMetadata = LetterMetadata(
    displayTitle = "Test brev fra vtp-pensjon",
    isSensitiv = false,
    distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
    brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
)

@RestController
@Tag(name = "Brevbaker")
@RequestMapping("rest/brevbaker")
class BrevbakerMock {

    @PostMapping("/letter/autobrev")
    fun genererPDF(
        @RequestBody letterRequest: AutobrevRequest,
    ): LetterResponse =
        LetterResponse(
            base64pdf = Base64.getEncoder()
                .encodeToString(javaClass.getResource("/brevbaker-response.pdf")!!.readBytes()),
            letterMetadata = mockBrevMetadata
        )

    @GetMapping("/templates/autobrev/{kode}")
    fun getTemplateDescription(@PathVariable("kode") templateKode: String): TemplateDescription =
        TemplateDescription(
            name = templateKode,
            letterDataClass = "OmsorgEgenAutoDto",
            languages = listOf(LanguageCode.BOKMAL, LanguageCode.NYNORSK, LanguageCode.ENGLISH),
            metadata = mockBrevMetadata
        )

    @GetMapping("/templates/autobrev")
    fun getTemplates(): List<String> =
        Brevkode.AutoBrev.values().map { it.name }

    @GetMapping("/isAlive")
    fun ping() = "Brevbaker kjører i vtp-pensjon"

    @GetMapping("/ping_authorized")
    fun pingAuth() = "Autorisert i vtp-pensjon"
}
