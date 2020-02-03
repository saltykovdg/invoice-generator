# Invoice generator

This is information about the "Invoice generator" program.

Used stack:
- [Spring Boot](https://spring.io/)
- [Lombok](https://projectlombok.org/)
- [Picocli](https://picocli.info/)
- [JasperReports](https://community.jaspersoft.com/)

# How to build it

execute commands:
- `cd ${PROJECT_DIR}` - open project directory
- `mvn clean package` - to build project

# How to run it

When you built it execute the command to generate invoice:

```
java -jar invoice-generator.jar ^
--service-agreement-date=2020-01-01 ^
--deadline-days=30 ^
--rate 100 ^
--hours 100 ^
--invoice-number 1 ^
--work-date-start=2020-01-01 ^
--work-date-end=2020-01-31
```

All available arguments:

- `-h, --help` - show this help message and exit.
- `-V, --version` - print version information and exit.
- `-JRTP, --jasper-reports-template-path=<jasperReportsTemplatePath>` - JasperReports template path
- `-OD, --output-dir=<outputDir>` - output directory
- `-SAD, --service-agreement-date=<serviceAgreementDate>` - service agreement date
- `-DD, --deadline-days=<deadlineDays>` - deadline days
- `-R, --rate=<rate>` - rate
- `-H, --hours=<hours>` - hours
- `-IN, --invoice-number=<invoiceNumber>` - invoice number
- `-WDE, --work-date-end=<workDateEnd>` - work date end
- `-WDS, --work-date-start=<workDateStart>` - work date start
- `-DE, --direct-expenses=<directExpenses>` - direct expenses
- `-DER, --direct-expenses-reason=<directExpensesReason>` - direct expenses reason

Before start you need create your template "invoice-report-template.jrxml" and use the argument `--jasper-reports-template-path=YOUR_TEMPLATE`, use "Jaspersoft Studio" for it.
