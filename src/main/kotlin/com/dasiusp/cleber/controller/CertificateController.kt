package com.dasiusp.cleber.controller

import com.dasiusp.cleber.infrastructure.service.certificate.NewCertificateService
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class CertificateController(
    private val newCertificateService: NewCertificateService
) {

    @PostMapping("/certificate")
    fun createCertificate(@RequestBody certificateRequest: NewCertificateRequest): ResponseEntity<String> {
        newCertificateService.create(certificateRequest)
        return ResponseEntity(HttpStatus.CREATED)
    }
}

data class NewCertificateRequest(
    @JsonProperty(value = "person_name") val personName: String,
    @JsonProperty(value = "person_email") val personEmail: String,
    @JsonProperty(value = "activity_date") val activityDate: LocalDate,
    @JsonProperty(value = "activity_name") val activityName: String,
    @JsonProperty(value = "activity_duration_hours") val activityDurationHours: Int
)