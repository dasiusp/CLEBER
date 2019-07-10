package com.dasiusp.cleber.pdf

import io.kotlintest.TestCase
import io.kotlintest.matchers.file.shouldContainFile
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.specs.FunSpec
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File
import java.time.LocalDate

class HelloWorldPdfGeneratorTest : FunSpec() {
    
    private val testDirectory = "out/test/test"
    val certificate = Certificate("Joao Joanino da Silva Pereira", LocalDate.parse("2019-07-20"), "Palestra", "do Jorge", 300, "FooBar")

    init {
        test("Should create file in the specified directory") {
            createFooBarPdf()
            File(testDirectory).shouldContainFile("FooBar.pdf")
        }
        
        test("Should write basic text to file") {
            createFooBarPdf()
            
            val document = PDDocument.load(File(testDirectory, "FooBar.pdf"))
            val stripper = PDFTextStripper()
            val text = stripper.getText(document)
            breaklineRemover(text) shouldContain "Certificado de Participação Certificamos que ${certificate.personName} participou do evento ${certificate.eventType} ${certificate.eventName} realizado na Escola de Artes Ciências e Humanidades da Universidade de São Paulo EACH-USP, com duração de ${certificate.duration} horas. São Paulo, ${certificate.date}. ${certificate.token}"
        }
    }

    private fun breaklineRemover (text: String): String {
        val auxString = text.replace("\n", " ")
        val resultantString = auxString.replace("\r", "")
        return resultantString
    }
    
    private fun createFooBarPdf() {
        HelloWorldPdfGenerator(testDirectory).createPdf(certificate)
    }
    
    override fun beforeTest(testCase: TestCase) {
        File(testDirectory).deleteRecursively()
    }
}
