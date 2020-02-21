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

    @Option(names = {"-R", "--rate"},
            description = "Rate")
    private String rate;

    @Option(names = {"-H", "--hours"},
            description = "Hours")
    private String hours;

    @Option(required = true,
            names = {"-IN", "--invoice-number"},
            description = "Invoice number")
    private String invoiceNumber;

    @Option(names = {"-IADTCD", "--invoice-add-days-to-current-date"},
            description = "Invoice add days to current date")
    private Integer invoiceAddDaysToCurrentDate;

    @Option(required = true,
            names = {"-DD", "--deadline-days"},
            description = "Deadline days")
    private Integer deadlineDays;

    @Option(names = {"-WDS", "--work-date-start"},
            description = "Work date start")
    private LocalDate workDateStart;

    @Option(names = {"-WDE", "--work-date-end"},
            description = "Work date end")
    private LocalDate workDateEnd;

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
                .rate(StringUtils.isEmpty(rate) ? "0" : rate)
                .hours(StringUtils.isEmpty(hours) ? "0" : hours)
                .invoiceNumber(invoiceNumber)
                .invoiceAddDaysToCurrentDate(invoiceAddDaysToCurrentDate != null ? invoiceAddDaysToCurrentDate : 0)
                .deadlineDays(deadlineDays)
                .workDateStart(workDateStart != null ? workDateStart : LocalDate.now())
                .workDateEnd(workDateEnd != null ? workDateEnd : LocalDate.now())
                .directExpenses(StringUtils.isEmpty(directExpenses) ? "0.0" : directExpenses)
                .build();
        generatorService.generate(params);
        return 0;
    }
}
