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
        
        val replacer = CertificateTextReplacer(certificate)
        
        document.use {
            addCertificateTitle(replacer.title)
            addCertificateBody(replacer.body)
            addCertificatePlaceAndDate(replacer.placeAndDate)
            addCertificateToken(replacer.token)
        }
    }

    private fun Document.addCertificateTitle(title: String) {
        add(writeParagraph(title, titleFont, Element.ALIGN_CENTER))
    }

    private fun Document.addCertificateBody(body: String) {
        add(writeParagraph(body, bodyFont, Element.ALIGN_JUSTIFIED))
    }
    
    private fun Document.addCertificatePlaceAndDate(placeAndDate: String) {
        add(writeParagraph(placeAndDate, bottomFont, Element.ALIGN_CENTER))
    }
    
    private fun Document.addCertificateToken(token: String) {
        add(writeParagraph(token, tokenFont, Element.ALIGN_CENTER))
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
    
    private inner class CertificateTextReplacer(val certificate: Certificate) {
        
        val title = certificateTitleText.withReplacedVariables()
        val body = certificateBodyText.withReplacedVariables()
        val placeAndDate = certificatePlaceAndDateText.withReplacedVariables()
        val token = certificateTokenText.withReplacedVariables()
    
        private fun String.withReplacedVariables(): String {
            return replace("%NOME_PESSOA%", certificate.personName)
                .replace("%TIPO_EVENTO%", certificate.eventType)
                .replace("%NOME_EVENTO%", certificate.eventName)
                .replace("%DURACAO%", "${certificate.duration}")
                .replace("%DATA%", certificate.date.formatDate())
                .replace("%TOKEN%", certificate.token)
        }
    
        private fun LocalDate.formatDate() = format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
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
