package com.craigburke.document.builder

import com.craigburke.document.core.Document
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream

/**
 * Document node item
 * @author Craig Burke
 */
class PdfDocument {

    int x = 0
    int y = 0

    Document document
    PDDocument pdDocument
    int pageNumber = 0

    PDPageContentStream contentStream
    List<PDPage> pages = []

    PdfDocument(Document document) {
        pdDocument = new PDDocument()
        this.document = document
        addPage()
    }

    void scrollToStartPosition() {
        x = document.margin.left
        y = document.margin.top
    }

    int getPageBottomY() {
        currentPage.mediaBox.height - document.margin.bottom
    }

    void addPage() {
        def newPage = new PDPage()
        pages << newPage
        pageNumber++

        contentStream?.close()
        contentStream = new PDPageContentStream(pdDocument, currentPage)

        scrollToStartPosition()
        pdDocument.addPage(newPage)
    }

    PDPage getCurrentPage() {
        pages[pageNumber - 1]
    }

    void setPageNumber(int value) {
        this.pageNumber = value
        contentStream?.close()
        contentStream = new PDPageContentStream(pdDocument, currentPage, true, true)
        scrollToStartPosition()
    }

    int getTranslatedY() {
        currentPage.mediaBox.height - y
    }

    void scrollDownPage(int amount) {
        if (remainingPageHeight < amount) {
            int amountDiff = amount - remainingPageHeight
            addPage()
            y += amountDiff
        }
        else {
            y += amount
        }

    }

    int translateY(int value) {
        currentPage.mediaBox.height - value
    }

    int getRemainingPageHeight() {
        (currentPage.mediaBox.height - document.margin.bottom) - y
    }

}
