package com.dasiusp.cleber.certificate

import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec
import java.time.LocalDate

class CertificateTextReplacerTest : FunSpec() {
    
    private val certificate = Certificate("name", LocalDate.of(1998, 2, 9), "type", "ename", 30, "token")
    private val target = CertificateTextReplacer(certificate)
    
    init {
        test("Should replace every variable with the certificate value") {
            val stringWithVariables = "%NOME_PESSOA% %TIPO_EVENTO% %NOME_EVENTO% %DURACAO% %TOKEN%"
            
            target.replaceOn(stringWithVariables) shouldBe "name type ename 30 token"
        }
        
        test("Should format date variable to correct format") {
            val stringWithVariables = "%DATA%"
            target.replaceOn(stringWithVariables) shouldBe "09/02/1998"
        }
    }
    
}