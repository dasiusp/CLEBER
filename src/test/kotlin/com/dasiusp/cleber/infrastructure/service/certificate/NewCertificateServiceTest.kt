package com.dasiusp.cleber.infrastructure.service.certificate

import com.dasiusp.cleber.controller.NewCertificateRequest
import com.dasiusp.cleber.infrastructure.repository.CertificateRepository
import com.dasiusp.cleber.infrastructure.service.email.MailSender
import com.dasiusp.cleber.infrastructure.service.pdf.PDFCertificateCreator
import com.dasiusp.cleber.type.Certificate
import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.specs.FunSpec
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import java.time.LocalDate
import java.util.UUID

private val randomUUID = UUID.fromString("123e4567-e89b-12d3-a456-426655440000")

class NewCertificateServiceTest : FunSpec() {
    
    private val mailSender: MailSender = mockk(relaxUnitFun = true)
    private val certificateCreator: PDFCertificateCreator = mockk()
    private val certificateRepository: CertificateRepository = mockk(relaxUnitFun = true)
    
    private val target = NewCertificateService(mailSender, certificateCreator, certificateRepository)
    
    private val newCertificateRequest =
        NewCertificateRequest("pname", "pEmail", LocalDate.of(1998, 2, 9), "aname", 30)
    private val certificateFromRequest =
        Certificate("pname", LocalDate.of(1998,2,9), "aname", 30, randomUUID)
    
    init {
        test("Should send an email for the new certificate") {
            target.create(newCertificateRequest)
            
            verify { mailSender.sendMail("pEmail", certificateFromRequest, "Colman".toByteArray()) }
        }
        
        test("Should persist the certificate in the repository") {
            target.create(newCertificateRequest)
            verify { certificateRepository.insert(certificateFromRequest) }
        }
    }
    
    override fun beforeTest(testCase: TestCase) {
        mockPdfCreation()
        mockRandomSource()
    }
    
    private fun mockPdfCreation() {
        every { certificateCreator.createPdf(certificateFromRequest) } returns "Colman".toByteArray()
    }
    
    private fun mockRandomSource() {
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns randomUUID
    }
    
    override fun afterTest(testCase: TestCase, result: TestResult) {
        clearAllMocks()
    }
}