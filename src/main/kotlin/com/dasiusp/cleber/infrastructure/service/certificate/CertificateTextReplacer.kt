package com.dasiusp.cleber.infrastructure.service.certificate

import com.dasiusp.cleber.type.Certificate
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CertificateTextReplacer(private val certificate: Certificate) {
    
    fun replaceOn(string: String) = string
        .replace("%NOME_PESSOA%", certificate.personName)
        .replace("%NOME_ATIVIDADE%", certificate.activityName)
        .replace("%DURACAO%", "${certificate.durationInHours}")
        .replace("%DATA%", certificate.activityDate.formatDate())
        .replace("%HOJE%", LocalDate.now().formatDate())
        .replace("%TOKEN%", certificate.token.toString())
    
    private fun LocalDate.formatDate() = format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
}
