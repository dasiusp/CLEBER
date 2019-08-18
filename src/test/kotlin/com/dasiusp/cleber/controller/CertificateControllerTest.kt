package com.dasiusp.cleber.controller

import com.dasiusp.cleber.infrastructure.repository.CertificateEntity
import com.dasiusp.cleber.infrastructure.service.certificate.CertificateFinder
import com.dasiusp.cleber.infrastructure.service.certificate.NewCertificateService
import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.LocalDate

class CertificateControllerTest : FunSpec() {

    private val newCertificateService= mockk<NewCertificateService>(relaxed = true)
    private val certificateFinder = mockk<CertificateFinder>()
    private val target = CertificateController(newCertificateService, certificateFinder)
    private val certificateRequest =
        NewCertificateRequest("person_name", "person_email", LocalDate.now(), "activity_name", 30)

    init {
        test("Controller should return created") {
            target.createCertificate(certificateRequest) shouldBe ResponseEntity(HttpStatus.CREATED)
        }
        
        test("Controller should delegate to the new certificate service") {
            target.createCertificate(certificateRequest)
            verify { newCertificateService.create(certificateRequest) }
        }
        
        test("Controller should return OK and the certificate when a token exists") {
            val certificateEntity = CertificateEntity("", "", "", 0, "token")
            every { certificateFinder.find("token") } returns certificateEntity
            target.getCertificate("token") shouldBe ResponseEntity.ok(certificateEntity)
        }
        
        test("Controller should return 404 when a token doesn't exist") {
            every { certificateFinder.find("token") } returns null
            
            target.getCertificate("token") shouldBe ResponseEntity.notFound().build()
        }
    }

}