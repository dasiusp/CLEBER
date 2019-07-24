package com.dasiusp.cleber.infrastructure.service.pdf

import com.dasiusp.cleber.infrastructure.service.certificate.CertificateTextReplacer
import com.dasiusp.cleber.type.Certificate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component


@Component
class PDFCertificateCreator(
    @Value("\${certificate.title}") private val certificateTitleText: String,
    @Value("\${certificate.body}") private val certificateBodyText: String,
    @Value("\${certificate.place.and.date}") private val certificatePlaceAndDateText: String,
    @Value("\${certificate.token.guide}") private val certificateTokenGuideText: String,
    @Value("\${certificate.token.text}") private val certificateTokenText: String
) {
    
    fun createPdf(certificate: Certificate): ByteArray {
        val replacer = CertificateTextReplacer(certificate)
        val pdfCertificate =
            with(replacer) {
                PDFCertificate(
                    replaceOn(certificateTitleText),
                    replaceOn(certificateBodyText),
                    replaceOn(certificatePlaceAndDateText),
                    replaceOn(certificateTokenGuideText),
                    replaceOn(certificateTokenText)
                )
            }
        return pdfCertificate.create()
    }
}
