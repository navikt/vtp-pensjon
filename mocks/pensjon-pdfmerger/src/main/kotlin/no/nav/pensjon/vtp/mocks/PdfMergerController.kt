package no.nav.pensjon.vtp.mocks

import org.springframework.http.MediaType.APPLICATION_PDF_VALUE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/pdfmerger")
class PdfMergerController {
    @GetMapping("/ping")
    fun ping() = "PONG"

    @PostMapping("/merge", produces = [APPLICATION_PDF_VALUE])
    fun merge() = javaClass.getResource("/merged.pdf").readBytes()
}
