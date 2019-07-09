package com.dasiusp.cleber.pdf

import com.dasiusp.cleber.pdf.PDFFont.bodyFont
import com.dasiusp.cleber.pdf.PDFFont.bottomFont
import com.dasiusp.cleber.pdf.PDFFont.titleFont
import com.dasiusp.cleber.pdf.PDFFont.tokenFont
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate

data class Certificate(
    val personName: String,
    val date: LocalDate,
    val eventType: String,
    val eventName: String,
    val duration: Int,
    val token: String
)
class HelloWorldPdfGenerator(
    private val outputDirectory: String
) {

    init {
        createOutputDirectory()
    }

    private fun createOutputDirectory() {
        File(outputDirectory).mkdirs()
    }

    fun createPdf(certificate: Certificate) {
        val targetFile = File(outputDirectory, "${certificate.token}.pdf")
        writeDocument(targetFile, certificate)
    }

    private fun writeDocument(targetFile: File, certificate: Certificate) {
        val document = Document()
        PdfWriter.getInstance(document, FileOutputStream(targetFile))
        
        document.use {
            addTitle(document)
            addBody(document, certificate)
            addPlaceandDate(document, certificate)
            addToken(document, certificate)
        }
    }

    private fun addTitle(document: Document) {
        document.use {
            add(writeParagraph("Certificado de Participação", titleFont, Element.ALIGN_CENTER))
        }
    }

    private fun addBody(document: Document, certificate: Certificate) {
        document.use {
            add(writeParagraph("Certificamos que ${certificate.personName} participou do evento ${certificate.eventType} ${certificate.eventName} realizado na Escola de Artes Ciências e Humanidades da Universidade de São Paulo EACH-USP, com duração de ${certificate.duration} horas.", bodyFont, Element.ALIGN_JUSTIFIED))
        }
    }

    private fun addPlaceandDate (document: Document, certificate: Certificate) {
        document.use {
            add(writeParagraph("\nSão Paulo, ${certificate.date}.", bottomFont, Element.ALIGN_CENTER))
        }
    }

    private fun addToken (document: Document, certificate: Certificate) {
        document.use {
            add(writeParagraph("${certificate.token}", tokenFont, Element.ALIGN_CENTER))
        }
    }

    private fun writeParagraph(text: String, font: Font, alignment: Int): Paragraph {
        val paragraph = Paragraph(text,font)
        paragraph.alignment = alignment
        return paragraph
    }
    
    private fun Document.use(block: Document.() -> Unit) {
        open()
        block()
        close()
    }
}

private object PDFFont {
    val titleFont = registerFont("merriweather","merriweatherfont", 24f)
    val bodyFont = registerFont("arial", "arialfont", 18f)
    val bottomFont = registerFont("arial", "arialfont", 14f)
    val tokenFont = registerFont("bold", "boldfont", 14f)

    private fun registerFont(fontName: String, alias: String, size: Float): Font {
        FontFactory.register("assets/fonts/$fontName.ttf", alias)
        return FontFactory.getFont(alias, size)
    }
}
