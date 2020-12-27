package no.nav.pensjon.vtp.mocks.virksomhet.dokumentproduksjon.v2

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDType0Font
import java.io.IOException

class PdfGeneratorUtil

private const val FONT_FILE = "fonts/OpenSans-Regular.ttf"
const val FONT_SIZE = 8
fun genererPdfByteArrayFraString(brev: String): ByteArray {
    try {
        PdfGeneratorUtil::class.java.classLoader.getResourceAsStream(FONT_FILE).use { fontInputStream ->
            val doc = PDDocument()
            val font: PDFont = PDType0Font.load(doc, fontInputStream)
            return DocContext(doc, font, FONT_SIZE)
                .renderLines(
                    brev.replace("\t", "  ").split(System.getProperty("line.separator").toRegex()).toTypedArray()
                )
                .saveAndClosePdf()
        }
    } catch (e: IOException) {
        throw IllegalStateException("Kunne ikke generere PDF", e)
    }
}
