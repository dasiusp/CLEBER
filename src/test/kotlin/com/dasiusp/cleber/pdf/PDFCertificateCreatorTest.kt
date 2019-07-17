package com.dasiusp.cleber.pdf

import io.kotlintest.TestCase
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File
import java.time.LocalDate

class PDFCertificateCreatorTest : FunSpec() {

    private val testDirectory = "out/test/test"
    private val certificate =
        Certificate("Joao Joanino da Silva Pereira", LocalDate.of(2019, 7, 20), "Palestra", "do Jorge", 300, "FooBar")

    init {
        test("Should write basic text to file") {
            val bytes = createFooBarPdf()
            val document = loadDocument()
            val stripper = PDFTextStripper()
            val text = stripper.getText(document)
            text.withoutLinebreaks() shouldContain "Certificado de participação Certificamos que Joao Joanino da Silva Pereira participou do evento Palestra do Jorge realizado na Escola de Artes Ciências e Humanidades da Universidade de São Paulo EACH-USP, com duração de 300 horas. São Paulo, 20/07/2019. FooBar"
        }

        test("Should create file in landscape format") {
            createFooBarPdf()
            val document = loadDocument()
            document.pages[0].mediaBox.height shouldBe PDRectangle.A4.width
            document.pages[0].mediaBox.width shouldBe PDRectangle.A4.height
        }

    }

    private fun String.withoutLinebreaks(): String {
        return replace("\r", "").replace("\n", " ").replace("   "," ")
    }

    private fun createFooBarPdf(): ByteArray {
        return PDFCertificateCreator(
            "Certificado de participação",
            "Certificamos que %NOME_PESSOA% participou do evento %TIPO_EVENTO% %NOME_EVENTO% realizado na Escola de Artes Ciências e Humanidades da Universidade de São Paulo EACH-USP, com duração de %DURACAO% horas.",
            "São Paulo, %DATA%.",
            "%TOKEN%"
        ).createPdf(certificate)
    }

    private fun loadDocument(): PDDocument {
        return PDDocument.load(File(testDirectory, "FooBar.pdf"))
    }

    override fun beforeTest(testCase: TestCase) {
        File(testDirectory).deleteRecursively()
    }
}
