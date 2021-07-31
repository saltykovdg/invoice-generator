# Invoice generator

This is information about the application "Invoice generator".

Used stack:
- [Kotlin](https://kotlinlang.org/)
- [Spring Boot](https://spring.io/)
- [Picocli](https://picocli.info/)
- [JasperReports](https://community.jaspersoft.com/)

# How to build it

Open project directory and execute the command: `mvn clean package`

# How to configure and run it

Put `application.yml` with your constant values 
```
invoice:
  output-dir: 
  template-path: 
  shift-current-date-days: 0
  deadline-days: 0
  service-agreement-date:
  rate: 0
```
next to `invoice-generator.jar`

Execute the command to generate invoice:

```
java -jar invoice-generator.jar ^
--invoice-number 1 ^
--work-date-start=2021-01-01 ^
--work-date-end=2021-01-31 ^
--hours 100
```

All available command line arguments:

- `-h, --help` - show this help message and exit.
- `-V, --version` - print version information and exit.
- `-IN, --invoice-number=<invoiceNumber>` - invoice number
- `-WDS, --work-date-start=<workDateStart>` - work date start
- `-WDE, --work-date-end=<workDateEnd>` - work date end
- `-H, --hours=<hours>` - hours
