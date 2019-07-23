package com.dasiusp.cleber.controller

import com.dasiusp.cleber.certificate.Certificate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class NewCertificateController {

    @PostMapping("/certificate")
    fun printCertificateData(@RequestBody certificate: Certificate): ResponseEntity<Certificate> {
        return ResponseEntity(certificate, HttpStatus.OK)
    }

}