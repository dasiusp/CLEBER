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
import java.time.format.DateTimeFormatter

data class Certificate(
    val personName: String,
    val date: LocalDate,
    val eventType: String,
    val eventName: String,
    val duration: Int,
    val token: String
)

class PDFCertificateCreator(
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
        val document = Document(PageSize.A4.rotate(), 10f, 10f, 100f, 0f)
        PdfWriter.getInstance(document, FileOutputStream(targetFile))

        document.use {
            addTitle()
            addBody(certificate)
            addPlaceandDate(certificate)
            addToken(certificate)
        }
    }

    private fun Document.addTitle() {
        add(writeParagraph("Certificado de Participação", titleFont, Element.ALIGN_CENTER))
    }

    private fun Document.addBody(certificate: Certificate) {
        add(
            writeParagraph(
                "Certificamos que ${certificate.personName} participou do evento ${certificate.eventType} ${certificate.eventName} realizado na Escola de Artes Ciências e Humanidades da Universidade de São Paulo EACH-USP, com duração de ${certificate.duration} horas.",
                bodyFont,
                Element.ALIGN_JUSTIFIED
            )
        )
    }

    private fun Document.addPlaceandDate(certificate: Certificate) {
        add(writeParagraph("São Paulo, ${certificate.date.formatDate()}.", bottomFont, Element.ALIGN_CENTER))
    }

    private fun Document.addToken(certificate: Certificate) {
        add(writeParagraph("${certificate.token}", tokenFont, Element.ALIGN_CENTER))
    }

    private fun LocalDate.formatDate(): String {
        return format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }

    private fun writeParagraph(text: String, font: Font, alignment: Int): Paragraph {
        val paragraph = Paragraph(text, font)
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
    val titleFont = registerFont("merriweather", "merriweatherfont", 24f)
    val bodyFont = registerFont("arial", "arialfont", 18f)
    val bottomFont = registerFont("arial", "arialfont", 14f)
    val tokenFont = registerFont("bold", "boldfont", 14f)

    private fun registerFont(fontName: String, alias: String, size: Float): Font {
        FontFactory.register("assets/fonts/$fontName.ttf", alias)
        return FontFactory.getFont(alias, size)
    }
}
