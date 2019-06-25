package com.dasiusp.cleber.pdf

import com.itextpdf.text.Document
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream

class HelloWorldPdfGenerator(
    private val outputDirectory: String
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
            add(Phrase("Hello, $name!"))
        }
    }
    
    private fun Document.use(block: Document.() -> Unit) {
        open()
        block()
        close()
    }
}
