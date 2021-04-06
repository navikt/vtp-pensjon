package no.nav.pensjon.vtp.mocks.virksomhet.dokumentproduksjon.v2

import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDFont

internal class PageContext(mediabox: PDRectangle, val content: PDPageContentStream, margin: Int, private val fontSize: Int) {
    private val textRenderingLineStartX: Int
    val textRenderingLineEndX: Int
    val textRenderingLineEndY: Int
    var textRenderingLineCurrentY: Int

    fun close() {
        content.close()
    }

    fun renderLine(info: String?, font: PDFont?, fontHeight: Int) {
        textRenderingLineCurrentY -= fontHeight
        content.beginText()
        content.setFont(font, fontSize.toFloat())
        content.newLineAtOffset(textRenderingLineStartX.toFloat(), textRenderingLineCurrentY.toFloat())
        content.showText(info)
        content.endText()
    }

    init {
        val textRenderingLineStartY = mediabox.upperRightY.toInt() - margin
        textRenderingLineEndY = mediabox.lowerLeftX.toInt() + 2 * margin
        textRenderingLineStartX = mediabox.lowerLeftX.toInt() + 2 * margin
        textRenderingLineEndX = mediabox.width.toInt() - textRenderingLineStartX - 2 * margin
        textRenderingLineCurrentY = textRenderingLineStartY
    }
}
