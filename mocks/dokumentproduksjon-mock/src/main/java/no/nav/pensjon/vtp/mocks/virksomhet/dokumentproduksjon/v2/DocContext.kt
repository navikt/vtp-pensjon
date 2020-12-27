package no.nav.pensjon.vtp.mocks.virksomhet.dokumentproduksjon.v2

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDFont
import java.awt.Color
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*
import kotlin.math.roundToInt

private fun newPage(doc: PDDocument, fontHeight: Int, fontSize: Int): PageContext {
    val page = PDPage()
    doc.addPage(page)
    val content = PDPageContentStream(doc, page)
    content.setNonStrokingColor(Color.BLACK)
    return PageContext(page.mediaBox, content, 2 * fontHeight, fontSize)
}

private fun fontHeight(font: PDFont, fontSize: Int): Int {
    val tempFontHeightDouble = (font.fontDescriptor.fontBoundingBox.height / 1000 * fontSize).toDouble()
    return tempFontHeightDouble.roundToInt()
}


internal class DocContext(private val doc: PDDocument, private val font: PDFont, private val fontSize: Int) {
    private val fontHeight: Int = fontHeight(font, fontSize)
    private var currentPage: PageContext

    init {
        currentPage = newPage(doc, fontHeight, fontSize)
    }

    private fun nextPage() {
        currentPage.close()
        currentPage = newPage(doc, fontHeight, fontSize)
    }

    private fun renderLine(info: String) {
        if (currentPage.textRenderingLineCurrentY < currentPage.textRenderingLineEndY) {
            nextPage()
        }
        currentPage.renderLine(info, font, fontHeight)
    }

    fun saveAndClosePdf(): ByteArray {
        currentPage.close()
        val byteArrayOutputStream = ByteArrayOutputStream()
        doc.save(byteArrayOutputStream)
        doc.close()
        return byteArrayOutputStream.toByteArray()
    }

    fun renderLines(pdfcontent: Array<String>): DocContext {
        for (strTemp in pdfcontent) {
            val lines = autowrappingLongLines(strTemp)
            for (i in lines.indices) {
                val line = lines[i]
                val charSpacing = setCharacterSpacingForLinje(line, i == lines.size - 1)
                currentPage.content.setCharacterSpacing(charSpacing)
                renderLine(line)
            }
        }
        return this
    }

    private fun autowrappingLongLines(inputText: String): List<String> {
        var text = inputText
        val lines: MutableList<String> = ArrayList()
        var lastSpace = -1
        while (text.isNotEmpty()) {
            var indexOfNextSpace = text.indexOf(' ', lastSpace + 1)
            if (indexOfNextSpace < 0) indexOfNextSpace = text.length
            var subString = text.substring(0, indexOfNextSpace)
            val sizeOfSubstring = fontSize * font.getStringWidth(subString) / 1000
            when {
                sizeOfSubstring > currentPage.textRenderingLineEndX -> {
                    if (lastSpace < 0) lastSpace = indexOfNextSpace
                    subString = text.substring(0, lastSpace)
                    lines.add(subString)
                    text = text.substring(lastSpace).trim { it <= ' ' }
                    lastSpace = -1
                }
                indexOfNextSpace == text.length -> {
                    lines.add(text)
                    text = ""
                }
                else -> {
                    lastSpace = indexOfNextSpace
                }
            }
        }
        return lines
    }

    private fun setCharacterSpacingForLinje(linje: String, setCharSpacingToZero: Boolean): Float {
        var charSpacing = 0f
        if (linje.isNotEmpty() && !setCharSpacingToZero) {
            val breddeTilLinje = fontSize * font.getStringWidth(linje) / 1000
            val plassTilOvers = currentPage.textRenderingLineEndX - breddeTilLinje
            if (plassTilOvers > 0) {
                charSpacing = plassTilOvers / (linje.length - 1)
            }
        }
        return charSpacing
    }
}
