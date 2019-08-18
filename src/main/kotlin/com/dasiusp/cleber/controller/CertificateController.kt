package com.dasiusp.cleber.controller

import com.dasiusp.cleber.infrastructure.repository.CertificateEntity
import com.dasiusp.cleber.infrastructure.service.certificate.CertificateFinder
import com.dasiusp.cleber.infrastructure.service.certificate.NewCertificateService
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class CertificateController(
    private val newCertificateService: NewCertificateService,
    private val certificateFinder: CertificateFinder
) {

    @PostMapping("/certificate")
    fun createCertificate(@RequestBody certificateRequest: NewCertificateRequest): ResponseEntity<String> {
        newCertificateService.create(certificateRequest)
        return ResponseEntity(HttpStatus.CREATED)
    }
    
    @GetMapping("/certificate/{token}")
    fun getCertificate(@PathVariable token: String): ResponseEntity<CertificateEntity> {
        val entity = certificateFinder.find(token)
        
        return if(entity == null) ResponseEntity.notFound().build() else ResponseEntity.ok(entity)
    }
}

data class NewCertificateRequest(
    @JsonProperty(value = "person_name") val personName: String,
    @JsonProperty(value = "person_email") val personEmail: String,
    @JsonProperty(value = "activity_date") val activityDate: LocalDate,
    @JsonProperty(value = "activity_name") val activityName: String,
    @JsonProperty(value = "activity_duration_hours") val activityDurationHours: Int
)