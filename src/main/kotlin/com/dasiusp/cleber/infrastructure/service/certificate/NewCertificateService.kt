package com.dasiusp.cleber.infrastructure.service.certificate

import com.dasiusp.cleber.controller.NewCertificateRequest
import com.dasiusp.cleber.infrastructure.repository.CertificateRepository
import com.dasiusp.cleber.infrastructure.service.email.MailSender
import com.dasiusp.cleber.infrastructure.service.pdf.PDFCertificateCreator
import com.dasiusp.cleber.type.Certificate
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class NewCertificateService(
    private val mailSender: MailSender,
    private val certificateCreator: PDFCertificateCreator,
    private val certificateRepository: CertificateRepository
) {
    
    fun create(certificateRequest: NewCertificateRequest) {
        val certificate = certificateRequest.toNewCertificate()
    
        persist(certificate)
        sendMail(certificateRequest.personEmail, certificate)
    }
    
    private fun persist(certificate: Certificate) {
        certificateRepository.insert(certificate)
    }
    
    private fun sendMail(personEmail: String, certificate: Certificate) {
        val pdf = certificateCreator.createPdf(certificate)
    
        mailSender.sendMail(personEmail, certificate, pdf)
    }
    
    private fun NewCertificateRequest.toNewCertificate() =
        Certificate(personName, activityDate, activityName, activityDurationHours, UUID.randomUUID())

}
