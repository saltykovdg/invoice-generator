package com.generator.invoice

import com.generator.invoice.command.GenerateNewInvoiceCommand
import com.generator.invoice.config.InvoiceGeneratorProperties
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import picocli.CommandLine

@SpringBootApplication
@EnableConfigurationProperties(InvoiceGeneratorProperties::class)
class InvoiceGeneratorApplication(
    private val generateNewInvoiceCommand: GenerateNewInvoiceCommand
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        CommandLine(generateNewInvoiceCommand).execute(*args)
    }
}

fun main(args: Array<String>) {
    runApplication<InvoiceGeneratorApplication>(*args)
}
