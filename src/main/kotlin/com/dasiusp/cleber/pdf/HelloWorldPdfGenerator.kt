package com.dasiusp.cleber.pdf

import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import kotlin.coroutines.coroutineContext


class HelloWorldPdfGenerator(
    private val outputDirectory: String,
    private val personName: String,
    private val date: LocalDate,
    private val eventType: String,
    private val eventName: String,
    private val duration: Int,
    private val fontDirectory: String,
    private val token: String
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

    fun registerFont(fontName: String, alias: String, size: Float): Font {
        FontFactory.register("$fontDirectory/$fontName.ttf", "$alias")
        return FontFactory.getFont("$alias", size)
    }

    fun writeParagraph(text: String, font: Font, alignment: Int): Paragraph {
        val paragraph = Paragraph(text,font)
        paragraph.setAlignment(alignment)
        return paragraph
    }

    private fun writeDocument(targetFile: File, name: String) {
        val document = Document()
        PdfWriter.getInstance(document, FileOutputStream(targetFile))
        
        document.use {
            add(writeParagraph("Certificado de Participação", registerFont("merriweather","merriweatherfont", 24f), 1))
            add(writeParagraph("\nCertificamos que $personName participou do evento $eventType $eventName realizado na Escola de Artes Ciências e Humanidades da Universidade de São Paulo EACH-USP, com duração de $duration horas.", registerFont("arial","arialfont", 18f), 3))
            add(writeParagraph("\nSão Paulo, ${date.dayOfMonth}/${date.monthValue}/${date.year}.", registerFont("arial", "arialfont", 14f), 1))
            add(writeParagraph("\n$token", registerFont("bold", "boldfont", 14f), 1))
        }
    }
    
    private fun Document.use(block: Document.() -> Unit) {
        open()
        block()
        close()
    }
}
