package com.dasiusp.cleber.controller

import com.dasiusp.cleber.infrastructure.service.certificate.NewCertificateService
import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec
import io.mockk.mockk
import io.mockk.verify
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.LocalDate

class CertificateControllerTest : FunSpec() {

    private val newCertificateService: NewCertificateService = mockk(relaxed = true)
    private val testController = CertificateController(newCertificateService)
    private val certificateRequest =
        NewCertificateRequest("person_name", "person_email", LocalDate.now(), "activity_name", 30)

    init {
        test("Controller should return created") {
            testController.createCertificate(certificateRequest) shouldBe ResponseEntity(HttpStatus.CREATED)
        }
        
        test("Controller should delegate to the new certificate service") {
            testController.createCertificate(certificateRequest)
            verify { newCertificateService.create(certificateRequest) }
        }
    }

}