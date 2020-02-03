package com.generator.invoice.service;

import com.generator.invoice.model.ParamsDto;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.export.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class GeneratorService {
    private final static String DEFAULT_JASPER_REPORTS_TEMPLATE = "jasperreports/invoice-report-template.jrxml";
    private final static String PREFIX = "invoice";
    private final static String EXTENSION_PDF = ".pdf";
    private final static String EXTENSION_DOCX = ".docx";

    private final DateTimeFormatter dateFormatterShort;
    private final DateTimeFormatter dateFormatterLong;
    private final DateTimeFormatter dateFormatterCurrent;

    public GeneratorService() {
        this.dateFormatterShort = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.ENGLISH);
        this.dateFormatterLong = DateTimeFormatter.ofPattern("dd'th' MMMM, yyyy", Locale.ENGLISH);
        this.dateFormatterCurrent = DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.ENGLISH);
    }

    public void generate(ParamsDto params) {
        log.info(params.toString());
        try {
            log.info("Start generate invoice.");
            LocalDate currentDate = LocalDate.now();
            BigDecimal rate = new BigDecimal(params.getRate());
            BigDecimal hours = new BigDecimal(params.getHours()).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal amount = rate.multiply(hours).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal directExpenses = new BigDecimal(params.getDirectExpenses());
            directExpenses = directExpenses.setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal totalDue = directExpenses.add(amount).setScale(2, BigDecimal.ROUND_HALF_UP);

            JasperReport jasperReport = getJasperReport(params.getJasperReportsTemplatePath());
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("serviceAgreementDate", params.getServiceAgreementDate().format(dateFormatterLong));
            parameters.put("currentDate", currentDate.format(dateFormatterCurrent));
            parameters.put("deadlineDate", currentDate.plusDays(params.getDeadlineDays()).format(dateFormatterCurrent));
            parameters.put("invoiceNumber", params.getInvoiceNumber());
            parameters.put("hours", hours.doubleValue());
            parameters.put("workDateStart", params.getWorkDateStart().format(dateFormatterCurrent));
            parameters.put("workDateEnd", params.getWorkDateEnd().format(dateFormatterCurrent));
            parameters.put("amount", amount.doubleValue());
            parameters.put("directExpensesReason", params.getDirectExpensesReason());
            parameters.put("directExpenses", directExpenses.doubleValue());
            parameters.put("totalDue", totalDue.doubleValue());
            parameters.put(JRParameter.REPORT_LOCALE, Locale.ENGLISH);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
            String path = getOutputPath(params.getInvoiceNumber(), params.getOutputDir());
            saveToPdf(jasperPrint, path);
            saveToDoc(jasperPrint, path);
            log.info("Invoice successfully generated.");
        } catch (JRException | IOException e) {
            log.error("Error", e);
        }
    }

    private JasperReport getJasperReport(String path) throws JRException {
        if (path == null) {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(DEFAULT_JASPER_REPORTS_TEMPLATE);
            return JasperCompileManager.compileReport(inputStream);
        }
        return JasperCompileManager.compileReport(path);
    }

    private String getOutputPath(Integer invoiceNumber, String outputDir) {
        String currentDate = LocalDate.now().format(dateFormatterShort);
        String outputPath = String.format("%s_%s_%s", PREFIX, currentDate, invoiceNumber);
        if (outputDir != null) {
            outputPath = outputDir + "/" + outputPath;
        }
        return outputPath;
    }

    private void saveToPdf(JasperPrint jasperPrint, String path) throws IOException, JRException {
        OutputStream outputStream = new FileOutputStream(path + EXTENSION_PDF);
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        outputStream.close();
    }

    private void saveToDoc(JasperPrint jasperPrint, String path) throws IOException, JRException {
        File file = new File(path + EXTENSION_DOCX);
        Exporter exporter = new JRDocxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));
        exporter.exportReport();
    }
}
