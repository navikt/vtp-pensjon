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
)

@RestController
@Tag(name = "Brevbaker")
@RequestMapping("rest/brevbaker")
class BrevbakerMock {

    @PostMapping("/letter/vedtak")
    fun genererPDF(
        @RequestBody letterRequest: VedtaksbrevRequest,
    ): LetterResponse =
        LetterResponse(
            base64pdf = Base64.getEncoder()
                .encodeToString(javaClass.getResource("/brevbaker-response.pdf")!!.readBytes()),
            letterMetadata = mockBrevMetadata
        )

    @GetMapping("/templates/vedtaksbrev/{kode}")
    fun getTemplateDescription(@PathVariable("kode") templateKode: String): TemplateDescription =
        TemplateDescription(
            name = templateKode,
            letterDataClass = "OmsorgEgenAutoDto",
            languages = listOf(LanguageCode.BOKMAL, LanguageCode.NYNORSK, LanguageCode.ENGLISH),
            metadata = mockBrevMetadata
        )

    @GetMapping("/templates/vedtaksbrev")
    fun getTemplates(): List<String> =
        Brevkode.Vedtak.values().map { it.name }

    @GetMapping("/isAlive")
    fun ping() = "Brevbaker kjører i vtp-pensjon"

    @GetMapping("/ping_authorized")
    fun pingAuth() = "Autorisert i vtp-pensjon"
}
