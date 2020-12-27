package no.nav.pensjon.vtp.mocks.virksomhet.dokumentproduksjon.v2

import no.nav.pensjon.vtp.mocks.virksomhet.dokumentproduksjon.v2.PdfGeneratorUtil.genererPdfByteArrayFraString
import org.apache.pdfbox.pdmodel.PDDocument
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.io.*

private const val INPUT_XML: String = "brev.xml"
private const val INPUT_TEXT: String = "paragraph.txt"

class PdfGeneratorUtilTest {
    @Test
    fun PdfGeneratorXmlTest() {
        getResourceAsStream(INPUT_XML).use { inputStream ->
            val inputString = hentStringFraInputStream(inputStream)

            val pdfDocument = genererPdfByteArrayFraString(inputString)

            PDDocument.load(ByteArrayInputStream(pdfDocument)).use { doc ->
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
            val pdfDocument = genererPdfByteArrayFraString(inputString)
            PDDocument.load(ByteArrayInputStream(pdfDocument)).use { doc ->
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
