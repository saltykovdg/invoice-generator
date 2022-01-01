package com.generator.invoice.config

import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "invoice")
data class InvoiceGeneratorProperties(
    val outputDir: String = "",
    val templatePath: String = "",
    val shiftCurrentDateDays: Int = 0,
    val deadlineDays: Int = 0,
    val serviceAgreementDate: String = "",
    val rate: Double = 0.0,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(InvoiceGeneratorProperties::class.java)
    }

    init {
        logger.info("Current properties: $this")
    }
}
