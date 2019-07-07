package com.dasiusp.cleber.pdf

import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import com.itextpdf.text.Font.BOLDITALIC
import com.itextpdf.text.FontFactory.HELVETICA
import com.itextpdf.text.pdf.BaseFont


class HelloWorldPdfGenerator(
    private val outputDirectory: String,
    private val personName: String,
    private val Day: Int,
    private val Month: Int,
    private val Year: Int,
    private val eventType: String,
    private val eventName: String,
    private val Hours: Int,
    private val fontDirectory: String
) {
    
    init {
        createOutputDirectory()
    }

    private fun createOutputDirectory() {
        File(outputDirectory).mkdirs()
    }
    
    fun createPdf(name: String) {
        val targetFile = File(outputDirectory, "$name.pdf")
        writeDocument(targetFile, name)
    }
    
    private fun writeDocument(targetFile: File, name: String) {
        val document = Document()
        PdfWriter.getInstance(document, FileOutputStream(targetFile))
        
        document.use {
            FontFactory.register("$fontDirectory/arial.ttf", "fontearial")
            FontFactory.register("$fontDirectory/merriweather.ttf", "fontemerriweather")
            val ArialFont1 = FontFactory.getFont("fontearial", 18.0f)
            val ArialFont2 = FontFactory.getFont("fontearial", 14.0f)
            val Merriweather = FontFactory.getFont("fontemerriweather", 24.0f)
            val text0 = Paragraph ("Certificado de Participação", Merriweather)
            val text1 = Paragraph("\nCertificamos que $personName participou do evento $eventType $eventName realizado na Escola de Artes Ciências e Humanidades da Universidade de São Paulo EACH-USP, com duração de $Hours horas.", Font(ArialFont1))
            val text2 = Paragraph("\nSão Paulo, $Day/$Month/$Year.", ArialFont2)
            text0.setAlignment(Element.ALIGN_CENTER)
            text1.setAlignment(Element.ALIGN_JUSTIFIED)
            text2.setAlignment(Element.ALIGN_CENTER)
            add(text0)
            add(text1)
            add(text2)
            //add(text1 ("Certificamos que $personName participou do evento $eventType $eventName realizado na Escola de Artes Ciencias e Humanidades da Universidade de Sao Paulo EACH-USP, com duraçao de $Hours horas. Sao Paulo $Day/$Month/$Year"))
        }
    }
    
    private fun Document.use(block: Document.() -> Unit) {
        open()
        block()
        close()
    }
}
