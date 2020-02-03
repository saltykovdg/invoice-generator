package com.generator.invoice;

import com.generator.invoice.command.GenerateNewInvoiceCommand;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;

@SpringBootApplication
public class InvoiceGeneratorApplication implements CommandLineRunner {
    private final GenerateNewInvoiceCommand generateNewInvoiceCommand;

    public InvoiceGeneratorApplication(GenerateNewInvoiceCommand generateNewInvoiceCommand) {
        this.generateNewInvoiceCommand = generateNewInvoiceCommand;
    }

    public static void main(String[] args) {
        SpringApplication.run(InvoiceGeneratorApplication.class, args);
    }

    @Override
    public void run(String... args) {
        new CommandLine(generateNewInvoiceCommand).execute(args);
    }
}
