package com.staj.gib.shopapi.config;

import com.staj.gib.shopapi.dto.request.TaxRequest;
import com.staj.gib.shopapi.service.TaxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final TaxService taxService;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting data initialization...");

        initializeTaxes();

        log.info("Data initialization completed.");
    }

    private void initializeTaxes() {
        // Check if categories already exist
        if (!taxService.getAllTaxes().isEmpty()) {
            log.info("Taxes already exist, skipping initialization.");
            return;
        }

        log.info("Initializing default taxes...");

        try {
            // Create default categories
            taxService.createTax(new TaxRequest("KDV"));
            taxService.createTax(new TaxRequest("OTV"));
            taxService.createTax(new TaxRequest("MTV"));

            log.info("Default taxes created successfully.");
        } catch (Exception e) {
            log.error("Error creating default tax: ", e);
        }
    }
}