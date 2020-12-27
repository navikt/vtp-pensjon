package no.nav.pensjon.vtp.mocks.virksomhet.dokumentproduksjon.v2;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.getProperty;

public class PdfGeneratorUtil {
    private static final String FONT_FILE = "fonts/OpenSans-Regular.ttf";
    private static final int FONT_SIZE = 8;

    public static byte[] genererPdfByteArrayFraString(String brev) {
        try {
            try (InputStream fontInputStream = PdfGeneratorUtil.class.getClassLoader().getResourceAsStream(FONT_FILE)) {
                PDDocument doc = new PDDocument();
                PDFont font = PDType0Font.load(doc, fontInputStream);

                return new DocContext(doc, font)
                        .renderLines(brev.replace("\t", "  ").split(getProperty("line.separator")))
                        .saveAndClosePdf();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Kunne ikke generere PDF", e);
        }
    }

    static class DocContext {
        public final int fontHeight;
        final PDDocument doc;
        final PDFont font;

        PageContext currentPage;

        private DocContext(PDDocument doc, PDFont font) throws IOException {
            this.doc = doc;
            this.font = font;
            this.fontHeight = fontHeight(font);

            this.currentPage = newPage(doc, fontHeight);
        }

        private static PageContext newPage(PDDocument doc, int fontHeight) throws IOException {
            PDPage page = new PDPage();
            doc.addPage(page);
            PDPageContentStream content = new PDPageContentStream(doc, page);
            content.setNonStrokingColor(Color.BLACK);

            return new PageContext(page.getMediaBox(), content, 2 * fontHeight);
        }

        public void nextPage() throws IOException {
            currentPage.close();

            this.currentPage = newPage(doc, fontHeight);
        }

        private static int fontHeight(PDFont font) {
            double tempFontHeightDouble = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * FONT_SIZE;
            return (int) Math.round(tempFontHeightDouble);
        }


        private void renderLine(String info) throws IOException {
            if (currentPage.textRenderingLineCurrentY < currentPage.textRenderingLineEndY) {
                nextPage();
            }

            currentPage.renderLine(info, font, fontHeight);
        }

        public byte[] saveAndClosePdf() throws IOException {
            currentPage.close();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            doc.save(byteArrayOutputStream);
            doc.close();
            return byteArrayOutputStream.toByteArray();
        }

        public DocContext renderLines(String[] pdfcontent) throws IOException {
            for (String strTemp : pdfcontent) {
                List<String> lines = autowrappingLongLines(strTemp);

                for (int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i);

                    float charSpacing = setCharacterSpacingForLinje(line, i == (lines.size() - 1));

                    currentPage.content.setCharacterSpacing(charSpacing);
                    renderLine(line);
                }
            }
            return this;
        }

        private List<String> autowrappingLongLines(String text) throws IOException {
            List<String> lines = new ArrayList<>();
            int lastSpace = -1;
            while (text.length() > 0) {
                int indexOfNextSpace = text.indexOf(' ', lastSpace + 1);
                if (indexOfNextSpace < 0)
                    indexOfNextSpace = text.length();
                String subString = text.substring(0, indexOfNextSpace);
                float sizeOfSubstring = FONT_SIZE * font.getStringWidth(subString) / 1000;
                if (sizeOfSubstring > currentPage.textRenderingLineEndX) {
                    if (lastSpace < 0)
                        lastSpace = indexOfNextSpace;
                    subString = text.substring(0, lastSpace);
                    lines.add(subString);
                    text = text.substring(lastSpace).trim();
                    lastSpace = -1;
                } else if (indexOfNextSpace == text.length()) {
                    lines.add(text);
                    text = "";
                } else {
                    lastSpace = indexOfNextSpace;
                }
            }
            return lines;
        }

        private float setCharacterSpacingForLinje(String linje, boolean setCharSpacingToZero) throws IOException {
            float charSpacing = 0;
            if (linje.length() > 0 && !setCharSpacingToZero) {
                float breddeTilLinje = FONT_SIZE * font.getStringWidth(linje) / 1000;
                float plassTilOvers = currentPage.textRenderingLineEndX - breddeTilLinje;
                if (plassTilOvers > 0) {
                    charSpacing = plassTilOvers / (linje.length() - 1);
                }
            }
            return charSpacing;
        }
    }

    static class PageContext {
        private final PDPageContentStream content;

        private final int textRenderingLineStartX;
        private final int textRenderingLineEndX;
        private final int textRenderingLineEndY;
        private int textRenderingLineCurrentY;

        public PageContext(PDRectangle mediabox, PDPageContentStream content, int margin) {
            this.content = content;

            int textRenderingLineStartY = (int) (mediabox.getUpperRightY()) - margin;

            this.textRenderingLineEndY = (int) (mediabox.getLowerLeftX()) + 2 * margin;
            this.textRenderingLineStartX = (int) (mediabox.getLowerLeftX()) + 2 * margin;
            this.textRenderingLineEndX = (int) (mediabox.getWidth()) - textRenderingLineStartX - 2 * margin;
            this.textRenderingLineCurrentY = textRenderingLineStartY;
        }

        void close() throws IOException {
            content.close();
        }

        public void renderLine(String info, PDFont font, int fontHeight) throws IOException {
            textRenderingLineCurrentY -= fontHeight;
            content.beginText();
            content.setFont(font, FONT_SIZE);
            content.newLineAtOffset(textRenderingLineStartX, textRenderingLineCurrentY);
            content.showText(info);
            content.endText();
        }
    }
}
