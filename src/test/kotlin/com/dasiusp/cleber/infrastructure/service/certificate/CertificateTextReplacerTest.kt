package com.dasiusp.cleber.infrastructure.service.certificate

import com.dasiusp.cleber.type.certificate
import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CertificateTextReplacerTest : FunSpec() {
    
    private val target = CertificateTextReplacer(certificate)
    
    init {
        test("Should replace every variable with the certificate value") {
            val stringWithVariables = "%NOME_PESSOA% %NOME_ATIVIDADE% %DURACAO% %TOKEN%"
            
            target.replaceOn(stringWithVariables) shouldBe "${certificate.personName} ${certificate.activityName} ${certificate.durationInHours} ${certificate.token}"
        }
        
        test("Should format date variable to correct format") {
            val stringWithVariables = "%DATA%"
            target.replaceOn(stringWithVariables) shouldBe "09/02/1998"
        }
        
        test("Should format today to the correct format") {
            val stringWithVariables = "%HOJE%"
            target.replaceOn(stringWithVariables) shouldBe LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        }
    }
    
}