package com.dasiusp.cleber.pdf

import io.kotlintest.TestCase
import io.kotlintest.matchers.file.shouldContainFile
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.specs.FunSpec
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File

class HelloWorldPdfGeneratorTest : FunSpec() {
    
    private val testDirectory = "out/test/test"
    private val personName = "Jorgineo"
    private val Day = 20
    private val Month = 7
    private val Year = 2019
    private val eventType = "Palestra"
    private val eventName = "Semana do sei la oq"
    private val Hours = 300
    private val fontDirectory = "assets/fonts"

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
            text shouldContain "Certificamos que Jorgineo participou do evento Palestra Semana do sei la oq realizado na Escola de Artes Ciências e Humanidades da Universidade de São Paulo EACH-USP, com duração de 300 horas. São Paulo, 20/7/2019."
        }
    }
    
    private fun createFooBarPdf() {
        HelloWorldPdfGenerator(testDirectory, personName, Day, Month, Year, eventType, eventName, Hours, fontDirectory).createPdf("FooBar")
    }
    
    override fun beforeTest(testCase: TestCase) {
        File(testDirectory).deleteRecursively()
    }
}
