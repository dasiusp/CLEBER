package com.dasiusp.cleber.pdf

import com.dasiusp.cleber.pdf.PDFFont.bodyFont
import com.dasiusp.cleber.pdf.PDFFont.bottomFont
import com.dasiusp.cleber.pdf.PDFFont.titleFont
import com.dasiusp.cleber.pdf.PDFFont.tokenFont
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
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

@Component
class PDFCertificateCreator(
    @Value("\${certificate.output.directory}") private val certificateOutputDirectory: String,
    @Value("\${certificate.title.text}") private val certificateTitleText: String,
    @Value("\${certificate.body.text}") private val certificateBodyText: String,
    @Value("\${certificate.place.and.date.text}") private val certificatePlaceAndDateText: String,
    @Value("\${certificate.token.text}") private val certificateTokenText: String
) {

    init {
        createOutputDirectory()
    }

    private fun createOutputDirectory() {
        File(certificateOutputDirectory).mkdirs()
    }

    fun createPdf(certificate: Certificate) {
        val targetFile = File(certificateOutputDirectory, "${certificate.token}.pdf")
        writeDocument(targetFile, certificate)
    }

    private fun writeDocument(targetFile: File, certificate: Certificate) {
        val document = Document(PageSize.A4.rotate())
        PdfWriter.getInstance(document, FileOutputStream(targetFile))

        document.use {
            addTitle(certificate)
            addBody(certificate)
            addPlaceAndDate(certificate)
            addToken(certificate)
        }
    }

    private fun Document.addTitle(certificate: Certificate) {
        add(writeParagraph(certificateTitleText.withReplacedVariables(certificate), titleFont, Element.ALIGN_CENTER))
    }

    private fun Document.addBody(certificate: Certificate) {
        add(writeParagraph(certificateBodyText.withReplacedVariables(certificate), bodyFont, Element.ALIGN_JUSTIFIED))
    }
    
    private fun Document.addPlaceAndDate(certificate: Certificate) {
        add(
            writeParagraph(
                certificatePlaceAndDateText.withReplacedVariables(certificate),
                bottomFont,
                Element.ALIGN_CENTER
            )
        )
    }
    
    private fun Document.addToken(certificate: Certificate) {
        add(writeParagraph(certificateTokenText.withReplacedVariables(certificate), tokenFont, Element.ALIGN_CENTER))
    }
    
    private fun String.withReplacedVariables(certificate: Certificate): String {
        return replace("%NOME_PESSOA%", certificate.personName)
            .replace("%TIPO_EVENTO%", certificate.eventType)
            .replace("%NOME_EVENTO%", certificate.eventName)
            .replace("%DURACAO%", "${certificate.duration}")
            .replace("%DATA%", certificate.date.formatDate())
            .replace("%TOKEN%", certificate.token)
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
