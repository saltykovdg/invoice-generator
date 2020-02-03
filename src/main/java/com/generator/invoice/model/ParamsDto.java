package com.generator.invoice.model;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParamsDto {
    private String jasperReportsTemplatePath;
    private String outputDir;
    private LocalDate serviceAgreementDate;
    private String rate;
    private String hours;
    private Integer invoiceNumber;
    private Integer deadlineDays;
    private LocalDate workDateStart;
    private LocalDate workDateEnd;
    private String directExpensesReason;
    private String directExpenses;
}
