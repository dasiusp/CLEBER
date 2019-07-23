package com.dasiusp.cleber.controller

import com.dasiusp.cleber.certificate.Certificate
import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.LocalDate

class NewCertificateControllerTest : FunSpec() {

    private val testController = NewCertificateController()
    private val certificate = Certificate("Joao", LocalDate.parse("2001-07-20"), "Palestra", "do Jorge", 300, "FooBar")

    init {
        test("Controller should return certificate data") {
            testController.printCertificateData(certificate) shouldBe ResponseEntity(certificate, HttpStatus.OK)
        }
    }

}