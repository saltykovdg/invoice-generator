package com.generator.invoice.service

import com.generator.invoice.config.InvoiceGeneratorProperties
import com.generator.invoice.dtos.ArgsDto
import net.sf.jasperreports.engine.JRParameter
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class GeneratorService(
    private val properties: InvoiceGeneratorProperties,
    private val reportService: ReportService
) {
    companion object {
        private val logger = LoggerFactory.getLogger(GeneratorService::class.java)
    }

    fun generate(args: ArgsDto) {
        logger.info("args: $args")
        try {
            val context = buildContext(args)
            logger.info("context: $context")
            reportService.buildReport(args, context);
        } catch (e: Exception) {
            logger.error("Error", e)
        }
    }

    private fun buildContext(args: ArgsDto): Map<String, Any> {
        val defaultDateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.ENGLISH)

        val currentDate = LocalDate.now().plusDays(properties.shiftCurrentDateDays.toLong())
        val rate = BigDecimal(properties.rate)
        val hours = BigDecimal(args.hours).setScale(2, BigDecimal.ROUND_HALF_UP)
        val totalDue = rate.multiply(hours).setScale(2, BigDecimal.ROUND_HALF_UP)

        return mapOf<String, Any>(
            "serviceAgreementDate" to LocalDate.parse(properties.serviceAgreementDate)
                .format(
                    DateTimeFormatter.ofPattern("dd'th' MMMM, yyyy", Locale.ENGLISH)
                ),
            "currentDate" to currentDate.format(defaultDateFormatter),
            "deadlineDate" to currentDate.plusDays(properties.deadlineDays.toLong()).format(defaultDateFormatter),
            "invoiceNumber" to args.invoiceNumber,
            "hours" to hours.toDouble(),
            "workDateStart" to args.workDateStart.format(defaultDateFormatter),
            "workDateEnd" to args.workDateEnd.format(defaultDateFormatter),
            "totalDue" to totalDue.toDouble(),
            JRParameter.REPORT_LOCALE to Locale.ENGLISH
        )
    }
}
