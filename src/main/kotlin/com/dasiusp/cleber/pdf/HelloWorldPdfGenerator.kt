package com.dasiusp.cleber.pdf

import com.itextpdf.text.Document
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream

class HelloWorldPdfGenerator(
    private val outputDirectory: String,
    private val personName: String,
    private val Day: Int,
    private val Month: Int,
    private val Year: Int,
    private val eventType: String,
    private val eventName: String,
    private val Hours: Int
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
            add(Phrase("Certificamos que $personName participou do evento $eventType $eventName realizado na Escola de Artes Ciencias e Humanidades da Universidade de Sao Paulo EACH-USP, com duraçao de $Hours horas. Sao Paulo $Day/$Month/$Year"))
        }
    }
    
    private fun Document.use(block: Document.() -> Unit) {
        open()
        block()
        close()
    }
}
