package com.generator.invoice.command

import com.generator.invoice.dtos.ArgsDto
import com.generator.invoice.service.GeneratorService
import org.springframework.stereotype.Component
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.time.LocalDate
import java.util.concurrent.Callable

@Component
@Command(
    name = "invoice-generator",
    mixinStandardHelpOptions = true,
    version = ["invoice-generator v2.1.0"],
    description = ["The command to generate a new invoice."]
)
class GenerateNewInvoiceCommand(
    val generatorService: GeneratorService,
) : Callable<Int> {
    @Option(required = true, names = ["-IN", "--invoice-number"], description = ["Invoice number"])
    private var invoiceNumber: String = ""

    @Option(names = ["-WDS", "--work-date-start"], description = ["Work date start"])
    private var workDateStart: LocalDate = LocalDate.now()

    @Option(names = ["-WDE", "--work-date-end"], description = ["Work date end"])
    private var workDateEnd: LocalDate = LocalDate.now()

    @Option(names = ["-H", "--hours"], description = ["Hours"])
    private var hours: Int = 0

    override fun call(): Int {
        generatorService.generate(
            ArgsDto(
                invoiceNumber = invoiceNumber,
                workDateStart = workDateStart,
                workDateEnd = workDateEnd,
                hours = hours
            )
        )
        return 0
    }
}
