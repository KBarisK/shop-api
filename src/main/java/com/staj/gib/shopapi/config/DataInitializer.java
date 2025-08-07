package com.staj.gib.shopapi.config;

import com.staj.gib.shopapi.dto.request.CategoryTaxRequest;
import com.staj.gib.shopapi.dto.request.CreateCategoryRequest;
import com.staj.gib.shopapi.dto.request.CreateProductRequest;
import com.staj.gib.shopapi.dto.request.TaxRequest;
import com.staj.gib.shopapi.service.CategoryService;
import com.staj.gib.shopapi.service.TaxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final TaxService taxService;
    private final CategoryService categoryService;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Starting data initialization...");

        initializeTaxes();
        initializeCategories();

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

    private void initializeCategories() {
        if (!categoryService.getAllCategories().isEmpty()) {
            log.info("Categories already exist, skipping initialization.");
            return;
        }

        log.info("Initializing default categories...");

        try {
            ArrayList<CategoryTaxRequest> taxRequests = new ArrayList<>();
            taxRequests.add(new CategoryTaxRequest(taxService.getTaxByName("KDV").getId(), BigDecimal.valueOf(1.0)));
            taxRequests.add(new CategoryTaxRequest(taxService.getTaxByName("OTV").getId(), BigDecimal.valueOf(50.0)));

            CreateCategoryRequest categoryRequest = new CreateCategoryRequest("Cep Telefonu", taxRequests);
            categoryService.createCategory(categoryRequest);

            log.info("Default categories created successfully.");
        } catch (Exception e) {
            log.error("Error creating default category: ", e);
        }
    }
}