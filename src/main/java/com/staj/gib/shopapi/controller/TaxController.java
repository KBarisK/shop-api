package com.staj.gib.shopapi.controller;

import com.staj.gib.shopapi.dto.request.TaxRequest;
import com.staj.gib.shopapi.dto.response.TaxResponse;
import com.staj.gib.shopapi.dto.request.UpdateTaxRequest;
import com.staj.gib.shopapi.service.TaxService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// https://spring.io/guides/tutorials/rest

@RestController // indicates that the data returned by each method is
                  //  written straight into the response body instead of rendering a template.
@RequestMapping("/api/v1/taxes")
@AllArgsConstructor
public class TaxController {
    private final TaxService service;

    @GetMapping
    public List<TaxResponse> all() {
        return service.getAllTaxes();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaxResponse newTax(@Valid @RequestBody TaxRequest newTax) {
        return service.createTax(newTax);
    }

    @GetMapping("/{id}")
    public TaxResponse one(@PathVariable UUID id) {
        return service.getTaxById(id);
    }

    @PutMapping
    public TaxResponse replaceTax(@Valid @RequestBody UpdateTaxRequest newTax) {
        return service.replaceTax(newTax);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTax(@PathVariable UUID id) {
        service.deleteTaxById(id);
    }
}
