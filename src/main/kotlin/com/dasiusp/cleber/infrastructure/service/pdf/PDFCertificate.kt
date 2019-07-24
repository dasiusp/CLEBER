package com.dasiusp.cleber.infrastructure.service.pdf

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import org.springframework.core.io.ClassPathResource
import java.io.ByteArrayOutputStream

// A4 height and width are inverted because we want the document in landscape
private val documentHeight = PDRectangle.A4.width
private val documentWidth = PDRectangle.A4.height

class PDFCertificate(
    private val title: String,
    private val body: String,
    private val placeAndDate: String,
    private val tokenText: String
) {

    private val document: PDDocument = PDDocument()
    private val page: PDPage = PDPage(PDRectangle(documentWidth, documentHeight))
    private val content: PDPageContentStream = PDPageContentStream(document, page)
    
    fun create(): ByteArray {
        content.populate()
        return finalize()
    }
    
    private fun PDPageContentStream.populate() {
        addImages()
    
        val titlePosition = addTitle()
        
        beginText()
        addBody(titlePosition)
        addPlaceAndDate()
        addTokenText()
        endText()
        
        close()
    }
    
    private fun PDPageContentStream.addImages() {
        addLogo()
        addLines()
    }
    
    private fun PDPageContentStream.addLogo() {
        val image = loadImage("logo")
        
        val margin = 150f
        val logoWidth = 80f
        val logoHeight = 90f
        
        drawImage(image, (documentWidth - logoWidth) / 2, documentHeight - margin, logoWidth, logoHeight)
    }
    
    private fun PDPageContentStream.addLines() {
        val image = loadImage("line")
        
        val margin = 30f
        val lineWidth = image.width * 0.75f
        val lineHeight = image.height * 0.75f
        
        drawImage(image, (documentWidth - lineWidth) / 2, documentHeight - margin, lineWidth, lineHeight)
        drawImage(image, (documentWidth - lineWidth) / 2, margin, lineWidth, lineHeight)
    }
    
    private fun loadImage(name: String): PDImageXObject {
        val imgBytes = ClassPathResource("images/$name.jpg").inputStream.readBytes()
        return PDImageXObject.createFromByteArray(document, imgBytes, name)
    }
    
    /**
     * @return The title position on screen, to allow placing elements below it
     */
    private fun PDPageContentStream.addTitle(): Float {
        beginText()
        val font = PDType1Font.HELVETICA_BOLD
        val fontSize = 24f
        val topMargin = 180f
    
        val titleWidth = font.getStringWidth(title) / 1000f * fontSize
        val titleHeight = font.fontDescriptor.fontBoundingBox.height / 1000f * fontSize
        
        setFont(font, fontSize)
        newLineAtOffset((documentWidth - titleWidth) / 2, documentHeight - topMargin - titleHeight)
        showText(title)
        
        endText()
        return topMargin + titleHeight
    }
    
    /**
     * @return The body's heigth, to be used as a margin for the bottom text
     */
    private fun PDPageContentStream.addBody(titlePosition: Float) {
        val box = page.mediaBox
    
        val startX = box.lowerLeftX  + 120f
        val startY = titlePosition + 125f
        
        
        val paragraph = Paragraph(startX, startY, body, box)
        paragraph.writeTo(this)
    }
    
    private fun PDPageContentStream.addPlaceAndDate() {
        setFont(PDType1Font.HELVETICA, 14f)
        newLineAtOffset(0f, -50f)
        showText(placeAndDate)
    }
    
    private fun PDPageContentStream.addTokenText() {
        setFont(PDType1Font.HELVETICA, 14f)
        newLineAtOffset(0f, -30f)
        showText(tokenText)
    }
    
    private fun finalize(): ByteArray {
        val outputStream = ByteArrayOutputStream()
        document.addPage(page)
        document.save(outputStream)
        document.close()
        return outputStream.toByteArray()
    }
}

private class Paragraph(
    private val startX: Float,
    private val startY: Float,
    private var text: String,
    mediaBox: PDRectangle,
    horizontalMargin: Float = 120f,
    private val font: PDType1Font = PDType1Font.HELVETICA,
    private val fontSize: Float = 18f
) {
    
    private val width = mediaBox.width - 2 * horizontalMargin
    private val leading = fontSize * 1.5f
    
    private val lines = mutableListOf<String>()
    
    private var lastSpace = -1
    
    init {
        calculateLines()
    }
    
    private fun calculateLines() {
        while(text.isNotEmpty()) {
            var spaceIndex = text.indexOf(' ',  lastSpace + 1)
            
            if(spaceIndex < 0) spaceIndex = text.length
            
            var substring = text.substring(0, spaceIndex)
            val size = fontSize * font.getStringWidth(substring) / 1000
            
            when {
                size > width -> {
                    if(lastSpace < 0) lastSpace = spaceIndex
                    substring = text.substring(0, lastSpace)
                    lines += substring
                    text = text.substring(lastSpace).trim()
                    lastSpace = -1
                }
                spaceIndex == text.length -> {
                    lines.add(text)
                    text = ""
                }
                else -> lastSpace = spaceIndex
            }
        }
    }
    
    fun writeTo(contentStream: PDPageContentStream) {
        contentStream.apply {
            setFont(font, fontSize)
            newLineAtOffset(startX, startY)
    
            for ((index, line) in lines.withIndex()) {
                var characterSpacing = 0f
                if(line.length > 1) {
                    val size = fontSize * font.getStringWidth(line) / 1000
                    val free = width - size
                    if (free > 0) {
                        characterSpacing = free / (line.length - 1)
                    }
                }
                
                if(index == lines.lastIndex) {
                    setCharacterSpacing(0f)
                } else {
                    setCharacterSpacing(characterSpacing)
                }
                
                showText(line)
                newLineAtOffset(0f, -leading)
            }
        }
    }
}
