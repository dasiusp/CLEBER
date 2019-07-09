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
    /*private val personName = "Jorgineo"
    private val date = LocalDate.parse("2019-07-22")
    private val eventType = "Palestra"
    private val eventName = "Semana do sei la oq"
    private val duration = 300
    private val fontDirectory = "assets/fonts"
    private val token = "Token de Validação"*/

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
            println(text)
            text shouldContain "Certificado de Participação\nCertificamos que Joao participou do evento Palestra do Jorge\nrealizado na Escola de Artes Ciências e Humanidades da\nUniversidade de São Paulo EACH-USP, com duração de 300\nhoras.\nSão Paulo, 2019-07-20.\nFooBar"
        }
    }
    
    private fun createFooBarPdf() {
        val certificate = Certificate("Joao", LocalDate.parse("2019-07-20"), "Palestra", "do Jorge", 300, "FooBar")
        HelloWorldPdfGenerator(testDirectory).createPdf(certificate)
    }
    
    override fun beforeTest(testCase: TestCase) {
        File(testDirectory).deleteRecursively()
    }
}
