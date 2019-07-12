package com.dasiusp.cleber.pdf

import com.dasiusp.cleber.pdf.PDFFont.bodyFont
import com.dasiusp.cleber.pdf.PDFFont.bottomFont
import com.dasiusp.cleber.pdf.PDFFont.titleFont
import com.dasiusp.cleber.pdf.PDFFont.tokenFont
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.util.ResourceUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.imageio.ImageIO

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
    @Value("\${certificate.images.directory}") private val certificateImagesDirectory: String,
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
        val document = Document(PageSize.A4.rotate(), 112.68f, 112.68f, 194.02f, 125.07f) //Os floats definem as margens esquerda, direita, acima e abaixo respectivamente, visando melhoria estética.
        PdfWriter.getInstance(document, FileOutputStream(targetFile))

        val replacer = CertificateTextReplacer(certificate)

        document.use {
            addCertificateTitle(replacer.title)
            addCertificateBody(replacer.body)
            addCertificatePlaceAndDate(replacer.placeAndDate)
            addCertificateToken(replacer.token)
            addCertificateImage("logo", 380f, 450f, 30f) //Esses valores de posição foram obtidos por tentativa e erro. Eles visam centralizar o logo na parte superior do PDF.
            addCertificateImage("line", (56f/1.34f), (43f/1.34f), 75f) //Cada imagem pode ser colocado em qualquer lugar do PDF.
            addCertificateImage("line", (56f/1.34f), pageSize.height-(43f/1.34f), 75f)
        }
    }

    private fun Document.addCertificateTitle(title: String) {
        val para = writeParagraph(title, titleFont, Element.ALIGN_CENTER, 20f) //O float "leading" foi escolhido em cada parágrafo apenas por questão estética.
        para.spacingAfter = 25f
        add(para)
    }

    private fun Document.addCertificateBody(body: String) {
        add(writeParagraph(body, bodyFont, Element.ALIGN_JUSTIFIED, 30f))

    }

    private fun Document.addCertificatePlaceAndDate(placeAndDate: String) {
        add(writeParagraph(placeAndDate, bottomFont, Element.ALIGN_CENTER, 60f))
    }

    private fun Document.addCertificateToken(token: String) {
        add(writeParagraph(token, tokenFont, Element.ALIGN_CENTER, 60f))
    }

    private fun Document.addCertificateImage(fileName: String, positionX: Float, positionY: Float, scalePercent: Float) {
        val file = File("$certificateImagesDirectory/$fileName.jpg")
        val image = Image.getInstance(ImageIO.read(file), null)
        image.setAbsolutePosition(positionX, positionY)
        image.scalePercent(scalePercent)
        add(image)
    }

    private fun writeParagraph(text: String, font: Font, alignment: Int, leading: Float): Paragraph {
        val paragraph = Paragraph(leading, text, font)
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
