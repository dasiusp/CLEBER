package com.dasiusp.cleber.infrastructure.service.pdf

import com.dasiusp.cleber.type.certificate
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.text.PDFTextStripper
import java.time.format.DateTimeFormatter

class PDFCertificateCreatorTest : FunSpec() {
    
    init {
        test("Should write basic text to file") {
            val bytes = createFooBarPdf()
            val document = PDDocument.load(bytes)
            val stripper = PDFTextStripper()
            val text = stripper.getText(document)
            text.withoutLinebreaks() shouldContain "Certificado de participação Certificamos que ${certificate.personName} participou do evento ${certificate.activityName} realizado na Escola de Artes Ciências e Humanidades da Universidade de São Paulo EACH-USP, com duração de ${certificate.durationInHours} horas. São Paulo, ${certificate.activityDate.format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy"))}. TOKEN GUIDE ${certificate.token}"
        }
        
        test("Should create pdf in landscape format") {
            val bytes = createFooBarPdf()
            
            val document = PDDocument.load(bytes)
            document.pages[0].mediaBox.height shouldBe PDRectangle.A4.width
            document.pages[0].mediaBox.width shouldBe PDRectangle.A4.height
        }
        
    }
    
    private fun String.withoutLinebreaks(): String {
        return replace("\r", "").replace("\n", " ").replace("   ", " ")
    }
    
    private fun createFooBarPdf(): ByteArray {
        return PDFCertificateCreator(
            "Certificado de participação",
            "Certificamos que %NOME_PESSOA% participou do evento %NOME_ATIVIDADE% realizado na Escola de Artes Ciências e Humanidades da Universidade de São Paulo EACH-USP, com duração de %DURACAO% horas.",
            "São Paulo, %HOJE%.",
            "TOKEN GUIDE",
            "%TOKEN%"
        ).createPdf(certificate)
    }
}
