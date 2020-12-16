package no.nav.pensjon.vtp.mocks.virksomhet.dokumentproduksjon.v2

import org.apache.pdfbox.pdmodel.PDDocument
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.io.*

private const val INPUT_XML: String = "brevxml.txt"
private const val INPUT_TEXT: String = "paragraph.txt"
private const val OUTPUT_PDF: String = "statiskBrev.pdf"

class PdfGeneratorUtilTest {
    private val renderer: PdfGeneratorUtil =
        PdfGeneratorUtil()

    @Test
    fun PdfGeneratorXmlTest() {
        getResourceAsStream(INPUT_XML).use { inputStream ->
            val inputString = hentStringFraInputStream(inputStream)
            renderer.genererPdfByteArrayFraString(inputString)
            PDDocument.load(FileInputStream(OUTPUT_PDF)).use { doc ->
                val numberOfPages = doc.numberOfPages
                Assertions.assertThat(numberOfPages).isEqualTo(2)
            }
        }
    }

    private fun getResourceAsStream(name: String): InputStream {
        return PdfGeneratorUtilTest::class.java.classLoader
            .getResourceAsStream(name)
            ?: throw FileNotFoundException("Unable to locate '$name' in classpath")
    }

    @Test
    fun PdfGeneratorLongParagraphTest() {
        getResourceAsStream(INPUT_TEXT).use { inputStream ->
            val inputString = hentStringFraInputStream(inputStream)
            renderer.genererPdfByteArrayFraString(inputString)
            PDDocument.load(FileInputStream(OUTPUT_PDF)).use { doc ->
                val numberOfPages = doc.numberOfPages
                Assertions.assertThat(numberOfPages).isGreaterThan(1)
            }
        }
    }

    private fun hentStringFraInputStream(inputStream: InputStream): String {
        val resultStringBuilder = StringBuilder()
        BufferedReader(InputStreamReader(inputStream)).use { br ->
            var line: String?
            while (br.readLine().also { line = it } != null) {
                resultStringBuilder.append(line).append(System.getProperty("line.separator"))
            }
        }
        return resultStringBuilder.toString()
    }
}
