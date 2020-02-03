package com.generator.invoice.command;

import com.generator.invoice.model.ParamsDto;
import com.generator.invoice.service.GeneratorService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import picocli.CommandLine.*;

import java.time.LocalDate;
import java.util.concurrent.Callable;

@Component
@Command(
        name = "generate-new-invoice",
        mixinStandardHelpOptions = true,
        version = "generate-new-invoice v1.0.0",
        description = "Command for generating new invoice.")
public class GenerateNewInvoiceCommand implements Callable<Integer> {
    private final GeneratorService generatorService;

    @Option(names = {"-JRTP", "--jasper-reports-template-path"},
            description = "JasperReports template path")
    private String jasperReportsTemplatePath;

    @Option(names = {"-OD", "--output-dir"},
            description = "Output directory")
    private String outputDir;

    @Option(required = true,
            names = {"-SAD", "--service-agreement-date"},
            description = "Service agreement date")
    private LocalDate serviceAgreementDate;

    @Option(required = true,
            names = {"-R", "--rate"},
            description = "Rate")
    private String rate;

    @Option(required = true,
            names = {"-H", "--hours"},
            description = "Hours")
    private String hours;

    @Option(required = true,
            names = {"-IN", "--invoice-number"},
            description = "Invoice number")
    private Integer invoiceNumber;

    @Option(required = true,
            names = {"-DD", "--deadline-days"},
            description = "Deadline days")
    private Integer deadlineDays;

    @Option(required = true,
            names = {"-WDS", "--work-date-start"},
            description = "Work date start")
    private LocalDate workDateStart;

    @Option(required = true,
            names = {"-WDE", "--work-date-end"},
            description = "Work date end")
    private LocalDate workDateEnd;

    @Option(names = {"-DER", "--direct-expenses-reason"},
            description = "Direct expenses reason")
    private String directExpensesReason;

    @Option(names = {"-DE", "--direct-expenses"},
            description = "Direct expenses")
    private String directExpenses;

    public GenerateNewInvoiceCommand(GeneratorService generatorService) {
        this.generatorService = generatorService;
    }

    @Override
    public Integer call() throws Exception {
        ParamsDto params = ParamsDto.builder()
                .jasperReportsTemplatePath(jasperReportsTemplatePath)
                .outputDir(outputDir)
                .serviceAgreementDate(serviceAgreementDate)
                .rate(rate)
                .hours(hours)
                .invoiceNumber(invoiceNumber)
                .deadlineDays(deadlineDays)
                .workDateStart(workDateStart)
                .workDateEnd(workDateEnd)
                .directExpensesReason(directExpensesReason)
                .directExpenses(StringUtils.isEmpty(directExpenses) ? "0.0" : directExpenses)
                .build();
        generatorService.generate(params);
        return 0;
    }
}
