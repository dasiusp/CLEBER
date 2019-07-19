package com.dasiusp.cleber.certificate

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CertificateTextReplacer(private val certificate: Certificate) {
    
    fun replaceOn(string: String) = string
        .replace("%NOME_PESSOA%", certificate.personName)
        .replace("%TIPO_EVENTO%", certificate.eventType)
        .replace("%NOME_EVENTO%", certificate.eventName)
        .replace("%DURACAO%", "${certificate.duration}")
        .replace("%DATA%", certificate.date.formatDate())
        .replace("%TOKEN%", certificate.token)
    
    private fun LocalDate.formatDate() = format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
}
