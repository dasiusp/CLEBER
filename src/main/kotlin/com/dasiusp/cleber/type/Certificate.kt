package com.dasiusp.cleber.type

import com.dasiusp.cleber.infrastructure.service.certificate.CertificateTextReplacer
import java.time.LocalDate
import java.util.UUID

data class Certificate(
    val personName: String,
    val activityDate: LocalDate,
    val activityName: String,
    val durationInHours: Int,
    val token: UUID
) {
    fun replaceVariablesIn(string: String) = CertificateTextReplacer(this).replaceOn(string)
}