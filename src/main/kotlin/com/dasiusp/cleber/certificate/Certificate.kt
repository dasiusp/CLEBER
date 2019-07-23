package com.dasiusp.cleber.certificate

import java.time.LocalDate

data class Certificate(
    val personName: String,
    val date: LocalDate,
    val eventType: String,
    val eventName: String,
    val durationInHours: Int,
    val token: String
) {
    fun replaceVariablesIn(string: String) = CertificateTextReplacer(this).replaceOn(string)
}