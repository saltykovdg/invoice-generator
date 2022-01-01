package com.generator.invoice.service

import com.generator.invoice.config.InvoiceGeneratorProperties
import com.generator.invoice.dtos.ArgsDto
import net.sf.jasperreports.engine.*
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter
import net.sf.jasperreports.export.SimpleExporterInput
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class ReportService(
    private val properties: InvoiceGeneratorProperties
) {
    companion object {
        private val logger = LoggerFactory.getLogger(ReportService::class.java)

        private const val DEFAULT_JASPER_REPORTS_TEMPLATE = "jasperreports/invoice-report-template.jrxml"
        private const val INVOICE_FILENAME_PREFIX = "invoice"
    }

    fun buildReport(args: ArgsDto, context: Map<String, Any>) {
        val jasperReport = getJasperReport(properties.templatePath)
        val jasperPrint = JasperFillManager.fillReport(jasperReport, context, JREmptyDataSource())
        val path = getOutputPath(args.invoiceNumber, properties.outputDir)
        val exportPdfStatus = saveToPdf(jasperPrint, path)
        val exportDocStatus = saveToDoc(jasperPrint, path)
        when {
            exportPdfStatus && exportDocStatus -> logger.info("The invoice was generated successfully.")
            else -> logger.info("Error on generating the report.")
        }
    }

    private fun getJasperReport(path: String) =
        when {
            path.isEmpty() ->
                JasperCompileManager.compileReport(
                    Thread.currentThread().contextClassLoader.getResourceAsStream(DEFAULT_JASPER_REPORTS_TEMPLATE)
                )
            else -> JasperCompileManager.compileReport(path)
        }

    private fun getOutputPath(invoiceNumber: String, outputDir: String): String {
        val outputPath = String.format(
            "%s_%s_%s",
            INVOICE_FILENAME_PREFIX,
            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd", Locale.ENGLISH)),
            invoiceNumber
        )
        takeIf { outputDir.isNotEmpty() } ?: return outputPath
        return "$outputDir/$outputPath"
    }

    private fun saveToPdf(jasperPrint: JasperPrint, path: String): Boolean {
        val filePath = "$path.pdf"
        takeIf { !filePath.isFileAlreadyExists() } ?: return false
        with(FileOutputStream(filePath)) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, this)
            this.close()
        }
        return true
    }

    private fun saveToDoc(jasperPrint: JasperPrint, path: String): Boolean {
        val filePath = "$path.docx"
        takeIf { !filePath.isFileAlreadyExists() } ?: return false
        with(JRDocxExporter()) {
            this.setExporterInput(SimpleExporterInput(jasperPrint))
            this.exporterOutput = SimpleOutputStreamExporterOutput(File(filePath))
            this.exportReport()
        }
        return true
    }

    private fun String.isFileAlreadyExists(): Boolean {
        val file = File(this)
        return when {
            file.exists() && !file.isDirectory -> {
                logger.error("File already exists '$this'!")
                true
            }
            else -> false
        }
    }
}
