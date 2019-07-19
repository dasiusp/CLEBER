package com.dasiusp.cleber.pdf

import com.dasiusp.cleber.certificate.Certificate
import com.dasiusp.cleber.certificate.CertificateTextReplacer
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component


@Component
class PDFCertificateCreator(
    @Value("\${certificate.title}") private val certificateTitleText: String,
    @Value("\${certificate.body}") private val certificateBodyText: String,
    @Value("\${certificate.place.and.date}") private val certificatePlaceAndDateText: String,
    @Value("\${certificate.token}") private val certificateTokenText: String
) {
    
    fun createPdf(certificate: Certificate): ByteArray {
        val replacer = CertificateTextReplacer(certificate)
        val pdfCertificate =
            with(replacer) {
                PDFCertificate(
                    replaceOn(certificateTitleText),
                    replaceOn(certificateBodyText),
                    replaceOn(certificatePlaceAndDateText),
                    replaceOn(certificateTokenText)
                )
            }
        return pdfCertificate.create()
    }
}
