package com.innovative.accounting.dataextract;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class PDFProcessor {
    private String pdfPath;
    private static final Logger logger = Logger.getLogger(PDFProcessor.class.getName());

    public PDFProcessor(ConfigLoader config) {
        this.pdfPath = config.getPdfPath();
    }

    public String loadPDF() {
        try (PDDocument document = PDDocument.load(new File(pdfPath))) {
            CustomPDFTextStripper stripper = new CustomPDFTextStripper();
            String extractedText = stripper.getText(document);
            return extractedText;
        } catch (IOException e) {
            logger.severe("Error loading PDF: " + e.getMessage());
            e.printStackTrace();
            return null;
            
        }
    }
    private static class CustomPDFTextStripper extends PDFTextStripper {
        private final Set<String> missingUnicodeFonts = new HashSet<>(); 

        public CustomPDFTextStripper() throws IOException {
            super();
        }

        @Override
        protected void writeString(String text, java.util.List<TextPosition> textPositions) throws IOException {
            StringBuilder correctedText = new StringBuilder();
            for (TextPosition textPosition : textPositions) {
                String unicode = textPosition.getUnicode();
                if (unicode != null && !unicode.isEmpty()) {
                    correctedText.append(unicode);
                } else {
                    correctedText.append("�");  // Usar un carácter de reemplazo estándar
                }
            }
            super.writeString(correctedText.toString());
        }
    }
}
