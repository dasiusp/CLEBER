package com.dasiusp.cleber.pdf

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
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
    @Value("\${certificate.title}") private val certificateTitleText: String,
    @Value("\${certificate.body}") private val certificateBodyText: String,
    @Value("\${certificate.place.and.date}") private val certificatePlaceAndDateText: String,
    @Value("\${certificate.token}") private val certificateTokenText: String
) {
    
    fun createPdf(certificate: Certificate): ByteArray {
        val replacer = CertificateTextReplacer(certificate)
        val pdfCertificate = PDFCertificate(replacer.title, replacer.body, replacer.placeAndDate, replacer.token)
        
        return pdfCertificate.create()
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
